package dai.pw4.controllers;
import dai.pw4.models.User;

import io.javalin.http.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class UsersController {

    private final ConcurrentHashMap<Integer, User> users;
    private final AtomicInteger userId = new AtomicInteger();

    public UsersController(ConcurrentHashMap<Integer, User> users) {
        this.users = users;
    }

    public void create(Context ctx) {
        User newUser = ctx.bodyValidator(User.class)
                .check(obj -> obj.username != null, "Missing username")
                .check(obj -> obj.password != null, "Missing password")
                .get();

        for (User user : users.values()) {
            if (user.username.equals(newUser.username)) {
                throw new ConflictResponse();
            }
        }

        User user = new User();

        user.username = newUser.username;
        user.password = newUser.password;


        ctx.status(HttpStatus.CREATED);
        ctx.json(user);
    }

    public void getOne(Context ctx) {
        Integer id = ctx.pathParamAsClass("id", Integer.class)
                .check(userId -> users.get(userId) != null, "User not found")
                .getOrThrow(message -> new NotFoundResponse());

        User user = users.get(id);

        ctx.json(user);
    }

    public void getMany(Context ctx) {
        String username = ctx.queryParam("username");

        List<User> users = new ArrayList<>();

        for (User user : this.users.values()) {
            if (username != null && !user.username.equals(username)) {
                continue;
            }

            users.add(user);
        }

        ctx.json(users);
    }

    public void update(Context ctx) {
        Integer id = ctx.pathParamAsClass("id", Integer.class)
                .check(userId -> users.get(userId) != null, "User not found")
                .getOrThrow(message -> new NotFoundResponse());

        User updateUser = ctx.bodyValidator(User.class)
                .check(obj -> obj.username != null, "Missing username")
                .check(obj -> obj.password != null, "Missing password")
                .get();

        User user = users.get(id);

        user.username = updateUser.username;
        user.password = updateUser.password;

        users.put(id, user);

        ctx.json(user);
    }

    public void delete(Context ctx) {
        Integer id = ctx.pathParamAsClass("id", Integer.class)
                .check(userId -> users.get(userId) != null, "User not found")
                .getOrThrow(message -> new NotFoundResponse());

        users.remove(id);

        ctx.status(HttpStatus.NO_CONTENT);
    }
}