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
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.synnapps.carouselview.CarouselView;

import java.util.List;
import java.util.Objects;

import tech.zenex.habits.adapters.HabitsRecyclerViewAdapter;
import tech.zenex.habits.database.HabitDetails;
import tech.zenex.habits.database.HabitsDatabase;
import tech.zenex.habits.dialogs.NewHabitBottomSheetFragment;
import tech.zenex.habits.utils.DatabaseBackupUtil;
import tech.zenex.habits.utils.HabitsPreferencesUtil;
import tech.zenex.habits.views.HabitsRecyclerView;

public class MainActivity extends AppCompatActivity {
    HabitsRecyclerView rv;
    CarouselView carouselView;
    ExtendedFloatingActionButton addHabitFAB;
    BottomAppBar appBar;
    int[] images = {R.drawable.img3, R.drawable.img2, R.drawable.img1};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String url = extras.getString("url");
            if (url != null) {
                Intent notificationIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0,
//                notificationIntent, 0);
                startActivity(notificationIntent);
            }
            for (String key : extras.keySet()) {
                String value = extras.getString(key);
                Log.d("Main Intent", "Key: " + key + " Value: " + value);
            }
        }
        setContentView(R.layout.activity_main);
        setupRecyclerView();
        appBar = findViewById(R.id.app_bar);
        appBar.setNavigationIcon(R.drawable.settings_light);
        setSupportActionBar(appBar);
        addHabitFAB = findViewById(R.id.add_habit);
        addHabitFAB.setOnClickListener(view -> openAddHabitFragment());
        carouselView = findViewById(R.id.carouselView);
        carouselView.setImageListener((position, imageView) -> imageView.setImageResource(images[position]));
        carouselView.setPageCount(images.length);
        HabitsPreferencesUtil.verifyPermissions(this);
    }



    private void setupRecyclerView() {
        rv = findViewById(R.id.habits_list);
        rv.setLayoutManager(new GridLayoutManager(this, 2));
        LiveData<List<HabitDetails>> data = getHabits();
        data.observe(this, habitDetails -> {
            Log.d("Habit-Data", Objects.requireNonNull(data.getValue()).toString());
            Objects.requireNonNull(rv.getAdapter()).notifyDataSetChanged();
        });
        rv.setEmptyView(findViewById(R.id.empty_view));
        rv.setAdapter(new HabitsRecyclerViewAdapter(this, data, getSupportFragmentManager()));
    }

    private LiveData<List<HabitDetails>> getHabits() {
        return HabitsDatabase.getDatabase(getApplicationContext()).habitDao().getAllHabits();
    }

    private void openAddHabitFragment() {
        NewHabitBottomSheetFragment bottomSheetFragment =
                new NewHabitBottomSheetFragment();
        bottomSheetFragment.show(getSupportFragmentManager(), "AddHabitBottomSheet");
        bottomSheetFragment.setCancelable(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.app_bar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                break;
            case R.id.backup:
                DatabaseBackupUtil.backup(this);
                Toast.makeText(this, "Backup Completed", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onPostResume() {
        super.onPostResume();
        Objects.requireNonNull(this.rv.getAdapter()).notifyDataSetChanged();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        String TAG = "Permission : ";
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.v(TAG, "Permission: " + permissions[0] + "was " + grantResults[0]);
        }
    }
}