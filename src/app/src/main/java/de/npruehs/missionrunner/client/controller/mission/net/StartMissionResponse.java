package de.npruehs.missionrunner.client.controller.mission.net;

import de.npruehs.missionrunner.client.model.character.CharacterStatus;
import de.npruehs.missionrunner.client.model.mission.MissionStatus;

public class StartMissionResponse {
    private String accountId;
    private MissionUpdate mission;
    private CharacterUpdate[] characters;

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public MissionUpdate getMission() {
        return mission;
    }

    public void setMission(MissionUpdate mission) {
        this.mission = mission;
    }

    public CharacterUpdate[] getCharacters() {
        return characters;
    }

    public void setCharacters(CharacterUpdate[] characters) {
        this.characters = characters;
    }

    public static class MissionUpdate {
        private int id;
        private MissionStatus status;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public MissionStatus getStatus() {
            return status;
        }

        public void setStatus(MissionStatus status) {
            this.status = status;
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
