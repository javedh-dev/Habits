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

import android.util.Log;

import androidx.room.TypeConverter;

import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import tech.zenex.habits.database.entities.HabitEntity;
import tech.zenex.habits.database.entities.JournalEntryEntity;

public class HabitsTypeConverters {

    private static final DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/YYYY HH:mm:ss.SSS");

    @TypeConverter
    public static LocalDateTime toDate(String dateTime) {
//        Log.d("Date", dateTime);
        return dateTime == null ? null : formatter.parseLocalDateTime(dateTime);
    }

    @TypeConverter
    public static String fromDate(LocalDateTime dateTime) {
        Log.d("Date", String.valueOf(dateTime));
        return dateTime == null ? null : formatter.print(dateTime);
    }

    @TypeConverter
    public String fromHabitTypeToString(HabitEntity.HabitType habitType) {
        return habitType.name();
    }

    @TypeConverter
    public HabitEntity.HabitType fromStringToHabitType(String habitType) {
        return habitType.equalsIgnoreCase("BREAK") ? HabitEntity.HabitType.BREAK :
                HabitEntity.HabitType.DEVELOP;
    }

    @TypeConverter
    public String fromJournalTypeToString(JournalEntryEntity.JournalType habitType) {
        return habitType.name();
    }

    @TypeConverter
    public JournalEntryEntity.JournalType fromStringToJournalType(String journalTYpe) {
        switch (journalTYpe) {
            case "GRATITUDE":
                return JournalEntryEntity.JournalType.GRATITUDE;
            case "GUILT":
                return JournalEntryEntity.JournalType.GUILT;
            case "ENCOURAGE":
                return JournalEntryEntity.JournalType.ENCOURAGE;
            default:
                return JournalEntryEntity.JournalType.GENERAL;
        }
    }
}
