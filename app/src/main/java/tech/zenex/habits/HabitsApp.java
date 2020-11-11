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

package tech.zenex.habits;

import android.app.Application;

import org.joda.time.LocalDateTime;

import java.util.ArrayList;
import java.util.List;

import tech.zenex.habits.database.HabitsDatabase;
import tech.zenex.habits.database.entities.HabitEntity;

public class HabitsApp extends Application {

    private static final List<HabitEntity> HABIT_ENTITIES = new ArrayList<>();

    static {
        HABIT_ENTITIES.add(new HabitEntity("Masturbation", "You should stop masturbating to be more " +
                "productive"
                , HabitEntity.HabitType.BREAK));
        HABIT_ENTITIES.add(new HabitEntity("Painting", "Get a hobby that can also pay your bills",
                HabitEntity.HabitType.DEVELOP));
        HABIT_ENTITIES.add(new HabitEntity("Exercise Daily", "Stay fit and healthy",
                HabitEntity.HabitType.DEVELOP));
        HABIT_ENTITIES.add(new HabitEntity("Gardening", "Grow a home garden which you can water daily",
                HabitEntity.HabitType.DEVELOP));
        HABIT_ENTITIES.add(new HabitEntity("Complaining", "It'll only be done if you do",
                HabitEntity.HabitType.BREAK));
        HABIT_ENTITIES.add(new HabitEntity("Coding", "It'll help you grow in career",
                HabitEntity.HabitType.DEVELOP));
        HABIT_ENTITIES.add(new HabitEntity("Procrastination", "Stop being lazy ass",
                HabitEntity.HabitType.BREAK));
        HABIT_ENTITIES.add(new HabitEntity("Negativity", "A positive attitude will help you grow.",
                HabitEntity.HabitType.BREAK));
        HABIT_ENTITIES.add(new HabitEntity("Watching TV", "Stop wasting your time on non-productive things " +
                "so " +
                "that you can spend it later", HabitEntity.HabitType.BREAK));
        HABIT_ENTITIES.add(new HabitEntity("Social Media", "Will help you stay away from negativity",
                HabitEntity.HabitType.BREAK));
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        TypefaceUtil.overrideFont(getApplicationContext(), "serif", "fonts/OpenSans.ttf");
//        HabitsDatabase.getDatabase(this);
//        HabitsDatabase.databaseWriteExecutor.execute(() -> {
//                loadHabits();
//        });
    }

    private void loadHabits() {
        try {
            HabitsDatabase.databaseWriteExecutor.execute(() -> HabitsDatabase.getDatabase(this).habitDao()
                    .deleteAllHabits());
//            HabitsDatabase.databaseWriteExecutor.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
            Thread.sleep(100);
            for (int i = 0; i < 10; i++) {
                HabitEntity habitEntity = HABIT_ENTITIES.remove((int) getSeed(0, 9 - i));
                habitEntity.setOnceADay(getSeed(0, 10) > 5);
                habitEntity.setCreationDate(new LocalDateTime(System.currentTimeMillis() - getSeed(0,
                        3888000000L)));
                habitEntity.setLastCheckIn(new LocalDateTime(getSeed(habitEntity.getCreationDate().toDate().getTime(),
                        System.currentTimeMillis())));
                habitEntity.setStreakDays((int) getSeed(21, 60));
                HabitsDatabase.databaseWriteExecutor.execute(() -> HabitsDatabase.getDatabase(this).habitDao()
                        .insert(habitEntity));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private long getSeed(long start, long end) {
        long seed = (long) (Math.random() * (end - start));
        return start + seed;
    }
}
