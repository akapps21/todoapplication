package com.akapps.todoapp.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface NotesRecordDao {

    @Query("SELECT * FROM notes_record")
    LiveData<List<Notes>> getAllRecords();

    @Insert
    void insertAll(Notes... notes);

    @Insert
    void insertOne(Notes notes);

    @Delete
    void delete(Notes notes);

    @Query("DELETE FROM notes_record")
    void deleteAll();

    @Query("DELETE FROM notes_record WHERE id = :notesItem")
    int delete(final int notesItem);

    @Query("UPDATE notes_record SET title = (:title), notesdescrip = (:description),currentTime = (:currentTime),currentDate = (:currentDate) WHERE id = (:id)")
    void updateQuantity(String title, String description, String currentTime, String currentDate, int id);

}

