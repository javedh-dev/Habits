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

import android.app.Activity;
import android.content.Context;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Collections;

import app.futured.donut.DonutProgressView;
import app.futured.donut.DonutSection;
import tech.zenex.habits.R;
import tech.zenex.habits.database.HabitDetails;

public class HabitCard extends RelativeLayout {

    private final Context context;
    private final TextView habitName;
    private final TextView progressPercentage;
    private final TextView checkIns;
    private final TextView journals;
    private final DonutProgressView streakProgressBar, dailyProgressBar;

    public HabitCard(Context context) {
        super(context);
        this.context = context;
        ((Activity) context).getLayoutInflater().inflate(R.layout.habit_card, this);
        this.habitName = findViewById(R.id.habit_name);
        this.streakProgressBar = findViewById(R.id.streak_progress);
        this.dailyProgressBar = findViewById(R.id.daily_progress);
        this.progressPercentage = findViewById(R.id.progress_percentage);
        this.checkIns = findViewById(R.id.check_ins);
        this.journals = findViewById(R.id.journals);
        this.streakProgressBar.setCap(100f);
        this.dailyProgressBar.setCap(100f);
    }

    public void setHabitName(CharSequence habitName) {
        this.habitName.setText(habitName);
    }

    public void setProgressPercentage(int progressPercentage, int color) {
        this.progressPercentage.setText(String.format(context.getResources().getString(R.string.habit_percentage_placeholder),
                progressPercentage));
        DonutSection progress = new DonutSection("streak", color, progressPercentage);
        this.streakProgressBar.submitData(Collections.singletonList(progress));
        this.dailyProgressBar.submitData(Collections.singletonList(progress));
//        this.progressBar.addAmount("streak", progressPercentage, color);
    }

    public void setCheckIns(CharSequence checkIns) {
        this.checkIns.setText(checkIns);
    }

    public void setJournals(CharSequence journals) {
        this.journals.setText(journals);
    }

    public void populateHabit(HabitDetails habitDetails) {
        setHabitName(habitDetails.getHabitEntity().getName());
        int percentage = (int) (Math.random() * 100);
        setProgressPercentage(percentage, habitDetails.getHabitEntity().getColor());
        setJournals("" + habitDetails.getJournalEntryEntities().size());
        setCheckIns("" + habitDetails.getHabitTrackerEntities().size());
    }

    @Override
    public boolean isClickable() {
        return true;
    }

    @Override
    public boolean hasFocusable() {
        return true;
    }

    /*@Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.d("Height", String.format("Size : %d Mode : %d", MeasureSpec.getSize(heightMeasureSpec),
                MeasureSpec.getMode(heightMeasureSpec)));
        Log.d("Width", String.format("Size : %d Mode : %d", MeasureSpec.getSize(widthMeasureSpec),
                MeasureSpec.getMode(widthMeasureSpec)));
    }*/
}
