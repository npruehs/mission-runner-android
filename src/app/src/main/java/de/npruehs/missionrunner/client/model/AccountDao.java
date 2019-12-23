package de.npruehs.missionrunner.client.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface AccountDao {
    @Insert(onConflict = REPLACE)
    void save(Account account);

    @Query("SELECT * FROM account WHERE id = :id")
    LiveData<Account> load(String id);
}
