package dataAccess;

import model.UserData;

public interface UserDAO {
    public void deleteUsers();
    public UserData getUser(String username);
    public UserData getUserByEmail(String email);
    public void addUser(UserData user);
}
