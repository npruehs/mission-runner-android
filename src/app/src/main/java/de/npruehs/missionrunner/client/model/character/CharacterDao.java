package de.npruehs.missionrunner.client.model.character;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface CharacterDao {
    @Insert(onConflict = REPLACE)
    void insert(Character[] characters);

    @Query("SELECT * FROM character WHERE accountId = :accountId")
    LiveData<Character[]> get(String accountId);
}
