/*
 * Copyright (C) 2020 - 2020 Javed Hussain <javedh.dev@gmail.com>
 * This file is part of Habits project.
 * This file and other under this project can not be copied and/or distributed
 * without the express permission of Javed Hussain
 */

package dev.javed.habits.utils;

/*
 * @created on 26 Dec, 2020 01:48 PM
 * @project - Habits
 * @author - javed
 */
public interface TaskCompletionListener<T> {
    void onTaskCompleted(T result);
}
