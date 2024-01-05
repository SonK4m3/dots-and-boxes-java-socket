package org.example.protocol;

import com.google.gson.Gson;
import org.example.model.MakeTurn;
import org.example.model.MessageData;
import org.example.model.Player;

public class ClientProtocol {
    public static String findingPlayer(Player player) {
        return new Gson().toJson(new MessageData("start", new Gson().toJson(player)));
    }

    public static String movePlayer(MakeTurn makeTurn) {
        return new Gson().toJson(new MessageData("move", new Gson().toJson(makeTurn)));
    }

    public static String message(String message) {
        return new Gson().toJson(new MessageData("message", new Gson().toJson(message)));
    }
}
