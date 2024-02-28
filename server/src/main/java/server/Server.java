package server;


import com.google.gson.Gson;

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
        Spark.delete("/clear", this::clear);
        Spark.post("/register", this::register);
        Spark.post("/login", this::login);
        Spark.delete("/logout", this::logout);
        Spark.post("/createGame", this::createGame);
        Spark.get("/listGames", this::listGames);
        Spark.put("/joinGame", this::joinGame);


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
        return "OK";
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
            res.type("application/json");
            res.status(e.StatusCode());
            return e.getMessage();
        } catch (Exception e) {
            res.type("application/json");
            res.status(500);
            return "Internal server error";
        }
    }

    private Object logout(Request req, Response res) {
        try{
            UserData user = new Gson().fromJson(req.body(), UserData.class);
            userService.logout(user);
            res.type("application/json");
            res.status(200);
            return "OK";
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

    private Object createGame(Request req, Response res) {
        try{
            String authToken = req.headers("Authorization");
            GameData game = new Gson().fromJson(req.body(), GameData.class);
            int gameID = gameService.createGame(game, authToken);
            res.type("application/json");
            res.status(200);
            return gameID;
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

    private Object listGames(Request req, Response res) {
        try{
            String authToken = req.headers("Authorization");
            res.type("application/json");
            res.status(200);
            return new Gson().toJson(gameService.listGames(authToken));
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

    private Object joinGame(Request req, Response res) {
        try{
            String authToken = req.headers("Authorization");
            JoinGameRequest game = new Gson().fromJson(req.body(), JoinGameRequest.class);
            gameService.joinGame(game.gameID(), authToken, game.color());
            res.type("application/json");
            res.status(200);
            return "OK";
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


}

