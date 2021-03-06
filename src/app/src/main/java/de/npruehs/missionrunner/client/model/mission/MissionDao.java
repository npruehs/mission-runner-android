package de.npruehs.missionrunner.client.model.mission;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface MissionDao {
    @Insert(onConflict = REPLACE)
    void insert(Mission[] missions);

    @Query("SELECT * FROM mission")
    LiveData<Mission[]> get();

    @Query("DELETE FROM mission")
    void clear();
}
