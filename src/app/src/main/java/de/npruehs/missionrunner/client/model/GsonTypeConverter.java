package de.npruehs.missionrunner.client.model;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import de.npruehs.missionrunner.client.model.mission.MissionRequirement;

public class GsonTypeConverter {
    @TypeConverter
    public static MissionRequirement[] gsonToMissionRequirementArray(String gson) {
        Type missionRequirementArrayType = new TypeToken<MissionRequirement[]>() {}.getType();
        return new Gson().fromJson(gson, missionRequirementArrayType);
    }

    @TypeConverter
    public static String missionRequirementArrayToGson(MissionRequirement[] missionRequirements) {
        return new Gson().toJson(missionRequirements);
    }
}
