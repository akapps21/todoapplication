package com.akapps.todoapp.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Notes.class},version = 1,exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public static final String DATABASE_NAME ="notes_record.db";
    public abstract NotesRecordDao notesRecordsDao();
}
