package org.example.server;

import com.google.gson.Gson;
import org.example.dao.PlayerDAO;
import org.example.dao.PlayingDAO;
import org.example.dao.RoomDAO;
import org.example.dao.ScoreDAO;
import org.example.data.DotsAndBoxesGame;
import org.example.model.*;
import org.example.protocol.ServerProtocol;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DotsAndBoxesServer extends WebSocketServer {
    private final Map<WebSocket, Player> playerConnections = new ConcurrentHashMap<>();
    private final Map<Player, WebSocket> connectedPlayers = new ConcurrentHashMap<>();
    private final List<DotsAndBoxesGame> activeGames = new ArrayList<>();
    private final Map<WebSocket, DotsAndBoxesGame> playerGames = new ConcurrentHashMap<>();
    private final PlayerDAO playerDAO = new PlayerDAO();
    private final RoomDAO roomDAO = new RoomDAO();
    private final PlayingDAO playingDAO = new PlayingDAO();
    private final ScoreDAO scoreDAO = new ScoreDAO();

    public DotsAndBoxesServer(InetSocketAddress address) {
        super(address);
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        System.out.println("New connection: " + conn.getRemoteSocketAddress());
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        System.out.println("Closed connection: " + conn.getRemoteSocketAddress());
        Player player = playerConnections.remove(conn);
        if (player != null) {
            connectedPlayers.remove(player);
            DotsAndBoxesGame game = playerGames.remove(conn);
            if (game != null) {
                game.disconnectPlayer(player);
                if (game.isGameEmpty()) {
                    activeGames.remove(game);
                }
            }
        }
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        System.out.println("--------------------------------");
        System.out.println("[" + conn.getRemoteSocketAddress() + ": " + message + "]\n");

        MessageData receivedMessage = deserializeMessage(message);
        if (receivedMessage == null)
            return;

        if (receivedMessage.getMessage().equals("start")) {
            handlePlayerConnection(conn, new Gson().fromJson(receivedMessage.getJsonData(), Player.class));
        } else if (receivedMessage.getMessage().equals("move")) {
            DotsAndBoxesGame game = playerGames.get(conn);
            if (game != null) {
                game.processPlayerMove(conn, message);
            }
        } else {
            System.out.println(receivedMessage);
        }
        System.out.println("--------------------------------");
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        ex.printStackTrace();
    }

    private void handlePlayerConnection(WebSocket conn, Player player) {
        if (connectedPlayers.containsValue(player)) {
            conn.send(ServerProtocol.message("Player " + player.getName() + " is currently playing with another player."));
            return;
        }

        boolean createPlayer = playerDAO.addPlayer(player);
        if (!createPlayer || player.getId() == -1) {
            conn.send(ServerProtocol.createPlayer("-1"));
            return;
        }

        playerConnections.put(conn, player);
        conn.send(ServerProtocol.createPlayer(String.valueOf(player.getId())));
        if (playerConnections.size() % 2 == 0) {
            pairPlayersAndStartGame(conn, player);
        } else {
            conn.send(ServerProtocol.message("Waiting for the second player to join..."));
        }
    }

    private void pairPlayersAndStartGame(WebSocket conn, Player player) {

        WebSocket player2 = null;
        for (WebSocket sk : playerConnections.keySet()) {
            if (!connectedPlayers.containsKey(sk) && sk != conn) {
                player2 = sk;
                break;
            }
        }

        Player player2Obj = playerConnections.get(player2);

        System.out.println(player2Obj.getName() + " " + player.getName());
        Room room = new Room(-1, new Date());
        boolean createRoom = roomDAO.createRoom(room);
        if(!createRoom) {
            return;
        }
        Playing playing1 = new Playing(-1, player, room);
        Playing playing2 = new Playing(-1, player2Obj, room);
        playingDAO.createPlaying(playing1);
        playingDAO.createPlaying(playing2);

//        player2.send(ServerProtocol.message(player.getName()));
//        conn.send(ServerProtocol.message(player2Obj.getName()));

        connectedPlayers.put(player, player2);
        connectedPlayers.put(player2Obj, conn);

        DotsAndBoxesGame game = new DotsAndBoxesGame(room, this);

        activeGames.add(game);
        playerGames.put(conn, game);
        playerGames.put(player2, game);

        game.connectPlayer(conn, player, playing1);
        game.connectPlayer(player2, player2Obj, playing2);

        game.startGame();
    }

    private MessageData deserializeMessage(String message) {
        return new Gson().fromJson(message, MessageData.class);
    }

    private void broadcastToAll(String message) {
        for (WebSocket conn : connectedPlayers.values()) {
            conn.send(message);
        }
    }

    public void saveScore(Room room, List<Score> ls) {
        for(Score score: ls) {
            scoreDAO.createScore(score);
        }
    }

    public void updateWinner(Room room, Player winner) {
        room.setWinner(winner);
        roomDAO.updateWinner(room);
    }
}
