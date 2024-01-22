package dai.pw4;

import dai.pw4.controllers.DrinkController;
import dai.pw4.controllers.AuthController;
import dai.pw4.models.Drink;

import io.javalin.Javalin;

import java.util.concurrent.ConcurrentHashMap;

public class Main {
    public static final int PORT = 8080;

    public static void main(String[] args) {
        Javalin app = Javalin.create();

        ConcurrentHashMap<Integer, Drink> drinks = new ConcurrentHashMap<>();

        ConcurrentHashMap<String, String> managerCredentials = new ConcurrentHashMap<>();


        AuthController authController = new AuthController(managerCredentials);
        DrinkController drinkController = new DrinkController(authController, drinks);

        // Auth routes
        app.post("/login", authController::login);
        app.post("/logout", authController::logout);
        app.post("/managers", authController::createManager);
        app.get("/managers", authController::listManager);
        app.delete("/managers/{username}", authController::deleteManager);

        // Introduction on how to navigate/use the api
        app.get("/", ctx -> ctx.result("Welcome in our bar!"));

        // Drink routes
        app.get("/drinks", drinkController::getAll);
        app.get("/drinks/{id}", drinkController::getOne);
        app.post("/drinks", drinkController::create);
        app.delete("/drinks/{id}", drinkController::delete);
        app.put("/drinks/{id}", drinkController::update);


        app.start(PORT);
    }
}