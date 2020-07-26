package coffeeMachine;

import com.google.gson.Gson;
import constants.CoffeeMachineConstants;
import inventory.InventoryManager;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import recipes.RecipeeManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/*
 This code can be tested from CoffeeMachineTest, it has functional test as per document.

 This class needs to be instantiated before anything else, This class interacts with user
 It has inventory and recipee both
 It also put control over maximum beverage maker by using Thread pool executor
 */
public class CoffeeMachine {
    private final int dispensers;
    private final InventoryManager inventoryManager;
    private final RecipeeManager recipeeManager;
    //To control maximum parallel beverage maker as per input
    private ExecutorService pool;

    public CoffeeMachine(String inputFileName) {
        inventoryManager = new InventoryManager();
        recipeeManager = new RecipeeManager();
        dispensers = initializeMachine(inputFileName);
        //Max thread for beverage maker
        pool = Executors.newFixedThreadPool(dispensers);
    }

    //Reads Json and initiatlize inventory,recipees and max thread pools
    private int initializeMachine(String fileName) {
        int dispensers = 0;
        try {
            URL resource = CoffeeMachine.class.getClassLoader().getResource(fileName);
            FileReader reader = new FileReader(new File(resource.getFile()));
            JSONParser jsonParser = new JSONParser();
            Object obj = jsonParser.parse(reader);

            JSONObject jsonObject = (JSONObject) obj;
            JSONObject machineJsonObject = (JSONObject) jsonObject.get(CoffeeMachineConstants.JSON_INPUT_MACHINE);


            JSONObject itemsQuantity = (JSONObject) machineJsonObject.get(CoffeeMachineConstants.JSON_INPUT_TOTAL_ITEMS_QUANTITY);
            HashMap<String, Object> result = new Gson().fromJson(itemsQuantity.toString(), HashMap.class);
            Set<String> set = result.keySet();
            for (String str : set) {
                inventoryManager.fillItem(str, (int) Double.parseDouble(result.get(str).toString()));
            }

            JSONObject beverages = (JSONObject) machineJsonObject.get(CoffeeMachineConstants.JSON_INPUT_BEVERAGES);
            HashMap<String, JSONObject> recipeMap = new Gson().fromJson(beverages.toString(), HashMap.class);
            Set<String> recipees = recipeMap.keySet();
            for (String recipe : recipees) {
                JSONObject jsonObj = (JSONObject) beverages.get(recipe);
                HashMap<String, Object> result1 = new Gson().fromJson(jsonObj.toString(), HashMap.class);
                Set<String> ingredients = result1.keySet();
                HashMap<String, Integer> recipeeIngredients = new HashMap<String, Integer>();
                for (String str : ingredients) {
                    recipeeIngredients.put(str, (int) Double.parseDouble(result1.get(str).toString()));
                }
                recipeeManager.addRecipee(recipe, recipeeIngredients);
            }
            dispensers = Integer.parseInt(((JSONObject) machineJsonObject.get(CoffeeMachineConstants.JSON_INPUT_OUTLETS)).get(CoffeeMachineConstants.JSON_INPUT_COUNT_N).toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dispensers;
    }

    public boolean prepareRecipee(String recipeeName) {
        try {
            //Call beverage maker in new thread if available
            pool.execute(new BeverageMaker(inventoryManager, recipeeManager, recipeeName));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
