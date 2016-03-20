package exceptions;

/**
 * This exception is thrown if the long url needed to find the short url is empty.
 * Created by Giuseppe Perniola on 16/03/2016.
 */
public class BlankUrlException extends Throwable {
    public BlankUrlException(String e){super(e);}
}
