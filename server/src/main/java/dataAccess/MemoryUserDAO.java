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
   


}
    


   

