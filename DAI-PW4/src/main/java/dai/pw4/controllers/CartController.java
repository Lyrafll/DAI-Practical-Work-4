package dai.pw4.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dai.pw4.models.Cart;
import dai.pw4.models.Drink;
import io.javalin.http.*;

import java.util.concurrent.ConcurrentHashMap;

public class CartController {
    private Cart cart;
    private final ConcurrentHashMap<Integer, Drink> drinks;

    public CartController(ConcurrentHashMap<Integer, Drink> drinks) {
        this.drinks = drinks;
        this.cart = new Cart();
    }

    public void add(Context ctx) {
        JsonObject requestBody = new JsonParser().parse(ctx.body()).getAsJsonObject();

        if (!requestBody.has("drinkId") || !requestBody.has("quantity")) {
            ctx.status(HttpStatus.BAD_REQUEST);
            return;
        }
        Drink drink = drinks.get(requestBody.get("drinkId").getAsInt());

        if (drink == null){
            ctx.status(HttpStatus.NOT_FOUND);
            return;
        }

        this.cart.add(drink, requestBody.get("quantity").getAsInt());

        ctx.status(HttpStatus.NO_CONTENT);
    }

    public void remove(Context ctx) {
        Integer drinkId = ctx.pathParamAsClass("id", Integer.class)
                .check(userId -> drinks.get(userId) != null, "Drink not found")
                .getOrThrow(message -> new NotFoundResponse());
        this.cart.remove(drinks.get(drinkId));
        ctx.status(HttpStatus.NO_CONTENT);
    }

    public void get(Context ctx) {
        ctx.status(HttpStatus.OK);
        ctx.json(new Gson().toJson(this.cart));
    }
}
