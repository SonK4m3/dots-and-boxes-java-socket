package org.example.model;

public class MakeTurn {
    private int x;
    private int y;
    private Rope rope;

    public MakeTurn(int x, int y, Rope rope) {
        this.x = x;
        this.y = y;
        this.rope = rope;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Rope getRope() {
        return rope;
    }

    public void setRope(Rope rope) {
        this.rope = rope;
    }

    @Override
    public String toString() {
        return "MakeTurn{" +
                "x=" + x +
                ", y=" + y +
                ", rope=" + rope +
                '}';
    }
}
