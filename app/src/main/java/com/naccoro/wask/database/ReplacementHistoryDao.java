package com.naccoro.wask.database;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

@Dao
public abstract class ReplacementHistoryDao implements BaseDao<ReplacementHistoryEntity> {
    @Query("SELECT * FROM replacement_histories")
    abstract List<ReplacementHistoryEntity> getAll();

    @Query("SELECT * FROM replacement_histories WHERE replace_date_month = :month")
    abstract List<ReplacementHistoryEntity> getAll(int month);
}
