package com.naccoro.wask.replacement.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.naccoro.wask.utils.DateUtils;

@Entity(tableName = "replacement_histories", indices = {@Index(value = {"replace_date"}, unique = true)})
public class ReplacementHistory {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="replacement_histories_id")
    private int id;

    @ColumnInfo(name = "replace_date")
    //YYYY-MM-DD 형태의 문자열로 저장
    private String replacedDate;

    @ColumnInfo(name = "replace_date_month")
    private int monthOfReplaceDate;

    public ReplacementHistory(String replacedDate) {
        this.replacedDate = replacedDate;
        this.monthOfReplaceDate = DateUtils.getMonth(replacedDate);
    }

    public int getId() {
        return id;
    }

    public String getReplacedDate() {
        return replacedDate;
    }

    public int getMonthOfReplaceDate() {
        return monthOfReplaceDate;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setReplacedDate(String replacedDate) {
        this.replacedDate = replacedDate;
        this.monthOfReplaceDate = DateUtils.getMonth(replacedDate);
    }

    public void setMonthOfReplaceDate(int monthOfReplaceDate) {
        DateUtils.checkMonthFormat(monthOfReplaceDate);
        setReplacedDate(DateUtils.replaceMonthOfDateFormat(getReplacedDate(), monthOfReplaceDate));
    }

    @NonNull
    @Override
    public String toString() {
        return "[id : " + getId() + " date : " + getReplacedDate() + "]";
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof ReplacementHistory) {
            return this.replacedDate.equals(((ReplacementHistory) obj).replacedDate);
        } else {
            return false;
        }
    }
}
