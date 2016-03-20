package exceptions;

/**
 * This exception is thrown if is not possible to create a short url from the hash function.
 * Created by Giuseppe Perniola on 16/03/2016.
 */
public class MaxAttemptsException extends Throwable {
    public MaxAttemptsException(String e) { super(e);}
}
