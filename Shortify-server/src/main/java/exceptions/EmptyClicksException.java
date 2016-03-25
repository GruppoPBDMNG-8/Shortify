package exceptions;

/**
 * This exception is thrown if the list of Clicks retrieved from the db is empty
 * Created by Giuseppe Perniola on 16/03/2016.
 */
public class EmptyClicksException extends Throwable {
    public EmptyClicksException(String s) { super(s);}
}
