/*
 * Copyright (C) 2020 - 2020 Javed Hussain <javedh.dev@gmail.com>
 * This file is part of Habits project.
 * This file and other under this project can not be copied and/or distributed
 * without the express permission of Javed Hussain
 */

package dev.javed.habits.fragments;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.Objects;

import dev.javed.habits.InsightsActivity;
import dev.javed.habits.R;
import dev.javed.habits.database.HabitDetails;
import dev.javed.habits.database.HabitsRepository;
import dev.javed.habits.utils.HabitsBasicUtil;
import dev.javed.habits.utils.HabitsConstants;

public class HabitMenuSheetFragment extends HabitsBottomSheet {
    ExtendedFloatingActionButton journalBtn, insightsBtn, editBtn, deleteBtn;

    public HabitMenuSheetFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.SheetDialog_WithoutKeyBoard);
    }

    public static HabitMenuSheetFragment newInstance(HabitDetails habitDetails) {
        Bundle args = new Bundle();
        args.putSerializable(HabitsConstants.ARGS_HABITS_DETAILS_KEY, habitDetails);
        HabitMenuSheetFragment f = new HabitMenuSheetFragment();
        f.setArguments(args);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        HabitDetails habitDetails = getHabitDetails();
        View v = inflater.inflate(R.layout.habit_menu_sheet, container, false);
//        BottomSheetBehavior<View> behavior = BottomSheetBehavior.from(v);
//        behavior.setPeekHeight(BottomSheetBehavior.STATE_EXPANDED);
        journalBtn = v.findViewById(R.id.journal_btn);
        insightsBtn = v.findViewById(R.id.insights_btn);
        editBtn = v.findViewById(R.id.edit_btn);
        deleteBtn = v.findViewById(R.id.delete_btn);
        journalBtn.setOnClickListener(v1 -> openJournalFragment(habitDetails));
        insightsBtn.setOnClickListener(v1 -> openInsightsActivity(habitDetails));
        editBtn.setOnClickListener(v1 -> openEditFragment(habitDetails));
        deleteBtn.setOnClickListener(v1 -> deleteHabit(habitDetails));
        return v;
    }

    @Nullable
    private HabitDetails getHabitDetails() {
        HabitDetails habitDetails = null;
        if (getArguments() != null) {
            habitDetails =
                    (HabitDetails) getArguments().getSerializable(HabitsConstants.ARGS_HABITS_DETAILS_KEY);
        }
        return habitDetails;
    }

    private void deleteHabit(HabitDetails habitDetails) {
        if (habitDetails != null) {
            HabitsRepository.deleteHabit(habitDetails.getHabitEntity().getHabitID(),
                    getContext(),
                    result -> {
                        if (result.isSuccessful()) {
                            Objects.requireNonNull(getDialog()).dismiss();
                        } else {
                            HabitsBasicUtil.notifyUser(requireActivity(), result.getMessage());
                        }
                    });
        } else HabitsBasicUtil.notifyUser(getContext(), R.string.try_again_message);
    }

    private void openEditFragment(@Nullable HabitDetails habitDetails) {
        if (habitDetails != null) {
            new HabitEditSheetFragment(habitDetails.getHabitEntity()).show(getParentFragmentManager(),
                    HabitsConstants.HABIT_EDIT_FRAGMENT_TAG);
        } else HabitsBasicUtil.notifyUser(getContext(), R.string.try_again_message);
        this.dismiss();
    }

    private void openInsightsActivity(@Nullable HabitDetails habitDetails) {
        if (habitDetails != null) {
            Intent intent = new Intent(getActivity(), InsightsActivity.class);
            intent.putExtra(HabitsConstants.INSIGHT_EXTRAS_HABIT_DETAILS_KEY, habitDetails);
            startActivity(intent);
        } else HabitsBasicUtil.notifyUser(getContext(), R.string.try_again_message);
        this.dismiss();
    }

    private void openJournalFragment(@Nullable HabitDetails habitDetails) {
        if (habitDetails != null) {
            JournalEntrySheetFragment.newInstance(habitDetails.getHabitEntity()).show(getParentFragmentManager(),
                    HabitsConstants.JOURNAL_ENTRY_FRAGMENT_TAG);
        } else HabitsBasicUtil.notifyUser(getContext(), R.string.try_again_message);
        this.dismiss();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        dismiss();
    }
}
