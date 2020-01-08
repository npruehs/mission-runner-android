package de.npruehs.missionrunner.client.controller.mission.net;

import de.npruehs.missionrunner.client.model.mission.Mission;

public class FinishMissionResponse {
    private MissionUpdate missions;
    private AccountUpdate account;

    public MissionUpdate getMissions() {
        return missions;
    }

    public void setMissions(MissionUpdate missions) {
        this.missions = missions;
    }

    public AccountUpdate getAccount() {
        return account;
    }

    public void setAccount(AccountUpdate account) {
        this.account = account;
    }

    public static class MissionUpdate {
        private int[] removedMissions;
        private Mission[] addedMissions;

        public int[] getRemovedMissions() {
            return removedMissions;
        }

        public void setRemovedMissions(int[] removedMissions) {
            this.removedMissions = removedMissions;
        }

        public Mission[] getAddedMissions() {
            return addedMissions;
        }

        public void setAddedMissions(Mission[] addedMissions) {
            this.addedMissions = addedMissions;
        }
    }

    public static class AccountUpdate {
        private int score;
        private int level;

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }
    }
}
