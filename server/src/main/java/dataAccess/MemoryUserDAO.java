package dataAccess;

import java.util.ArrayList;
import model.UserData;

public class MemoryUserDAO implements UserDAO {

    ArrayList<UserData> users = new ArrayList<UserData>();


    public MemoryUserDAO() {
    }


    @Override
    public void deleteUsers() {
        users = new ArrayList<UserData>();
    }    

    @Override
    public UserData getUser(String username) {
        for (UserData user : users) {
            if (user.username().equals(username)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public UserData getUserByEmail(String email) {
        for (UserData user : users) {
            if (user.email().equals(email)) {
                return user;
            }
        }
        return null;
    }
   

    @Override
    public void addUser(UserData user) {
        users.add(user);
    }


}
    


   

