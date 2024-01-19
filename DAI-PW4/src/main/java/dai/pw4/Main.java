package dai.pw4;

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


        // Introduction on how to navigate/use the api
        app.get("/", ctx -> ctx.result("Hello, world!"));

        // Drink routes
        app.get("/drinks", drinkController::getAll);
        app.get("/drinks/{id}", drinkController::getOne);
        app.post("/drinks", drinkController::create);
        app.delete("/drinks", ctx -> ctx.result("Hello, world!"));


        app.start(PORT);
    }
}