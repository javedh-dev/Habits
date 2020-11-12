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

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import com.github.dhaval2404.colorpicker.MaterialColorPickerDialog;
import com.github.dhaval2404.colorpicker.model.ColorShape;
import com.github.dhaval2404.colorpicker.model.ColorSwatch;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;

import org.joda.time.LocalDateTime;

import java.util.Objects;

import tech.zenex.habits.R;
import tech.zenex.habits.database.entities.HabitEntity;
import tech.zenex.habits.models.MainActivityViewModel;

public class NewHabitBottomSheetFragment extends BottomSheetDialogFragment {
    ViewPager2 newHabitWizard;
    FragmentManager fragmentManager;
    MaterialButton create, cancel;
    HabitEntity habitEntity;
    EditText habitName, habitDesc;
    MaterialButtonToggleGroup habitType;
    SwitchCompat onceADay;
    Button advancedButton;
    LinearLayout advancedOptions;
    View colorPicker;
    int color = Color.RED;

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

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        habitEntity = new HabitEntity();
        View view = inflater.inflate(R.layout.new_habit_sheet, container, false);
        habitName = view.findViewById(R.id.habit_name);
        habitDesc = view.findViewById(R.id.habit_desc);
        habitType = view.findViewById(R.id.habit_type);
        onceADay = view.findViewById(R.id.once_a_day);
        advancedButton = view.findViewById(R.id.advanced_button);
        advancedOptions = view.findViewById(R.id.advanced_options);
        colorPicker = view.findViewById(R.id.color_picker);
//        targetDate = view.findViewById(R.id.target_date);
        /*newHabitWizard = view.findViewById(R.id.new_habit_wizard);
        newHabitWizard.setAdapter(new NewHabitWizardAdapter(this, habit));
        newHabitWizard.getCurrentItem();
        newHabitWizard.setUserInputEnabled(false);
        getDialog().getWindow().setStatusBarColor(getResources().getColor(android.R.color.transparent));*/
        create = view.findViewById(R.id.create);
        cancel = view.findViewById(R.id.cancel);
        cancel.setOnClickListener(view1 -> Objects.requireNonNull(getDialog()).dismiss());
        create.setOnClickListener(view1 -> {
            habitEntity.setName(habitName.getText().toString());
            habitEntity.setHabitType(getHabitType(habitType.getCheckedButtonId()));
            habitEntity.setDescription(habitDesc.getText().toString());
            habitEntity.setCreationDate(LocalDateTime.now());
            habitEntity.setOnceADay(onceADay.isChecked());
            habitEntity.setColor(color);
            MainActivityViewModel.addHabit(habitEntity);
            Objects.requireNonNull(getDialog()).dismiss();
        });
        advancedButton.setOnClickListener(v -> {
            if (advancedOptions.getVisibility() == View.GONE) {
                advancedOptions.setVisibility(View.VISIBLE);
            } else {
                advancedOptions.setVisibility(View.GONE);
            }
        });

        colorPicker.setOnClickListener(v -> new MaterialColorPickerDialog.
                Builder(Objects.requireNonNull(getContext()),
                "Pick Color",
                "Choose",
                "Cancel",
                (i, s) -> {
                    colorPicker.setBackgroundColor(i);
                    color = i;
                },
                null,
                ColorSwatch._900,
                ColorShape.CIRCLE,
                null)
                .show());

        /*targetDate.setOnClickListener(v -> {
            AlertDialog dialog = new DatePickerDialog(Objects.requireNonNull(getContext()));
            dialog.show();
        });*/

        return view;
    }

    private HabitEntity.HabitType getHabitType(int id) {
        if (id == R.id.develop) return HabitEntity.HabitType.DEVELOP;
        else return HabitEntity.HabitType.BREAK;
    }
}
