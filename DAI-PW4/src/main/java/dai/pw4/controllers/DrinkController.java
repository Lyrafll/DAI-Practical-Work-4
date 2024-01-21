package dai.pw4.controllers;

import dai.pw4.models.Drink;
import io.javalin.http.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class DrinkController {
    private final ConcurrentHashMap<Integer, Drink> drinks;
    private final AtomicInteger drinkId = new AtomicInteger();

    public DrinkController(ConcurrentHashMap<Integer, Drink> drinks) {
        this.drinks = drinks;
    }

    public void create(Context ctx) {
        Drink newDrink = ctx.bodyValidator(Drink.class)
                .check(obj -> obj.name != null, "Missing name")
                .check(obj -> obj.price != null, "Missing price")
                .get();

        Drink drink = new Drink();

        drink.id = drinkId.getAndIncrement();
        drink.name = newDrink.name;
        drink.price = newDrink.price;

        drinks.put(drink.id, drink);

        ctx.status(HttpStatus.CREATED);
        ctx.json(drink);
    }

    public void delete(Context ctx) {
        Integer id = ctx.pathParamAsClass("id", Integer.class)
                .check(userId -> drinks.get(userId) != null, "Drink not found")
                .getOrThrow(message -> new NotFoundResponse());
        drinks.remove(id);

        ctx.status(HttpStatus.NO_CONTENT);
    }

    public void update(Context ctx) {

        Integer id = ctx.pathParamAsClass("id", Integer.class)
                .check(userId -> drinks.get(userId) != null, "Drink not found")
                .getOrThrow(message -> new NotFoundResponse());

        Drink newDrink = ctx.bodyValidator(Drink.class)
                .check(obj -> obj.name != null, "Missing name")
                .check(obj -> obj.price != null, "Missing price")
                .get();

        Drink drink = drinks.get(id);

        drink.name = newDrink.name;
        drink.price = newDrink.price;

        ctx.status(HttpStatus.NO_CONTENT);
    }

    public void getOne(Context ctx) {
        Integer id = ctx.pathParamAsClass("id", Integer.class)
                .check(userId -> drinks.get(userId) != null, "Drink not found")
                .getOrThrow(message -> new NotFoundResponse());

        Drink drink = drinks.get(id);

        ctx.json(drink);
    }
    public void getAll(Context ctx) {
        ctx.json(this.drinks);
    }
}