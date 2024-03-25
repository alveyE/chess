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
        PostLogin postLogin = new PostLogin(url);
        Scanner scanner = new Scanner(System.in);
        String command = "";
        boolean isLoggedIn = false;
        while (!command.equals("quit")) {
            System.out.println("Enter a command:");
            command = scanner.nextLine();
            if (!isLoggedIn) {
                String response = preLogin.runCommand(command);
                System.out.println(response);
                if (response.startsWith("Successfully logged in")) {
                    isLoggedIn = true;
                }
            } else if (isLoggedIn ) {
                String response = postLogin.runCommand(command);
                System.out.println(response);
                if (response.startsWith("Successfully logged out")) {
                    isLoggedIn = false;
                }
            } 
        }
        scanner.close();
        
    }
}