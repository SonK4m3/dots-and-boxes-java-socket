package org.example.model;

import java.util.Date;

public class Room {
    private int id;
    private Date createTime;
    private Player winner;

    public Room(int id, Date createTime) {
        this.id = id;
        this.createTime = createTime;
        this.winner = null;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Player getWinner() {
        return winner;
    }

    public void setWinner(Player winner) {
        this.winner = winner;
    }
}
