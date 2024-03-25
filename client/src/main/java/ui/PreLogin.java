package ui;

public class PreLogin {

    private final String url;
    public ServerFacade serverFacade;

    public PreLogin(String url) {
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
            case "register":
                if (args.length != 3) {
                    return "Usage: register <username> <password> <email>";
                }
                return serverFacade.register(args[0], args[1], args[2]).toString();
            case "login":
                if (args.length != 2) {
                    return "Usage: login <username> <password>";
                }
                if(serverFacade.login(args[0], args[1]) == null){
                    return "Invalid username or password";
                }
                else return "Successfully logged in!";
                
            case "quit":
                return "Goodbye!";
            default:
                return "register <username> <password> <email> - to create an account\nlogin <username> <password> - to play chess\nquit - playing chess\nhelp - with possible commands";
        }


        
    }


}
