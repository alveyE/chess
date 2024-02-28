package dataAccess;


import java.util.ArrayList;

import model.GameData;
public class MemoryGameDAO implements GameDAO{

    ArrayList<GameData> games = new ArrayList<GameData>();
   

    @Override
    public void deleteGames() {
    }
    
}
