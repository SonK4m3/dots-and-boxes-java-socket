package org.example.model;

import java.io.Serializable;

public class Rope implements Serializable {
    private Player player;
    private int order;

    public Rope(Player player, int order) {
        this.player = player;
        this.order = order;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    @Override
    public String toString() {
        return "Rope{" +
                "player=" + player +
                ", order=" + order +
                '}';
    }
}
