package passoffTests.serviceTests;

import service.GameService;
import service.ResponseException;
import service.UserService;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;

import org.junit.jupiter.api.*;

import dataAccess.DataAccessException;
import model.AuthData;
import model.GameData;
import model.UserData;


public class GameServiceTest {

    private UserService userService;
    private GameService gameService;


    @BeforeEach
    public void setUp() throws DataAccessException{
        userService = new UserService();
        gameService = new GameService();
        gameService.clear();
        userService.clear();
    }


    @Test
    void createGameSuccess() throws DataAccessException, ResponseException{
        GameData game = new GameData(1, "a", "b", "game", null);
        UserData user = new UserData("a", "b", "c");
        AuthData auth = userService.register(user);
        assertNotNull(auth);
        assertEquals("a", auth.username());

        int gameID = gameService.createGame(game, auth.authToken());
        assertNotNull(gameID);
    }

    @Test
    void createGameFail() {
        GameData game = new GameData(1, "a", "b", "game", null);
        assertThrows(ResponseException.class, () -> gameService.createGame(game, null));
        assertThrows(ResponseException.class, () -> gameService.createGame(game, "invalid"));
    }


    @Test
    void listGameSuccess() throws DataAccessException, ResponseException{
        GameData game = new GameData(1, "a", "b", "game", null);
        UserData user = new UserData("a", "b", "c");
        AuthData auth = assertDoesNotThrow(() -> userService.register(user));
        assertNotNull(auth);
        assertEquals("a", auth.username());

        int gameID = gameService.createGame(game, auth.authToken());
        assertNotNull(gameID);

        GameData game2 = new GameData(2, "a", "b", "game2", null);
        int gameID2 = gameService.createGame(game2, auth.authToken());
        assertNotNull(gameID2);

        ArrayList<GameData> games = gameService.listGames(auth.authToken());
        assertNotNull(games);
        assertEquals(2, games.size());

    }

    @Test
    void listGameFail() {
        assertThrows(ResponseException.class, () -> gameService.listGames(null));
        assertThrows(ResponseException.class, () -> gameService.listGames("invalid"));
    }


    @Test
    void joinGameSuccess(){
        GameData game = new GameData(1, "a", "b", "game", null);
        UserData user = new UserData("a", "b", "c");
        AuthData auth = assertDoesNotThrow(() -> userService.register(user));
        assertNotNull(auth);
        assertEquals("a", auth.username());

        int gameID = assertDoesNotThrow(() -> gameService.createGame(game, auth.authToken()));
        assertNotNull(gameID);

        assertDoesNotThrow(() -> gameService.joinGame(gameID, auth.authToken(), "WHITE"));
    }

    @Test
    void joinGameFail() {
        assertThrows(ResponseException.class, () -> gameService.joinGame(1, null, "WHITE"));
        assertThrows(ResponseException.class, () -> gameService.joinGame(1, "invalid", "WHITE"));
        assertThrows(ResponseException.class, () -> gameService.joinGame(1, "valid", "invalid"));
    }
    

    





}
