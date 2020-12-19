/*
 * Copyright (C) 2020 - 2020 Javed Hussain <javedh.dev@gmail.com>
 * This file is part of Habits project.
 * This file and other under this project can not be copied and/or distributed
 * without the express permission of Javed Hussain
 */

package tech.zenex.habits.fragments;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Objects;

import tech.zenex.habits.R;
import tech.zenex.habits.database.HabitsDatabase;
import tech.zenex.habits.database.HabitsRepository;
import tech.zenex.habits.database.entities.HabitEntity;
import tech.zenex.habits.database.entities.JournalEntryEntity;
import tech.zenex.habits.utils.HabitsBasicUtil;
import tech.zenex.habits.utils.HabitsConstants;

public class JournalEntrySheetFragment extends HabitsBottomSheet {

    public JournalEntrySheetFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.SheetDialog);
    }

    public static JournalEntrySheetFragment newInstance(HabitEntity habitEntity) {
        Bundle args = new Bundle();
        args.putSerializable(HabitsConstants.ARGS_HABITS_ENTITY_KEY, habitEntity);
        JournalEntrySheetFragment f = new JournalEntrySheetFragment();
        f.setArguments(args);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.journal_entry_sheet, container, false);
        TextView journalTitle = v.findViewById(R.id.journal_title);
        TextView entry = v.findViewById(R.id.entry);
        Button add = v.findViewById(R.id.add_entry);
        Button cancel = v.findViewById(R.id.cancel);
        Spinner journalType = v.findViewById(R.id.journal_type);
        setupForHabitEntity(journalTitle, entry, add, journalType);
        cancel.setOnClickListener(v12 -> Objects.requireNonNull(getDialog()).dismiss());
        return v;
    }

    private void setupForHabitEntity(TextView journalTitle, TextView entry, Button add, Spinner journalType) {
        HabitEntity habitEntity = getHabitEntity();
        journalTitle.setText(habitEntity != null ? habitEntity.getName() : "NULL");
        add.setOnClickListener(v1 -> HabitsDatabase.databaseWriteExecutor.execute(() -> {
            if (habitEntity != null) {
                HabitsRepository.addJournalEntry(
                        new JournalEntryEntity(habitEntity.getHabitID(),
                                JournalEntryEntity.getJournalType(journalType.getSelectedItem().toString()),
                                entry.getText().toString()), getContext());
            } else HabitsBasicUtil.notifyUser(getContext(), R.string.try_again_message);
            Objects.requireNonNull(getDialog()).dismiss();
        }));
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

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        dismiss();
    }

}
