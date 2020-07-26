package coffeeMachine;

import org.junit.Test;


public class CoffeeMachineTest {

    @Test
    public void coffeeMachineTest() {
        final CoffeeMachine coffeeMachine = new CoffeeMachine("input.json");
        new Thread(() -> coffeeMachine.prepareRecipee("hot_coffee")).start();
        new Thread(() -> coffeeMachine.prepareRecipee("hot_tea")).start();
        new Thread(() -> coffeeMachine.prepareRecipee("black_tea")).start();
        new Thread(() -> coffeeMachine.prepareRecipee("green_tea")).start();
    }
}
