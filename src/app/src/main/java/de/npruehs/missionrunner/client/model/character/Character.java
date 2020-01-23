package de.npruehs.missionrunner.client.model.character;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import de.npruehs.missionrunner.client.model.EnumTypeConverter;
import de.npruehs.missionrunner.client.model.GsonTypeConverter;

@Entity
public class Character {
    @PrimaryKey
    private int id;

    private String name;

    @TypeConverters(EnumTypeConverter.class)
    private CharacterStatus status;

    private int missionId;

    @TypeConverters(GsonTypeConverter.class)
    private CharacterSkill[] skills;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public CharacterSkill[] getSkills() {
        return skills;
    }

    public void setSkills(CharacterSkill[] skills) {
        this.skills = skills;
    }
}
