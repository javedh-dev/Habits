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
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.zenex.habits.R;

import java.util.Objects;

import tech.zenex.habits.models.Habit;
import tech.zenex.habits.models.MainActivityViewModel;

public class NewHabitBottomSheetFragment extends BottomSheetDialogFragment {
    ViewPager2 newHabitWizard;
    FragmentManager fragmentManager;
    MaterialButton create, cancel;
    Habit habit;
    EditText habitTitle;
    MaterialButtonToggleGroup habitType;

    public NewHabitBottomSheetFragment(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.SheetDialog);
//        getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
//        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        habit = new Habit();
        View view = inflater.inflate(R.layout.new_habit_sheet, container, false);
        habitTitle = view.findViewById(R.id.habit_title);
        habitType = view.findViewById(R.id.habit_type);
        /*newHabitWizard = view.findViewById(R.id.new_habit_wizard);
        newHabitWizard.setAdapter(new NewHabitWizardAdapter(this, habit));
        newHabitWizard.getCurrentItem();
        newHabitWizard.setUserInputEnabled(false);
        getDialog().getWindow().setStatusBarColor(getResources().getColor(android.R.color.transparent));*/
        create = view.findViewById(R.id.create);
        cancel = view.findViewById(R.id.cancel);
        cancel.setOnClickListener(view1 -> Objects.requireNonNull(getDialog()).dismiss());
        create.setOnClickListener(view1 -> {
            habit.setName(habitTitle.getText().toString());
            habit.setHabitType(getHabitType(habitType.getCheckedButtonId()));
            MainActivityViewModel.addHabit(habit);
            Objects.requireNonNull(getDialog()).dismiss();
        });

        return view;
    }

    private Habit.HabitType getHabitType(int id) {
        if (id == R.id.develop) return Habit.HabitType.DEVELOP;
        else return Habit.HabitType.BREAK;
    }
}
