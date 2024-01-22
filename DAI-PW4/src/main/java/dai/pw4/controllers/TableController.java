package dai.pw4.controllers;

import dai.pw4.models.Table;
import dai.pw4.controllers.AuthController;
import io.javalin.http.*;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class TableController {
    private final ConcurrentHashMap<Integer, Table> tables;

    private final AuthController authController;

    public TableController(AuthController authController, ConcurrentHashMap<Integer, Table> tables) {
        this.authController = authController;
        this.tables = tables;
    }

    public void create(Context ctx) {
        String sessionToken = ctx.cookie("sessionToken");
        if(!authController.isValidSession(sessionToken)){
            throw new UnauthorizedResponse("sessionToken not valid.\n");
        }

        Table newTable = ctx.bodyValidator(Table.class)
                .check(obj -> obj.id != null, "Missing id")
                .check(obj -> obj.seats != null, "Missing seats")
                .get();

        Table table = new Table();

        table.id = newTable.id;
        table.seats = newTable.seats;
        table.available = true;

        tables.put(table.id, table);

        ctx.status(HttpStatus.CREATED);
        ctx.json(table);
    }

    public void delete(Context ctx) {
        String sessionToken = ctx.cookie("sessionToken");
        if(!authController.isValidSession(sessionToken)){
            throw new UnauthorizedResponse("sessionToken not valid.\n");
        }

        Integer tableId = ctx.pathParamAsClass("id", Integer.class)
                .check(id -> tables.get(id) != null, "Table not found")
                .getOrThrow(message -> new NotFoundResponse());
        tables.remove(tableId);

        ctx.status(HttpStatus.OK);
    }

    public void select(Context ctx) {

        Integer tableId = ctx.pathParamAsClass("id", Integer.class)
                .check(id -> tables.get(id) != null, "Table not found")
                .getOrThrow(message -> new NotFoundResponse());

        ctx.cookie("tableId", tableId.toString());

        ctx.status(HttpStatus.OK);
    }

    public void getAll(Context ctx) {
        ctx.json(tables.values());
    }
}
