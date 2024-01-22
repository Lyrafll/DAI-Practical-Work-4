package dai.pw4;

import dai.pw4.controllers.CartController;
import dai.pw4.controllers.DrinkController;
import dai.pw4.models.Drink;
import io.javalin.Javalin;

import java.util.concurrent.ConcurrentHashMap;

public class Main {
    public static final int PORT = 8080;

    public static void main(String[] args) {
        Javalin app = Javalin.create();

        ConcurrentHashMap<Integer, Drink> drinks = new ConcurrentHashMap<>();

        DrinkController drinkController = new DrinkController(drinks);
        CartController cartController = new CartController(drinks);


        // Introduction on how to navigate/use the api
        app.get("/", ctx -> ctx.result("Hello, world!"));

        // Drink routes
        app.get("/drinks", drinkController::getAll);
        app.get("/drinks/{id}", drinkController::getOne);
        app.post("/drinks", drinkController::create);
        app.delete("/drinks/{id}", drinkController::delete);
        app.put("/drinks/{id}", drinkController::update);

        // Cart routes
        app.get("/cart", cartController::get);
        app.post("/cart", cartController::add);
        app.delete("/cart/{id}", cartController::remove);


        app.start(PORT);
    }
}