package com.akapps.todoapp.database;

import androidx.lifecycle.LiveData;
import androidx.room.Room;

import com.akapps.todoapp.MyApplication;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AppDataBaseRepo {
    private static AppDataBaseRepo appDatabaseRepo;
    private final AppDatabase roomDb;
    private Executor executor = Executors.newFixedThreadPool(8);

    private AppDataBaseRepo() {
        roomDb = Room.databaseBuilder(MyApplication.getApplication(), AppDatabase.class, AppDatabase.DATABASE_NAME).build();
    }

    public static synchronized AppDataBaseRepo getInstance() {
        if (appDatabaseRepo == null) {
            appDatabaseRepo = new AppDataBaseRepo();
        }
        return appDatabaseRepo;
    }

    public void insertRecord(Notes record) {
        executor.execute(() -> roomDb.notesRecordsDao().insertAll(record));
    }

    public void deleteRecord() {
        executor.execute(() -> roomDb.notesRecordsDao().deleteAll());
    }

    public LiveData<List<Notes>> getAllRecords() {
        return roomDb.notesRecordsDao().getAllRecords();
    }

    public void deleteItem(int recorditem) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                roomDb.notesRecordsDao().delete(recorditem);
            }
        });
    }


    public void insertCheckRecord(String title, String description, String currentTime, String currentDate, int id) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                roomDb.notesRecordsDao().updateQuantity(title, description, currentTime, currentDate, id);
            }
        });
    }
}
