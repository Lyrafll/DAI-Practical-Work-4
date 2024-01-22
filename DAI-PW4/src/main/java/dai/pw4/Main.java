package dai.pw4;

import dai.pw4.controllers.DrinkController;
import dai.pw4.controllers.AuthController;
import dai.pw4.controllers.TableController;
import dai.pw4.models.Drink;
import dai.pw4.models.Table;

import io.javalin.Javalin;

import java.util.concurrent.ConcurrentHashMap;

public class Main {
    public static final int PORT = 8080;

    public static void main(String[] args) {
        Javalin app = Javalin.create();

        ConcurrentHashMap<Integer, Drink> drinks = new ConcurrentHashMap<>();

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
        app.get("/tables/{id}", tableController::select);


        // Drink routes
        app.get("/drinks", drinkController::getAll);
        app.get("/drinks/{id}", drinkController::getOne);
        app.post("/drinks", drinkController::create);
        app.delete("/drinks/{id}", drinkController::delete);
        app.put("/drinks/{id}", drinkController::update);


        app.start(PORT);
    }
}