/*
 * Copyright (C) 2020 - 2020 Javed Hussain <javedh.dev@gmail.com>
 * This file is part of Habits project.
 * This file and other under this project can not be copied and/or distributed
 * without the express permission of Javed Hussain
 */

package tech.zenex.habits;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.synnapps.carouselview.CarouselView;

import java.util.List;
import java.util.Objects;

import tech.zenex.habits.adapters.HabitsRecyclerViewAdapter;
import tech.zenex.habits.database.HabitDetails;
import tech.zenex.habits.database.HabitsDatabase;
import tech.zenex.habits.fragments.HabitEditSheetFragment;
import tech.zenex.habits.utils.DatabaseBackupUtil;
import tech.zenex.habits.utils.HabitsBasicUtil;
import tech.zenex.habits.utils.HabitsConstants;
import tech.zenex.habits.views.HabitsRecyclerView;

public class MainActivity extends AppCompatActivity {
    HabitsRecyclerView rv;
    CarouselView carouselView;
    ExtendedFloatingActionButton addHabitFAB;
    BottomAppBar appBar;
    int[] images = {
            R.drawable.img3,
            R.drawable.img2,
            R.drawable.img1
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String url = extras.getString(HabitsConstants.MAIN_ACTIVITY_EXTRAS_URL_KEY);
            if (url != null) {
                Intent notificationIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(notificationIntent);
            }
        }
        setContentView(R.layout.activity_main);
        appBar = findViewById(R.id.app_bar);
        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.settings, null);
        if (drawable != null) {
            drawable.setTint(getResources().getColor(R.color.inverseIconColor, null));
        }
        appBar.setNavigationIcon(drawable);
        setSupportActionBar(appBar);
        addHabitFAB = findViewById(R.id.add_habit);
        addHabitFAB.setOnClickListener(view -> openAddHabitFragment());
        if (getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
            setupCarousel();
            setupRecyclerView(2);
        } else {
            setupRecyclerView(4);
        }
    }

    private void setupCarousel() {
        carouselView = findViewById(R.id.carouselView);
        carouselView.setImageListener((position, imageView) -> imageView.setImageResource(images[position]));
        carouselView.setPageCount(images.length);
    }

    private void setupRecyclerView(int gridSize) {
        rv = findViewById(R.id.habits_list);
        rv.removeAllViews();
        rv.setLayoutManager(new GridLayoutManager(this, gridSize));
        LiveData<List<HabitDetails>> data = getHabits();
        data.observe(this, habitDetails -> Objects.requireNonNull(rv.getAdapter()).notifyDataSetChanged());
        rv.setEmptyView(findViewById(R.id.empty_view));
        rv.setAdapter(new HabitsRecyclerViewAdapter(this, data, getSupportFragmentManager()));
        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy < -5 && !addHabitFAB.isExtended()) {
                    addHabitFAB.extend();
                } else if (dy > 5 && addHabitFAB.isExtended()) {
                    addHabitFAB.shrink();
                }
            }

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
    }

    private LiveData<List<HabitDetails>> getHabits() {
        return HabitsDatabase.getDatabase(getApplicationContext()).habitDao().getAllHabits();
    }

    private void openAddHabitFragment() {
        HabitEditSheetFragment bottomSheetFragment =
                new HabitEditSheetFragment();
        bottomSheetFragment.show(getSupportFragmentManager(), "AddHabitBottomSheet");
        bottomSheetFragment.setCancelable(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (!HabitsBasicUtil.getDefaultSharedPreference(this)
                .getBoolean("backup", false) || user == null) {
            menu.removeItem(R.id.backup);
            menu.removeItem(R.id.restore);
            if (user == null) {
                HabitsBasicUtil.getDefaultSharedPreference(this)
                        .edit().putBoolean("backup", false).apply();
            }
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
        } else if (itemId == R.id.backup) {
            DatabaseBackupUtil.backup(this);
        } else if (itemId == R.id.restore) {
            DatabaseBackupUtil.restore(this);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        Objects.requireNonNull(this.rv.getAdapter()).notifyDataSetChanged();
        invalidateOptionsMenu();
    }

}