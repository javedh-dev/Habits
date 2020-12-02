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
import android.graphics.Color;

import org.joda.time.Days;
import org.joda.time.LocalDateTime;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import tech.zenex.habits.R;
import tech.zenex.habits.database.HabitDetails;
import tech.zenex.habits.database.HabitsDatabase;
import tech.zenex.habits.database.entities.HabitEntity;
import tech.zenex.habits.database.entities.HabitTrackerEntity;

public class HabitsBasicUtil {

    private static final Random RANDOM = new Random();

    public static int getStreak(HabitDetails habitDetails) {
        return Days.daysBetween(habitDetails.getHabitEntity().getStreakStart().toLocalDate(),
                LocalDateTime.now().toLocalDate()).getDays();
    }

    public static int getDaysFromStart(HabitDetails habitDetails) {
        return Days.daysBetween(habitDetails.getHabitEntity().getCreationDate().toLocalDate(),
                LocalDateTime.now().toLocalDate()).getDays();
    }

    public static Map<Long, Integer> getCheckInsByDays(HabitDetails habitDetails) {
        Map<Long, Integer> temp = new HashMap<>();
        for (HabitTrackerEntity trackerEntity : habitDetails.getHabitTrackerEntities()) {
            long date = TimeUnit.MILLISECONDS.toDays(
                    trackerEntity.getCheckInTime().toDate().getTime()
            );
            int counter = 0;
            if (temp.containsKey(date)) {
                counter = temp.get(date);
            }
            temp.put(date, counter + 1);
        }
        return temp;
    }

    public static void generateTestData(Context context) {
        HabitsDatabase database = HabitsDatabase.getDatabase(context);

        HabitEntity entity = new HabitEntity();
        entity.setColor(getRandomColor());
        entity.setHabitType(getRandomHabitType());
        entity.setOnceADay(getRandomOnceADay());
        entity.setName(getRandomTitle(context));
        entity.setDescription(getRandomDesc(context));
    }

    private static String getRandomDesc(Context context) {
        String[] titles = context.getResources().getStringArray(R.array.habits_desc);
        return titles[RANDOM.nextInt(titles.length)];
    }

    private static String getRandomTitle(Context context) {
        String[] titles = context.getResources().getStringArray(R.array.habits_title);
        return titles[RANDOM.nextInt(titles.length)];
    }

    private static boolean getRandomOnceADay() {
        return RANDOM.nextInt(100) < 20;
    }

    private static HabitEntity.HabitType getRandomHabitType() {
        return RANDOM.nextInt(100) < 50 ? HabitEntity.HabitType.BREAK : HabitEntity.HabitType.DEVELOP;
    }

    private static int getRandomColor() {
        return Color.rgb(RANDOM.nextInt(255), RANDOM.nextInt(255), RANDOM.nextInt(255));
    }
}
