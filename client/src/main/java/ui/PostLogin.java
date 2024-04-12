package ui;

import chess.ChessGame.TeamColor;
import model.JoinGameRequest;
import webSocket.WebSocketFacade;
import webSocketMessages.userCommands.JoinObserver;
import webSocketMessages.userCommands.JoinPlayer;

public class PostLogin {

    private final String url;
    public String token;
    public ServerFacade serverFacade;
    public WebSocketFacade webSocketFacade;

    public PostLogin(String url, String token) {
        this.url = url;
        this.token = token;
        serverFacade = new ServerFacade(url);
        try{
        this.webSocketFacade = new WebSocketFacade();
        }catch(Exception e){
            System.out.println("Error: " + e.getMessage());
        }
    }

    public String runCommand(String command){
        Gameplay gameplay = new Gameplay(url, token);

        String[] commandParts = command.toLowerCase().split(" ");
        String cmd = commandParts[0];
        String[] args = new String[commandParts.length - 1];
        for (int i = 1; i < commandParts.length; i++) {
            args[i - 1] = commandParts[i];
        }
        switch (cmd) {
            case "create":
                if (args.length != 1) {
                    return "Usage: create <gameName>";
                }
                return serverFacade.createGame(token, args[0]).toString();
            case "list":
                if (args.length != 0) {
                    return "Usage: list";
                }
                return serverFacade.listGames(token).toString();
            case "join":
                if (args.length != 2 && args.length != 1) {
                    return "Usage: join <gameId> <color>";
                }
                String color = "";
                if (args.length == 2) {
                    color = args[1];
                }
                JoinGameRequest req = new JoinGameRequest(Integer.parseInt(args[0]), color);
                //var res = serverFacade.joinGame(token, req);
                try{
                webSocketFacade.joinPlayer(new JoinPlayer(token, Integer.parseInt(args[0]), color == "white" ? TeamColor.WHITE : TeamColor.BLACK));
                }catch(Exception e){
                    return "Error: " + e.getMessage();
                }
                return gameplay.playGame();
                
            case "observe":
                if (args.length != 1) {
                    return "Usage: observe <gameId>";
                }
                JoinGameRequest re = new JoinGameRequest(Integer.parseInt(args[0]), "");
                //serverFacade.joinGame(cmd, re);
                try{
                webSocketFacade.observe(new JoinObserver(token, Integer.parseInt(args[0])));
                }catch(Exception e){
                    return "Error: " + e.getMessage();
                }
                return gameplay.playGame();
            case "logout":
                serverFacade.logout(token);
                return "Logged out!";            
            case "quit":
                return "Goodbye!";
            default:
                return "create <gameName> - to create a game\nlist - to list all games\njoin <gameID> <color> - to join a game\nquit - to quit";
        }
    }
    
}
