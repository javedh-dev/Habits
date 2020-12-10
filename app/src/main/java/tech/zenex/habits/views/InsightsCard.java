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
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import tech.zenex.habits.R;
import tech.zenex.habits.utils.HabitsBasicUtil;

public class InsightsCard extends CardView {

    private int iconRef = R.drawable.ic_icon;
    private String insightName = "Insight";
    private String insightDesc = "Description";
    private TextView desc;

    public InsightsCard(@NonNull Context context) {
        super(context);
        createView();
    }

    public InsightsCard(@NonNull Context context,
                        @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.InsightsCard,
                0, 0);

        try {
            iconRef = a.getResourceId(R.styleable.InsightsCard_insight_icon, R.drawable.ic_icon);
            insightName = a.getString(R.styleable.InsightsCard_insight_name);
            insightDesc = a.getString(R.styleable.InsightsCard_insight_desc);
        } finally {
            a.recycle();
        }
        createView();
    }

    public InsightsCard(@NonNull Context context,
                        @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        createView();
    }

    public void setInsightDesc(String insightDesc) {
        this.desc.setText(insightDesc);

    }

    protected void createView() {
        View view = inflate(getContext(), R.layout.insight_card, null);
        addView(view);
        setUseCompatPadding(true);
        setCardBackgroundColor(HabitsBasicUtil.getRandomColor(getContext()));
        TextView title = findViewById(R.id.insight_title);
        ImageView icon = findViewById(R.id.insight_icon);
        desc = findViewById(R.id.insight_desc);
        title.setText(insightName);
        icon.setColorFilter(Color.WHITE, android.graphics.PorterDuff.Mode.SRC_IN);
        icon.setImageResource(iconRef);
        desc.setText(insightDesc);
        setRadius(HabitsBasicUtil.pxFromDp(getContext(), 10));
    }
}
