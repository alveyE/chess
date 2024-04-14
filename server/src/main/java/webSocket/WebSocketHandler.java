package webSocket;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import com.google.gson.Gson;

import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessGame.TeamColor;
import dataAccess.AuthDAO;
import dataAccess.DatabaseAuthDAO;
import dataAccess.DatabaseGameDAO;
import dataAccess.DatabaseUserDAO;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import model.AuthData;
import model.GameData;
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
                    //check for joining wrong team
                    if(gameDAO.getGame(gameId).blackUsername() == authData.username() && color == TeamColor.WHITE){
                        connectionManager.error(session, "Wrong team");
                        return;
                    }
                    if(gameDAO.getGame(gameId).whiteUsername() == authData.username() && color == TeamColor.BLACK){
                        connectionManager.error(session, "Wrong team");
                        return;
                    }
                    if(gameDAO.getGame(gameId).blackUsername() != null && gameDAO.getGame(gameId).whiteUsername() != null){
                        //join as observer
                            connectionManager.addConnection(authToken, session, gameId);
                            connectionManager.broadcast(authToken, new Notification("User " + authData.username() + " has joined the game as an observer"), gameId);
                            connectionManager.respond(authToken, gameId, gameDAO.getGame(gameId));
                        
                        
                        return;
                    }
                    if(color != TeamColor.BLACK && color != TeamColor.WHITE){
                        connectionManager.error(session, "Invalid team color");
                        return;
                    }
                    connectionManager.addConnection(authToken, session, gameId);
                    gameService.joinGame(gameId, authToken, color == TeamColor.WHITE ? "white" : "black");
                    connectionManager.broadcast(authToken, new Notification("User " + authData.username() + " has joined the game as a player"), gameId);
                    connectionManager.respond(authToken, gameId, gameDAO.getGame(gameId));
                }else{
                    connectionManager.error(session, "Game does not exist");
                
                }
            }else{
                connectionManager.error(session, "Invalid auth token");
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
                }else{
                    connectionManager.error(session, "Game does not exist");
                }
            }else{
                connectionManager.error(session, "Invalid auth token");
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
        TeamColor playerColor = gameDAO.getGame(gameId).blackUsername() == authDAO.getAuth(authToken).username() ? TeamColor.BLACK : TeamColor.WHITE;

        try{
            AuthData authData = authDAO.getAuth(authToken);
            if(authData != null){
                if(gameDAO.getGame(gameId) != null){
                    //check if moving for opponent
                    ChessPiece piece = gameDAO.getGame(gameId).game().getBoard().getPiece(move.getStartPosition());
                    if(piece == null){
                        connectionManager.error(session, "Invalid move");
                        return;
                    }
                    if(playerColor == TeamColor.WHITE && piece.getTeamColor() == TeamColor.BLACK){
                        connectionManager.error(session, "Not your piece");
                        return;
                    }
                    if(playerColor == TeamColor.BLACK && piece.getTeamColor() == TeamColor.WHITE){
                        connectionManager.error(session, "Not your piece");
                        return;
                    }

                    if(playerColor == TeamColor.WHITE && gameDAO.getGame(gameId).game().getTeamTurn() == TeamColor.BLACK){
                        connectionManager.error(session, "Not your turn");
                        return;
                    }
                    if(playerColor == TeamColor.BLACK && gameDAO.getGame(gameId).game().getTeamTurn() == TeamColor.WHITE){
                        connectionManager.error(session, "Not your turn");
                        return;
                    }
            
                    if(!gameDAO.getGame(gameId).blackUsername().equals(authData.username()) && !gameDAO.getGame(gameId).whiteUsername().equals(authData.username())){
                        connectionManager.error(session, "Not a player");
                        return;
                    }
                    
                
                    if(gameDAO.getGame(gameId).game().gameIsOver()){
                        connectionManager.error(session, "Game is over");
                        return;
                    }

                    if(gameDAO.getGame(gameId).game().getTeamTurn() != playerColor){
                        connectionManager.error(session, "Not your turn");
                        return;
                    }
                    gameDAO.getGame(gameId).game().makeMove(move);
                    connectionManager.makeMove(authToken, gameId, gameDAO.getGame(gameId).game());
                    connectionManager.broadcast(authToken, new Notification("User " + authData.username() + " has made a move"), gameId);
                }else{
                    connectionManager.error(session, "Game does not exist");
                }
            }else{
                connectionManager.error(session, "Invalid auth token");
            }
        } catch (Exception e){
            connectionManager.error(session, "Error");
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
                    GameData gameData = gameDAO.getGame(gameId);
                    
                    if(gameData.game().gameIsOver()){
                        connectionManager.error(session, "Game is over");
                        return;
                    }
                    if(!gameDAO.getGame(gameId).blackUsername().equals(authData.username()) && !gameDAO.getGame(gameId).whiteUsername().equals(authData.username())){
                        connectionManager.error(session, "Not a player");
                        return;
                    }

                    GameData resignedGame = gameDAO.getGame(gameId);
                    resignedGame.game().setResign(true);
                    gameDAO.setGameData(gameId, resignedGame.game());
                    connectionManager.resign(authToken, gameId, new Notification("User " + authData.username() + " has resigned"));
                    connectionManager.removeConnection(authToken);

                }else{
                    connectionManager.error(session, "Game does not exist");
                }
            }else{
                connectionManager.error(session, "Invalid auth token");
            }
        } catch (Exception e){
            connectionManager.error(session, "error");
            e.printStackTrace();
        }

    }




    @OnWebSocketError
    public void onError(Session session, Throwable throwable) {
        // Log the error or take other appropriate action
        System.err.println("WebSocket Error: " + throwable.getMessage());
    }
    

    
}
