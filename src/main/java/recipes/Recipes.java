package recipes;

import java.util.Map;

public class Recipes {
    public final String name;
    public final Map<String, Integer> itemQuantityList;

    public Recipes(String name, Map<String, Integer> itemQuantityList) {
        this.name = name;
        this.itemQuantityList = itemQuantityList;
    }

    public Map<String, Integer> getRecipeItems() {
        return this.itemQuantityList;
    }
}
