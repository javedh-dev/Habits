/*
 * Copyright (C) 2020 - 2020 Javed Hussain <javedh.dev@gmail.com>
 * This file is part of Habits project.
 * This file and other under this project can not be copied and/or distributed
 * without the express permission of Javed Hussain
 */

package dev.javed.habits.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.StringRes;
import androidx.preference.PreferenceManager;

import org.joda.time.Days;
import org.joda.time.LocalDateTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import dev.javed.habits.R;
import dev.javed.habits.database.HabitDetails;
import dev.javed.habits.database.HabitsDatabase;
import dev.javed.habits.database.entities.HabitEntity;
import dev.javed.habits.database.entities.HabitTrackerEntity;
import dev.javed.habits.database.entities.JournalEntryEntity;

public class HabitsBasicUtil {

    private static final Random RANDOM = new Random();
    private static final List<String> titles = new ArrayList<>();


    public static int getDaysFromStart(HabitDetails habitDetails) {
        return Days.daysBetween(habitDetails.getHabitEntity().getCreationDate().toLocalDate(),
                LocalDateTime.now().toLocalDate()).getDays();
    }

    public static void generateTestData(Context context) {
        for (int i = 0; i < 25; i++) {
            HabitEntity habitEntity = createHabit(context);
            generateTrackerAndJournalData(habitEntity, context);
        }
    }


    private static HabitEntity createHabit(Context context) {
        HabitEntity entity = new HabitEntity();
        entity.setColor(getRandomColor(context));
        entity.setHabitType(getRandomHabitType(50, HabitEntity.HabitType.DEVELOP));
        entity.setOnceADay(getRandomOnceADay());
        entity.setName(getRandomTitle(context));
        entity.setDescription(getRandomDesc(context));
        entity.setStreakDays(getRandomNumberInRange(21, 42));
        entity.setCreationDate(getRandomDate(20));
        entity.setHabitID(entity.getName().hashCode());
        HabitsDatabase database = HabitsDatabase.getDatabase(context);
        database.habitDao().insert(entity);
        return entity;
    }

    private static void generateTrackerAndJournalData(HabitEntity habitEntity, Context context) {
        LocalDateTime start = habitEntity.getCreationDate();
        LocalDateTime now = LocalDateTime.now();
        while (start.compareTo(now) < 0) {
            if (habitEntity.isOnceADay()) {
                int temp = RANDOM.nextInt(100);
                generateHabitTracker(habitEntity, context, start);
                if (temp > 50) {
                    generateJournalEntry(habitEntity, context, start);
                }

            } else {
                int temp = RANDOM.nextInt(2);
                for (int i = 0; i <= temp; i++) {
                    generateJournalEntry(habitEntity, context, start);
                }
                temp = RANDOM.nextInt(2);
                for (int i = 0; i <= temp; i++) {
                    generateHabitTracker(habitEntity, context, start);
                }
            }
            start = start.plusDays(1);
        }
    }

    private static void generateHabitTracker(HabitEntity habitEntity, Context context, LocalDateTime start) {
        HabitTrackerEntity entity = new HabitTrackerEntity();
        entity.setCheckInTime(getRandomTime(start));
        entity.setHabitID(habitEntity.getHabitID());
        entity.setType(getRandomHabitType(90, habitEntity.getHabitType()));
        HabitsDatabase database = HabitsDatabase.getDatabase(context);
        long id = database.habitTrackerDAO().insert(entity);
        entity.setId(database.habitTrackerDAO().getIdFromRowId(id));
    }

    private static void generateJournalEntry(HabitEntity habitEntity, Context context, LocalDateTime start) {
        JournalEntryEntity entity = new JournalEntryEntity();
        entity.setEntry(getRandomJournalEntry(context));
        entity.setHabitID(habitEntity.getHabitID());
        entity.setJournalType(getRandomJournalType());
        entity.setTimestamp(getRandomTime(start));
        HabitsDatabase database = HabitsDatabase.getDatabase(context);
        long id = database.journalDao().insert(entity);
        entity.setJournalId(database.journalDao().getIdFromRowId(id));
    }

    private static LocalDateTime getRandomDate(int previousDays) {
        int temp = RANDOM.nextInt(previousDays);
        return LocalDateTime.now().minusDays(temp);
    }

    private static int getRandomNumberInRange(int start, int end) {
        return RANDOM.nextInt(end - start) + start;
    }

    private static String getRandomDesc(Context context) {
        String[] titles = context.getResources().getStringArray(R.array.habits_desc);
        return titles[RANDOM.nextInt(titles.length)];
    }

    public static String getRandomTitle(Context context) {
        if (titles.isEmpty())
            Collections.addAll(titles, context.getResources().getStringArray(R.array.habits_title));
        return titles.remove(RANDOM.nextInt(titles.size()));
    }

    private static String getRandomJournalEntry(Context context) {
        String[] titles = context.getResources().getStringArray(R.array.journals);
        return titles[RANDOM.nextInt(titles.length)];
    }

    private static boolean getRandomOnceADay() {
        return RANDOM.nextInt(100) < 20;
    }

    private static HabitEntity.HabitType getRandomHabitType(int threshold, HabitEntity.HabitType habitType) {
        return RANDOM.nextInt(100) < threshold ? habitType : habitType.not();
    }

    public static int getRandomColor(Context context) {
        int[] colors = context.getResources().getIntArray(R.array.colors_palette);
        return colors[RANDOM.nextInt(colors.length)];
    }

    public static int getRandomCardColor(Context context) {
        int[] colors = context.getResources().getIntArray(R.array.card_background_palette);
        return colors[RANDOM.nextInt(colors.length)];
    }

    private static JournalEntryEntity.JournalType getRandomJournalType() {
        JournalEntryEntity.JournalType[] types = JournalEntryEntity.JournalType.values();
        return types[RANDOM.nextInt(types.length)];
    }

    public static float pxFromDp(final Context context, final float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }

    public static void notifyUser(Context context, @StringRes int message) {
        Toast.makeText(context, context.getResources().getString(message), Toast.LENGTH_LONG).show();
    }

    public static void notifyUser(Activity context, String message) {
        context.runOnUiThread(() -> Toast.makeText(context, message, Toast.LENGTH_LONG).show());
    }

    public static SharedPreferences getDefaultSharedPreference(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    private static LocalDateTime getRandomTime(LocalDateTime start) {
        return start.withTime(RANDOM.nextInt(24), RANDOM.nextInt(60), RANDOM.nextInt(60),
                RANDOM.nextInt(1000));
    }

    public static Bitmap getBitmapFromView(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    public static Bitmap getBitmapFromView(View view, int defaultColor) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(defaultColor);
        view.draw(canvas);
        return bitmap;
    }


}
