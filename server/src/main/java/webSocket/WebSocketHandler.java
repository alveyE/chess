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
import webSocketMessages.userCommands.Resign;
import webSocketMessages.userCommands.JoinObserver;
import webSocketMessages.userCommands.JoinPlayer;
import webSocketMessages.userCommands.Leave;
import webSocketMessages.userCommands.MakeMove;
import webSocketMessages.userCommands.UserGameCommand;

@WebSocket
public class WebSocketHandler {

    private final ConnectionManager connectionManager = new ConnectionManager();
    private final GameService gameService = new GameService();
    private final AuthDAO authDAO = new DatabaseAuthDAO();
    private final UserDAO userDAO = new DatabaseUserDAO();
    private final GameDAO gameDAO = new DatabaseGameDAO();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        switch(command.getCommandType()){
            case JOIN_PLAYER:
                joinGame(message, session);
                break;
            case JOIN_OBSERVER:
                joinObserver(message, session);
                break;
            case MAKE_MOVE:
                makeMove(message, session);
                break;
            case LEAVE:
                leave(message, session);
                break;
            case RESIGN:
                resign(message, session);
                break;
        }

    }

    public void joinGame(String str, Session session){
        JoinPlayer joinPlayer = new Gson().fromJson(str, JoinPlayer.class);
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
        JoinObserver joinObserver = new Gson().fromJson(str, JoinObserver.class);
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
        MakeMove makeMove = new Gson().fromJson(str, MakeMove.class);
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
        Leave leave = new Gson().fromJson(str, Leave.class);
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
        Resign resign = new Gson().fromJson(str, Resign.class);
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
