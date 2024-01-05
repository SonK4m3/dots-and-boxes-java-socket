package org.example.model;

public class Score {
    private int id;
    private int boxes;
    private Playing playing;

    public Score(int id, int boxes, Playing playing) {
        this.id = id;
        this.boxes = boxes;
        this.playing = playing;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBoxes() {
        return boxes;
    }

    public void setBoxes(int boxes) {
        this.boxes = boxes;
    }

    public Playing getPlaying() {
        return playing;
    }

    public void setPlaying(Playing playing) {
        this.playing = playing;
    }
}
