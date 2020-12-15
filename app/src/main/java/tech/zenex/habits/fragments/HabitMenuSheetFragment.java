/*
 * Copyright (C) 2020 - 2020 Javed Hussain <javedh.dev@gmail.com>
 * This file is part of Habits project.
 * This file and other under this project can not be copied and/or distributed
 * without the express permission of Javed Hussain
 */

package tech.zenex.habits.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import tech.zenex.habits.InsightsActivity;
import tech.zenex.habits.R;
import tech.zenex.habits.database.HabitDetails;
import tech.zenex.habits.database.HabitsRepository;
import tech.zenex.habits.utils.HabitsConstants;

public class HabitMenuSheetFragment extends BottomSheetDialogFragment {
    FragmentManager fragmentManager;
    HabitDetails habitDetails;
    ExtendedFloatingActionButton journalBtn, insightsBtn, editBtn, deleteBtn;

    public HabitMenuSheetFragment(FragmentManager fragmentManager, HabitDetails habitDetails) {
        this.fragmentManager = fragmentManager;
        this.habitDetails = habitDetails;
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
        View v = inflater.inflate(R.layout.habit_menu_sheet, container, false);
        journalBtn = v.findViewById(R.id.journal_btn);
        insightsBtn = v.findViewById(R.id.insights_btn);
        editBtn = v.findViewById(R.id.edit_btn);
        deleteBtn = v.findViewById(R.id.delete_btn);
        journalBtn.setOnClickListener(v1 -> openJournalFragment());
        insightsBtn.setOnClickListener(v1 -> openInsightsActivity());
        editBtn.setOnClickListener(v1 -> openEditFragment());
        deleteBtn.setOnClickListener(v1 -> deleteHabit());
        return v;
    }

    private void deleteHabit() {
        HabitsRepository.deleteHabit(habitDetails.getHabitEntity().getHabitID(), getContext());
        this.dismiss();
    }

    private void openEditFragment() {
        new HabitEditSheetFragment(habitDetails.getHabitEntity()).show(fragmentManager,
                HabitsConstants.HABIT_EDIT_FRAGMENT_TAG);
        this.dismiss();
    }

    private void openInsightsActivity() {
        Intent intent = new Intent(getActivity(), InsightsActivity.class);
        intent.putExtra(HabitsConstants.INSIGHT_EXTRAS_HABIT_DETAILS_KEY, habitDetails);
        startActivity(intent);
        this.dismiss();
    }

    private void openJournalFragment() {
        new JournalEntrySheetFragment(fragmentManager, habitDetails.getHabitEntity()).show(fragmentManager,
                HabitsConstants.JOURNAL_ENTRY_FRAGMENT_TAG);
        this.dismiss();
    }

}
