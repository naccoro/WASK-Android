package com.naccoro.wask.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.naccoro.wask.utils.DateUtils;

@Entity(tableName = "replacement_histories")
public class ReplacementHistoryEntity {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="replacement_histories_id")
    private int id;

    @ColumnInfo(name = "replace_date")
    //YYYY-MM-DD 형태의 문자열로 저장
    private String replacedDate;

    @ColumnInfo(name = "replace_date_month")
    private int monthOfReplaceDate;

    public ReplacementHistoryEntity(int id, String replacedDate) {
        this.id = id;
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
        setMonthOfReplaceDate(DateUtils.getMonth(replacedDate));
    }

    public void setMonthOfReplaceDate(int monthOfReplaceDate) {
        DateUtils.checkMonthFormat(monthOfReplaceDate);
        this.monthOfReplaceDate = monthOfReplaceDate;
        setReplacedDate(DateUtils.replaceMonthOfDateFormat(getReplacedDate(), monthOfReplaceDate));
    }
}
