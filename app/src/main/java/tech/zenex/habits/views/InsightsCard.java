/*
 * Copyright (C) 2020 - 2020 Javed Hussain <javedh.dev@gmail.com>
 * This file is part of Habits project.
 * This file and other under this project can not be copied and/or distributed
 * without the express permission of Javed Hussain
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
import tech.zenex.habits.utils.HabitsConstants;

public class InsightsCard extends CardView {

    private int iconRef = R.drawable.ic_icon;
    private String insightName;
    private String insightDesc;
    private TextView desc;

    public InsightsCard(@NonNull Context context) {
        super(context);
        insightName = HabitsConstants.EMPTY_STRING;
        insightDesc = HabitsConstants.EMPTY_STRING;
        createView();
    }

    public InsightsCard(@NonNull Context context,
                        @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs) {
        super(context, attrs);
        parseAttrs(context, attrs);
        createView();
    }

    public InsightsCard(@NonNull Context context,
                        @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        parseAttrs(context, attrs);
        createView();
    }

    private void parseAttrs(@NonNull Context context,
                            @org.jetbrains.annotations.Nullable AttributeSet attrs) {
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
