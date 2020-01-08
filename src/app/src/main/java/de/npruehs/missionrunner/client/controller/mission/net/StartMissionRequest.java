package de.npruehs.missionrunner.client.controller.mission.net;

public class StartMissionRequest {
    private String accountId;
    private int missionId;
    private int[] characterIds;

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public int getMissionId() {
        return missionId;
    }

    public void setMissionId(int missionId) {
        this.missionId = missionId;
    }

    public int[] getCharacterIds() {
        return characterIds;
    }

    public void setCharacterIds(int[] characterIds) {
        this.characterIds = characterIds;
    }
}
