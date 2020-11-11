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

import androidx.lifecycle.ViewModel;

import java.util.List;

import tech.zenex.habits.database.HabitDetails;
import tech.zenex.habits.database.HabitsDatabase;
import tech.zenex.habits.database.entities.HabitEntity;
import tech.zenex.habits.database.entities.HabitTrackerEntity;
import tech.zenex.habits.database.entities.JournalEntryEntity;

import static tech.zenex.habits.database.HabitsDatabase.getDatabase;

public class MainActivityViewModel extends ViewModel {
    private static final HabitsDatabase habitsDatabase;
    private static List<HabitDetails> mHabits;
   /* public MainActivityViewModel(Context context){
        HabitsDatabase.getDatabase(context);
        mHabits =
    }*/

    static {
        habitsDatabase = getDatabase(null);
//        HabitsDatabase.databaseWriteExecutor.execute(() ->
//                mHabits = habitsDatabase.habitDao().getAllHabits());
    }

    public static List<HabitDetails> getHabits() {
        while (mHabits == null) {
//            mHabits = habitsDatabase.habitDao().getAllHabits();

        }
        return mHabits;
    }

    public static void addHabit(HabitEntity habitEntity) {
        HabitsDatabase.databaseWriteExecutor.execute(() -> habitsDatabase.habitDao().insert(habitEntity));
//        Objects.requireNonNull(mHabits.getValue()).add(habitEntity);
    }

    public static void addHabitTracker(HabitTrackerEntity habitTrackerEntity) {
        HabitsDatabase.databaseWriteExecutor.execute(() -> habitsDatabase.habitTrackerDAO().insert(habitTrackerEntity));
    }

    public static void addJournalEntry(JournalEntryEntity journalEntryEntity) {
        HabitsDatabase.databaseWriteExecutor.execute(() -> habitsDatabase.journalDao().insert(journalEntryEntity));
    }

}
