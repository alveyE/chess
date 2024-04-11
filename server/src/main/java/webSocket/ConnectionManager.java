package webSocket;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.jetty.websocket.api.Session;

import com.google.gson.Gson;

import chess.ChessGame;
import model.GameData;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.serverMessages.Error;


public class ConnectionManager {


    public final ConcurrentHashMap<String, Connection> connections = new ConcurrentHashMap<>();

    public void addConnection(String user, Session session, int gameID) {
        Connection connection = new Connection(user, session, gameID);
        connections.put(user, connection);
    }

    public void removeConnection(String user) {
        connections.remove(user);
    }

    public void broadcast(String user, ServerMessage message, int gameID) {
        connections.forEach((key, connection) -> {
            if(connection.session.isOpen() && connection.gameID == gameID && !connection.user.equals(user)){
                try {
                    connection.send(message.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
  
        });
        
    }

    public void respond(String user, int gameID, GameData gameData) {
        connections.forEach((key, connection) -> {
            if (connection.session.isOpen() && connection.gameID == gameID && !connection.user.equals(user)) {
                try {
                    connection.send(new Gson().toJson(new LoadGame(gameData.game())));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void error(Session session, String message){
        try {
            session.getRemote().sendString(new Gson().toJson(new Error(message)));
        } catch (IOException e) {
            e.printStackTrace();
            }
    }

    public void makeMove(String user, int gameID, ChessGame move) throws IOException{
        connections.forEach((key, connection) -> {
            if (connection.session.isOpen() && connection.gameID == gameID) {
                try {
                    connection.send(new Gson().toJson(new LoadGame(move)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }


    
    
}

