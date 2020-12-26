/*
 * Copyright (C) 2020 - 2020 Javed Hussain <javedh.dev@gmail.com>
 * This file is part of Habits project.
 * This file and other under this project can not be copied and/or distributed
 * without the express permission of Javed Hussain
 */

package dev.javed.habits.fragments;

import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

import dev.javed.habits.R;
import dev.javed.habits.database.HabitsDatabase;
import dev.javed.habits.database.HabitsRepository;
import dev.javed.habits.database.entities.HabitEntity;
import dev.javed.habits.database.entities.JournalEntryEntity;
import dev.javed.habits.utils.HabitsBasicUtil;
import dev.javed.habits.utils.HabitsConstants;

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
        entry.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() >= 3) {
                    add.setEnabled(true);
                    add.setTextColor(getResources().getColor(R.color.colorPrimary, null));
                } else {
                    add.setEnabled(false);
                    add.setTextColor(getResources().getColor(R.color.grey, null));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
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
                        new JournalEntryEntity(habitEntity.getHabitID(), JournalEntryEntity.getJournalType(
                                journalType.getSelectedItem().toString()), entry.getText().toString()),
                        getContext(), result -> {
                            if (result.isSuccessful()) {
                                Objects.requireNonNull(getDialog()).dismiss();
                            } else {
                                HabitsBasicUtil.notifyUser(requireActivity(), result.getMessage());
                            }
                        });
            } else HabitsBasicUtil.notifyUser(getContext(), R.string.try_again_message);
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
