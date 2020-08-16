package com.naccoro.wask.replacement.repository;

import android.content.Context;
import android.util.Log;

import com.naccoro.wask.replacement.WaskDatabaseManager;
import com.naccoro.wask.replacement.model.ReplacementHistory;
import com.naccoro.wask.utils.DateUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ReplacementHistoryRepository {

    public static ReplacementHistoryRepository instance = null;

    private static final String TAG = "ReplacementHistoryRepo";

    private ReplacementHistoryDao dao;

    private List<ReplacementHistory> cachedReplacementHistory = null;

    boolean cacheIsDirty = false;

    public static ReplacementHistoryRepository getInstance(Context context) {
        if (instance == null) {
            synchronized (ReplacementHistoryRepository.class) {
                if (instance == null) {
                    instance = new ReplacementHistoryRepository(context);
                }
            }
        }

        return instance;
    }

    private ReplacementHistoryRepository(Context context) {
        dao = WaskDatabaseManager.getInstance(context).replacementHistoryDao();
    }

    public void getAll(LoadHistoriesCallback callback) {
        getAll(-1, callback);
    }

    public void getAll(int month, LoadHistoriesCallback callback) {
        checkCache();
        List<ReplacementHistory> result = getAllWithMonthFromCached(month);
        Log.d(TAG, "getAll: " + result.toString());

        if (result.isEmpty()) {
            callback.onDataNotAvailable();
        } else {
            callback.onHistoriesLoaded(result);
        }
    }

    public void get(int date, GetHistoriesCallback callback) {
        checkCache();
        ReplacementHistory result = getAllWithDateFromCached(date);

        if (result != null) {
            Log.d(TAG, "get: " + result.toString());
            callback.onTaskLoaded(result);
        } else {
            Log.d(TAG, "get: " + null);
            callback.onDataNotAvailable();
        }
    }

    public int getLastReplacement() {
        checkCache();

        if (cachedReplacementHistory.size() == 0) {
            return -1;
        }

        Collections.sort(cachedReplacementHistory, (firstHistory, secondHistory) -> {
            int firstDate = firstHistory.getReplacedDate();
            int secondDate = secondHistory.getReplacedDate();

            return Integer.compare(firstDate, secondDate);
        });

        return cachedReplacementHistory.get(cachedReplacementHistory.size() - 1).getReplacedDate();
    }

    public void getDateAroundThreeMonth(int month, LoadHistoriesAsDateCallback callback) {
        getDateAll(month - 1, month + 1, callback);
    }

    private void getDateAll(int startMonth, int endMonth, LoadHistoriesAsDateCallback callback) {

        List<Integer> replacementHistories = dao.getDateAll(startMonth, endMonth);

        if (replacementHistories != null) {
            callback.onHistoriesLoaded(replacementHistories);
        } else {
            callback.onDataNotAvailable();
        }
    }

    public void insert(ReplacementHistory newReplacementHistory, InsertHistoryCallback callback) {
        if (checkReduplication(newReplacementHistory) && callback != null) {
            callback.onDuplicated();
            return;
        }

        dao.insert(newReplacementHistory);
        Log.d(TAG, "insert: " + newReplacementHistory.toString());

        updateHistories();

        if (callback != null) {
            callback.onSuccess();
        }
    }

    private boolean checkReduplication(ReplacementHistory newReplacementHistory) {
        checkCache();
        return cachedReplacementHistory.contains(newReplacementHistory);
    }

    private void checkCache() {
        if (cachedReplacementHistory == null || cacheIsDirty) {
            updateCachedHistories();
        }
    }

    public void insert(int date, InsertHistoryCallback callback) {
        ReplacementHistory newReplacementHistory = new ReplacementHistory(date);
        insert(newReplacementHistory, callback);
    }

    public void insertToday(InsertHistoryCallback callback) {
        insert(DateUtils.getToday(), callback);
    }

    public void delete(ReplacementHistory deleteReplacementHistory) {
        Log.d(TAG, "delete: " + deleteReplacementHistory);
        dao.delete(deleteReplacementHistory);
        cachedReplacementHistory = null;
    }

    public void delete(int date) {
        Log.d(TAG, "delete: " + date);
        dao.delete(date);
        updateHistories();
    }
    
    public void deleteAll() {
        Log.d(TAG, "deleteAll: delete all of record");
        dao.deleteAll();
        updateHistories();
    }

    public void deleteToday() {
        delete(DateUtils.getToday());
    }

    public void update(ReplacementHistory updatedReplacementHistory) {
        Log.d(TAG, "update: " + updatedReplacementHistory.toString());
        dao.update(updatedReplacementHistory);
        updateHistories();
    }

    public void updateHistories() {
        cacheIsDirty = true;
    }

    private void updateCachedHistories() {
        cachedReplacementHistory = dao.getAll();
        Log.d(TAG, "updateCachedHistory: cache is updated");
        cacheIsDirty = false;
    }

    private List<ReplacementHistory> getAllWithMonthFromCached(int month) {
        if (month != -1) {
            List<ReplacementHistory> result = new ArrayList<>();
            for (ReplacementHistory history : cachedReplacementHistory) {
                if (history.getMonthOfReplaceDate() == month) {
                    result.add(history);
                }
            }
            return result;
        } else {
            return cachedReplacementHistory;
        }
    }

    private ReplacementHistory getAllWithDateFromCached(int date) {
        for (ReplacementHistory history : cachedReplacementHistory) {
            if (history.getReplacedDate() == date) {
                return history;
            }
        }
        return null;
    }

    public interface LoadHistoriesCallback {

        void onHistoriesLoaded(List<ReplacementHistory> histories);

        void onDataNotAvailable();
    }

    public interface LoadHistoriesAsDateCallback {

        void onHistoriesLoaded(List<Integer> histories);

        void onDataNotAvailable();
    }

    public interface GetHistoriesCallback {

        void onTaskLoaded(ReplacementHistory history);

        void onDataNotAvailable();
    }

    public interface InsertHistoryCallback {

        void onSuccess();

        void onDuplicated();
    }
 }
