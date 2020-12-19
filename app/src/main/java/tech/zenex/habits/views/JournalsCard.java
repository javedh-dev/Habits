/*
 * Copyright (C) 2020 - 2020 Javed Hussain <javedh.dev@gmail.com>
 * This file is part of Habits project.
 * This file and other under this project can not be copied and/or distributed
 * without the express permission of Javed Hussain
 */

package tech.zenex.habits.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import java.util.Collections;
import java.util.List;

import tech.zenex.habits.R;
import tech.zenex.habits.database.entities.JournalEntryEntity;
import tech.zenex.habits.utils.HabitsBasicUtil;

public class JournalsCard extends CardView {

    private LinearLayout container;
    private TextView title;

    public JournalsCard(@NonNull Context context) {
        super(context);
        initializeView();
    }

    public JournalsCard(@NonNull Context context,
                        @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs) {
        super(context, attrs);
        initializeView();
    }

    public JournalsCard(@NonNull Context context,
                        @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initializeView();
    }

    private void initializeView() {
        container = new LinearLayout(getContext());
        container.setOrientation(LinearLayout.VERTICAL);
        addView(container);
        title = new TextView(getContext());
        title.setTextSize(16);
        title.setText(R.string.journal_entries_text);
        title.setTextColor(HabitsBasicUtil.getRandomColor(getContext()));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        int margin = (int) HabitsBasicUtil.pxFromDp(getContext(), 20);
        params.setMargins(margin, margin, margin, margin);
        title.setLayoutParams(params);
        title.setTypeface(null, Typeface.BOLD);
        container.addView(title);
    }

    public void addEntries(List<JournalEntryEntity> entryEntities) {
        if (entryEntities.isEmpty()) {
            title.setText(R.string.no_journal_entries_text);
        }
        Collections.reverse(entryEntities);
        for (JournalEntryEntity entity : entryEntities) {
            container.addView(new JournalCard(getContext(), entity.getTimestamp(), entity.getJournalType(),
                    entity.getEntry()));
        }
    }

}
