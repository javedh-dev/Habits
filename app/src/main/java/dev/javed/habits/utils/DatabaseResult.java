/*
 * Copyright (C) 2020 - 2020 Javed Hussain <javedh.dev@gmail.com>
 * This file is part of Habits project.
 * This file and other under this project can not be copied and/or distributed
 * without the express permission of Javed Hussain
 */

package dev.javed.habits.utils;

/*
 * @created on 26 Dec, 2020 01:52 PM
 * @project - Habits
 * @author - javed
 */
public class DatabaseResult {
    private boolean isSuccessful;
    private String message;
    private Throwable exception;

    public boolean isSuccessful() {
        return isSuccessful;
    }

    public void setSuccessful(boolean successful) {
        isSuccessful = successful;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Throwable getException() {
        return exception;
    }

    public void setException(Throwable exception) {
        this.exception = exception;
    }
}
