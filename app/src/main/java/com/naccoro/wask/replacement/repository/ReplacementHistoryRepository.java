package com.naccoro.wask.replacement.repository;

import android.content.Context;
import android.util.Log;

import com.naccoro.wask.replacement.WaskDatabaseManager;
import com.naccoro.wask.replacement.model.ReplacementHistoryEntity;

import java.util.ArrayList;
import java.util.List;

public class ReplacementHistoryRepository {
    
    private static final String TAG = "ReplacementHistoryRepo";

    private ReplacementHistoryDao dao;

    private List<ReplacementHistoryEntity> cachedReplacementHistory = null;

    public ReplacementHistoryRepository(Context context) {
        dao = WaskDatabaseManager.getInstance(context).replacementHistoryDao();
    }

    public List<ReplacementHistoryEntity> getAll() {
        return getAll(-1);
    }

    public List<ReplacementHistoryEntity> getAll(int month) {
        if (cachedReplacementHistory == null) {
            updateCachedHistory();
        }
        List<ReplacementHistoryEntity> result = getAllFromCached(month);
        Log.d(TAG, "getAll: " + result.toString());
        return result;
    }

    public ReplacementHistoryEntity get(String date) {
        if (cachedReplacementHistory == null) {
            updateCachedHistory();
        }

        ReplacementHistoryEntity result = getAllFromCached(date);

        if (result != null) {
            Log.d(TAG, "get: " + result.toString());
        } else {
            Log.d(TAG, "get: " + null);
        }
        return result;
    }

    public void updateCachedHistory() {
        cachedReplacementHistory = dao.getAll();
        Log.d(TAG, "updateCachedHistory: cache is updated");
    }
    
    public void insert(ReplacementHistoryEntity newReplacementHistory) {
        Log.d(TAG, "insert: " + newReplacementHistory.toString());
        dao.insert(newReplacementHistory);
        updateCachedHistory();
    }
    
    public void insert(String date) {
        ReplacementHistoryEntity newReplacementHistory = new ReplacementHistoryEntity(date);
        insert(newReplacementHistory);
        updateCachedHistory();
    }

    public void delete(ReplacementHistoryEntity deleteReplacementHistory) {
        Log.d(TAG, "delete: " + deleteReplacementHistory);
        dao.delete(deleteReplacementHistory);
        updateCachedHistory();
    }

    public void delete(String date) {
        Log.d(TAG, "delete: " + date);
        dao.delete(date);
        updateCachedHistory();
    }
    
    public void deleteAll() {
        Log.d(TAG, "deleteAll: delete all of record");
        dao.deleteAll();
        updateCachedHistory();
    }

    public void update(ReplacementHistoryEntity updatedReplacementHistory) {
        Log.d(TAG, "update: " + updatedReplacementHistory.toString());
        dao.update(updatedReplacementHistory);
        updateCachedHistory();
    }

    private List<ReplacementHistoryEntity> getAllFromCached(int month) {
        if (month != -1) {
            List<ReplacementHistoryEntity> result = new ArrayList<>();
            for (ReplacementHistoryEntity history : cachedReplacementHistory) {
                if (history.getMonthOfReplaceDate() == month) {
                    result.add(history);
                }
            }
            return result;
        } else {
            return cachedReplacementHistory;
        }
    }

    private ReplacementHistoryEntity getAllFromCached(String date) {
        for (ReplacementHistoryEntity history : cachedReplacementHistory) {
            if (history.getReplacedDate().equals(date)) {
                return history;
            }
        }
        return null;
    }
}
