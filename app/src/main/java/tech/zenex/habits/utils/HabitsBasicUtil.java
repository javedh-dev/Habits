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

package tech.zenex.habits.utils;

import android.content.Context;

import org.joda.time.Days;
import org.joda.time.LocalDateTime;

import java.util.Random;

import tech.zenex.habits.R;
import tech.zenex.habits.database.HabitDetails;
import tech.zenex.habits.database.HabitsDatabase;
import tech.zenex.habits.database.entities.HabitEntity;
import tech.zenex.habits.database.entities.HabitTrackerEntity;
import tech.zenex.habits.database.entities.JournalEntryEntity;

public class HabitsBasicUtil {

    private static final Random RANDOM = new Random();


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
        HabitsDatabase database = HabitsDatabase.getDatabase(context);
        long id = database.habitDao().insert(entity);
        entity.setHabitID(database.habitDao().getIdFromRowId(id));
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
                int temp = RANDOM.nextInt(1);
                for (int i = 0; i <= temp; i++) {
                    generateJournalEntry(habitEntity, context, start);
                }
                temp = RANDOM.nextInt(1);
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
        String[] titles = context.getResources().getStringArray(R.array.habits_title);
        return titles[RANDOM.nextInt(titles.length)];
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

    private static JournalEntryEntity.JournalType getRandomJournalType() {
        JournalEntryEntity.JournalType[] types = JournalEntryEntity.JournalType.values();
        return types[RANDOM.nextInt(types.length)];
    }

    private static LocalDateTime getRandomTime(LocalDateTime start) {
        return start.withTime(RANDOM.nextInt(24), RANDOM.nextInt(60), RANDOM.nextInt(60),
                RANDOM.nextInt(1000));
    }

}
