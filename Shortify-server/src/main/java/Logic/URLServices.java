package logic;

import dao.RedisDAO;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * Created by Giuseppe on 26/02/2016.
 */
public class URLServices {

    private static final String STATISTICS_STR = "+";
    private static final int URL_SIZE = 5;
    private static final int MAX_TRIMMING_ATTEMPTS = 15;

    public static boolean isStatsURL(String URL){
        if ( URL.endsWith(STATISTICS_STR))
            return true;
        else
            return false;
    }

    public static String cutStatsChar (String URL){
        return URL.substring(0, URL.length()-1);
    }


    public static String trimHttp (String URL){
        if (URL.startsWith("http://") || URL.startsWith("https://"))
            return URL;
        else
            return "http://" + URL;
    }


    public static String trimHashedURL(String HashedURL, int startPos){
        if (startPos <= MAX_TRIMMING_ATTEMPTS)
            return HashedURL.substring(startPos, startPos + URL_SIZE);
        else return null;
    }


    public static String trimDash(String longURL){
        if (longURL.endsWith("/")){return longURL.substring(0, longURL.length()-1);}
        else return longURL;
    }


    public static String HashURL(String longURL) {
        String hash40 = DigestUtils.sha1Hex(longURL);
        //System.out.println("Hash: " + hash40);
        return hash40;
    }

    public static String FindIfAlreadyShorted(String longURL){
        boolean breakSearch = false;
        String shortURL = "";
        String returnedURL = "";
        int startPoint = 0;
        String HashedURL = URLServices.HashURL(longURL);
        while (!breakSearch && startPoint <= MAX_TRIMMING_ATTEMPTS){
            shortURL = URLServices.trimHashedURL(HashedURL,startPoint);
            if(RedisDAO.exists(shortURL)) {
                if (RedisDAO.getLongURL(shortURL) != longURL) startPoint++;
                else {
                    breakSearch = true;
                    returnedURL = shortURL;
                }
            }
            else{
                breakSearch = true;

            }
        }
        return returnedURL;
    }


    public static String createShortURL(String longURL){
        boolean URLinserted = false;
        String shortURL = "";
        int startPoint = 0;
        String HashedURL = URLServices.HashURL(longURL);
        while (!URLinserted && startPoint <= MAX_TRIMMING_ATTEMPTS){
            shortURL = URLServices.trimHashedURL(HashedURL,startPoint);
            if((RedisDAO.setShortURL(shortURL,longURL) == 0) && (RedisDAO.getLongURL(shortURL) == longURL)) {
                System.out.println("COLLISION DETECTED: " + shortURL);
                startPoint++;
            }else URLinserted = true;
        }
        return shortURL;
    }

}
