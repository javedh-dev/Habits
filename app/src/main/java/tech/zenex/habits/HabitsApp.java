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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import tech.zenex.habits.database.HabitsDatabase;
import tech.zenex.habits.models.database.Habit;

public class HabitsApp extends Application {

    private static final List<Habit> habits = new ArrayList<>();

    static {
        habits.add(new Habit("Masturbation", "You should stop masturbating to be more productive"
                , Habit.HabitType.BREAK));
        habits.add(new Habit("Painting", "Get a hobby that can also pay your bills",
                Habit.HabitType.DEVELOP));
        habits.add(new Habit("Exercise Daily", "Stay fit and healthy",
                Habit.HabitType.DEVELOP));
        habits.add(new Habit("Gardening", "Grow a home garden which you can water daily",
                Habit.HabitType.DEVELOP));
        habits.add(new Habit("Complaining", "It'll only be done if you do",
                Habit.HabitType.BREAK));
        habits.add(new Habit("Coding", "It'll help you grow in career",
                Habit.HabitType.DEVELOP));
        habits.add(new Habit("Procrastination", "Stop being lazy ass",
                Habit.HabitType.BREAK));
        habits.add(new Habit("Negativity", "A positive attitude will help you grow.",
                Habit.HabitType.BREAK));
        habits.add(new Habit("Watching TV", "Stop wasting your time on non-productive things so " +
                "that you can spend it later", Habit.HabitType.BREAK));
        habits.add(new Habit("Social Media", "Will help you stay away from negativity",
                Habit.HabitType.BREAK));
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        TypefaceUtil.overrideFont(getApplicationContext(), "serif", "fonts/OpenSans.ttf");
        HabitsDatabase.getDatabase(this);
        HabitsDatabase.databaseWriteExecutor.execute(() -> {
            int habits = HabitsDatabase.getDatabase(this).habitDao().getAllHabits().size();
            if (habits == 0)
                loadHabits();
        });
    }

    private void loadHabits() {
        try {
            HabitsDatabase.databaseWriteExecutor.execute(() -> HabitsDatabase.getDatabase(this).habitDao()
                    .deleteAllHabits());
//            HabitsDatabase.databaseWriteExecutor.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
            Thread.sleep(100);
            for (int i = 0; i < 10; i++) {
                Habit habit = habits.remove((int) getSeed(0, 9 - i));
                habit.setOnceADay(getSeed(0, 10) > 5);
                habit.setCreationDate(new Date(System.currentTimeMillis() - getSeed(0, 3888000000L)));
                habit.setLastCheckIn(new Date(getSeed(habit.getCreationDate().getTime(),
                        System.currentTimeMillis())));
                habit.setStreakDays((int) getSeed(21, 60));
                HabitsDatabase.databaseWriteExecutor.execute(() -> HabitsDatabase.getDatabase(this).habitDao()
                        .insert(habit));
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
