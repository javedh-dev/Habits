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
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.DialogFragment;

import com.github.dhaval2404.colorpicker.MaterialColorPickerDialog;
import com.github.dhaval2404.colorpicker.model.ColorShape;
import com.github.dhaval2404.colorpicker.model.ColorSwatch;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;

import org.joda.time.LocalDateTime;

import java.text.MessageFormat;
import java.util.Objects;

import tech.zenex.habits.R;
import tech.zenex.habits.database.HabitsRepository;
import tech.zenex.habits.database.entities.HabitEntity;

public class NewHabitBottomSheetFragment extends BottomSheetDialogFragment {
    private MaterialButton create;

    private HabitEntity habitEntity;
    private EditText habitName, habitDesc;
    private MaterialButtonToggleGroup habitType;
    private SwitchCompat onceADay;
    private LinearLayout advancedOptions;
    private View colorPicker;
    private int color = Color.RED;
    private SeekBar streakSeekBar;
    private TextView streakText;
    private boolean isUpdate = false;

    public NewHabitBottomSheetFragment() {
    }

    public NewHabitBottomSheetFragment(HabitEntity habitEntity) {
        this.habitEntity = habitEntity;
        this.isUpdate = true;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.SheetDialog);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_habit_sheet, container, false);
        findViews(view);
        Button advancedButton = view.findViewById(R.id.advanced_button);
        MaterialButton cancel = view.findViewById(R.id.cancel);
        cancel.setOnClickListener(view1 -> Objects.requireNonNull(getDialog()).dismiss());
        create.setOnClickListener(view1 -> createNewHabit());
        advancedButton.setOnClickListener(v -> toggleAdvancedButton());

        colorPicker.setOnClickListener(v -> getColorPickerBuilder().show());

        streakSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                streakText.setText(MessageFormat.format("{0}{1}", progress, getString(R.string.days_text)));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        setUpView();
        return view;
    }

    @NonNull
    private MaterialColorPickerDialog.Builder getColorPickerBuilder() {
        return new MaterialColorPickerDialog.
                Builder(requireContext(), "Pick Color", "Choose", "Cancel",
                (i, s) -> {
                    colorPicker.setBackgroundColor(i);
                    color = i;
                }, null, ColorSwatch.A400, ColorShape.CIRCLE, null);
    }

    private void toggleAdvancedButton() {
        if (advancedOptions.getVisibility() == View.GONE) {
            advancedOptions.setVisibility(View.VISIBLE);
        } else {
            advancedOptions.setVisibility(View.GONE);
        }
    }

    private void createNewHabit() {
        if (habitEntity == null)
            habitEntity = new HabitEntity();
        habitEntity.setName(habitName.getText().toString());
        habitEntity.setHabitType(getHabitType(habitType.getCheckedButtonId()));
        habitEntity.setDescription(habitDesc.getText().toString());
        habitEntity.setCreationDate(LocalDateTime.now());
        habitEntity.setOnceADay(onceADay.isChecked());
        habitEntity.setColor(color);
        habitEntity.setStreakDays(streakSeekBar.getProgress());
        HabitsRepository.upsertHabit(habitEntity);
        Objects.requireNonNull(getDialog()).dismiss();
    }

    private void findViews(View view) {
        habitName = view.findViewById(R.id.habit_name);
        habitDesc = view.findViewById(R.id.habit_desc);
        habitType = view.findViewById(R.id.habit_type);
        onceADay = view.findViewById(R.id.once_a_day);
        advancedOptions = view.findViewById(R.id.advanced_options);
        colorPicker = view.findViewById(R.id.color_picker);
        streakSeekBar = view.findViewById(R.id.streak_seekbar);
        streakText = view.findViewById(R.id.streak_text);
        create = view.findViewById(R.id.create);
    }

    private void setUpView() {
        if (habitEntity != null) {
            habitName.setText(habitEntity.getName());
            habitDesc.setText(habitEntity.getDescription());
            int hType = habitEntity.getHabitType() == HabitEntity.HabitType.BREAK ? R.id.break_btn :
                    R.id.develop_btn;
            habitType.check(hType);
            colorPicker.setBackgroundColor(habitEntity.getColor());
            color = habitEntity.getColor();
            onceADay.setChecked(habitEntity.isOnceADay());
            streakSeekBar.setProgress(habitEntity.getStreakDays());
            if (isUpdate) {
                create.setText(R.string.update_button);
            }
            habitType.getCheckedButtonId();
        }
    }

    private HabitEntity.HabitType getHabitType(int id) {
        if (id == R.id.develop_btn) return HabitEntity.HabitType.DEVELOP;
        else return HabitEntity.HabitType.BREAK;
    }
}
