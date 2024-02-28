package dataAccess;

import model.AuthData;

public interface AuthDAO {
    public void deleteAuth();
    public void addAuth(AuthData authData);
    public AuthData getAuth(String username);
}
