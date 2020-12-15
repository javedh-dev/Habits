/*
 * Copyright (C) 2020 - 2020 Javed Hussain <javedh.dev@gmail.com>
 * This file is part of Habits project.
 * This file and other under this project can not be copied and/or distributed
 * without the express permission of Javed Hussain
 */

package tech.zenex.habits.adapters;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import org.joda.time.LocalDateTime;

import java.util.List;
import java.util.Objects;

import tech.zenex.habits.R;
import tech.zenex.habits.database.HabitDetails;
import tech.zenex.habits.fragments.HabitCheckInSheetFragment;
import tech.zenex.habits.fragments.HabitMenuSheetFragment;
import tech.zenex.habits.utils.HabitsConstants;
import tech.zenex.habits.utils.HabitsStats;
import tech.zenex.habits.views.HabitCard;

public class HabitsRecyclerViewAdapter extends RecyclerView.Adapter<HabitsRecyclerViewAdapter.ViewHolder> {

    private final Context context;
    private final FragmentManager fragmentManager;
    LiveData<List<HabitDetails>> habits;

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
            HabitMenuSheetFragment fragment = new HabitMenuSheetFragment(fragmentManager,
                    habitDetails);
            fragment.show(fragmentManager, HabitsConstants.JOURNAL_ENTRY_FRAGMENT_TAG);
        });
        holder.habitCard.setOnLongClickListener(v -> {
            if (habitDetails.getHabitEntity().isOnceADay() &&
                    stats.getLastCheckIn().toLocalDate().equals(LocalDateTime.now().toLocalDate())) {
                Snackbar.make(holder.habitCard, R.string.already_check_in_message, Snackbar.LENGTH_LONG).show();
            } else {
                HabitCheckInSheetFragment bottomSheetFragment =
                        new HabitCheckInSheetFragment(fragmentManager, habitDetails.getHabitEntity());
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
