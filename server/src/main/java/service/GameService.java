package service;

import dataAccess.DataAccessException;
import dataAccess.MemoryGameDAO;
import model.GameData;

public class GameService {

    private final static MemoryGameDAO gameDAO = new MemoryGameDAO();

    public void clear() throws DataAccessException{
        gameDAO.deleteGames();
    }

    // public int createGame(GameData game, String authToken) throws DataAccessException, ResponseException{
    //     if(game == null || authToken == null){
    //         throw new ResponseException(400, "Bad Request");
    //     }
    //     if(!UserService.validateAuth(authToken)){
    //         throw new ResponseException(400, "Unauthorized");
    //     }
    //     if(gameDAO.getGame(game.gameID()) != null){
    //         throw new ResponseException(400, "Game already exists");
    //     }

    //     return gameDAO.addGame(game);

    // }

    
}
