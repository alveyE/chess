package dataAccess;


import java.util.ArrayList;

import model.GameData;
import model.UserData;
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


    @Override
    public void deleteGame(int gameID) {
        for (GameData game : games) {
            if (game.gameID() == gameID) {
                games.remove(game);
                break;
            }
        }
    }

    @Override
    public GameData getGame(int gameID) {
        for (GameData game : games) {
            if (game.gameID() == gameID) {
                return game;
            }
        }
        return null;
    }

    @Override
    public ArrayList<GameData> getGames() {
        return games;
    }

    @Override
    public GameData joinGame(int gameID, String username, String color) {
        for (GameData game : games) {
            if (game.gameID() == gameID) {
                if(color.equals("WHITE")){
                    int index = games.indexOf(game);
                    if (index != -1) {
                        games.remove(index);
                        GameData updatedGame = new GameData(game.gameID(), username, game.blackUsername(), game.gameName(), game.game());
                        games.add(index, updatedGame);
                    }                }else if(color.equals("BLACK")){
                        int index = games.indexOf(game);
                        if (index != -1) {
                            games.remove(index);
                            GameData updatedGame = new GameData(game.gameID(), game.whiteUsername(), username, game.gameName(), game.game());
                            games.add(index, updatedGame);
                        }                }
                break;
            }
        }
        return null;
    }
    
}
