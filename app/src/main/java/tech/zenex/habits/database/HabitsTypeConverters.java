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

package tech.zenex.habits.database;

import androidx.room.TypeConverter;

import java.util.Date;

import tech.zenex.habits.models.database.Habit;
import tech.zenex.habits.models.database.JournalEntry;

public class HabitsTypeConverters {
    @TypeConverter
    public String fromHabitTypeToString(Habit.HabitType habitType) {
        return habitType.name();
    }

    @TypeConverter
    public Habit.HabitType fromStringToHabitType(String habitType) {
        return habitType.equalsIgnoreCase("BREAK") ? Habit.HabitType.BREAK : Habit.HabitType.DEVELOP;
    }

    @TypeConverter
    public static Date toDate(Long dateLong) {
        return dateLong == null ? null : new Date(dateLong);
    }

    @TypeConverter
    public static Long fromDate(Date date) {
        return date == null ? null : date.getTime();
    }

    @TypeConverter
    public String fromJournalTypeToString(JournalEntry.JournalType habitType) {
        return habitType.name();
    }

    @TypeConverter
    public JournalEntry.JournalType fromStringToJournalType(String journalTYpe) {
        JournalEntry.JournalType type = JournalEntry.JournalType.GENERAL;
        switch (journalTYpe) {
            case "GRATITUDE":
                return JournalEntry.JournalType.GRATITUDE;
            case "GUILT":
                return JournalEntry.JournalType.GUILT;
            case "ENCOURAGE":
                return JournalEntry.JournalType.ENCOURAGE;
            default:
                return JournalEntry.JournalType.GENERAL;
        }
    }
}
