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
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import org.joda.time.Days;
import org.joda.time.LocalDateTime;

import java.util.List;
import java.util.Objects;

import tech.zenex.habits.R;
import tech.zenex.habits.database.HabitDetails;
import tech.zenex.habits.dialogs.HabitCheckInBottomSheetFragment;
import tech.zenex.habits.dialogs.HabitMenuSheetFragment;

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
        View card = LayoutInflater.from(context).inflate(R.layout.habit_card, parent, false);
        return new ViewHolder(card);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HabitDetails habitDetails = Objects.requireNonNull(habits.getValue()).get(position);
        holder.habitName.setText(habitDetails.getHabitEntity().getName());
        int percentage = (int) (Math.random() * 100);
        holder.progressBar.setProgress(percentage);
        holder.progressPercentage.setText(String.format(context.getString(R.string.habit_percentage_placeholder), percentage));
        holder.progressBar.setProgressBarColor(habitDetails.getHabitEntity().getColor());
        holder.checkins.setText("" + habitDetails.getHabitTrackerEntities().size());

        holder.journals.setText("" + habitDetails.getJournalEntryEntities().size());

        holder.card.setOnClickListener(view -> {
            if (habitDetails.getHabitEntity().isOnceADay() &&
                    habitDetails.getHabitEntity().getLastCheckIn().toLocalDate()
                            .equals(LocalDateTime.now().toLocalDate())) {
                Snackbar.make(holder.card, "You have already checked in Today.", Snackbar.LENGTH_LONG).show();
            } else {
                HabitCheckInBottomSheetFragment bottomSheetFragment =
                        new HabitCheckInBottomSheetFragment(fragmentManager, habitDetails.getHabitEntity());
                bottomSheetFragment.show(fragmentManager, "HabitCheckInSheet");
            }
        });
        holder.card.setOnLongClickListener(v -> {
//            JournalEntrySheetFragment bottomSheetFragment = new JournalEntrySheetFragment(fragmentManager,
//                    habitDetails.getHabitEntity(), this);
            HabitMenuSheetFragment fragment = new HabitMenuSheetFragment(fragmentManager,
                    habitDetails.getHabitEntity());
            fragment.show(fragmentManager, "JournalEntrySheet");
//            showPopUpMenu(holder.circleMenuView);
            return true;
        });
    }

    private int getPercentage(HabitDetails habitDetails) {
        int streak = Days.daysBetween(LocalDateTime.now(),
                habitDetails.getHabitEntity().getLastFailed()).getDays();
        return (int) (streak / (float) habitDetails.getHabitEntity().getStreakDays()) * 100;
    }

    @Override
    public int getItemCount() {
        return habits.getValue() != null ? habits.getValue().size() : 0;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView habitName;
        private final TextView progressPercentage;
        private final TextView checkins;
        private final TextView journals;
        private final CircularProgressBar progressBar;
        private final View card;
//        private final CircleMenuView circleMenuView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.card = itemView;
            this.habitName = itemView.findViewById(R.id.habit_name);
            this.progressBar = itemView.findViewById(R.id.progress_bar);
            this.progressPercentage = itemView.findViewById(R.id.progress_percentage);
            this.checkins = itemView.findViewById(R.id.check_ins);
            this.journals = itemView.findViewById(R.id.journals);
//            this.circleMenuView = itemView.findViewById(R.id.circle_menu);
        }
    }

//    private void showPopUpMenu(CircleMenuView circleMenuView){
//        circleMenuView.setVisibility(View.VISIBLE);
//        circleMenuView.open(true);
//    }

}
