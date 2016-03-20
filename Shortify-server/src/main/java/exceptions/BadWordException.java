package exceptions;

/**
 * This exception is thrown if a custom url contains a forbidden word.
 * Created by Giuseppe Perniola on 16/03/2016.
 */
public class BadWordException extends Throwable {
    public BadWordException(String s) { super(s);}
}
