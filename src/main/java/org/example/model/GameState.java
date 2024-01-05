package org.example.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GameState implements Serializable {
    private Player playerAtTurn = null;
    private Rope[][] field = emptyField();
    private Player[][] boxes = emptyBoxes();
    private Integer winningPlayer = -1;
    private boolean isBoardFull = false;
    private List<Player> connectedPlayers = new ArrayList<Player>();

    public GameState() {
    }

    public void setPlayerAtTurn(Player player) {
        this.playerAtTurn = player;
    }

    public Player getPlayerAtTurn() {
        return playerAtTurn;
    }

    public Rope[][] getField() {
        return field;
    }

    public Player[][] getBoxes() {
        return boxes;
    }

    public Integer getWinningPlayer() {
        return winningPlayer;
    }

    public boolean isBoardFull() {
        return isBoardFull;
    }

    public List<Player> getConnectedPlayers() {
        return connectedPlayers;
    }

    public static final int DOTS = 6;
    public static final int BOXES = 5;

    public static Rope[][] emptyField() {
        Rope[][] emptyField = new Rope[DOTS * 2][DOTS];
        for (int i = 0; i < DOTS * 2; i++) {
            for (int j = 0; j < DOTS; j++) {
                emptyField[i][j] = null;
            }
        }
        return emptyField;
    }

    public static Player[][] emptyBoxes() {
        Player[][] emptyBoxes = new Player[BOXES][BOXES];
        for (int i = 0; i < BOXES; i++) {
            for (int j = 0; j < BOXES; j++) {
                emptyBoxes[i][j] = null;
            }
        }
        return emptyBoxes;
    }

    public void setRope(int x, int y, Rope rope) {
        this.field[x][y] = rope;
    }

    public void setBox(int x, int y, Player player) {
        this.boxes[x][y] = player;
    }

    public void setWinningPlayer(Integer winningPlayer) {
        this.winningPlayer = winningPlayer;
    }

    public void setBoardFull(boolean boardFull) {
        isBoardFull = boardFull;
    }
}
