package webSocket;

import java.net.URI;

import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.serverMessages.Error;

import webSocketMessages.userCommands.JoinObserver;
import webSocketMessages.userCommands.JoinPlayer;
import webSocketMessages.userCommands.Leave;
import webSocketMessages.userCommands.MakeMove;
import webSocketMessages.userCommands.Resign;


import javax.websocket.*;

import com.google.gson.Gson;

import chess.ChessBoard;
import ui.DrawBoard;
import webSocketMessages.serverMessages.ServerMessage;

public class WebSocketFacade extends Endpoint {

    public Session session;
    public static ChessBoard board = new ChessBoard();

    public WebSocketFacade() throws Exception{
        URI uri = new URI("ws://localhost:8080/connect");
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();

        this.session = container.connectToServer(this, uri);
        this.session.addMessageHandler(new MessageHandler.Whole<String>(){

            @OnMessage
            public void onMessage(String message){
                ServerMessage action = new Gson().fromJson(message, ServerMessage.class);
                switch(action.getServerMessageType()){
                    case NOTIFICATION:
                        notification(message);
                        break;
                    case LOAD_GAME:
                        loadGame(message);
                        break;
                    case ERROR:
                        error(message);
                    
                        break;
                }
            
            }
        });


    }

    private void notification(String message){
        Notification notification = new Gson().fromJson(message, Notification.class);
        System.out.println(notification.getMessage());
    }

    private void loadGame(String message){
        System.out.println("Load Game");
        LoadGame loadGame = new Gson().fromJson(message, LoadGame.class);
        DrawBoard drawBoard = new DrawBoard(loadGame.game.getBoard());
        System.out.println(drawBoard.drawWhite());
        board = loadGame.game.getBoard(); 
    }

    private void error(String message){
        System.out.println("Error");
        Error error = new Gson().fromJson(message, Error.class);
        System.out.println(error.errorMessage);
    }

    public void sendMessage(String message) throws Exception{
        this.session.getBasicRemote().sendText(message);
    }

    @OnOpen
    public void onOpen(Session session, EndpointConfig config){
        System.out.println("Connected to server");
    }

    public void joinPlayer(JoinPlayer join) throws Exception{
        sendMessage(new Gson().toJson(join));
    }

    public void makeMove(MakeMove move) throws Exception{
        sendMessage(new Gson().toJson(move));
    }

    public void observe(JoinObserver join) throws Exception{
        sendMessage(new Gson().toJson(join));
    }

    public void leaveGame(Leave leave) throws Exception{
        sendMessage(new Gson().toJson(leave));
    }

    public void resign(Resign resign) throws Exception{
        sendMessage(new Gson().toJson(resign));
    }

    



    
}
