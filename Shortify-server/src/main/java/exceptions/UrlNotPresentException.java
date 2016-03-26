package exceptions;

/**
 * This exception is thrown if the short url is not present in the db.
 * Created by Giuseppe Perniola on 16/03/2016.
 */
public class UrlNotPresentException extends Throwable {
    public UrlNotPresentException(String e) {super (e);}
}
