package webSocket;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import com.google.gson.Gson;

import chess.ChessMove;
import chess.ChessGame.TeamColor;
import dataAccess.AuthDAO;
import dataAccess.DatabaseAuthDAO;
import dataAccess.DatabaseGameDAO;
import dataAccess.DatabaseUserDAO;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import model.AuthData;
import service.GameService;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.userCommands.JOIN_OBSERVER;
import webSocketMessages.userCommands.JOIN_PLAYER;
import webSocketMessages.userCommands.LEAVE;
import webSocketMessages.userCommands.MAKE_MOVE;
import webSocketMessages.userCommands.RESIGN;
import webSocketMessages.userCommands.UserGameCommand;

@WebSocket
public class WebSockets {

    private final ConnectionManager connectionManager = new ConnectionManager();
    private final GameService gameService = new GameService();
    private final AuthDAO authDAO = new DatabaseAuthDAO();
    private final UserDAO userDAO = new DatabaseUserDAO();
    private final GameDAO gameDAO = new DatabaseGameDAO();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        //add switch

    }

    public void joinGame(String str, Session session){
        JOIN_PLAYER joinPlayer = new Gson().fromJson(str, JOIN_PLAYER.class);
        int gameId = joinPlayer.getGameID();
        String authToken = joinPlayer.getAuthString();
        TeamColor color = joinPlayer.getPlayerColor();

        try{
            AuthData authData = authDAO.getAuth(authToken);
            if(authData != null){
                if(gameDAO.getGame(gameId) != null){
                    connectionManager.addConnection(authToken, session, gameId);
                    gameService.joinGame(gameId, authToken, color == TeamColor.WHITE ? "white" : "black");
                    connectionManager.broadcast(authToken, new Notification("User " + authData.username() + " has joined the game as a player"), gameId);
                    connectionManager.respond(authToken, gameId, gameDAO.getGame(gameId));
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    public void joinObserver(String str, Session session) {
        JOIN_OBSERVER joinObserver = new Gson().fromJson(str, JOIN_OBSERVER.class);
        int gameId = joinObserver.getGameID();
        String authToken = joinObserver.getAuthString();

        try{
            AuthData authData = authDAO.getAuth(authToken);
            if(authData != null){
                if(gameDAO.getGame(gameId) != null){
                    connectionManager.addConnection(authToken, session, gameId);
                    connectionManager.broadcast(authToken, new Notification("User " + authData.username() + " has joined the game as an observer"), gameId);
                    connectionManager.respond(authToken, gameId, gameDAO.getGame(gameId));
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void makeMove(String str, Session session){
        MAKE_MOVE makeMove = new Gson().fromJson(str, MAKE_MOVE.class);
        int gameId = makeMove.getGameID();
        String authToken = makeMove.getAuthString();
        ChessMove move = makeMove.move;

        try{
            AuthData authData = authDAO.getAuth(authToken);
            if(authData != null){
                if(gameDAO.getGame(gameId) != null){
                    gameDAO.getGame(gameId).game().makeMove(move);
                    connectionManager.makeMove(authToken, gameId, gameDAO.getGame(gameId).game());
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        
        }

    }

    public void leave(String str, Session session){
        LEAVE leave = new Gson().fromJson(str, LEAVE.class);
        int gameId = leave.getGameID();
        String authToken = leave.getAuthString();

        try{
            AuthData authData = authDAO.getAuth(authToken);
            if(authData != null){
                if(gameDAO.getGame(gameId) != null){
                    connectionManager.removeConnection(authToken);
                    connectionManager.broadcast(authToken, new Notification("User " + authData.username() + " has left the game"), gameId);
                    connectionManager.respond(authToken, gameId, gameDAO.getGame(gameId));
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void resign(String str, Session session){
        RESIGN resign = new Gson().fromJson(str, RESIGN.class);
        int gameId = resign.getGameID();
        String authToken = resign.getAuthString();

        try{
            AuthData authData = authDAO.getAuth(authToken);
            if(authData != null){
                if(gameDAO.getGame(gameId) != null){
                    connectionManager.removeConnection(authToken);
                    connectionManager.broadcast(authToken, new Notification("User " + authData.username() + " has resigned"), gameId);
                    connectionManager.respond(authToken, gameId, gameDAO.getGame(gameId));
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }

    }





    

    
}
