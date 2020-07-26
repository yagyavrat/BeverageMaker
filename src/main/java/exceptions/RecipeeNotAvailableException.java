package exceptions;

/*

Exception when recipee of certains beverage is not available
 */
public class RecipeeNotAvailableException extends Exception {
    public String message;

    public RecipeeNotAvailableException(String message) {
        this.message = message;
    }
}
