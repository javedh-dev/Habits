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

package tech.zenex.habits.models;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import tech.zenex.habits.database.HabitsDatabase;
import tech.zenex.habits.models.database.Habit;
import tech.zenex.habits.models.database.HabitTracker;
import tech.zenex.habits.models.database.JournalEntry;

import static tech.zenex.habits.database.HabitsDatabase.getDatabase;

public class MainActivityViewModel extends ViewModel {
    private static MutableLiveData<List<Habit>> mHabits = new MutableLiveData<>();
    private static HabitsDatabase habitsDatabase;
   /* public MainActivityViewModel(Context context){
        HabitsDatabase.getDatabase(context);
        mHabits =
    }*/

    static {
        habitsDatabase = getDatabase(null);
        HabitsDatabase.databaseWriteExecutor.execute(() ->
                mHabits.postValue(habitsDatabase.habitDao().getAllHabits()));
    }

    public static MutableLiveData<List<Habit>> getHabits() {
        return mHabits;
    }

    private static void loadHabits() {
        List<Habit> habits = new ArrayList<>();
        for (int i = 0; i < 17; i++) {
            habits.add(new Habit("Habit-" + i, "I want to achieve this goal in " + i * 10 + " days.",
                    Habit.HabitType.BREAK, false));
        }
        mHabits.setValue(habits);
    }

    public static void addHabit(Habit habit) {
        HabitsDatabase.databaseWriteExecutor.execute(() -> habitsDatabase.habitDao().insert(habit));
        Objects.requireNonNull(mHabits.getValue()).add(habit);
    }

    public static void addHabitTracker(HabitTracker habitTracker) {
        HabitsDatabase.databaseWriteExecutor.execute(() -> habitsDatabase.habitTrackerDAO().insert(habitTracker));
    }

    public static void addJournalEntry(JournalEntry journalEntry) {
        HabitsDatabase.databaseWriteExecutor.execute(() -> habitsDatabase.journalDao().insert(journalEntry));
    }

}
