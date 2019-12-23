package de.npruehs.missionrunner.client.model;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = Account.class, version = 1)
public abstract class AccountDatabase extends RoomDatabase {
    public abstract AccountDao accountDao();
}
