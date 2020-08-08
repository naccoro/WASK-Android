package com.naccoro.wask.database;

import android.content.Context;
import android.util.Log;

import java.util.List;

public class ReplacementHistoryRepository {
    
    private static final String TAG = "ReplacementHistoryRepo";

    private ReplacementHistoryDao dao;

    public ReplacementHistoryRepository(Context context) {
        dao = WaskDatabaseManager.getInstance(context).replacementHistoryDao();
    }

    public List<ReplacementHistoryEntity> getAll() {
        return getAll(-1);
    }

    public List<ReplacementHistoryEntity> getAll(int month) {
        List<ReplacementHistoryEntity> result;
        if (month == -1) {
            result = dao.getAll();
        } else {
            result = dao.getAll(month);
        }
        Log.d(TAG, "getAll: " + result.toString());
        return result;
    }

    public ReplacementHistoryEntity get(String date) {
        ReplacementHistoryEntity result = dao.get(date);
        Log.d(TAG, "get: " + result.toString());
        return result;
    }
    
    public void insert(ReplacementHistoryEntity newReplacementHistory) {
        Log.d(TAG, "insert: " + newReplacementHistory.toString());
        dao.insert(newReplacementHistory);
    }
    
    public void insert(String date) {
        ReplacementHistoryEntity newReplacementHistory = new ReplacementHistoryEntity(date);
        insert(newReplacementHistory);
    }

    public void delete(ReplacementHistoryEntity deleteReplacementHistory) {
        Log.d(TAG, "delete: " + deleteReplacementHistory);
        dao.delete(deleteReplacementHistory);
    }

    public void delete(String date) {
        Log.d(TAG, "delete: " + date);
        dao.delete(date);
    }
    
    public void deleteAll() {
        Log.d(TAG, "deleteAll: delete all of record");
        dao.deleteAll();
    }

    public void update(ReplacementHistoryEntity updatedReplacementHistory) {
        Log.d(TAG, "update: " + updatedReplacementHistory.toString());
        dao.update(updatedReplacementHistory);
    }
}
