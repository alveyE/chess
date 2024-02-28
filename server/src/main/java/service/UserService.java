package service;

import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryUserDAO;
import model.AuthData;
import model.UserData;

public class UserService {


        private final static MemoryAuthDAO authDAO = new MemoryAuthDAO();
        private final static MemoryUserDAO userDAO = new MemoryUserDAO();
    // public AuthData register(UserData user) {}
    // public AuthData login(UserData user) {}
    public void logout(UserData user) {}


    public void clear(){
        authDAO.deleteAuth();
        userDAO.deleteUsers();
    }
}
