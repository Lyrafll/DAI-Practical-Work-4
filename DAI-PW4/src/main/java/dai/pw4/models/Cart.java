package dai.pw4.models;

import java.util.ArrayList;

public class Cart {
    private ArrayList<Item<Drink, Integer>> drinks = new ArrayList<>();

    public Cart(){

    }

    static class Item<F, S> {
        F first;
        S second;

        public Item(F first, S second){
            this.first = first;
            this.second = second;
        }

        public F getFirst() {
            return first;
        }

        public S getSecond() {
            return second;
        }

        public void setSecond(S second){
            this.second = second;
        }
    }

    public void add(Drink drink, Integer amount){
        drinks.stream().filter(e -> e.getFirst().equals(drink))
                        .findFirst()
                        .ifPresentOrElse(pair -> {
                            pair.setSecond(pair.getSecond() + amount);
                        },
                                () -> drinks.add(new Item<>(drink, amount))
                        );
    }

    public void remove(Drink drink){
        drinks.stream().filter(e -> e.getFirst().equals(drink))
                .findFirst()
                .ifPresent(
                        drinkIntegerPair -> {
                            drinks.remove(drinkIntegerPair);
                        }
                );
    }
}
