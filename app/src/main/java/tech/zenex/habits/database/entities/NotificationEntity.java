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

package tech.zenex.habits.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import org.joda.time.LocalDateTime;

import java.util.Map;

@Entity(tableName = "notifications")
public class NotificationEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String from;
    private String to;
    private String title;
    private String description;
    private Map<String, String> data;
    @ColumnInfo(name = "received_time")
    private LocalDateTime receivedTime = LocalDateTime.now();
    @ColumnInfo(name = "is_read")
    private boolean isRead = false;

    @Ignore
    public NotificationEntity(String from, String to, String title, String description,
                              Map<String, String> data, LocalDateTime receivedTime, boolean isRead) {
        this.from = from;
        this.to = to;
        this.title = title;
        this.description = description;
        this.data = data;
        this.receivedTime = receivedTime;
        this.isRead = isRead;
    }

    public NotificationEntity() {
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }

    public LocalDateTime getReceivedTime() {
        return receivedTime;
    }

    public void setReceivedTime(LocalDateTime receivedTime) {
        this.receivedTime = receivedTime;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
