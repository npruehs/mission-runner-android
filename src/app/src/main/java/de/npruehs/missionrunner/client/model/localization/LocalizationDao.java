package de.npruehs.missionrunner.client.model.localization;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface LocalizationDao {
    @Insert(onConflict = REPLACE)
    void insert(LocalizedString[] strings);

    @Query("SELECT * FROM localizedstring")
    LiveData<LocalizedString[]> get();

    @Query("DELETE FROM localizedstring")
    void clear();
}
