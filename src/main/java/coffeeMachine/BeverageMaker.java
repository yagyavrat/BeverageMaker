package coffeeMachine;

import exceptions.InventoryInSufficientException;
import exceptions.InventoryNotAvailableException;
import exceptions.RecipeeNotAvailableException;
import inventory.InventoryManager;
import recipes.RecipeeManager;


/*
This is beverage Maker,
Separate instance of BeverageMaker is instanitated for each order from CoffeeMachine
*/
public class BeverageMaker implements Runnable {
    public InventoryManager inventoryManager;
    public RecipeeManager recipeeManager;
    public String recipeName;

    public BeverageMaker(InventoryManager inventoryManager, RecipeeManager recipeeManager, String recipeName) {
        this.inventoryManager = inventoryManager;
        this.recipeeManager = recipeeManager;
        this.recipeName = recipeName;
    }

    private void prepareBeverage() {
        try {
            recipeeManager.validateRecipee(recipeName);
            inventoryManager.validateInventoryForRecipes(recipeeManager.getRecipeeByName(recipeName));
            inventoryManager.consumeInventoryForRecipes(recipeeManager.getRecipeeByName(recipeName));
            System.out.println(recipeName + " is prepared");
        } catch (InventoryInSufficientException exception) {
            System.out.println(recipeName + " cannot be prepared because " + exception.message + " is not sufficient");
        } catch (InventoryNotAvailableException exception) {
            System.out.println(recipeName + " cannot be prepared because " + exception.message + " is not available");
        } catch (RecipeeNotAvailableException exception) {
            System.out.println(recipeName + " is not supported");
        }
    }

    public void run() {
        prepareBeverage();
    }
}
