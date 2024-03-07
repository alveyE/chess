package server;


import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

import dataAccess.DataAccessException;
import dataAccess.DatabaseManager;
import dataAccess.DatabaseUserDAO;
import model.AuthData;
import model.GameData;
import model.JoinGameRequest;
import model.UserData;
import service.GameService;
import service.ResponseException;
import service.UserService;
import spark.*;

public class Server {

    private final UserService userService;
    private final GameService gameService;

    public Server() {
        this.userService = new UserService();
        this.gameService = new GameService();
        
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.delete("/db", this::clear);
        Spark.post("/user", this::register);
        Spark.post("/session", this::login);
        Spark.delete("/session", this::logout);
        Spark.post("/game", this::createGame);
        Spark.get("/game", this::listGames);
        Spark.put("/game", this::joinGame);


        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private Object clear(Request req, Response res) {
        try{
        userService.clear();
        gameService.clear();
        res.type("application/json");
        res.status(200);
        return "";
        } catch (Exception e) {
            res.type("application/json");
            res.status(500);
            return "Internal server error";
        }

    }

    private Object register(Request req, Response res) {

        try{
            UserData user = new Gson().fromJson(req.body(), UserData.class);
            AuthData auth = userService.register(user);
            res.type("application/json");
            res.status(200);
            res.body(new Gson().toJson(auth));
            return new Gson().toJson(auth);
        }catch(ResponseException e){
            res.type("application/json");
            res.status(e.StatusCode());
            return e.getMessage();
        } catch (Exception e) {
            res.type("application/json");
            res.status(500);
            return "Internal server error";
        }

    }

    private Object login(Request req, Response res) {

        try{
            UserData user = new Gson().fromJson(req.body(), UserData.class);
            AuthData auth = userService.login(user);
            res.type("application/json");
            res.status(200);
            res.body(new Gson().toJson(auth));
            return new Gson().toJson(auth);
        }catch(ResponseException e){
            res.status(e.StatusCode());
            return e.getMessage();
        } catch (DataAccessException e) {
            res.type("application/json");
            res.status(500);
            return "{\"message\": \"Internal server error\"}";
        }
    }

    private Object logout(Request req, Response res) {
        try{
            String authToken = req.headers("Authorization");
            userService.logout(authToken);
            res.type("application/json");
            res.status(200);
            return "";
        }catch(ResponseException e){
            res.type("application/json");
            res.status(e.StatusCode());
            return e.getMessage();
        } catch (Exception e) {
            res.type("application/json");
            res.status(500);
            return e.getMessage();
        }
    }

    private Object createGame(Request req, Response res) {
        try{
            String authToken = req.headers("Authorization");
            GameData game = new Gson().fromJson(req.body(), GameData.class);
            int gameID = gameService.createGame(game, authToken);
            res.type("application/json");
            res.status(200);
            return new Gson().toJson(Collections.singletonMap("gameID", gameID));        
        }catch(ResponseException e){
            res.type("application/json");
            res.status(e.StatusCode());
            return e.getMessage();
        } catch (Exception e) {
            res.type("application/json");
            res.status(500);
            return e.getMessage();
        }
    }

    private Object listGames(Request req, Response res) {
        try{
            String authToken = req.headers("Authorization");
            res.type("application/json");
            res.status(200);
            List<GameData> games = gameService.listGames(authToken);
            Map<String, Object> response = new HashMap<>();
            response.put("games", games);
            return new Gson().toJson(response);        }catch(ResponseException e){
            res.type("application/json");
            res.status(e.StatusCode());
            return e.getMessage();
        } catch (Exception e) {
            res.type("application/json");
            res.status(500);
            return e.getMessage();
        }
    }

    private Object joinGame(Request req, Response res) {
        try{
            String authToken = req.headers("Authorization");
            JoinGameRequest game = new Gson().fromJson(req.body(), JoinGameRequest.class);
            gameService.joinGame(game.gameID(), authToken, game.playerColor());
            res.type("application/json");
            res.status(200);
            return "";
        }catch(ResponseException e){
            res.type("application/json");
            res.status(e.StatusCode());
            return e.getMessage();
        } catch (Exception e) {
            res.type("application/json");
            res.status(500);
            return e.getMessage();
        }
    }


}

