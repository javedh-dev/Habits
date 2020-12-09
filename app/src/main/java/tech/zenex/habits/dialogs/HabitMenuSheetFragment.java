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

//        journalBtn.shrink();
//        insightsBtn.shrink();
//        editBtn.shrink();
//        deleteBtn.shrink();

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
        new NewHabitBottomSheetFragment(habitDetails.getHabitEntity()).show(fragmentManager, "EditHabit");
        this.dismiss();
    }

    private void openInsightsActivity() {
        Intent intent = new Intent(getActivity(), InsightsActivity.class);
        intent.putExtra("habit", habitDetails);
        startActivity(intent);
//        Toast.makeText(getContext(), "Coming Soon...", Toast.LENGTH_LONG).show();
        this.dismiss();
    }

    private void openJournalFragment() {
        new JournalEntrySheetFragment(fragmentManager, habitDetails.getHabitEntity()).show(fragmentManager,
                "Journal");
        this.dismiss();
    }

}
