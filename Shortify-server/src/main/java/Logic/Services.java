package Logic;

import DAO.RedisDAO;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * Created by Giuseppe on 26/02/2016.
 */
public class Services {


    public static String setShortURL(String longURL, String customURL){

        int startPoint = 0;
        String shortURL = "";

        longURL = URLServices.trimHttp(longURL);
        longURL = URLServices.trimDash(longURL);

        if (customURL == ""){
            //short the URL
            boolean URLinserted = false;
            String HashedURL = URLServices.HashURL(longURL);
            while (!URLinserted){
                shortURL = URLServices.trimHashedURL(HashedURL,startPoint);
                if((RedisDAO.setShortURL(shortURL,longURL) == 0) && (RedisDAO.getLongURL(shortURL) == longURL)) {
                    startPoint++;
                }else URLinserted = true;
            }
        }
        else{
            //use the custom URL
            if((RedisDAO.setShortURL(customURL,longURL) != 0)) {
                shortURL = customURL;
            }
        }
        return shortURL;
    }

    public static  String redirectURL(String shortURL){
        String longURL = new String();
        String jsonData = new String();
        JsonObject
        String jsonResponse = new String();
        Gson gson = new Gson();

        if ( URLServices.isStatsURL(shortURL)) {
            String trimmedShortURL = URLServices.cutStatsChar(shortURL);
            longURL = RedisDAO.getLongURL(shortURL);
            if (longURL == null) longURL = "404.html";
            else{
                URLStatistics URLStats = new URLStatistics(shortURL, longURL);
                jsonResponse = gson.toJson(URLStats);
                jsonResponse = gson.
            }
            // statistics
        }
        else {
            longURL = RedisDAO.getLongURL(shortURL);
            if (longURL == null)
                longURL = "404.html";
        }
        return longURL;
    }
}
