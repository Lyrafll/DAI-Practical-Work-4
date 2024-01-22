package dai.pw4.models;

import kotlin.Pair;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private ArrayList<Pair<Drink, Integer>> drinks = new ArrayList<>();

    public Cart(){

    }

    public void add(Drink drink, Integer amount){
        drinks.stream().filter(e -> e.component1().equals(drink))
                        .findFirst()
                        .ifPresentOrElse(pair -> {
                            pair = new Pair<>(pair.component1(), pair.component2() + amount);
                        },
                                () -> drinks.add(new Pair<>(drink, amount))
                        );
    }

    public void remove(Drink drink){
        drinks.stream().filter(e -> e.component1().equals(drink))
                .findFirst()
                .ifPresent(
                        drinkIntegerPair -> {
                            drinks.remove(drinkIntegerPair);
                        }
                );
    }
}
