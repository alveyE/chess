package service;

import dataAccess.DataAccessException;
import dataAccess.MemoryGameDAO;
import model.GameData;

import java.util.ArrayList;

public class GameService {

    private final static MemoryGameDAO gameDAO = new MemoryGameDAO();

    public void clear() throws DataAccessException{
        gameDAO.deleteGames();
    }

    public int createGame(GameData game, String authToken) throws DataAccessException, ResponseException{
        if(game == null || authToken == null){
            throw new ResponseException(400, "{\"message\": \"Error: Bad Request\"}");
        }
        if(!UserService.validateAuth(authToken)){
            throw new ResponseException(401, "{\"message\": \"Error: unauthorized\"}");
        }


        return gameDAO.createGame(game.gameName());

    }

    public void deleteGame(int gameID, String authToken) throws DataAccessException, ResponseException{
        if(authToken == null){
            throw new ResponseException(400, "{\"message\": \"Error: Bad Request\"}");
        }
        if(!UserService.validateAuth(authToken)){
            throw new ResponseException(401, "{\"message\": \"Error: unauthorized\"}");
        }
        if(gameDAO.getGame(gameID) == null){
            throw new ResponseException(401, "{\"message\": \"Error: Game does not exist\"}");
        }
        gameDAO.deleteGame(gameID);
    }

    public ArrayList<GameData> listGames(String authToken) throws DataAccessException, ResponseException{
        if(authToken == null){
            throw new ResponseException(400, "{\"message\": \"Error: Bad Request\"}");
        }
        if(!UserService.validateAuth(authToken)){
            throw new ResponseException(401, "{\"message\": \"Error: unauthorized\"}");
        }
        return gameDAO.getGames();
    }

    public void joinGame(int gameID, String authToken, String color) throws DataAccessException, ResponseException{
        if(authToken == null){
            throw new ResponseException(400, "{\"message\": \"Error: Bad Request\"}");
        }
        if(!UserService.validateAuth(authToken)){
            throw new ResponseException(401, "{\"message\": \"Error: unauthorized\"}");
        }
        if(gameDAO.getGame(gameID) == null){
            throw new ResponseException(400, "{\"message\": \"Error: Game does not exist\"}");
        }
        if(color == null){
            gameDAO.joinGame(gameID, UserService.getUsername(authToken), "SPECTATOR");
            return;
        }
        if(!color.equals("WHITE") && !color.equals("BLACK")){
            throw new ResponseException(403, "{\"message\": \"Error: Invalid Color\"}");
        }
        GameData game = gameDAO.getGame(gameID);
        if (game != null) {
            if ((color.equals("WHITE") && game.whiteUsername() != null) ||
                (color.equals("BLACK") && game.blackUsername() != null)) {
                throw new ResponseException(403, "{\"message\": \"Error: Color already taken\"}");
            }
        }
        gameDAO.joinGame(gameID, UserService.getUsername(authToken), color);   
    }
    

    
}
