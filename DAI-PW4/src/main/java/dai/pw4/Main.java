package dai.pw4;

import dai.pw4.controllers.AuthController;
import dai.pw4.controllers.CartController;
import dai.pw4.controllers.DrinkController;
import dai.pw4.controllers.TableController;
import dai.pw4.models.Drink;
import dai.pw4.models.Table;
import java.util.Optional;

import io.javalin.Javalin;

import java.util.concurrent.ConcurrentHashMap;

public class Main {
    public static final int PORT = 8080;

    public static void main(String[] args) {
        Javalin app = Javalin.create();

        ConcurrentHashMap<Integer, Drink> drinks = new ConcurrentHashMap<>();
        Drink drink1 = new Drink();
        drink1.id = 42;
        drink1.name = "Cola";
        drink1.price = 5.8;
        drinks.put(drink1.id, drink1);

        ConcurrentHashMap<String, String> managerCredentials = new ConcurrentHashMap<>();
        ConcurrentHashMap<Integer, Table> tables = new ConcurrentHashMap<>();
        Table table10 = new Table();
        table10.id = 10;
        table10.seats = 6;
        table10.available = true;
        tables.put(table10.id, table10);


        AuthController authController = new AuthController(managerCredentials);
        TableController tableController = new TableController(authController, tables);
        DrinkController drinkController = new DrinkController(authController, drinks);
        CartController cartController = new CartController(drinks);

        // Introduction on how to navigate/use the api
        app.get("/", ctx -> ctx.result("Welcome in our bar!"));

        // Auth routes
        app.post("/login", authController::login);
        app.post("/logout", authController::logout);
        app.post("/managers", authController::createManager);
        app.get("/managers", authController::listManager);
        app.delete("/managers/{username}", authController::deleteManager);

        // Table routes
        app.get("/tables", tableController::getAll);
        app.post("/tables", tableController::create);
        app.delete("/tables/{id}", tableController::delete);
        app.get("/setTable/{id}", tableController::select);
        app.get("/clearTable", tableController::unselect);

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