package de.npruehs.missionrunner.client.model;

public class Account {
    private String id;
    private int level;
    private int score;

    public String getId() {
        return id;
    }

    public int getLevel() {
        return level;
    }

    public int getScore() {
        return score;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
