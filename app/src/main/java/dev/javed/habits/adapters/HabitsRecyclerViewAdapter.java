/*
 * Copyright (C) 2020 - 2020 Javed Hussain <javedh.dev@gmail.com>
 * This file is part of Habits project.
 * This file and other under this project can not be copied and/or distributed
 * without the express permission of Javed Hussain
 */

package dev.javed.habits.adapters;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import org.joda.time.LocalDateTime;

import java.util.List;
import java.util.Objects;

import dev.javed.habits.R;
import dev.javed.habits.database.HabitDetails;
import dev.javed.habits.fragments.HabitCheckInSheetFragment;
import dev.javed.habits.fragments.HabitMenuSheetFragment;
import dev.javed.habits.utils.HabitsBasicUtil;
import dev.javed.habits.utils.HabitsConstants;
import dev.javed.habits.utils.HabitsStats;
import dev.javed.habits.views.HabitCard;

public class HabitsRecyclerViewAdapter extends RecyclerView.Adapter<HabitsRecyclerViewAdapter.ViewHolder> {

    private final Context context;
    private final FragmentManager fragmentManager;
    private final LiveData<List<HabitDetails>> habits;
    public HabitsRecyclerViewAdapter(Context context, LiveData<List<HabitDetails>> mainActivityViewModel,
                                     FragmentManager fragmentManager) {
        this.context = context;
        this.habits = mainActivityViewModel;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(new HabitCard(context));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HabitDetails habitDetails = Objects.requireNonNull(habits.getValue()).get(position);
        HabitsStats stats = HabitsStats.calculateStats(habitDetails);
        holder.habitCard.populateHabit(habitDetails, stats);
        holder.habitCard.setOnClickListener(view -> {
            HabitMenuSheetFragment fragment = HabitMenuSheetFragment.newInstance(habitDetails);
            fragment.show(fragmentManager, HabitsConstants.JOURNAL_ENTRY_FRAGMENT_TAG);
        });
        holder.habitCard.setOnLongClickListener(v -> {
            if (habitDetails.getHabitEntity().isOnceADay() &&
                    stats.getLastCheckIn().toLocalDate().equals(LocalDateTime.now().toLocalDate())) {
                HabitsBasicUtil.notifyUser(context, R.string.already_check_in_message);
            } else {
                HabitCheckInSheetFragment bottomSheetFragment =
                        HabitCheckInSheetFragment.newInstance(habitDetails.getHabitEntity());
                bottomSheetFragment.show(fragmentManager, HabitsConstants.HABIT_CHECK_IN_FRAGMENT_TAG);
            }
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return habits.getValue() != null ? habits.getValue().size() : 0;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final HabitCard habitCard;

        public ViewHolder(@NonNull HabitCard habitCard) {
            super(habitCard);
            this.habitCard = habitCard;
        }
    }
}
