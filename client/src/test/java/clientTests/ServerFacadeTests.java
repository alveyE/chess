package clientTests;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.*;

import com.google.gson.Gson;

import model.AuthData;
import model.GameData;
import server.Server;
import ui.ServerFacade;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        facade = new ServerFacade("http://localhost:" + port);
        System.out.println("Started test HTTP server on " + port);
    }

    @AfterAll
    static void stopServer() {
        facade.clear();
        server.stop();
    }

    @BeforeEach
    void clear(){
        facade.clear();
    }


    @Test
    public void registerTestSuccess() {
        AuthData data = assertDoesNotThrow(()-> facade.register("user", "password", "email@mail.com"));
        Assertions.assertEquals(data.username(), "user");
    }

    @Test public void registerTestFail(){
        assertDoesNotThrow(()-> facade.register("user", "password", "email@mail.com"));
        assertThrows(Exception.class, ()-> facade.register("user", "password", "email@mail.com"));
    }

    @Test
    public void loginTestSuccess() {
        assertDoesNotThrow(()-> facade.register("user", "password", "email@mail.com"));
        AuthData data = assertDoesNotThrow(()-> facade.login("user", "password"));
        Assertions.assertEquals(data.username(), "user");
    }

    @Test
    public void loginTestFail() {
        assertThrows(Exception.class, ()-> facade.login("not_a_user", "password"));
    }

    @Test
    public void logoutTestSuccess() {
        assertDoesNotThrow(()-> facade.register("user", "password", "email@mail.com"));
        AuthData data = assertDoesNotThrow(()-> facade.login("user", "password"));
        assertDoesNotThrow(()-> facade.logout(data.authToken()));
    }

    @Test
    public void logoutTestFail() {
        assertDoesNotThrow(()-> facade.register("user", "password", "email@mail.com"));
        AuthData data = assertDoesNotThrow(()-> facade.login("user", "password"));
        assertDoesNotThrow(()-> facade.logout(data.authToken()));
        assertThrows(Exception.class, ()-> facade.logout(data.authToken()));
    }

    @Test
    public void createGameTestSuccess() {
        assertDoesNotThrow(()-> facade.register("user", "password", "email@mail.com"));
        AuthData data = assertDoesNotThrow(()-> facade.login("user", "password"));
        assertDoesNotThrow(()-> facade.createGame(data.authToken(), "game"));
    }

    @Test
    public void createGameTestFail() {
        assertDoesNotThrow(()-> facade.register("user", "password", "email@mail.com"));
        AuthData data = assertDoesNotThrow(()-> facade.login("user", "password"));
        assertThrows(Exception.class, ()-> facade.createGame("invalid token", "game"));
    }


    @Test
    public void listGamesTestSuccess() {
        assertDoesNotThrow(()-> facade.register("user", "password", "email@mail.com"));
        AuthData data = assertDoesNotThrow(()-> facade.login("user", "password"));
        assertDoesNotThrow(()-> facade.createGame(data.authToken(), "game"));
        assertDoesNotThrow(()-> facade.createGame(data.authToken(), "game2"));
        var games = assertDoesNotThrow(()-> facade.listGames(data.authToken()));
        Assertions.assertTrue(games.games.size() > 1);
    }

    @Test
    public void listGamesTestFail() {
        assertDoesNotThrow(()-> facade.register("user", "password", "email@mail.com"));
        AuthData data = assertDoesNotThrow(()-> facade.login("user", "password"));
        assertDoesNotThrow(()-> facade.createGame(data.authToken(), "game"));
        assertDoesNotThrow(()-> facade.createGame(data.authToken(), "game2"));
        assertDoesNotThrow(()-> facade.logout(data.authToken()));
        assertThrows(Exception.class, ()-> facade.listGames(data.authToken()));
    }

    @Test
    public void joinGameTestSuccess(){
        assertDoesNotThrow(()-> facade.register("user", "password", "email@mail.com"));
        AuthData data = assertDoesNotThrow(()-> facade.login("user", "password"));
        var game = assertDoesNotThrow(()-> facade.createGame(data.authToken(), "game"));
        var joinReq = new model.JoinGameRequest(game.gameID(), "black");
        assertDoesNotThrow(()-> facade.joinGame(data.authToken(), joinReq));
    }

    @Test
    public void joinGameTestFail(){
        assertDoesNotThrow(()-> facade.register("user", "password", "email@mail.com"));
        AuthData data = assertDoesNotThrow(()-> facade.login("user", "password"));
        var game = assertDoesNotThrow(()-> facade.createGame(data.authToken(), "game"));
        var joinReq = new model.JoinGameRequest(game.gameID(), "black");
        assertThrows(Exception.class, ()-> facade.joinGame("invalid", joinReq));
    }


        


}
