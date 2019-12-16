package de.npruehs.missionrunner.model;

public class Account {
    private String accountId;
    private int level;
    private int score;

    public String getAccountId() {
        return accountId;
    }

    public int getLevel() {
        return level;
    }

    public int getScore() {
        return score;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
