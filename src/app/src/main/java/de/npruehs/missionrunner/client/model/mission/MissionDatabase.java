package de.npruehs.missionrunner.client.model.mission;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = Mission.class, version = 1)
public abstract class MissionDatabase extends RoomDatabase {
    public abstract MissionDao missionDao();
}
