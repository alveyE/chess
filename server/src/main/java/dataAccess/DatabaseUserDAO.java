package dataAccess;

import model.UserData;

public class DatabaseUserDAO implements UserDAO {

        public DatabaseUserDAO(){
            try {
                DatabaseManager.createDatabase();
                var statement = "CREATE TABLE IF NOT EXISTS users (username VARCHAR(255) PRIMARY KEY, password VARCHAR(255), email VARCHAR(255))";
                var conn = DatabaseManager.getConnection();
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }catch (Exception e){
                System.out.println(e);
            }
        }


        @Override
        public UserData getUser(String username) {
            try {
                var statement = "SELECT * FROM users WHERE username = ?";
                var conn = DatabaseManager.getConnection();
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.setString(1, username);
                    try (var results = preparedStatement.executeQuery()) {
                        if (results.next()) {
                            return new UserData(results.getString("username"), results.getString("password"), results.getString("email"));
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println(e);
            }      
            return null;                      
        }


        @Override
        public UserData getUserByEmail(String email) {
            try {
                var statement = "SELECT * FROM users WHERE email = ?";
                var conn = DatabaseManager.getConnection();
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.setString(1, email);
                    try (var results = preparedStatement.executeQuery()) {
                        if (results.next()) {
                            return new UserData(results.getString("username"), results.getString("password"), results.getString("email"));
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println(e);
            }      
            return null;                      
        }


        @Override
        public void addUser(UserData user) {
            try {
                var statement = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
                var conn = DatabaseManager.getConnection();
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.setString(1, user.username());
                    preparedStatement.setString(2, user.password());
                    preparedStatement.setString(3, user.email());
                    preparedStatement.executeUpdate();
                }
            } catch (Exception e) {
                System.out.println(e);
            }      
        }


        @Override
        public void deleteUsers() {
            try {
                var statement = "DELETE FROM users";
                var conn = DatabaseManager.getConnection();
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            } catch (Exception e) {
                System.out.println(e);
            }      
        }
    }
