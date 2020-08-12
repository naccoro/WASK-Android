package com.naccoro.wask.replacement.repository;

import androidx.room.Dao;
import androidx.room.Query;

import com.naccoro.wask.base.BaseDao;
import com.naccoro.wask.replacement.model.ReplacementHistory;

import java.util.List;

@Dao
public abstract class ReplacementHistoryDao implements BaseDao<ReplacementHistory> {
    @Query("SELECT * FROM replacement_histories")
    abstract List<ReplacementHistory> getAll();

    @Query("SELECT * FROM replacement_histories WHERE replace_date_month = :month")
    abstract List<ReplacementHistory> getAll(int month);

    @Query("SELECT * FROM replacement_histories WHERE replace_date = :date")
    abstract ReplacementHistory get(int date);

    @Query("DELETE FROM replacement_histories")
    abstract void deleteAll();

    @Query("DELETE FROM replacement_histories WHERE replace_date = :date")
    abstract void delete(int date);
}
