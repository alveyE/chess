package service;

import dataAccess.MemoryGameDAO;

public class GameService {

    private final static MemoryGameDAO gameDAO = new MemoryGameDAO();

    public void clear(){
        gameDAO.deleteGames();
    }

    
}
