/*
 * Copyright (c) 2020.  Zenex.Tech@ https://zenex.tech
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under
 * the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

package tech.zenex.habits.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;

import tech.zenex.habits.R;
import tech.zenex.habits.database.entities.JournalEntryEntity;

public class JournalCard extends LinearLayout {
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
        timestampTv.setText(DateTimeFormat.forPattern("dd MMM, yyyy hh:mm a").print(timestamp));
        typeTv.setText(type.toString());
        entryTv.setText(entry);
    }

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

    public JournalCard(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
