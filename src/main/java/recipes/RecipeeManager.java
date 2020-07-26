package recipes;

import exceptions.RecipeeNotAvailableException;

import java.util.HashMap;
import java.util.Map;

/*
This takes care of storing and providing recipee of beverage
It stores Recipee object against each recipee name

Recipee doesn't chnage with time
 */

public class RecipeeManager {
    public Map<String, Recipes> map = null;

    public RecipeeManager() {
        map = new HashMap<String, Recipes>();
    }

    public void addRecipee(String name, Map<String, Integer> itemQuantityList) {
        if (!map.containsKey(name)) {
            map.put(name, new Recipes(name, itemQuantityList));
        }
    }

    public Recipes getRecipeeByName(String string) {
        return map.get(string);
    }

    public void validateRecipee(String name) throws RecipeeNotAvailableException {
        if (!map.containsKey(name)) {
            throw new RecipeeNotAvailableException(name);
        }
    }
}
