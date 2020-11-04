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

package tech.zenex.habits.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.mikhaellopez.circularprogressbar.CircularProgressBar;
import com.zenex.habits.R;

import java.util.List;
import java.util.Objects;

import tech.zenex.habits.database.HabitsDatabase;
import tech.zenex.habits.dialogs.HabitCheckInBottomSheetFragment;
import tech.zenex.habits.dialogs.JournalEntrySheetFragment;
import tech.zenex.habits.models.database.Habit;
import tech.zenex.habits.models.database.HabitTracker;
import tech.zenex.habits.models.database.JournalEntry;

public class HabitsRecyclerViewAdapter extends RecyclerView.Adapter<HabitsRecyclerViewAdapter.ViewHolder> {

    MutableLiveData<List<Habit>> habits;
    private Context context;
    private FragmentManager fragmentManager;

    public HabitsRecyclerViewAdapter(Context context, MutableLiveData<List<Habit>> mainActivityViewModel,
                                     FragmentManager fragmentManager) {
        this.context = context;
        this.habits = mainActivityViewModel;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View card = LayoutInflater.from(context).inflate(R.layout.habit_card, parent, false);
        return new ViewHolder(card);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Habit habit = Objects.requireNonNull(habits.getValue()).get(position);
        holder.habitName.setText(habit.getName());
        int percentage = (int) (Math.random() * 100);
        holder.progressBar.setProgress(percentage);
        holder.progressPercentage.setText(String.format(context.getString(R.string.habit_percentage_placeholder), percentage));
        HabitsDatabase.databaseWriteExecutor.execute(() -> {
            List<HabitTracker> trackers = HabitsDatabase.getDatabase(context).habitTrackerDAO().
                    getAllTrackerEntriesForHabit(habit.getHabitID());
            holder.checkins.setText("" + trackers.size());
        });

        HabitsDatabase.databaseWriteExecutor.execute(() -> {
            List<JournalEntry> journals = HabitsDatabase.getDatabase(context).journalDao().
                    getAllJournalEntriesForHabit(habit.getHabitID());
            holder.journals.setText("" + journals.size());
        });

        holder.card.setOnClickListener(view -> {
            HabitCheckInBottomSheetFragment bottomSheetFragment =
                    new HabitCheckInBottomSheetFragment(fragmentManager, habit, this);
            bottomSheetFragment.show(fragmentManager, "HabitCheckInSheet");
        });
        holder.card.setOnLongClickListener(v -> {
            JournalEntrySheetFragment bottomSheetFragment = new JournalEntrySheetFragment(fragmentManager,
                    habit, this);
            bottomSheetFragment.show(fragmentManager, "JournalEntrySheet");
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return habits.getValue() != null ? habits.getValue().size() : 0;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView habitName, progressPercentage, checkins, journals;
        private CircularProgressBar progressBar;
        private View card;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.card = itemView;
            this.habitName = itemView.findViewById(R.id.habit_name);
            this.progressBar = itemView.findViewById(R.id.progress_bar);
            this.progressPercentage = itemView.findViewById(R.id.progress_percentage);
            this.checkins = itemView.findViewById(R.id.check_ins);
            this.journals = itemView.findViewById(R.id.journals);
        }
    }


}
