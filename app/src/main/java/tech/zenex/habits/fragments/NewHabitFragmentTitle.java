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

package tech.zenex.habits.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.zenex.habits.R;

import java.util.Objects;

import tech.zenex.habits.interfaces.NewHabitRecyclerViewListener;
import tech.zenex.habits.models.Habit;

public class NewHabitFragmentTitle extends Fragment implements NewHabitRecyclerViewListener {

    private TextInputEditText habitTitle;

    public static NewHabitFragmentTitle getInstance() {
        return new NewHabitFragmentTitle();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_habit_fragment_title, container, false);
        habitTitle = view.findViewById(R.id.habit_title);
        return view;
    }

    @Override
    public void onNext(Habit habit) {
        habit.setName(Objects.requireNonNull(habitTitle.getText()).toString());
    }
}