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
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import org.joda.time.LocalDateTime;

import java.util.List;
import java.util.Objects;

import tech.zenex.habits.database.HabitDetails;
import tech.zenex.habits.dialogs.HabitCheckInBottomSheetFragment;
import tech.zenex.habits.dialogs.HabitMenuSheetFragment;
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
        holder.habitCard.populateHabit(habitDetails);

        holder.habitCard.setOnClickListener(view -> {
            if (habitDetails.getHabitEntity().isOnceADay() &&
                    habitDetails.getHabitEntity().getLastCheckIn().toLocalDate()
                            .equals(LocalDateTime.now().toLocalDate())) {
                Snackbar.make(holder.habitCard, "You have already checked in Today.", Snackbar.LENGTH_LONG).show();
            } else {
                HabitCheckInBottomSheetFragment bottomSheetFragment =
                        new HabitCheckInBottomSheetFragment(fragmentManager, habitDetails.getHabitEntity());
                bottomSheetFragment.show(fragmentManager, "HabitCheckInSheet");
            }
        });
        holder.habitCard.setOnLongClickListener(v -> {
            HabitMenuSheetFragment fragment = new HabitMenuSheetFragment(fragmentManager,
                    habitDetails.getHabitEntity());
            fragment.show(fragmentManager, "JournalEntrySheet");
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
