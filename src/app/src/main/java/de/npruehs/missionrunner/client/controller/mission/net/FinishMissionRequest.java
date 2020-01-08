package de.npruehs.missionrunner.client.controller.mission.net;

public class FinishMissionRequest {
    private String accountId;
    private int missionId;

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
}
