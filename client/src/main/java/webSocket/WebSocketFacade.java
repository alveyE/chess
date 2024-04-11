package webSocket;

import java.net.URI;

import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.serverMessages.ServerMessage;

import javax.websocket.ContainerProvider;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

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
        System.out.println("Notification");
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
        Notification error = new Gson().fromJson(message, Notification.class);
        System.out.println(error.getMessage());
    }

    public void sendMessage(String message) throws Exception{
        this.session.getBasicRemote().sendText(message);
    }

    @Override
    public void onOpen(Session session, EndpointConfig config){
        System.out.println("Connected to server");
    }

    
}
