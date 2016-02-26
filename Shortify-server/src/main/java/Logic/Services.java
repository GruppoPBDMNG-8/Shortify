package Logic;

import DAO.RedisDAO;

/**
 * Created by Giuseppe on 26/02/2016.
 */
public class Services {


    public static String setShortURL( String longURL){

        int startPoint = 0;
        String shortURL = new String();
        boolean URLinserted = false;
        String HashedURL = URLServices.HashURL(longURL);

        while (!URLinserted){
            shortURL = URLServices.trimHashedURL(HashedURL,startPoint);
            if((RedisDAO.setShortURL(shortURL,longURL) == 0) && (RedisDAO.getLongURL(shortURL) == longURL)) {
                    startPoint++;
            }else URLinserted = true;
        }
        return shortURL;
    }

    public static  String redirectURL(String shortURL){
        String longURL = new String();

        if ( URLServices.isStatsURL(shortURL)) {
            String trimmedShortURL = URLServices.cutStatsChar(shortURL);
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
