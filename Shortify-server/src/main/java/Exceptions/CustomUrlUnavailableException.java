package exceptions;

/**
 * This exception is thrown if a short/custom url is already present in the db.
 * Created by Giuseppe Perniola on 16/03/2016.
 */
public class CustomUrlUnavailableException extends Throwable {
    public CustomUrlUnavailableException(String e) { super(e);}
}
