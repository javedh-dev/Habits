/*
 * Copyright (C) 2020 - 2020 Javed Hussain <javedh.dev@gmail.com>
 * This file is part of Habits project.
 * This file and other under this project can not be copied and/or distributed
 * without the express permission of Javed Hussain
 */

package dev.javed.habits;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.synnapps.carouselview.CarouselView;
import com.takusemba.spotlight.Spotlight;
import com.takusemba.spotlight.Target;
import com.takusemba.spotlight.effet.RippleEffect;
import com.takusemba.spotlight.shape.Circle;

import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

import java.util.List;
import java.util.Objects;

import dev.javed.habits.adapters.HabitsRecyclerViewAdapter;
import dev.javed.habits.database.HabitDetails;
import dev.javed.habits.database.HabitsDatabase;
import dev.javed.habits.fragments.HabitEditSheetFragment;
import dev.javed.habits.fragments.QuoteSheetFragment;
import dev.javed.habits.utils.DatabaseBackupUtil;
import dev.javed.habits.utils.HabitsBasicUtil;
import dev.javed.habits.utils.HabitsConstants;
import dev.javed.habits.views.HabitsRecyclerView;

public class MainActivity extends AppCompatActivity {

    private final int[] images = {
            R.drawable.img3,
            R.drawable.img2,
            R.drawable.img1
    };
    private HabitsRecyclerView rv;
    private ExtendedFloatingActionButton addHabitFAB;
    private SwipeRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handleExtras();
        setContentView(R.layout.activity_main);
        setupAppBar();
        addHabitFAB = findViewById(R.id.add_habit);
        addHabitFAB.setOnClickListener(view -> openAddHabitFragment());
        refreshLayout = findViewById(R.id.refresh);
        refreshLayout.setOnRefreshListener(() -> {
            Objects.requireNonNull(rv.getAdapter()).notifyDataSetChanged();
            new Handler().postDelayed(() -> refreshLayout.setRefreshing(false), 1000);
        });
        if (isLandscape()) {
            setupCarousel();
            setupRecyclerView(2);
        } else {
            setupRecyclerView(4);
        }
        checkQuote();
    }

    private void checkQuote() {
        long quoteShownAt = HabitsBasicUtil.getDefaultSharedPreference(this).getLong("quote_last_shown", 0);
        boolean dailyQuote = HabitsBasicUtil.getDefaultSharedPreference(this).getBoolean("daily_quote", true);
        if (dailyQuote && Days.daysBetween(new LocalDate(quoteShownAt), LocalDateTime.now().toLocalDate()).getDays() > 0) {
            fetchQuote();
        }
    }

    private void handleExtras() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String url = extras.getString(HabitsConstants.MAIN_ACTIVITY_EXTRAS_URL_KEY);
            if (url != null) {
                Intent notificationIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(notificationIntent);
            }
        }
    }

    private void setupAppBar() {
        BottomAppBar appBar = findViewById(R.id.app_bar);
        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.settings, null);
        if (drawable != null) {
            drawable.setTint(getResources().getColor(R.color.inverseIconColor, null));
        }
        appBar.setNavigationIcon(drawable);
        setSupportActionBar(appBar);
    }

    private boolean isLandscape() {
        return getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE;
    }

    private void setupCarousel() {
        CarouselView carouselView = findViewById(R.id.carouselView);
        carouselView.setImageListener((position, imageView) -> imageView.setImageResource(images[position]));
        carouselView.setPageCount(images.length);
    }

    private void setupRecyclerView(int gridSize) {
        rv = findViewById(R.id.habits_list);
        rv.removeAllViews();
        rv.setLayoutManager(new GridLayoutManager(this, gridSize));
        LiveData<List<HabitDetails>> data = getHabits();
        data.observe(this, habitDetails -> {
            Objects.requireNonNull(rv.getAdapter()).notifyDataSetChanged();
            if (!habitDetails.isEmpty() && !HabitsBasicUtil.getDefaultSharedPreference(this).
                    getBoolean(HabitsConstants.PREFERENCE_HABIT_CARD_SPOTLIGHT, false) && !habitDetails.isEmpty()) {
                new Handler().postDelayed(this::showSpotlight, 1000);
            }

        });
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

    private void showSpotlight() {
        View v = View.inflate(this, R.layout.spotlight_overlay, null);
        View focus = rv.getChildAt(0);
        Target target = new Target.Builder()
                .setAnchor(focus)
                .setOverlay(v)
                .setShape(new Circle(focus.getWidth() / 2f))
                .setEffect(new RippleEffect(0, focus.getWidth(), getColor(R.color.colorAccent)))
                .build();

        Spotlight sp = new Spotlight.Builder(this)
                .setTargets(target)
                .setBackgroundColor(Color.argb(255, 255, 255, 255))
                .build();
        sp.start();
        HabitsBasicUtil.getDefaultSharedPreference(this).edit().putBoolean(HabitsConstants.PREFERENCE_HABIT_CARD_SPOTLIGHT, true).apply();
        v.findViewById(R.id.close).setOnClickListener(v1 -> sp.finish());
    }

    private LiveData<List<HabitDetails>> getHabits() {
        return HabitsDatabase.getDatabase(getApplicationContext()).habitDao().getAllHabits();
    }

    private void openAddHabitFragment() {
        HabitEditSheetFragment bottomSheetFragment =
                new HabitEditSheetFragment();
        bottomSheetFragment.show(getSupportFragmentManager(), HabitsConstants.HABIT_EDIT_FRAGMENT_TAG);
        bottomSheetFragment.setCancelable(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (!HabitsBasicUtil.getDefaultSharedPreference(this)
                .getBoolean(HabitsConstants.PREFERENCE_BACKUP, false) || user == null) {
            menu.removeItem(R.id.backup);
            menu.removeItem(R.id.restore);
            if (user == null) {
                HabitsBasicUtil.getDefaultSharedPreference(this)
                        .edit().putBoolean(HabitsConstants.PREFERENCE_BACKUP, false).apply();
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
        } else if (itemId == R.id.quote) {
            fetchQuote();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        Objects.requireNonNull(this.rv.getAdapter()).notifyDataSetChanged();
        invalidateOptionsMenu();
    }

    private void fetchQuote() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Quotes").document("qod").get().
                addOnSuccessListener(documentSnapshot -> {
                    String author = String.valueOf(documentSnapshot.get("author"));
                    String quote = String.valueOf(documentSnapshot.get("quote"));
                    QuoteSheetFragment quoteSheetFragment = QuoteSheetFragment.newInstance(author, quote);
                    quoteSheetFragment.show(getSupportFragmentManager(), null);
                });
        HabitsBasicUtil.getDefaultSharedPreference(this).edit().putLong("quote_last_shown",
                LocalDate.now().toDate().getTime()).apply();
    }

}