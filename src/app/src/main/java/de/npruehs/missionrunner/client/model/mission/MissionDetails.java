package de.npruehs.missionrunner.client.model.mission;

import de.npruehs.missionrunner.client.model.character.Character;

public class MissionDetails {
    private final Mission[] missions;
    private final Character[] characters;

    public MissionDetails(Mission[] missions, Character[] characters) {
        this.missions = missions;
        this.characters = characters;
    }

    public Mission[] getMissions() {
        return missions;
    }

    public Character[] getCharacters() {
        return characters;
    }
}
