package dataAccess;

import model.GameData;
import java.util.ArrayList;

import com.google.gson.Gson;
import java.sql.Statement;

import chess.ChessGame;


public class DatabaseGameDAO implements GameDAO{


    public DatabaseGameDAO(){
        try {
            DatabaseManager.createDatabase();
            var statement = "CREATE TABLE IF NOT EXISTS games (gameID INTEGER PRIMARY KEY AUTO_INCREMENT, whiteUsername VARCHAR(255), blackUsername VARCHAR(255), gameName VARCHAR(255), gameData VARCHAR(255))";
            var conn = DatabaseManager.getConnection();
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
            }
        }catch (Exception e){
            System.out.println(e);
        }
    }

    @Override
    public void deleteGames(){
        try {
            var statement = "DELETE FROM games";
            var conn = DatabaseManager.getConnection();
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
            }
        }catch (Exception e){
            System.out.println(e);
        }
    }

    @Override
    public int createGame(String gameName){
        try {
            var statement = "INSERT INTO games (whiteUsername, blackUsername, gameName, gameData) VALUES (?, ?, ?, ?)";
            var conn = DatabaseManager.getConnection();
            try (var preparedStatement = conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setString(1, null);
                preparedStatement.setString(2, null);
                preparedStatement.setString(3, gameName);
                preparedStatement.setString(4, new Gson().toJson(new ChessGame()));
                preparedStatement.executeUpdate();
                try (var results = preparedStatement.getGeneratedKeys()) {
                    if (results.next()) {
                        
                        return results.getInt(1);
                    }
                }
            }
        }catch (Exception e){
            System.out.println(e);
        }
        return 0;
    }


    @Override
    public void deleteGame(int gameID){
        try {
            var statement = "DELETE FROM games WHERE gameID = ?";
            var conn = DatabaseManager.getConnection();
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setInt(1, gameID);
                preparedStatement.executeUpdate();
            }
        }catch (Exception e){
            System.out.println(e);
        }
    }

    @Override
    public ArrayList<GameData> getGames(){
        ArrayList<GameData> games = new ArrayList<>();
        try {
            var statement = "SELECT * FROM games";
            var conn = DatabaseManager.getConnection();
            try (var preparedStatement = conn.prepareStatement(statement)) {
                try (var results = preparedStatement.executeQuery()) {
                    while (results.next()) {
                        ChessGame game = new Gson().fromJson(results.getString("gameData"), ChessGame.class);
                        games.add(new GameData(results.getInt("gameID"), results.getString("whiteUsername"), results.getString("blackUsername"), results.getString("gameName"), game));
                    }
                }
            }
        }catch (Exception e){
            System.out.println(e);
        }
        return games;
    }

    @Override
    public GameData getGame(int gameID){
        try {
            var statement = "SELECT * FROM games WHERE gameID = ?";
            var conn = DatabaseManager.getConnection();
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setInt(1, gameID);
                try (var results = preparedStatement.executeQuery()) {
                    if (results.next()) {
                        ChessGame game = new Gson().fromJson(results.getString("gameData"), ChessGame.class);
                        return new GameData(results.getInt("gameID"), results.getString("whiteUsername"), results.getString("blackUsername"), results.getString("gameName"), game);
                    }
                }
            }
        }catch (Exception e){
            System.out.println(e);
        }
        return null;
    }

    @Override
    public GameData joinGame(int gameID, String username, String color){
        if(color.toUpperCase().equals("SPECTATOR")){
            return getGame(gameID);
        }
        try {
            if(color.toUpperCase().equals("WHITE") || color.toUpperCase().equals("BLACK")){
            var statement = "UPDATE games SET blackUsername = ? WHERE gameID = ?";
            if (color.toUpperCase().equals("WHITE")){
                statement = "UPDATE games SET whiteUsername = ? WHERE gameID = ?";
            }
            var conn = DatabaseManager.getConnection();
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setString(1, username);
                preparedStatement.setInt(2, gameID);
                preparedStatement.executeUpdate();
            }
            return getGame(gameID); 

        }
        }catch (Exception e){
            System.out.println(e);
        }
        return null;
    }

   public void setGameData(int gameID, ChessGame game){
        try {
            var statement = "UPDATE games SET gameData = ? WHERE gameID = ?";
            var conn = DatabaseManager.getConnection();
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setString(1, new Gson().toJson(game));
                preparedStatement.setInt(2, gameID);
                preparedStatement.executeUpdate();
            }
        }catch (Exception e){
            System.out.println(e);
        }
    }

    



    

   
    
}
