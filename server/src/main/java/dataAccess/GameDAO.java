package dataAccess;

import model.GameData;

import java.util.ArrayList;

public interface GameDAO {
    public void deleteGames();
    public int createGame(String gameName);
    public void deleteGame(int gameID);
    public GameData getGame(int gameID);
    public ArrayList<GameData> getGames();
    public void joinGame(int gameID, String username, String color);
}
