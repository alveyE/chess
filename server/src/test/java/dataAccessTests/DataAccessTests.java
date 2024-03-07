package dataAccessTests;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dataAccess.DataAccessException;
import dataAccess.DatabaseAuthDAO;
import dataAccess.DatabaseGameDAO;
import dataAccess.DatabaseUserDAO;
import model.AuthData;
import model.UserData;
import service.ResponseException;

public class DataAccessTests {

      

        private DatabaseAuthDAO authDAO;
        private DatabaseUserDAO userDAO;
        private DatabaseGameDAO gameDAO;

        @BeforeEach
        public void setUp() {
            authDAO = new DatabaseAuthDAO();
            userDAO = new DatabaseUserDAO();
            gameDAO = new DatabaseGameDAO();

                authDAO.deleteAuth();
                userDAO.deleteUsers();
                gameDAO.deleteGames();
            
        }




        @Test
        void addAuthSuccess() {
            AuthData auth = new AuthData("authToken", "username");
            assertDoesNotThrow(() -> authDAO.addAuth(auth));
        }


        @Test
        void getAuthSuccess() {
            AuthData auth = new AuthData("authToken2", "username2");
            authDAO.addAuth(auth);
            assertDoesNotThrow(() -> authDAO.getAuth("authToken2"));
        }

   

        @Test
        void deleteAuthSuccess() {
            AuthData auth = new AuthData("authToken3", "username3");
            authDAO.addAuth(auth);
            assertDoesNotThrow(() -> authDAO.deleteAuth("authToken3"));
        }

   

        @Test
        void addUserSuccess() {
            UserData  user = new UserData("username4", "password4", "email4");
            assertDoesNotThrow(() -> userDAO.addUser(user));
        }

    

        @Test
        void getUserSuccess() {
            UserData  user = new UserData("username6", "password6", "email6");
            userDAO.addUser(user);
            assertDoesNotThrow(() -> userDAO.getUser("username6"));
        }


        @Test
        void getUserByEmailSuccess() {
            UserData  user = new UserData("username7", "password7", "email7");
            userDAO.addUser(user);
            assertDoesNotThrow(() -> userDAO.getUserByEmail("email7"));
        }

      


        @Test
        void createGameSuccess() {
            assertDoesNotThrow(() -> gameDAO.createGame("new game"));
        }

        @Test
        void getGameSuccess() {
            int id = gameDAO.createGame("new game2");
            assertDoesNotThrow(() -> gameDAO.getGame(id));
        }

     

        @Test
        void getGamesSuccess() {
            gameDAO.createGame("new game3");
            assertDoesNotThrow(() -> gameDAO.getGames());
        }

     

        @Test
        void deleteGameSuccess() {
            int id = gameDAO.createGame("new game4");
            assertDoesNotThrow(() -> gameDAO.deleteGame(id));
        }


     





}
