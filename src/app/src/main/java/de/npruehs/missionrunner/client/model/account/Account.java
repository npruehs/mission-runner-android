package de.npruehs.missionrunner.client.model.account;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Account {
    @PrimaryKey
    @NonNull
    private String id;

    private String name;
    private int level;
    private int score;

    public String getId() {
        return id;
    }

    public String getName() { return name; }

    public int getLevel() {
        return level;
    }

    public int getScore() {
        return score;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) { this.name = name; }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
