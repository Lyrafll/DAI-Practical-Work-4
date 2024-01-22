package dai.pw4.controllers;

import dai.pw4.models.User;
import io.javalin.http.*;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.List;
import java.util.Map;

public class AuthController {

    private final ConcurrentHashMap<String, String> managerCredentials;
    private final ConcurrentHashMap<String, String> authenticatedSessions;

    public AuthController(ConcurrentHashMap<String, String> managerCredentials) {
        this.managerCredentials = managerCredentials;
        this.authenticatedSessions = new ConcurrentHashMap<>();
        if(managerCredentials.isEmpty()){
            managerCredentials.put("admin", hashPassword("admin"));
        }
    }

    public void login(Context ctx) {

        User loginUser = ctx.bodyValidator(User.class)
                .check(obj -> obj.username != null, "Missing username")
                .check(obj -> obj.password != null, "Missing password")
                .get();

        // Check if user exists
        if (managerCredentials.containsKey(loginUser.username)) {
            // Check if the provided password matches the stored hashed password
            if (checkPassword(loginUser.password, managerCredentials.get(loginUser.username))) {
                // Authentication successful
                // remove old session token is session was already active for this user
                if(authenticatedSessions.containsValue(loginUser.username)){
                    // remove any active session linked to this username
                    for (Map.Entry<String, String> entry : authenticatedSessions.entrySet()) {
                        if (loginUser.username.equals(entry.getValue())) {
                            authenticatedSessions.remove(entry.getKey());
                        }
                    }
                }
                // Generate and store a session token
                String sessionToken = generateSessionToken();
                authenticatedSessions.put(sessionToken, loginUser.username);

                // Set the session token as a cookie
                ctx.cookie("sessionToken", sessionToken);

                ctx.result("Authentication successful for user: " + loginUser.username + "\n");
            } else {
                throw new UnauthorizedResponse("Invalid password.\n");
                //ctx.status(401).result("Authentication failed. Invalid password.");
            }
        } else {
            throw new UnauthorizedResponse("username not found.\n");
            //ctx.status(401).result("Authentication failed. Manager not found.");
        }
    }

    public void logout(Context ctx) {
        String sessionToken = ctx.cookie("sessionToken");
        if(!isValidSession(sessionToken)){
            throw new UnauthorizedResponse("sessionToken not valid.\n");
        }

        authenticatedSessions.remove(sessionToken); // not null check is in isValidSession()
        ctx.removeCookie("sessionToken");
        ctx.status(HttpStatus.NO_CONTENT).result("Logout successful.\n");
    }

    public void createManager(Context ctx) {
        String sessionToken = ctx.cookie("sessionToken");
        if(!isValidSession(sessionToken)){
            ctx.removeCookie("sessionToken");
            throw new UnauthorizedResponse("sessionToken not valid.\n");
        }

        User newUser = ctx.bodyValidator(User.class)
                .check(obj -> obj.username != null, "Missing username")
                .check(obj -> obj.password != null, "Missing password")
                .get();

        if(managerCredentials.get(newUser.username)!=null){
            throw new ConflictResponse("username already taken.\n");
        }

        managerCredentials.put(newUser.username, hashPassword(newUser.password));

        ctx.status(HttpStatus.CREATED);
        ctx.json(newUser);
    }

    public void listManager(Context ctx) {
        String sessionToken = ctx.cookie("sessionToken");
        if(!isValidSession(sessionToken)){
            ctx.removeCookie("sessionToken");
            throw new UnauthorizedResponse("sessionToken not valid.\n");
        }

        List<String> managers = new ArrayList<>();

        for (String managerName : managerCredentials.keySet()) {
            managers.add(managerName);
        }

        ctx.json(managers);
    }

    public void deleteManager(Context ctx) {
        String sessionToken = ctx.cookie("sessionToken");
        if(!isValidSession(sessionToken)){
            ctx.removeCookie("sessionToken");
            throw new UnauthorizedResponse("sessionToken not valid.\n");
        }
        String username = ctx.pathParam("username");
        if(authenticatedSessions.get(sessionToken).equals(username)){
            throw new UnauthorizedResponse("deleting it own user is not permitted.\n");
        }
        if (!managerCredentials.containsKey(username)){
            throw new NotFoundResponse("username not found.\n");
        }

        // remove manager from database
        managerCredentials.remove(username);
        // remove any active session linked to the deleted username
        for (Map.Entry<String, String> entry : authenticatedSessions.entrySet()) {
            if (username.equals(entry.getValue())) {
                authenticatedSessions.remove(entry.getKey());
            }
        }

        ctx.status(HttpStatus.NO_CONTENT).result("delete successful.\n");
    }

    public boolean isValidSession(String sessionToken) {
        // Check if the session token is valid
        return sessionToken != null && authenticatedSessions.containsKey(sessionToken);
    }

    private String generateSessionToken() {
        // Generate a unique session token (use a more secure method in production)
        return java.util.UUID.randomUUID().toString();
    }

    public String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password.\n", e);
        }
    }

    private boolean checkPassword(String plainPassword, String hashedPassword) {
        // Validate the provided password against the stored hashed password
        return hashPassword(plainPassword).equals(hashedPassword);
    }
}
