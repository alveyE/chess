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
        public void deleteAuth(String authToken) {
            for (AuthData auth : userCredentials) {
                if (auth.authToken().equals(authToken)) {
                    userCredentials.remove(auth);
                    return;
                }
            }
        }
   

        @Override
        public void addAuth(AuthData authData) {
            userCredentials.add(authData);
        }

        @Override
        public AuthData getAuth(String authToken) {
            for (AuthData auth : userCredentials) {
                if (auth.authToken().equals(authToken)) {
                    return auth;
                }
            }
            return null;
        }


}
    


   

