package dai.pw4.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dai.pw4.models.Cart;
import dai.pw4.models.Drink;
import io.javalin.http.*;

import java.util.concurrent.ConcurrentHashMap;

public class CartController {
    private final ConcurrentHashMap<Integer, Drink> drinks;
    private final ConcurrentHashMap<String, Cart> carts;

    public CartController(ConcurrentHashMap<Integer, Drink> drinks) {
        this.drinks = drinks;
        this.carts = new ConcurrentHashMap<>();
    }

    public void add(Context ctx) {
        String tableId = ctx.cookie("tableId");
        if(tableId == null){
            throw new UnauthorizedResponse("no tableId");
        }

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

        if(!carts.containsKey(tableId)){
            Cart cart = new Cart();
            cart.add(drink, requestBody.get("quantity").getAsInt());
            carts.put(tableId, cart);
        } else {
            carts.get(tableId).add(drink, requestBody.get("quantity").getAsInt());
        }
        //ctx.json(carts.get(tableId));
        ctx.status(HttpStatus.OK);
    }

    public void remove(Context ctx) {
        String tableId = ctx.cookie("tableId");
        if(tableId == null){
            throw new UnauthorizedResponse("no tableId");
        }
        Integer drinkId = ctx.pathParamAsClass("id", Integer.class)
                .check(userId -> drinks.get(userId) != null, "Drink not found")
                .getOrThrow(message -> new NotFoundResponse());

        if(!carts.containsKey(tableId)){
            throw new NoContentResponse();
        } else{
            carts.get(tableId).remove(drinks.get(drinkId));
        }

        ctx.status(HttpStatus.OK);
    }

    public void get(Context ctx) {
        String tableId = ctx.cookie("tableId");
        if(tableId == null){
            throw new UnauthorizedResponse("no tableId");
        }

        if(!carts.containsKey(tableId)){
            throw new NoContentResponse();
        }

        ctx.status(HttpStatus.OK);
        ctx.json(new Gson().toJson(carts.get(tableId)));
    }
}
