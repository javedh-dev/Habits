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
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import java.util.Collections;
import java.util.List;

import tech.zenex.habits.database.entities.JournalEntryEntity;
import tech.zenex.habits.utils.HabitsBasicUtil;

public class JournalsCard extends CardView {

    private LinearLayout container;

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
//        setCardBackgroundColor(HabitsBasicUtil.getRandomColor(getContext()));
        container = new LinearLayout(getContext());
        container.setOrientation(LinearLayout.VERTICAL);
        addView(container);
        TextView title = new TextView(getContext());
        title.setTextSize(16);
        title.setText("Journal Entries");
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
//        container.removeAllViews();
        Collections.reverse(entryEntities);
        for (JournalEntryEntity entity : entryEntities) {
            container.addView(new JournalCard(getContext(), entity.getTimestamp(), entity.getJournalType(),
                    entity.getEntry()));
        }
    }

}
