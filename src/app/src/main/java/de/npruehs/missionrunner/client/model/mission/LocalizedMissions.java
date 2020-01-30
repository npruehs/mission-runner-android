package de.npruehs.missionrunner.client.model.mission;

import de.npruehs.missionrunner.client.model.localization.LocalizationData;

public class LocalizedMissions {
    private final Mission[] missions;
    private final LocalizationData localization;

    public LocalizedMissions(Mission[] missions, LocalizationData localization) {
        this.missions = missions;
        this.localization = localization;
    }

    public Mission[] getMissions() {
        return missions;
    }

    public LocalizationData getLocalization() {
        return localization;
    }
}
