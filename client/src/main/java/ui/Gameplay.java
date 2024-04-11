package ui;

import chess.ChessMove;
import chess.ChessPosition;
import webSocket.WebSocketFacade;
import webSocketMessages.userCommands.MAKE_MOVE;

public class Gameplay {

    private String url;
    public String token;
    public ServerFacade serverFacade;
    public WebSocketFacade webSocketFacade;
    public int gameId;

    public Gameplay(String url, String token) {
        this.url = url;
        this.token = token;
        serverFacade = new ServerFacade(url);
    }

    public String runCommand(String command){
        String[] commandParts = command.toLowerCase().split(" ");
        String cmd = commandParts[0];
        String[] args = new String[commandParts.length - 1];
        for (int i = 1; i < commandParts.length; i++) {
            args[i - 1] = commandParts[i];
        }
        //commands are help, redraw, leave, move, resign, and highlight
        switch (cmd) {
            case "help":
                return "Commands: help, redraw, leave, move, resign, highlight";
            case "redraw":
                return "Redrawing board";
            case "leave":
                return "Leaving game";
            case "move":
                if(args.length != 2){
                    return "Invalid move command. Usage: move <start position> <end position>";
                }
                ChessPosition start = new ChessPosition(Integer.parseInt(cmd.split("")[0]), Integer.parseInt(cmd.split("")[1]));
                ChessPosition end = new ChessPosition(Integer.parseInt(cmd.split("")[2]), Integer.parseInt(cmd.split("")[3]));
                try{
                webSocketFacade.makeMove(new MAKE_MOVE(token, gameId, new ChessMove(start, end, null)));
                return "Move made successfully";
                } catch (Exception e){
                    return "Invalid move command. Usage: move <start position> <end position>";
                }
            case "resign":
                return "Resigning game";
            case "highlight":
                return "Highlighting board";
            default:
                return "Commands: help, redraw, leave, move, resign, highlight";
        }
        


    }



}
