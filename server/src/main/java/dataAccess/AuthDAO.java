package dataAccess;

import model.AuthData;

public interface AuthDAO {
    public void deleteAuth() throws DataAccessException;
    public void addAuth(AuthData authData);
    public AuthData getAuth(String authToken);
    public void deleteAuth(String authToken);
}
