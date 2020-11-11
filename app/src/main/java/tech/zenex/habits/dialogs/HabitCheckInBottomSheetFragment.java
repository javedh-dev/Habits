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

package tech.zenex.habits.dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButtonToggleGroup;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import tech.zenex.habits.R;
import tech.zenex.habits.adapters.HabitsRecyclerViewAdapter;
import tech.zenex.habits.database.HabitsDatabase;
import tech.zenex.habits.database.entities.HabitEntity;
import tech.zenex.habits.database.entities.HabitTrackerEntity;
import tech.zenex.habits.models.MainActivityViewModel;

public class HabitCheckInBottomSheetFragment extends BottomSheetDialogFragment {
    FragmentManager fragmentManager;
    HabitEntity habitEntity;
    MaterialButtonToggleGroup checkIn;
    HabitsRecyclerViewAdapter adapter;

    public HabitCheckInBottomSheetFragment(FragmentManager fragmentManager, HabitEntity habitEntity,
                                           HabitsRecyclerViewAdapter adapter) {
        this.fragmentManager = fragmentManager;
        this.habitEntity = habitEntity;
        this.adapter = adapter;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.SheetDialog);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.habit_checkin_sheet, container, false);
        checkIn = v.findViewById(R.id.check_in);
        checkIn.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            HabitEntity.HabitType checkInStatus = isCheckInStatus(checkedId, isChecked);
            HabitsDatabase.databaseWriteExecutor.execute(() -> {
                HabitTrackerEntity tracker = new HabitTrackerEntity(habitEntity.getHabitID(), checkInStatus);
                MainActivityViewModel.addHabitTracker(tracker);
            });
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    Objects.requireNonNull(getDialog()).dismiss();
                }
            }, 200);
        });
        return v;
    }

    private HabitEntity.HabitType isCheckInStatus(int checkedId, boolean isChecked) {
        if (checkedId == R.id.break_btn && isChecked) {
            return HabitEntity.HabitType.BREAK;
        } else {
            return HabitEntity.HabitType.DEVELOP;
        }
    }

    @Override
    public void onDestroy() {
        adapter.notifyDataSetChanged();
        super.onDestroy();
    }
}
