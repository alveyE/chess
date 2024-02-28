package passoffTests.serviceTests;

import service.GameService;
import service.UserService;
import org.junit.jupiter.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import dataAccess.DataAccessException;


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

    



    
}
