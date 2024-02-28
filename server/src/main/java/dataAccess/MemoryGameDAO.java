package dataAccess;


import java.util.ArrayList;

import model.GameData;
public class MemoryGameDAO implements GameDAO{

    ArrayList<GameData> games = new ArrayList<GameData>();
   

    @Override
    public void deleteGames() {
        games = new ArrayList<GameData>();
    }

    @Override
    public int createGame(String gameName) {
        int randomId = (int) (Math.random() * 1000);
        for (GameData game : games) {
            if (game.gameID() == randomId) {
                randomId = (int) (Math.random() * 1000);
            }
        }
        GameData game = new GameData(randomId, null, null, gameName, null);
        games.add(game);
        return game.gameID();
    }
    
}
