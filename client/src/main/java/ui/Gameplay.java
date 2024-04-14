package ui;

import java.util.Scanner;

import chess.ChessMove;
import chess.ChessPosition;
import webSocket.WebSocketFacade;
import webSocketMessages.userCommands.MakeMove;

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

    public String playGame(){
        //print game board
        DrawBoard drawBoard = new DrawBoard(webSocketFacade.board);
        drawBoard.drawWhite();
        
        Scanner input = new Scanner(System.in);
        System.out.println("Enter command: ");
        String command = input.nextLine();
        while(!command.equals("leave")){
            System.out.println(runCommand(command));
            System.out.println("Enter command: ");
            command = input.nextLine();
        }
        return "Goodbye!";
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
                DrawBoard drawBoard = new DrawBoard(webSocketFacade.board);
                drawBoard.drawWhite();
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
                webSocketFacade.makeMove(new MakeMove(token, gameId, new ChessMove(start, end, null)));
                //draw board
                DrawBoard drawBoard2 = new DrawBoard(webSocketFacade.board);
                drawBoard2.drawWhite();
                
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
