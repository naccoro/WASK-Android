package com.naccoro.wask.replacement;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.naccoro.wask.replacement.model.ReplacementHistory;
import com.naccoro.wask.replacement.repository.ReplacementHistoryDao;

@Database(entities = {ReplacementHistory.class}, version = 1)
public abstract class WaskDatabaseManager extends RoomDatabase {
    public abstract ReplacementHistoryDao replacementHistoryDao();

    private static WaskDatabaseManager instance;

    public static WaskDatabaseManager getInstance(Context context) {
        if (instance == null) {
            synchronized (WaskDatabaseManager.class) {
                instance = Room
                        .databaseBuilder(context.getApplicationContext(), WaskDatabaseManager.class, "wask.db")
                        .allowMainThreadQueries()
                        .build();
            }
        }
        return instance;
    }
}
