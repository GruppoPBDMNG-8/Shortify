package logic;

import dao.RedisDAO;
import entity.Click;
import exceptions.*;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * Created by Giuseppe on 26/02/2016.
 */
public class Services {


    public static String setShortURL(String longURL, String customURL, String ip, String userAgent)
            throws CustomUrlUnavailableException, MaxAttemptsException, BlankUrlException {

        if (longURL.isEmpty())throw new BlankUrlException("Server error, please try again");
        String shortURL = "";
        longURL = URLServices.trimHttp(longURL);
        longURL = URLServices.trimDash(longURL);
        JsonObject jsonShortURL = new JsonObject();

        if (customURL.isEmpty()) { //SHORT THE URL
           // System.out.println("IM SHORTING IT - " + customURL );
            shortURL = URLServices.FindIfAlreadyShorted(longURL); //USE SHORT URL ALREADY INSERTED
            if (shortURL == "") { //CREATE A NEW SHORT URL
                shortURL = URLServices.createShortURL(longURL);
                if (shortURL == "") throw new MaxAttemptsException("Server error, please try another URL");
            }
        }
        else { // USE CUSTOM URL
           // System.out.println("CUSTOM IT - " + customURL );
            if ((RedisDAO.setShortURL(customURL, longURL) == 0))
                throw new CustomUrlUnavailableException("Custom URL not available, please try a different one");
            else {
                shortURL = customURL;
            }
        }
        Click click = new Click(ip, userAgent);
        RedisDAO.setClick(shortURL, new Gson().toJson(click));

        jsonShortURL.addProperty("shortURL", shortURL);
        return  new Gson().toJson(jsonShortURL);
    }




    public static  String redirectURL(String shortURL, String ip, String userAgent) throws UrlNotPresentException, EmptyClicksException {
        String longURL = new String();
        JsonObject jsonLongURL = new JsonObject();
        String jsonResponse = new String();
        Gson gson = new Gson();

        longURL = RedisDAO.getLongURL(shortURL);

        if ( URLServices.isStatsURL(shortURL)) {
            shortURL = URLServices.cutStatsChar(shortURL);
           // System.out.println("redirect is STATS (" + shortURL + ")");
            longURL = RedisDAO.getLongURL(shortURL);
            if (longURL == null) throw new UrlNotPresentException("URL not found");
            else{
                URLStatistics URLStats = new URLStatistics(shortURL, longURL);
                jsonResponse = gson.toJson(URLStats);
            }
            // statistics
        }
        else {
            longURL = RedisDAO.getLongURL(shortURL);
            if (longURL == null) throw new UrlNotPresentException("URL not found");
            else {
                jsonLongURL.addProperty("redirectURL", longURL);
                jsonResponse = gson.toJson(jsonLongURL);
                Click click = new Click(ip, userAgent);
                RedisDAO.setClick(shortURL, new Gson().toJson(click));
            }
        }
        return jsonResponse;
    }


}
