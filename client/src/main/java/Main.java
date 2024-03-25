import java.util.Scanner;

import ui.PostLogin;
import ui.PreLogin;

public class Main {
    public static void main(String[] args) {
        String url = "http://localhost:8080";
        if (args.length == 1) {
            url = args[0];
        }
        PreLogin preLogin = new PreLogin(url);
        PostLogin postLogin = null;
        Scanner scanner = new Scanner(System.in);
        String command = "";
        boolean isLoggedIn = false;
        while (!command.equals("quit")) {
            System.out.println("Enter a command:");
            command = scanner.nextLine();
            if (!isLoggedIn) {
                String response = preLogin.runCommand(command);
                System.out.println(response);
                if(response.startsWith("AUTH")) {
                    postLogin = new PostLogin(url, response.substring(4));
                    isLoggedIn = true;
                }
                
            } else  {
                String response = postLogin.runCommand(command);
                System.out.println(response);
                if (response.startsWith("Logged out")) {
                    isLoggedIn = false;
                }
            } 
        }
        scanner.close();
        
    }
}