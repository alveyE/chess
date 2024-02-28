package dataAccess;

import java.util.ArrayList;
import model.AuthData;



public class MemoryAuthDAO implements AuthDAO {
    

        ArrayList<AuthData> userCredentials = new ArrayList<AuthData>();

        public MemoryAuthDAO() {
        }

        @Override
        public void deleteAuth() {
            userCredentials = new ArrayList<AuthData>();
        }
   


}
    


   

