package de.npruehs.missionrunner.client.model;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import de.npruehs.missionrunner.client.model.character.CharacterSkill;
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

    @TypeConverter
    public static CharacterSkill[] gsonToCharacterSkillArray(String gson) {
        Type characterSkillArrayType = new TypeToken<CharacterSkill[]>() {}.getType();
        return new Gson().fromJson(gson, characterSkillArrayType);
    }

    @TypeConverter
    public static String characterSkillArrayToGson(CharacterSkill[] characterSkills) {
        return new Gson().toJson(characterSkills);
    }
}
