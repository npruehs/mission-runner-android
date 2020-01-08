package de.npruehs.missionrunner.client.controller.mission.net;

import de.npruehs.missionrunner.client.model.character.CharacterStatus;
import de.npruehs.missionrunner.client.model.mission.Mission;

public class FinishMissionResponse {
    private MissionUpdate missions;
    private AccountUpdate account;
    private CharacterUpdate[] characters;

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

    public CharacterUpdate[] getCharacters() {
        return characters;
    }

    public void setCharacters(CharacterUpdate[] characters) {
        this.characters = characters;
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

    public static class CharacterUpdate {
        private int id;
        private CharacterStatus status;
        private int missionId;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public CharacterStatus getStatus() {
            return status;
        }

        public void setStatus(CharacterStatus status) {
            this.status = status;
        }

        public int getMissionId() {
            return missionId;
        }

        public void setMissionId(int missionId) {
            this.missionId = missionId;
        }
    }
}
