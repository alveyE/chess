package service;

import dataAccess.DataAccessException;
import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryUserDAO;
import model.AuthData;
import model.UserData;
import java.util.UUID;

public class UserService {


        private final static MemoryAuthDAO authDAO = new MemoryAuthDAO();
        private final static MemoryUserDAO userDAO = new MemoryUserDAO();
    public AuthData register(UserData user) throws DataAccessException, ResponseException{
        String username = user.username();
        String password = user.password();
        String email = user.email();
        if (username == null || password == null || email == null) {
            throw new ResponseException(400, "Username, password, and email are required");
        }
        if(userDAO.getUser(username) != null) {
            throw new ResponseException(400, "Username is already taken");
        }
        if(userDAO.getUserByEmail(email) != null) {
            throw new ResponseException(400, "Email is already taken");
        }
        userDAO.addUser(user);
        AuthData auth = new AuthData(UUID.randomUUID().toString(), password);
        authDAO.addAuth(auth);
        return auth;
    }
     public AuthData login(UserData user) throws DataAccessException, ResponseException{
        String username = user.username();
        String password = user.password();
        if (username == null || password == null) {
            throw new ResponseException(400, "Username and password are required");
        }
        //get password from user
        UserData user1 = userDAO.getUser(username);
        if(user1 == null || !user1.password().equals(password)) {
            throw new ResponseException(400, "Username or password is incorrect");
        }
        AuthData auth = new AuthData(UUID.randomUUID().toString(), password);
        authDAO.addAuth(auth);
        return auth;
     }

    public void logout(UserData user) throws DataAccessException, ResponseException{
        String username = user.username();
        if (username == null) {
            throw new ResponseException(400, "Username is required");
        }
        if(userDAO.getUser(username) == null) {
            throw new ResponseException(400, "Username does not exist");
        }
        authDAO.deleteAuth();
    }


    public static boolean validateAuth(String authToken) throws DataAccessException{
        if(authToken == null) {
            return false;
        }
        return authDAO.getAuth(authToken) != null;
    }    

    public static String getUsername(String authToken) throws DataAccessException{
        if(authToken == null) {
            return null;
        }
        AuthData auth = authDAO.getAuth(authToken);
        if(auth == null) {
            return null;
        }
        return userDAO.getUser(auth.username()).username();
    }


    public void clear() throws DataAccessException{
        authDAO.deleteAuth();
        userDAO.deleteUsers();
    }
}
