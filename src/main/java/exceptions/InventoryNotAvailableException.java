package exceptions;

/*
    Exception when item quantity in inventory is 0 or not available at all
 */

public class InventoryNotAvailableException extends Exception {
    public String message;

    public InventoryNotAvailableException(String message) {
        this.message = message;
    }
}
