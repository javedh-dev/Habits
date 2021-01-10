/*
 * Copyright (C) 2020 - 2020 Javed Hussain <javedh.dev@gmail.com>
 * This file is part of Habits project.
 * This file and other under this project can not be copied and/or distributed
 * without the express permission of Javed Hussain
 */

package dev.javed.habits.fragments;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;

import org.joda.time.LocalDateTime;

import java.text.MessageFormat;
import java.util.Objects;

import dev.javed.habits.R;
import dev.javed.habits.database.HabitsRepository;
import dev.javed.habits.database.entities.HabitEntity;
import dev.javed.habits.utils.HabitsBasicUtil;
import dev.javed.habits.utils.HabitsConstants;
import petrov.kristiyan.colorpicker.ColorPicker;

public class HabitEditSheetFragment extends HabitsBottomSheet {
    private MaterialButton create;

    private HabitEntity habitEntity;
    private EditText habitName, habitDesc;
    private MaterialButtonToggleGroup habitType;
    private SwitchCompat onceADay;
    private LinearLayout advancedOptions;
    private ImageButton colorPicker;
    private int color = Color.RED;
    private SeekBar streakSeekBar;
    private TextView streakText;
    private boolean isUpdate = false;

    public HabitEditSheetFragment() {
    }

    public HabitEditSheetFragment(HabitEntity habitEntity) {
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
        habitName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() >= 3) {
                    create.setEnabled(true);
                    create.setTextColor(getResources().getColor(R.color.colorPrimary, null));
                } else {
                    create.setEnabled(false);
                    create.setTextColor(getResources().getColor(R.color.grey, null));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        Button advancedButton = view.findViewById(R.id.advanced_button);
        MaterialButton cancel = view.findViewById(R.id.cancel);
        cancel.setOnClickListener(view1 -> Objects.requireNonNull(getDialog()).dismiss());
        create.setOnClickListener(view1 -> createNewHabit());
        advancedButton.setOnClickListener(v -> toggleAdvancedButton());

        colorPicker.setOnClickListener(v -> getColorPickerBuilder().show());

        streakSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                streakText.setText(MessageFormat.format(HabitsConstants.STREAK_DAYS_FORMAT, progress,
                        getString(R.string.days_text)));
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
    private ColorPicker getColorPickerBuilder() {

        return new ColorPicker(requireActivity()).setOnChooseColorListener(
                new ColorPicker.OnChooseColorListener() {
                    @Override
                    public void onChooseColor(int position, int c) {
                        colorPicker.setImageTintList(ColorStateList.valueOf(c));
                        color = c;
                    }

                    @Override
                    public void onCancel() {
                    }
                }).setRoundColorButton(true)
                .setColors(getResources().getIntArray(R.array.colors_palette));
    }

    private void toggleAdvancedButton() {
        if (advancedOptions.getVisibility() == View.GONE) {
            advancedOptions.setVisibility(View.VISIBLE);
        } else {
            advancedOptions.setVisibility(View.GONE);
        }
    }

    private void createNewHabit() {
        if (habitEntity == null) {
            habitEntity = new HabitEntity();
            habitEntity.setCreationDate(LocalDateTime.now());
        }
        habitEntity.setName(habitName.getText().toString());
        habitEntity.setHabitType(getHabitType(habitType.getCheckedButtonId()));
        habitEntity.setDescription(habitDesc.getText().toString());
        habitEntity.setOnceADay(onceADay.isChecked());
        habitEntity.setColor(color);
        habitEntity.setStreakDays(streakSeekBar.getProgress());
        HabitsRepository.upsertHabit(habitEntity, getContext(), result -> {
            if (result.isSuccessful()) {
                Objects.requireNonNull(getDialog()).dismiss();
            } else {
                HabitsBasicUtil.notifyUser(requireActivity(), result.getMessage());
            }
        });
    }

    private void findViews(View view) {
        habitName = view.findViewById(R.id.habit_name);
        habitDesc = view.findViewById(R.id.habit_desc);
        habitType = view.findViewById(R.id.habit_type);
        onceADay = view.findViewById(R.id.once_a_day);
        advancedOptions = view.findViewById(R.id.advanced_options);
        colorPicker = view.findViewById(R.id.color_picker);
        streakSeekBar = view.findViewById(R.id.streak_seekbar);
        streakSeekBar.setMax(Integer.parseInt(Objects.requireNonNull(HabitsBasicUtil.getDefaultSharedPreference(getContext()).
                getString(HabitsConstants.PREFERENCE_STREAK_LIMIT, "100"))));
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
            colorPicker.setImageTintList(ColorStateList.valueOf(habitEntity.getColor()));
            color = habitEntity.getColor();
            onceADay.setChecked(habitEntity.isOnceADay());
            streakSeekBar.setProgress(habitEntity.getStreakDays());
            if (isUpdate) {
                create.setText(R.string.update_text);
            }
            habitType.getCheckedButtonId();
        }
    }

    private HabitEntity.HabitType getHabitType(int id) {
        if (id == R.id.develop_btn) return HabitEntity.HabitType.DEVELOP;
        else return HabitEntity.HabitType.BREAK;
    }
}
