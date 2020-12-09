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

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import tech.zenex.habits.R;
import tech.zenex.habits.database.HabitDetails;

public class InsightsRecyclerViewAdapter extends RecyclerView.Adapter<InsightsRecyclerViewAdapter.ViewHolder> {

    private final Context context;
    private final FragmentManager fragmentManager;
    HabitDetails habitDetails;

    public InsightsRecyclerViewAdapter(Context context, HabitDetails habitDetails,
                                       FragmentManager fragmentManager) {
        this.context = context;
        this.habitDetails = habitDetails;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.insight_card, null);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 20;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View v) {
            super(v);
        }
    }
}
