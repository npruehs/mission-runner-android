package de.npruehs.missionrunner.client.model;

import androidx.room.TypeConverter;

import de.npruehs.missionrunner.client.model.mission.MissionStatus;

public class EnumTypeConverter {
    @TypeConverter
    public static MissionStatus stringToMissionStatus(String missionStatus) {
        return Enum.valueOf(MissionStatus.class, missionStatus);
    }

    @TypeConverter
    public static String missionStatusToString(MissionStatus missionStatus) {
        return missionStatus.toString();
    }
}
