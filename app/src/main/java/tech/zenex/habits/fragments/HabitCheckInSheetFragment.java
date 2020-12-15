/*
 * Copyright (C) 2020 - 2020 Javed Hussain <javedh.dev@gmail.com>
 * This file is part of Habits project.
 * This file and other under this project can not be copied and/or distributed
 * without the express permission of Javed Hussain
 */

package tech.zenex.habits.fragments;

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
import tech.zenex.habits.database.HabitsDatabase;
import tech.zenex.habits.database.HabitsRepository;
import tech.zenex.habits.database.entities.HabitEntity;
import tech.zenex.habits.database.entities.HabitTrackerEntity;

public class HabitCheckInSheetFragment extends BottomSheetDialogFragment {
    FragmentManager fragmentManager;
    HabitEntity habitEntity;
    MaterialButtonToggleGroup checkIn;

    public HabitCheckInSheetFragment(FragmentManager fragmentManager, HabitEntity habitEntity) {
        this.fragmentManager = fragmentManager;
        this.habitEntity = habitEntity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.SheetDialog_WithoutKeyBoard);
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
                HabitsRepository.addHabitTracker(tracker, getContext());
                if (checkInStatus != habitEntity.getHabitType()) {
                    HabitsDatabase.getDatabase(getContext()).habitDao().update(habitEntity);
                }
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
        if (checkedId == R.id.success_btn && isChecked) {
            return habitEntity.getHabitType();
        } else {
            return habitEntity.getHabitType() == HabitEntity.HabitType.DEVELOP ?
                    HabitEntity.HabitType.BREAK : HabitEntity.HabitType.DEVELOP;
        }
    }
}
