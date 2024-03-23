package ui;

public class PostLogin {

    private final String url;
    public String token;
    public ServerFacade serverFacade;

    public PostLogin(String url) {
        this.url = url;
        serverFacade = new ServerFacade(url);
    }

    public String runCommand(String command){
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
            case "quit":
                return "Goodbye!";
            default:
                return "create <gameName> - to create a game\nlist - to list all games\nquit - to quit";
        }
    }
    
}
