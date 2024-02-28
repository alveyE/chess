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
   

        @Override
        public void addAuth(AuthData authData) {
            userCredentials.add(authData);
        }

        @Override
        public AuthData getAuth(String username) {
            for (AuthData auth : userCredentials) {
                if (auth.username().equals(username)) {
                    return auth;
                }
            }
            return null;
        }


}
    


   

