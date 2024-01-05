package org.example.client;

import com.google.gson.Gson;
import org.example.model.MessageData;
import org.example.model.Player;
import org.example.protocol.ClientProtocol;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Scanner;

public class LocalClient extends WebSocketClient {

    private final Gson gson = new Gson();
    private Player player = new Player(1, "Sonkame");

    public LocalClient(URI serverUri) {
        super(serverUri);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("Connected to WebSocket server");
        String playerJson = ClientProtocol.findingPlayer(player);
        send(playerJson);

        sendMessageAfterConnecting();
    }

    @Override
    public void onMessage(String message) {
        System.out.println("Received message: " + message);
        // Handle your message processing logic here
        handleServerMessage(message);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("Connection closed: " + reason);
    }

    @Override
    public void onError(Exception ex) {
        ex.printStackTrace();
    }

    private void sendMessageAfterConnecting() {
        // Get a message from the user and send it to the server
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter a message to send to the server: ");
        new Thread(() -> {
            while (true) {
                String userMessage = scanner.nextLine();
                String sendMessage = gson.toJson(new MessageData("message", userMessage));
                send(sendMessage);
            }
        }).start();
    }

    private void handleServerMessage(String message) {
        // Handle server messages here
        // For example, you can print the message or implement specific logic based on the content

        // If the message contains information about the opponent, you can extract and handle it
        if (message.startsWith("You are paired with ")) {
            String opponentName = message.substring("You are paired with ".length());
            System.out.println("Your opponent is: " + opponentName);
            // You can use opponentName in your game logic
        }
    }

    public static void main(String[] args) {
        try {
            // Change the URI to match your WebSocket server URL and port
            LocalClient client = new LocalClient(new URI("ws://10.19.21.21:8080"));
            client.connect();

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
