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

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "habits")
public class Habit {

    @PrimaryKey(autoGenerate = true)
    private int id;
    @NonNull
    private String name = "";
    private String description;
    private HabitType habitType;
//    private List<String> reasons;
//    private List<Question> questions;
//    private Repetition repetition;

    public Habit() {

    }

    @Ignore
    public Habit(@NonNull String name, String description, HabitType habitType) {
        this.name = name;
        this.description = description;
        this.habitType = habitType;
//        this.reasons = new ArrayList<>();
//        this.questions = new ArrayList<>();
    }

    @Ignore
    public Habit(@NonNull String name, String description, HabitType habitType, List<String> reasons, List<Question> questions) {
        this.name = name;
        this.description = description;
        this.habitType = habitType;
//        this.reasons = reasons;
//        this.questions = questions;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public HabitType getHabitType() {
        return habitType;
    }

    public void setHabitType(HabitType habitType) {
        this.habitType = habitType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

//    public List<String> getReasons() {
//        return reasons;
//    }
//
//    public void setReasons(List<String> reasons) {
//        this.reasons = reasons;
//    }
//
//    public List<Question> getQuestions() {
//        return questions;
//    }
//
//    public void addQuestion(Question question) {
//        this.questions.add(question);
//    }
//
//    public Repetition getRepetition() {
//        return repetition;
//    }
//
//    public void setRepetition(Repetition repetition) {
//        this.repetition = repetition;
//    }


    @NonNull
    @Override
    public String toString() {
        return "Habit{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", habitType=" + habitType +
                '}';
    }

    public enum HabitType {DEVELOP, BREAK}

    public enum Repetition {DAILY, WEEKLY, ALTERNATE_DAYS}
}
