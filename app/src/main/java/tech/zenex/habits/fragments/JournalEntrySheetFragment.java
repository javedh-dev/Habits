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
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Objects;

import tech.zenex.habits.R;
import tech.zenex.habits.database.HabitsDatabase;
import tech.zenex.habits.database.HabitsRepository;
import tech.zenex.habits.database.entities.HabitEntity;
import tech.zenex.habits.database.entities.JournalEntryEntity;

public class JournalEntrySheetFragment extends BottomSheetDialogFragment {
    FragmentManager fragmentManager;
    HabitEntity habitEntity;

    public JournalEntrySheetFragment(FragmentManager fragmentManager, HabitEntity habitEntity) {
        this.fragmentManager = fragmentManager;
        this.habitEntity = habitEntity;
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
        View v = inflater.inflate(R.layout.journal_entry_sheet, container, false);
        TextView journalTitle = v.findViewById(R.id.journal_title);
        TextView entry = v.findViewById(R.id.entry);
        Button add = v.findViewById(R.id.add_entry);
        Button cancel = v.findViewById(R.id.cancel);
        Spinner journalType = v.findViewById(R.id.journal_type);
        journalTitle.setText(this.habitEntity.getName());
        add.setOnClickListener(v1 -> HabitsDatabase.databaseWriteExecutor.execute(() -> {
            HabitsRepository.addJournalEntry(
                    new JournalEntryEntity(habitEntity.getHabitID(),
                            JournalEntryEntity.getJournalType(journalType.getSelectedItem().toString()),
                            entry.getText().toString()), getContext());
            Objects.requireNonNull(getDialog()).dismiss();
        }));
        cancel.setOnClickListener(v12 -> Objects.requireNonNull(getDialog()).dismiss());
        return v;
    }

}
