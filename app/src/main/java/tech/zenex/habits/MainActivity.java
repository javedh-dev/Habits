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

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.synnapps.carouselview.CarouselView;

import java.util.List;
import java.util.Objects;

import tech.zenex.habits.adapters.HabitsRecyclerViewAdapter;
import tech.zenex.habits.database.HabitDetails;
import tech.zenex.habits.database.HabitsDatabase;
import tech.zenex.habits.dialogs.NewHabitBottomSheetFragment;
import tech.zenex.habits.utils.HabitsSharedPreferencesUtil;

public class MainActivity extends AppCompatActivity {
    RecyclerView rv;
    CarouselView carouselView;
    ExtendedFloatingActionButton addHabitFAB;
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
        LiveData<List<HabitDetails>> data = getHabits();
        data.observe(this, habitDetails -> {
            Log.d("Habit-Data", data.getValue().toString());
            Objects.requireNonNull(rv.getAdapter()).notifyDataSetChanged();
        });
        rv.setAdapter(new HabitsRecyclerViewAdapter(this, data, getSupportFragmentManager()));

//        MainActivityViewModel.getHabits().observe(this, habits -> Objects.requireNonNull(rv.getAdapter())
//        .notifyDataSetChanged());

//        Log.d("Habit-Data",MainActivityViewModel.getHabits().toString());
        appBar = findViewById(R.id.app_bar);
        appBar.setNavigationIcon(R.drawable.settings);
        setSupportActionBar(appBar);
        addHabitFAB = findViewById(R.id.add_habit);
        addHabitFAB.setOnClickListener(view -> changeFABAlignment());
        carouselView = findViewById(R.id.carouselView);
        carouselView.setImageListener((position, imageView) -> imageView.setImageResource(images[position]));
        carouselView.setPageCount(images.length);

    }

    private LiveData<List<HabitDetails>> getHabits() {
        return HabitsDatabase.getDatabase(getApplicationContext()).habitDao().getAllHabits();
    }

    private boolean checkLoginStatus() {
        try {
            SharedPreferences sp = HabitsSharedPreferencesUtil.getSharedPreference("login_cred",
                    getApplicationContext());
            return sp.getBoolean("isLoggedIn", false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void changeFABAlignment() {
        NewHabitBottomSheetFragment bottomSheetFragment =
                new NewHabitBottomSheetFragment(getSupportFragmentManager());
        bottomSheetFragment.show(getSupportFragmentManager(), "AddHabitBottomSheet");
        bottomSheetFragment.setCancelable(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.app_bar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
//            Toast.makeText(getApplicationContext(),"Home clicked",Toast.LENGTH_LONG).show();
//            new HomeNavigationBottomSheetFragment(getSupportFragmentManager()).show
//            (getSupportFragmentManager(), "HomeMenu");
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}