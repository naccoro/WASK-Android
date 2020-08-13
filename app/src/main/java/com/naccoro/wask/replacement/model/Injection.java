package com.naccoro.wask.replacement.model;

import android.content.Context;

import androidx.annotation.NonNull;

import com.naccoro.wask.replacement.repository.ReplacementHistoryRepository;

public class Injection {

    public static ReplacementHistoryRepository replacementHistoryRepository(@NonNull Context context) {
        return new ReplacementHistoryRepository(context);
    }
}