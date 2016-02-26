package Logic;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * Created by Giuseppe on 26/02/2016.
 */
public class URLServices {

    private static final String STATISTICS_STR = "+";
    private static final int URL_SIZE = 5;
    private static final int MAX_TRIMMING_ATTEMPTS = 35;

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

    public static String HashURL(String longURL) {
        String hash40 = DigestUtils.sha1Hex(longURL);
        System.out.println("Hash: " + hash40);
        return hash40;
    }

    public static String trimHashedURL(String HashedURL, int startPos){
        if (startPos <= MAX_TRIMMING_ATTEMPTS)
            return HashedURL.substring(startPos, startPos + URL_SIZE);
        else return null;
    }
}
