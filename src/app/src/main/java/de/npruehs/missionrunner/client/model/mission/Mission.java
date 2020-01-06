package de.npruehs.missionrunner.client.model.mission;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import de.npruehs.missionrunner.client.model.EnumTypeConverter;
import de.npruehs.missionrunner.client.model.GsonTypeConverter;

@Entity
public class Mission {
    @PrimaryKey
    private int id;

    private String accountId;

    private String name;

    @TypeConverters(EnumTypeConverter.class)
    private MissionStatus status;

    @TypeConverters(GsonTypeConverter.class)
    private MissionRequirement[] requirements;

    private int requiredTime;

    private int reward;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MissionStatus getStatus() {
        return status;
    }

    public void setStatus(MissionStatus status) {
        this.status = status;
    }

    public MissionRequirement[] getRequirements() {
        return requirements;
    }

    public void setRequirements(MissionRequirement[] requirements) {
        this.requirements = requirements;
    }

    public int getRequiredTime() {
        return requiredTime;
    }

    public void setRequiredTime(int requiredTime) {
        this.requiredTime = requiredTime;
    }

    public int getReward() {
        return reward;
    }

    public void setReward(int reward) {
        this.reward = reward;
    }
}
