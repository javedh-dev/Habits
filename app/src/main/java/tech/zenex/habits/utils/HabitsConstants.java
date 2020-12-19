/*
 * Copyright (C) 2020 - 2020 Javed Hussain <javedh.dev@gmail.com>
 * This file is part of Habits project.
 * This file and other under this project can not be copied and/or distributed
 * without the express permission of Javed Hussain
 */

package tech.zenex.habits.utils;

/*
 * @created on 15 Dec, 2020 08:42 AM
 * @project - Habits
 * @author - javed
 */

public class HabitsConstants {

    // Generic Constants
    public static final String EMPTY_STRING = "";
    public static final String JOURNAL_ENTRY_FRAGMENT_TAG = "JournalEntrySheet";
    public static final String HABIT_CHECK_IN_FRAGMENT_TAG = "HabitCheckInSheet";
    public static final String HABIT_EDIT_FRAGMENT_TAG = "HabitEditSheet";
    public static final String INSIGHT_EXTRAS_HABIT_DETAILS_KEY = "HABIT_DETAILS";
    public static final String MAIN_ACTIVITY_EXTRAS_URL_KEY = "URL";
    public static final String ARGS_HABITS_ENTITY_KEY = "HABIT_ENTITY";
    public static final String ARGS_HABITS_DETAILS_KEY = "HABIT_DETAILS";
    public static final String DONUT_VIEW_SECTION_NAME = "Streak";

    // Filename Constants
    public static final String DATABASE_FILENAME = "habits.db";
    public static final String DATABASE_BACKUP_FILENAME = "backup.habits";
    public static final String REMOTE_STORAGE_BASE_DIR = "/user/";

    // Preferences
    public static final String PREFERENCE_IS_FIRST_RUN = "is_first_run";
    public static final String PREFERENCE_ALLOWED_FAILURES = "allowed_failures";
    public static final String PREFERENCE_BACKUP = "backup";
    public static final String PREFERENCE_BACKUP_KEY = "backup_key";
    public static final String PREFERENCE_VERSION = "version";
    public static final String PREFERENCE_SHOW_HABIT_TYPE = "show_habit_type";
    public static final String PREFERENCE_SHOW_ONCE_PER_DAY = "show_once_per_day";

    // Logging Tags
    public static final String FIREBASE_LOG = "HABITS_FIREBASE";

    // Logging Messages
    public static final String FIREBASE_ERROR_GETTING_TOKEN = "Error getting token from Firebase.";
    public static final String FIREBASE_TOKEN_ACQUIRED = "Token acquired successfully from Firebase : %s";

    // String Formatters
    public static final String STREAK_DAYS_FORMAT = "{0} {1}";
    public static final String STREAK_PERCENTAGE_FORMAT = "{0}%";
    public static final String JOURNAL_CARD_DATE_FORMAT = "dd MMM, yyyy hh:mm a";

    private HabitsConstants() {
    }


}
