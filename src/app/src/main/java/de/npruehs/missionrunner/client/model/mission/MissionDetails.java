package de.npruehs.missionrunner.client.model.mission;

import de.npruehs.missionrunner.client.model.character.Character;
import de.npruehs.missionrunner.client.model.localization.LocalizationData;

public class MissionDetails {
    private final Mission[] missions;
    private final Character[] characters;
    private final LocalizationData localization;

    public MissionDetails(Mission[] missions, Character[] characters, LocalizationData localization) {
        this.missions = missions;
        this.characters = characters;
        this.localization = localization;
    }

    public Mission[] getMissions() {
        return missions;
    }

    public Character[] getCharacters() {
        return characters;
    }

    public LocalizationData getLocalization() { return localization; }
}
