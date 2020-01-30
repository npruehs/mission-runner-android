package de.npruehs.missionrunner.client.model.localization;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = LocalizedString.class, version = 1)
public abstract class LocalizationDatabase extends RoomDatabase {
    public abstract LocalizationDao localizationDao();
}
