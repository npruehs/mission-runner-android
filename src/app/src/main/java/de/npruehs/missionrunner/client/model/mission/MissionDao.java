package de.npruehs.missionrunner.client.model.mission;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface MissionDao {
    @Insert(onConflict = REPLACE)
    void save(Mission[] missions);

    @Query("SELECT * FROM mission WHERE accountId = :accountId")
    LiveData<Mission[]> load(String accountId);
}
