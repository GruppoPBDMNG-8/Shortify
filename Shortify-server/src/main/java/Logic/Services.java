package logic;

import dao.RedisDAO;
import entity.Click;
import entity.URLStatistics;
import exceptions.*;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import utilities.BadWordsFilter;

import java.util.List;

/**
 * Contains services for creating a short url and to get long url and statistics from the database
 * Created by Giuseppe Perniola on 26/02/2016.
 */
public class Services {

    private static final String CLICKS = ":clicks";

    /**
     * This function finds if there is already a short url associated to the long url taken in input, if there isnt,
     * then calculates a new short url and gives it in output as a json string. Uses the custom url as a short url if is
     * provided.
     * @param longURL the long url from which calculate the short url
     * @param customURL an optional string which contains a custom url to memorize as the short url associated to the
     *                  long url
     * @param ip the ip of the client that requested the short url
     * @param userAgent the user agent of the client that requested the short url
     * @return a json string containing the short url associated to the long url
     * @throws CustomUrlUnavailableException thrown if the custom url is already present in the db
     * @throws MaxAttemptsException thrown if is not possible to create a short url from the hash function
     * @throws BlankUrlException thrown if the long url needed to create the short url is empty
     * @throws BadWordException  thrown if a custom url contains a forbidden word
     */
    public static String setShortURL(String longURL, String customURL, String ip, String userAgent)
            throws CustomUrlUnavailableException, MaxAttemptsException, BlankUrlException, BadWordException {

        if (longURL.isEmpty())throw new BlankUrlException("Server error, please try again");
        String shortURL = "";
        longURL = URLServices.trimHttp(longURL);
        longURL = URLServices.trimDash(longURL);
        JsonObject jsonShortURL = new JsonObject();

        if (customURL.isEmpty()) { //SHORT THE URL
            //shortURL = URLServices.FindIfAlreadyShorted(longURL); //USE SHORT URL ALREADY INSERTED
            //if (shortURL == "") { //CREATE A NEW SHORT URL
                shortURL = URLServices.createShortURL(longURL);
                if (shortURL == "") throw new MaxAttemptsException("Server error, please try another URL");
          //  }
        }
        else { // USE CUSTOM URL
            if (BadWordsFilter.isBadword(customURL)) throw new BadWordException("Custom URL not available, please try a different one");
            if ((RedisDAO.setString(customURL, longURL) == 0))
                throw new CustomUrlUnavailableException("Custom URL not available, please try a different one");
            else {
                shortURL = customURL;
            }
        }
        Click click = new Click(ip, userAgent);
        RedisDAO.pushList(shortURL + CLICKS, new Gson().toJson(click));
        jsonShortURL.addProperty("shortURL", shortURL);
        return  new Gson().toJson(jsonShortURL);
    }


    /**
     * Given a short url this function finds and returns, as a json string, the long url or the statistics associated to it
     * @param shortURL the short url needed to find the long url or the statistics associated
     * @param ip the ip of the client requesting the long url
     * @param userAgent the user agent of the client requesting the long url
     * @return returns a json string containing the list of statistics or the long url
     * @throws UrlNotPresentException thrown if the short url is not present in the db
     * @throws EmptyClicksException thrown if the list of Clicks retrieved from the db is empty
     */
    public static  String redirectURL(String shortURL, String ip, String userAgent) throws UrlNotPresentException, EmptyClicksException {
        String longURL = new String();
        JsonObject jsonLongURL = new JsonObject();
        String jsonResponse = new String();
        Gson gson = new Gson();

        if ( URLServices.isStatsURL(shortURL)) { //SEND STATISTICS
            shortURL = URLServices.cutStatsChar(shortURL);
            longURL = RedisDAO.getString(shortURL);
            if (longURL == null) throw new UrlNotPresentException("URL not found");
            List<String> stringClicks =  RedisDAO.getList(shortURL + CLICKS);
            if (stringClicks.isEmpty()) throw new EmptyClicksException("Internal server error, please try again later");
            URLStatistics URLStats = new URLStatistics(longURL, stringClicks);
            jsonResponse = gson.toJson(URLStats);
        }
        else { //SEND REDIRECT LINK
            longURL = RedisDAO.getString(shortURL);
            if (longURL == null) throw new UrlNotPresentException("URL not found");
            jsonLongURL.addProperty("redirectURL", longURL);
            jsonResponse = gson.toJson(jsonLongURL);
            Click click = new Click(ip, userAgent);
            RedisDAO.pushList(shortURL + CLICKS, new Gson().toJson(click));
        }
        return jsonResponse;
    }

    /**
     * Checks if the db is already populated by the populator
     * @return true if the db is populated, false otherwise
     */
    public static boolean DBisPopulated (){
        return RedisDAO.exists("db_is_populated");
    }

    /**
     * Sets the db as already populated by the populator
     * @return 1 if the set was successful, 0 otherwise
     */
    public static Integer setDBisPopulated(){
        return RedisDAO.setString("db_is_populated","yes it is");
    }

    /**
     * deletes the url from the db (the key associated to the long url and the key associated to the list of clicks)
     * @param url the short url to delete
     */
    public static void deleteURL(String url){
        RedisDAO.deleteKey(url);
        RedisDAO.deleteKey(url + CLICKS);
    }

}
