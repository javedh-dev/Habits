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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.preference.PreferenceManager;

import org.joda.time.Days;
import org.joda.time.LocalDateTime;

import java.util.Collections;

import app.futured.donut.DonutProgressView;
import app.futured.donut.DonutSection;
import tech.zenex.habits.R;
import tech.zenex.habits.database.HabitDetails;
import tech.zenex.habits.database.entities.HabitEntity;

public class HabitCard extends RelativeLayout {

    private final Context context;
    private final TextView habitName;
    private final TextView progressPercentage;
    private final TextView checkIns;
    private final TextView journals;
    private final TextView streak;
    private final TextView failed;
    private final ImageView habitType;
    private final ImageView onceADay;
    private final DonutProgressView streakProgressBar;

    public HabitCard(Context context) {
        super(context);
        this.context = context;
        ((Activity) context).getLayoutInflater().inflate(R.layout.habit_card, this);
        this.habitName = findViewById(R.id.habit_name);
        this.streakProgressBar = findViewById(R.id.streak_progress);
        this.progressPercentage = findViewById(R.id.progress_percentage);
        this.checkIns = findViewById(R.id.check_ins);
        this.journals = findViewById(R.id.journals);
        this.streak = findViewById(R.id.streak);
        this.failed = findViewById(R.id.failed);
        this.habitType = findViewById(R.id.habit_type);
        this.onceADay = findViewById(R.id.once_a_day);
        this.streakProgressBar.setCap(100f);
    }

    public void setHabitName(CharSequence habitName) {
        this.habitName.setText(habitName);
    }

    public void setProgressPercentage(int progressPercentage, int color) {
        this.progressPercentage.setText(String.format(context.getResources().getString(R.string.habit_percentage_placeholder), progressPercentage));
        DonutSection progress = new DonutSection("streak", color, progressPercentage);
        this.streakProgressBar.submitData(Collections.singletonList(progress));
    }

    public void setCheckIns(CharSequence checkIns) {
        this.checkIns.setText(checkIns);
    }

    public void setJournals(CharSequence journals) {
        this.journals.setText(journals);
    }

    public void setStreak(CharSequence streak) {
        this.streak.setText(streak);
    }

    public void setHabitType(HabitEntity.HabitType type) {
        habitType.setImageResource(type == HabitEntity.HabitType.DEVELOP ? R.drawable.link_dark :
                R.drawable.broken_link_dark);
        boolean showHabitType = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(
                "show_habit_type", false);
        habitType.setVisibility(showHabitType ? VISIBLE : GONE);

    }

    public void setOnceADay(boolean onceADay) {
        this.onceADay.setImageResource(onceADay ? R.drawable.once_dark : R.drawable.infinity_dark);
        boolean showOncePerDay = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(
                "show_once_per_day", false);
        this.onceADay.setVisibility(showOncePerDay ? VISIBLE : GONE);
    }

    public void setFailed(CharSequence failed) {
        this.failed.setText(failed);
    }

    public void populateHabit(HabitDetails habitDetails) {
        setHabitName(habitDetails.getHabitEntity().getName());
        int percentage = getPercentage(habitDetails);
        setProgressPercentage(percentage, habitDetails.getHabitEntity().getColor());
        setJournals("" + habitDetails.getJournalEntryEntities().size());
        setCheckIns("" + habitDetails.getHabitTrackerEntities().size());
        setStreak(String.valueOf(getStreak(habitDetails)));
        setFailed(String.valueOf(habitDetails.getHabitEntity().getFailureCounter()));
        setHabitType(habitDetails.getHabitEntity().getHabitType());
        setOnceADay(habitDetails.getHabitEntity().isOnceADay());
    }

    private int getPercentage(HabitDetails habitDetails) {
        int streak = getStreak(habitDetails);
        int per = (int) ((streak / (float) habitDetails.getHabitEntity().getStreakDays()) * 100);
        return Math.min(per, 100);
    }

    private int getStreak(HabitDetails habitDetails) {
        return Days.daysBetween(habitDetails.getHabitEntity().getStreakStart().toLocalDate(),
                LocalDateTime.now().toLocalDate()).getDays();
    }

    @Override
    public boolean isClickable() {
        return true;
    }

    @Override
    public boolean hasFocusable() {
        return true;
    }

}
