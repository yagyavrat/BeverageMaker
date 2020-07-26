package inventory;

import exceptions.InventoryInSufficientException;
import exceptions.InventoryNotAvailableException;
import recipes.Recipes;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


/*

This class manages inventory of items.
1. it create inventory, each coffee machine has 1 inventory manages
2. Any refill of inventory
3. Any consumption of inventory item
4. Only 1 thread can access modify inventry at a time.


 */
public class InventoryManager {
    private final ConcurrentHashMap<String, Item> inventory;

    public InventoryManager() {
        inventory = new ConcurrentHashMap<String, Item>();
    }

    //This function can be used to access inventory detail on screen
    public int getAvailableQuantityByItemName(String itemName) {
        if (inventory.containsKey(itemName)) {
            return inventory.get(itemName).quantity;
        }
        return 0;
    }

    private synchronized boolean consumeQuantityOfItem(String itemName, int quantity) {
        if (inventory.containsKey(itemName) && inventory.get(itemName).quantity >= quantity) {
            Item item = inventory.get(itemName);
            item.quantity = item.quantity - quantity;
            return true;
        }
        return false;
    }

    public synchronized boolean consumeInventoryForRecipes(Recipes recipes) throws InventoryNotAvailableException, InventoryInSufficientException {
        validateInventoryForRecipes(recipes);
        Map<String, Integer> itemQuantityMap = recipes.getRecipeItems();
        Set<String> keySet = itemQuantityMap.keySet();
        for (String str : keySet) {
            consumeQuantityOfItem(str, itemQuantityMap.get(str));
        }
        return true;
    }

    public synchronized int fillItem(String itemName, int quantity) {
        if (inventory.containsKey(itemName)) {
            Item item = inventory.get(itemName);
            item.quantity = item.quantity + quantity;
            return item.quantity;
        } else {
            Item item = new Item(itemName, quantity);
            inventory.put(itemName, item);
            return item.quantity;
        }
    }

    public synchronized boolean validateInventoryForRecipes(Recipes recipes) throws InventoryNotAvailableException, InventoryInSufficientException {
        Map<String, Integer> itemQuantityMap = recipes.getRecipeItems();
        Set<String> keySet = itemQuantityMap.keySet();
        for (String str : keySet) {
            if (!inventory.containsKey(str)) {
                throw new InventoryNotAvailableException(str);
            }
            if (inventory.containsKey(str) && inventory.get(str).quantity == 0) {
                throw new InventoryNotAvailableException(str);
            } else if (inventory.containsKey(str) && inventory.get(str).quantity < itemQuantityMap.get(str)) {
                throw new InventoryInSufficientException(str);
            }
        }
        return true;
    }
}
