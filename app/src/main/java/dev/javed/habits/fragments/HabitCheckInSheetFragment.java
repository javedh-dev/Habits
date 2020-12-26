/*
 * Copyright (C) 2020 - 2020 Javed Hussain <javedh.dev@gmail.com>
 * This file is part of Habits project.
 * This file and other under this project can not be copied and/or distributed
 * without the express permission of Javed Hussain
 */

package dev.javed.habits.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.button.MaterialButtonToggleGroup;

import java.util.Objects;

import dev.javed.habits.R;
import dev.javed.habits.database.HabitsRepository;
import dev.javed.habits.database.entities.HabitEntity;
import dev.javed.habits.database.entities.HabitTrackerEntity;
import dev.javed.habits.utils.HabitsBasicUtil;
import dev.javed.habits.utils.HabitsConstants;

public class HabitCheckInSheetFragment extends HabitsBottomSheet {
    MaterialButtonToggleGroup checkIn;


    public HabitCheckInSheetFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.SheetDialog_WithoutKeyBoard);
    }

    public static HabitCheckInSheetFragment newInstance(HabitEntity habitEntity) {
        Bundle args = new Bundle();
        args.putSerializable(HabitsConstants.ARGS_HABITS_ENTITY_KEY, habitEntity);
        HabitCheckInSheetFragment f = new HabitCheckInSheetFragment();
        f.setArguments(args);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.habit_checkin_sheet, container, false);
        checkIn = v.findViewById(R.id.check_in);
        checkIn.addOnButtonCheckedListener((group, checkedId, isChecked) -> checkInDatabase(checkedId,
                isChecked));
        return v;
    }

    private void checkInDatabase(int checkedId, boolean isChecked) {
        HabitEntity habitEntity = getHabitEntity();
        if (habitEntity != null) {
            HabitEntity.HabitType checkInStatus = isCheckInStatus(checkedId, isChecked, habitEntity);
            HabitTrackerEntity tracker = new HabitTrackerEntity(habitEntity.getHabitID(),
                    checkInStatus);
            HabitsRepository.addHabitTracker(tracker, getContext(), result -> {
                if (result.isSuccessful()) {
                    Objects.requireNonNull(getDialog()).dismiss();
                } else {
                    HabitsBasicUtil.notifyUser(requireActivity(), result.getMessage());
                }
            });
        } else {
            HabitsBasicUtil.notifyUser(getContext(), R.string.check_in_failed_message);
        }
    }

    @Nullable
    private HabitEntity getHabitEntity() {
        HabitEntity habitEntity = null;
        if (getArguments() != null) {
            habitEntity = (HabitEntity) getArguments().getSerializable(
                    HabitsConstants.ARGS_HABITS_ENTITY_KEY);
        }
        return habitEntity;
    }

    private HabitEntity.HabitType isCheckInStatus(int checkedId, boolean isChecked, HabitEntity habitEntity) {
        if (checkedId == R.id.success_btn && isChecked) {
            return habitEntity.getHabitType();
        } else {
            return habitEntity.getHabitType() == HabitEntity.HabitType.DEVELOP ?
                    HabitEntity.HabitType.BREAK : HabitEntity.HabitType.DEVELOP;
        }
    }
}
