package dataAccess;

import model.AuthData;

public class DatabaseAuthDAO implements AuthDAO{

    public DatabaseAuthDAO(){
        try {
            DatabaseManager.createDatabase();
            var statement = "CREATE TABLE IF NOT EXISTS auth (authToken TEXT PRIMARY KEY, username TEXT)";
            var conn = DatabaseManager.getConnection();
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
            }
        }catch (Exception e){
            System.out.println(e);
        }
    }


    @Override
    public void addAuth(AuthData authData) {
        try {
            var statement = "INSERT INTO auth (authToken, username) VALUES (?, ?)";
            var conn = DatabaseManager.getConnection();
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setString(1, authData.authToken());
                preparedStatement.setString(2, authData.username());
                preparedStatement.executeUpdate();
            }
        }catch (Exception e){
            System.out.println(e);
        }
    }

    @Override
    public AuthData getAuth(String authToken) {
        try {
            var statement = "SELECT * FROM auth WHERE authToken = ?";
            var conn = DatabaseManager.getConnection();
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setString(1, authToken);
                try (var results = preparedStatement.executeQuery()) {
                    if (results.next()) {
                        return new AuthData(results.getString("authToken"), results.getString("username"));
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }      
        return null;                      
    }

    @Override
    public void deleteAuth(String authToken) {
        try {
            var statement = "DELETE FROM auth WHERE authToken = ?";
            var conn = DatabaseManager.getConnection();
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setString(1, authToken);
                preparedStatement.executeUpdate();
            }
        }catch (Exception e){
            System.out.println(e);
        }
    }

    @Override
    public void deleteAuth() {
        try {
            var statement = "DELETE FROM auth";
            var conn = DatabaseManager.getConnection();
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
            }
        }catch (Exception e){
            System.out.println(e);
        }
    }
    
}
