package exceptions;


/*
Exception when inventory of an item is insufficient
 */
public class InventoryInSufficientException extends Exception {
    public String message;

    public InventoryInSufficientException(String message) {
        this.message = message;
    }
}
