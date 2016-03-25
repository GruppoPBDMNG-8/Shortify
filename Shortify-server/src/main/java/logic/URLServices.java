package logic;

import dao.RedisDAO;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * Contains functions to trim urls and generate hash.
 * Created by Giuseppe Perniola on 26/02/2016.
 */
public class URLServices {

    private static final String STATISTICS_STR = "+";
    private static final int URL_SIZE = 5;
    private static final int MAX_TRIMMING_ATTEMPTS = 15;

    /**
     * Checks if the url ends with the special char needed to get the statistics from the db
     * @param URL the url to check
     * @return true if the short url is requesting the statistics, false otherwise
     */
    public static boolean isStatsURL(String URL){
        if ( URL.endsWith(STATISTICS_STR)) return true;
        else return false;
    }

    /**
     * cuts the special char for statistics at the end of the url
     * @param URL
     * @return the url without the last char
     */
    public static String cutStatsChar (String URL){
        return URL.substring(0, URL.length()-1);
    }

    /**
     * adds the "http://" at the begin of the url
     * @param URL
     * @return
     */
    public static String trimHttp (String URL){
        if (URL.startsWith("http://") || URL.startsWith("https://")) return URL;
        else return "http://" + URL;
    }

    /**
     * Cuts the hash of the url
     * @param HashedURL
     * @param startPos the starting position of the cut
     * @return the cutted hash
     */
    public static String trimHashedURL(String HashedURL, int startPos){
        if (startPos <= MAX_TRIMMING_ATTEMPTS) return HashedURL.substring(startPos, startPos + URL_SIZE);
        else return null;
    }

    /**
     * cuts the "/" from the end of the url
     * @param longURL
     * @return
     */
    public static String trimDash(String longURL){
        if (longURL.endsWith("/"))return longURL.substring(0, longURL.length()-1);
        else return longURL;
    }

    /**
     * Uses the SHA-1 hash algorithm to generate the hash of the long url
     * @param longURL
     * @return
     */
    public static String HashURL(String longURL) {
        return DigestUtils.sha1Hex(longURL);
    }

    /**
     * finds if there is already a short url associated to the long url, if there isnt then generates a new short url
     * @param longURL
     * @return the short url associated to the long url
     */
    public static String createShortURL(String longURL){
        boolean breakSearch = false;
        String shortURL = "";
        String returnedURL = "";
        int startPoint = 0;
        String HashedURL = URLServices.HashURL(longURL);
        while (!breakSearch && startPoint <= MAX_TRIMMING_ATTEMPTS){
            shortURL = URLServices.trimHashedURL(HashedURL,startPoint);
            if(RedisDAO.exists(shortURL)) { //check if shortURL already exists
                if (!RedisDAO.getString(shortURL).equals(longURL)) {//same shortURL but different longURL: collision
                    //collision detected: tries to trim the hash to get a new shortURL
                    System.out.println(longURL + " - " + RedisDAO.getString(shortURL) );
                    startPoint++;
                    System.out.println("COLLISION DETECTED: " + shortURL);
                }
                else {//if already exists and the longURL is the same, returns the shortURL
                    breakSearch = true;
                    returnedURL = shortURL;
                }
            }
            else{//if doesnt exists, inserts the shortURL in the db and returns it
                breakSearch = true;
                RedisDAO.setString(shortURL,longURL);
                returnedURL = shortURL;
            }
        }
        return returnedURL;
    }

}
