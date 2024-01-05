package org.example.protocol;

import com.google.gson.Gson;
import org.example.model.GameState;
import org.example.model.MessageData;

public class ServerProtocol {
    public static String gameState(GameState state) {
        return new Gson().toJson(new MessageData("state", new Gson().toJson(state)));
    }

    public static String message(String message) {
        return new Gson().toJson(new MessageData("message", new Gson().toJson(message)));
    }

    public static String startGame(String message) {
        return new Gson().toJson(new MessageData("start", new Gson().toJson(message)));
    }

    public static String createPlayer(String message) {
        return new Gson().toJson(new MessageData("player", new Gson().toJson(message)));
    }
}
