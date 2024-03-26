package ui;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.management.RuntimeErrorException;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import chess.ChessGame;
import model.AuthData;
import model.GameData;
import model.JoinGameRequest;
import model.UserData;

public class ServerFacade {
    
    private final String url;

    public ServerFacade(String url) {
        this.url = url;
    }


    public AuthData register(String username, String password, String email) throws RuntimeException{
        String path = "/user";
        UserData user = new UserData(username, password, email);
        return sendRequest("POST", path, user, null, AuthData.class);
    }

    public AuthData login(String username, String password) throws RuntimeException{
        String path = "/session";
        var req = Map.of("username", username, "password", password);
        return sendRequest("POST", path, req, null, AuthData.class);
    }

    public void logout(String token) throws RuntimeException{
        String path = "/session";
        sendRequest("DELETE", path, null, token, null);
    }

    public void clear() throws RuntimeException{
        String path = "/db";
        sendRequest("DELETE", path, null, null, null);
    }

    public GameData createGame(String token, String gameName) throws RuntimeException{
        String path = "/game";
        GameData game = new GameData(0, null, null, gameName, new ChessGame());
        return sendRequest("POST", path, game, token, GameData.class);
    }

    public class GameListResponse {
        public List<GameData> games;

        @Override
        public String toString() {
            return new Gson().toJson(games);
        }
    }

    public GameListResponse listGames(String token) throws RuntimeException{
        String path = "/game";
        return sendRequest("GET", path, null, token, GameListResponse.class);
    }

   public GameData joinGame(String token, JoinGameRequest req) throws RuntimeException{
        String path = "/game";
        return sendRequest("PUT", path, req, token, GameData.class);
    }


    private <T> T sendRequest(String method, String path, Object req, String header, Class<T> format) throws RuntimeException {
        try {
            URI uri = new URI(url + path);
            HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
            http.setRequestMethod(method);
            if (header != null) {
                http.setRequestProperty("Authorization", header);
            }
            writeRequestBody(req, http);
            http.connect();
            throwFailure(http);
            if(format == null) return null;         

            return readBody(http, format);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }


    private void writeRequestBody(Object req, HttpURLConnection http) {
        if (req != null) {
            http.setDoOutput(true);
            try (OutputStream out = http.getOutputStream()) {
                out.write(new Gson().toJson(req).getBytes());
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> format) {
        try (var in = http.getInputStream()) {
            return new Gson().fromJson(new String(in.readAllBytes()), format);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private static void throwFailure(HttpURLConnection http) {
        try {
            if (http.getResponseCode() != 200) {
                throw new RuntimeException(http.getResponseMessage());
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }


}
