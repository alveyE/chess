package server;


import service.GameService;
import service.UserService;
import spark.*;

public class Server {

    private final UserService userService;
    private final GameService gameService;

    public Server(UserService userService, GameService gameService) {
        this.userService = userService;
        this.gameService = gameService;
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.post("/clear", this::clear);

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
}
