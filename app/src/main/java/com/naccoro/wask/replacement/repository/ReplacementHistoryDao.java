package com.naccoro.wask.replacement.repository;

import androidx.room.Dao;
import androidx.room.Query;

import com.naccoro.wask.base.BaseDao;
import com.naccoro.wask.replacement.model.ReplacementHistoryEntity;

import java.util.List;

@Dao
public abstract class ReplacementHistoryDao implements BaseDao<ReplacementHistoryEntity> {
    @Query("SELECT * FROM replacement_histories")
    abstract List<ReplacementHistoryEntity> getAll();

    @Query("SELECT * FROM replacement_histories WHERE replace_date_month = :month")
    abstract List<ReplacementHistoryEntity> getAll(int month);

    @Query("SELECT * FROM replacement_histories WHERE replace_date = :date")
    abstract ReplacementHistoryEntity get(String date);

    @Query("DELETE FROM replacement_histories")
    abstract void deleteAll();

    @Query("DELETE FROM replacement_histories WHERE replace_date = :date")
    abstract void delete(String date);
}
