package de.npruehs.missionrunner.client.model.account;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface AccountDao {
    @Insert(onConflict = REPLACE)
    void insert(Account account);

    @Query("SELECT * FROM account WHERE id = :id")
    LiveData<Account> get(String id);
}
