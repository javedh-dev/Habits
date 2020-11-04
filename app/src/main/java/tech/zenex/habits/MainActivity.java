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

package tech.zenex.habits;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.synnapps.carouselview.CarouselView;
import com.zenex.habits.R;

import java.util.Objects;

import tech.zenex.habits.adapters.HabitsRecyclerViewAdapter;
import tech.zenex.habits.dialogs.NewHabitBottomSheetFragment;
import tech.zenex.habits.models.MainActivityViewModel;
import tech.zenex.habits.utils.HabitsSharedPreferencesUtil;

public class MainActivity extends AppCompatActivity {
    RecyclerView rv;
    CarouselView carouselView;
    FloatingActionButton addHabitFAB;
    BottomAppBar appBar;
    int[] images = {R.drawable.img3, R.drawable.img2, R.drawable.img1};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*if (!checkLoginStatus()) {
            Intent i = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
        }*/
        setContentView(R.layout.activity_main);
        rv = findViewById(R.id.habits_list);
        rv.setLayoutManager(new GridLayoutManager(this, 2));
        rv.setAdapter(new HabitsRecyclerViewAdapter(this, MainActivityViewModel.getHabits(), getSupportFragmentManager()));

        MainActivityViewModel.getHabits().observe(this, habits -> Objects.requireNonNull(rv.getAdapter()).notifyDataSetChanged());

        appBar = findViewById(R.id.app_bar);
        addHabitFAB = findViewById(R.id.add_habit);
        addHabitFAB.setOnClickListener(view -> changeFABAlignment());
        carouselView = findViewById(R.id.carouselView);
        carouselView.setPageCount(images.length);
        carouselView.setImageListener((position, imageView) -> imageView.setImageResource(images[position]));

    }

    private boolean checkLoginStatus() {
        try {
            SharedPreferences sp = HabitsSharedPreferencesUtil.getSharedPreference("login_cred", getApplicationContext());
            return sp.getBoolean("isLoggedIn", false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void changeFABAlignment() {
        NewHabitBottomSheetFragment bottomSheetFragment = new NewHabitBottomSheetFragment(getSupportFragmentManager());
        bottomSheetFragment.show(getSupportFragmentManager(), "AddHabitBottomSheet");
        bottomSheetFragment.setCancelable(false);
    }
}