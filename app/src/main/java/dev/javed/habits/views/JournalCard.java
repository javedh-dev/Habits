/*
 * Copyright (C) 2020 - 2020 Javed Hussain <javedh.dev@gmail.com>
 * This file is part of Habits project.
 * This file and other under this project can not be copied and/or distributed
 * without the express permission of Javed Hussain
 */

package dev.javed.habits.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;

import dev.javed.habits.R;
import dev.javed.habits.database.entities.JournalEntryEntity;
import dev.javed.habits.utils.HabitsConstants;

public class JournalCard extends LinearLayout {

    public JournalCard(Context context) {
        super(context);
    }

    public JournalCard(Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public JournalCard(Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs,
                       int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public JournalCard(Context context, LocalDateTime timestamp, JournalEntryEntity.JournalType type,
                       String entry) {
        super(context);
        View v = inflate(context, R.layout.journal_card, null);
        addView(v);
        v.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        v.requestLayout();
        TextView timestampTv = findViewById(R.id.timestamp);
        TextView typeTv = findViewById(R.id.journal_type);
        TextView entryTv = findViewById(R.id.entry);
        timestampTv.setText(DateTimeFormat.forPattern(HabitsConstants.JOURNAL_CARD_DATE_FORMAT).print(timestamp));
        typeTv.setText(type.toString());
        entryTv.setText(entry);
    }
}
