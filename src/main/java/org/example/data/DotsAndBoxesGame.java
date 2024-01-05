package org.example.data;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.example.model.*;
import org.example.protocol.ServerProtocol;
import org.example.server.DotsAndBoxesServer;
import org.java_websocket.WebSocket;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DotsAndBoxesGame {
    public GameState state = new GameState();
    private final Map<Player, WebSocket> playerSockets = new ConcurrentHashMap<>();
    private Room room = null;
    private final Map<Player, Score> playerPlaying = new ConcurrentHashMap<>();
    private DotsAndBoxesServer server;
    public DotsAndBoxesGame(Room room, DotsAndBoxesServer server) {
        this.room = room;
        this.server = server;
    }

    public void startGame() {
        sendToPlayers(ServerProtocol.startGame("Game starting!"));
        sendToPlayers(ServerProtocol.gameState(state));

        // Add your game initialization logic here
        // For example, initialize the game board, set the initial player, etc.

//        gameExecutor.execute(() -> {
        // Game loop or any other game logic
//            while (!Thread.currentThread().isInterrupted()) {
        // Example: Send a message to players
//                sendToPlayers("It's player 1's turn!");
//                String player1Move = receiveFromPlayer(playerSockets.get(player1));
//                processMove(player1Move);

        // Check game conditions, switch player turn, etc.

        // Repeat for player 2
//                sendToPlayers("It's player 2's turn!");
//                String player2Move = receiveFromPlayer(player2);
//                processMove(player2Move);

        // Check game conditions, switch player turn, etc.

        // Repeat the game loop
//            }
//        });
    }

    public void connectPlayer(WebSocket webSocket, Player player, Playing playing) {
        if (state.getPlayerAtTurn() == null) {
            state.setPlayerAtTurn(player);
        }
        if (playerSockets.containsValue(webSocket)) {
            return;
        }
        playerSockets.put(player, webSocket);
        playerPlaying.put(player, new Score(-1, 0, playing));
        state.getConnectedPlayers().add(player);
    }

    public void disconnectPlayer(Player player) {
        state.getConnectedPlayers().remove(player);
        playerSockets.remove(player);
    }

    private void broadcast(GameState gameState) {
        playerSockets.values().forEach((sk) -> {
            sk.send(new Gson().toJson(gameState));
        });
    }

    private void finishTurn(WebSocket player, int x, int y, int order) {
        if (state.getField()[x][y] != null || state.getWinningPlayer() != -1) {
            return;
        }
        Player currentPlayer = state.getPlayerAtTurn();
        System.out.println(currentPlayer.getName() + ": " + x + " " + y + " " + order);

        if (playerSockets.get(currentPlayer) != player) {
            return;
        }

        state.setRope(x, y, new Rope(currentPlayer, order));
        boolean gotTheBox = false;
        if (order == 1) { // vertical
            if (x > GameState.DOTS) {
                List<Rope> boxA = Arrays.asList(
                        state.getField()[y][x - GameState.DOTS - 1], state.getField()[y + 1][x - GameState.DOTS - 1],
                        state.getField()[x - 1][y], state.getField()[x][y]); // top, down, left, right
                if (areNonNull(boxA)) {
                    state.setBox(y, x - GameState.DOTS - 1, currentPlayer);
                    gotTheBox = true;
                }
            }

            if (x < GameState.DOTS + GameState.BOXES) {
                List<Rope> boxB = Arrays.asList(
                        state.getField()[y][x - GameState.DOTS], state.getField()[y + 1][x - GameState.DOTS],
                        state.getField()[x + 1][y], state.getField()[x][y]); // top, down, right, left
                if (areNonNull(boxB)) {
                    state.setBox(y, x - GameState.DOTS, currentPlayer);
                    gotTheBox = true;
                }
            }
        } else { // horizontal
            if (x > 0) {
                List<Rope> boxA = Arrays.asList(
                        state.getField()[y + GameState.DOTS][x - 1], state.getField()[y + GameState.DOTS + 1][x - 1],
                        state.getField()[x - 1][y], state.getField()[x][y]); // left, right, top, down
                if (areNonNull(boxA)) {
                    state.setBox(x - 1, y, currentPlayer);
                    gotTheBox = true;
                }
            }

            if (x < GameState.DOTS - 1) {
                List<Rope> boxB = Arrays.asList(
                        state.getField()[y + GameState.DOTS][x], state.getField()[x + GameState.DOTS + 1][x],
                        state.getField()[x + 1][y], state.getField()[x][y]); // left, right, down, top
                if (areNonNull(boxB)) {
                    state.setBox(x, y, currentPlayer);
                    gotTheBox = true;
                }
            }
        }

        state.setBoardFull(areNonNull(state.getBoxes()));

        if (!gotTheBox) {
            changeTurn(currentPlayer);
        }

        if (state.isBoardFull()) {
            state.setWinningPlayer(getWinningPlayer().getId());
            server.updateWinner(room, getWinningPlayer());
            server.saveScore(room, playerPlaying.values().stream().toList());
            System.out.println("player win: " + state.getWinningPlayer());
        }
    }

    private void startNewRoundDelayed() {

    }

    private Player getWinningPlayer() {
        Map<Player, Integer> valueFrequencyMap = new HashMap<>();

        for (Player[] row : state.getBoxes()) {
            for (Player value : row) {
                valueFrequencyMap.put(value, valueFrequencyMap.getOrDefault(value, 0) + 1);
                playerPlaying.get(value).setBoxes(valueFrequencyMap.getOrDefault(value, 0) + 1);
            }
        }

        Player mostFrequentValue = null;
        int maxFrequency = 0;

        for (Map.Entry<Player, Integer> entry : valueFrequencyMap.entrySet()) {
            if (entry.getValue() > maxFrequency) {
                maxFrequency = entry.getValue();
                mostFrequentValue = entry.getKey();
            }
        }

        return mostFrequentValue;
    }

    private void sendToPlayers(String message) {
        playerSockets.values().forEach((sk) -> {
            sk.send(message);
        });
    }

    public void processPlayerMove(WebSocket player, String move) {
        System.out.println("Game processing move from " + player.getRemoteSocketAddress() + ": " + move);

        MessageData messageData = new Gson().fromJson(move, MessageData.class);
        MakeTurn makeTurn = new Gson().fromJson(messageData.getJsonData(), MakeTurn.class);
        finishTurn(player, makeTurn.getX(), makeTurn.getY(), makeTurn.getRope().getOrder());

        sendToPlayers(ServerProtocol.gameState(state));
    }

    public boolean isGameEmpty() {
        return playerSockets.isEmpty();
    }

    private boolean areNonNull(Player[][] matrix) {
        for (Player[] row : matrix) {
            for (Player player : row) {
                if (player == null) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean areNonNull(List<Rope> list) {
        for(Rope r : list) {
            if(r == null) {
                return false;
            }
        }
        return true;
    }

    private void changeTurn(Player currentPlayer) {
        int currentPlayerIndex = state.getConnectedPlayers().indexOf(currentPlayer);
        int nextPlayerIndex = currentPlayerIndex == state.getConnectedPlayers().size() - 1 ? 0 : currentPlayerIndex + 1;
        state.setPlayerAtTurn(state.getConnectedPlayers().get(nextPlayerIndex));
    }
}
