package de.npruehs.missionrunner.client.model.mission;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Period;

import de.npruehs.missionrunner.client.model.DateTimeTypeConverter;
import de.npruehs.missionrunner.client.model.EnumTypeConverter;
import de.npruehs.missionrunner.client.model.GsonTypeConverter;

@Entity
public class Mission {
    @PrimaryKey
    private int id;

    private String name;

    @TypeConverters(EnumTypeConverter.class)
    private MissionStatus status;

    @TypeConverters(GsonTypeConverter.class)
    private MissionRequirement[] requirements;

    private int requiredTime;

    private int reward;

    @TypeConverters(DateTimeTypeConverter.class)
    private DateTime finishTime;

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

    public DateTime getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(DateTime finishTime) {
        this.finishTime = finishTime;
    }

    public int getRemainingSeconds() {
        Period remainingTime = new Period(DateTime.now(DateTimeZone.UTC), getFinishTime());
        return Math.max(remainingTime.getSeconds(), 0);
    }

    public boolean isRunning() {
        return getStatus() == MissionStatus.RUNNING && getRemainingSeconds() > 0;
    }

    public boolean isFinished() {
        return getStatus() == MissionStatus.FINISHED ||
                (getStatus() == MissionStatus.RUNNING && getRemainingSeconds() <= 0);
    }
}
