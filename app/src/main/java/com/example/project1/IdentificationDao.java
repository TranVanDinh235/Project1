package com.example.project1;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface IdentificationDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Identification identification);

    @Delete
    void deleteIdentification(Identification identification);

    @Update(onConflict = REPLACE)
    void updateIdentification(Identification identification);

    @Query("SELECT * from Identification LIMIT 1")
    Identification[] getAnyIdentification();

    @Query("SELECT * from Identification ORDER BY fullname ASC")
    LiveData<List<Identification>> getAllIdentification();
}
