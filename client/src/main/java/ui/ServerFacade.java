package ui;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.List;
import java.util.Map;

import javax.management.RuntimeErrorException;

import com.google.gson.Gson;

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
