package service;

import dataAccess.DataAccessException;
import dataAccess.MemoryGameDAO;

public class GameService {

    private final static MemoryGameDAO gameDAO = new MemoryGameDAO();

    public void clear() throws DataAccessException{
        gameDAO.deleteGames();
    }

    
}
