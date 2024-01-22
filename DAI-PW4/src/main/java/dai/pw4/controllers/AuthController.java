package dai.pw4.controllers;

import dai.pw4.models.User;
import dai.pw4.models.Manager;
import io.javalin.http.*;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.concurrent.ConcurrentHashMap;
import java.util.ArrayList;
import java.util.List;

public class AuthController {

    private final ConcurrentHashMap<String, String> managerCredentials;
    private final ConcurrentHashMap<String, String> authenticatedSessions;

    public AuthController(ConcurrentHashMap<String, String> managerCredentials) {
        this.managerCredentials = managerCredentials;
        this.authenticatedSessions = new ConcurrentHashMap<>();
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
                // Generate and store a session token
                String sessionToken = generateSessionToken();
                authenticatedSessions.put(sessionToken, loginUser.username);

                // Set the session token as a cookie
                ctx.cookie("sessionToken", sessionToken);

                ctx.result("Authentication successful for user: " + loginUser.username);
            } else {
                throw new UnauthorizedResponse("Invalid password.");
                //ctx.status(401).result("Authentication failed. Invalid password.");
            }
        } else {
            throw new UnauthorizedResponse("username not found.");
            //ctx.status(401).result("Authentication failed. Manager not found.");
        }
    }

    public void logout(Context ctx) {
        String sessionToken = ctx.cookie("sessionToken");
        if(!isValidSession(sessionToken)){
            throw new UnauthorizedResponse("sessionToken not valid");
        }

        authenticatedSessions.remove(sessionToken); // not null check is in isValidSession()
        ctx.removeCookie("sessionToken");
        ctx.status(HttpStatus.NO_CONTENT).result("Logout successful");
    }

    public void createManager(Context ctx) {
        String sessionToken = ctx.cookie("sessionToken");
        if(!isValidSession(sessionToken)){
            throw new UnauthorizedResponse("sessionToken not valid");
        }

        User newUser = ctx.bodyValidator(User.class)
                .check(obj -> obj.username != null, "Missing username")
                .check(obj -> obj.password != null, "Missing password")
                .get();

        if(managerCredentials.get(newUser.username)!=null){
            throw new ConflictResponse("username already taken");
        }

        managerCredentials.put(newUser.username, hashPassword(newUser.password));

        ctx.status(HttpStatus.CREATED);
        ctx.json(newUser);
    }

    public void listManager(Context ctx) {
        String sessionToken = ctx.cookie("sessionToken");
        if(!isValidSession(sessionToken)){
            throw new UnauthorizedResponse("sessionToken not valid");
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
            throw new UnauthorizedResponse("sessionToken not valid");
        }

        String username = ctx.pathParam("username");
        if (!managerCredentials.containsKey(username)){
            throw new NotFoundResponse();
        }

        managerCredentials.remove(username);

        ctx.status(HttpStatus.NO_CONTENT).result("delete successful");
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
            throw new RuntimeException("Error hashing password.", e);
        }
    }

    private boolean checkPassword(String plainPassword, String hashedPassword) {
        // Validate the provided password against the stored hashed password
        return hashPassword(plainPassword).equals(hashedPassword);
    }
}
