package dai.pw4.models;

import java.util.ArrayList;

public class Cart {
    private ArrayList<Item<Drink, Integer>> drinks = new ArrayList<>();

    public Cart(){

    }

    static class Item<Drink, Integer> {
        Drink drink;
        Integer quantity;

        public Item(Drink first, Integer second){
            this.drink = first;
            this.quantity = second;
        }

        public Drink getDrink() {
            return drink;
        }

        public Integer getQuantity() {
            return quantity;
        }

        public void setQuantity(Integer quantity){
            this.quantity = quantity;
        }
    }

    public void add(Drink drink, Integer amount){
        drinks.stream().filter(e -> e.getDrink().equals(drink))
                        .findFirst()
                        .ifPresentOrElse(pair -> {
                            pair.setQuantity(pair.getQuantity() + amount);
                        },
                                () -> drinks.add(new Item<>(drink, amount))
                        );
    }

    public void remove(Drink drink){
        drinks.stream().filter(e -> e.getDrink().equals(drink))
                .findFirst()
                .ifPresent(
                        drinkIntegerPair -> {
                            drinks.remove(drinkIntegerPair);
                        }
                );
    }
}
