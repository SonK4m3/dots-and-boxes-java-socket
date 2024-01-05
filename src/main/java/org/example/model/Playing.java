package org.example.model;

public class Playing {
    private int id;
    private Player player;
    private Room room;

    public Playing(int id, Player player, Room room) {
        this.id = id;
        this.player = player;
        this.room = room;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }
}
