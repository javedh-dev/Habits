/*
 * Copyright (C) 2020 - 2020 Javed Hussain <javedh.dev@gmail.com>
 * This file is part of Habits project.
 * This file and other under this project can not be copied and/or distributed
 * without the express permission of Javed Hussain
 */

package dev.javed.habits;

import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;

import org.joda.time.format.DateTimeFormat;

import java.util.ArrayList;

import dev.javed.habits.database.HabitDetails;
import dev.javed.habits.utils.HabitsBasicUtil;
import dev.javed.habits.utils.HabitsConstants;
import dev.javed.habits.utils.HabitsStats;
import dev.javed.habits.views.InsightsCard;
import dev.javed.habits.views.JournalsCard;

public class InsightsActivity extends AppCompatActivity {
    private RadarChart radarChart;
    private HabitDetails habitDetails;
    private HabitsStats habitsStats;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parseExtras();
        setupToolBar();
        displayRadarChart();
        InsightsCard streakDays = findViewById(R.id.streak_days);
        InsightsCard streakStart = findViewById(R.id.streak_start);
        InsightsCard checkIns = findViewById(R.id.check_ins);
        InsightsCard totalFailures = findViewById(R.id.total_failures);
        InsightsCard journals = findViewById(R.id.journals);
        JournalsCard journalEntries = findViewById(R.id.journal_entries);
        streakDays.setInsightDesc(String.valueOf(habitsStats.getStreakDays()));
        streakStart.setInsightDesc(DateTimeFormat.forPattern("dd MMM, yyyy").print(habitsStats.getStreakStart()));
        checkIns.setInsightDesc(String.valueOf(habitsStats.getTotalCheckIns()));
        totalFailures.setInsightDesc(String.valueOf(habitsStats.getTotalFailures()));
        journals.setInsightDesc(String.valueOf(habitsStats.getTotalJournalCount()));
        journalEntries.addEntries(habitDetails.getJournalEntryEntities());
    }

    private void displayRadarChart() {
        defineChart();
        setRadarChartData();
        setupXAxis();
        setupYAxis();
        radarChart.getLegend().setEnabled(false);
    }

    private void setupYAxis() {
        YAxis yAxis = radarChart.getYAxis();
        yAxis.setLabelCount(5, false);
        yAxis.setAxisMinimum(0f);
        yAxis.setAxisMaximum(100f);
        yAxis.setDrawLabels(false);
    }

    private void setupXAxis() {
        XAxis xAxis = radarChart.getXAxis();
        xAxis.setYOffset(0f);
        xAxis.setXOffset(0f);
        xAxis.setValueFormatter(new ValueFormatter() {
            private final String[] radarParams = getResources().getStringArray(R.array.radar_values);

            @Override
            public String getFormattedValue(float value) {
                return radarParams[(int) value % radarParams.length];
            }
        });
        xAxis.setTextColor(getColor(R.color.colorPrimaryDark));
    }

    private void defineChart() {
        radarChart = findViewById(R.id.radar);
        radarChart.getDescription().setEnabled(false);
        radarChart.setWebLineWidth(1f);
        radarChart.setWebColor(getColor(R.color.colorPrimary));
        radarChart.setWebLineWidthInner(1f);
        radarChart.setWebColorInner(getColor(R.color.colorPrimary));
        radarChart.setWebAlpha(100);
        radarChart.animateXY(1400, 1400, Easing.EaseInOutQuad);
    }

    private void setupToolBar() {
        setContentView(R.layout.activity_insights);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(habitDetails.getHabitEntity().getName());
        }
    }

    private void parseExtras() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            habitDetails =
                    (HabitDetails) extras.getSerializable(HabitsConstants.INSIGHT_EXTRAS_HABIT_DETAILS_KEY);
            habitsStats = HabitsStats.calculateStats(habitDetails);
        }
    }

    private void setRadarChartData() {
        ArrayList<RadarEntry> entries = new ArrayList<>();
        entries.add(getStreakValue());
        entries.add(getJournalValue());
        entries.add(getFailureValue());
        entries.add(getCheckInValue());
        entries.add(getConsistencyValue());

        RadarDataSet set1 = new RadarDataSet(entries, "insight");
        set1.setColor(habitDetails.getHabitEntity().getColor());
        set1.setFillColor(habitDetails.getHabitEntity().getColor());
        set1.setDrawFilled(true);
        set1.setFillAlpha(180);
        set1.setLineWidth(2f);
        set1.setDrawHighlightCircleEnabled(true);
        set1.setDrawHighlightIndicators(false);

        ArrayList<IRadarDataSet> sets = new ArrayList<>();
        sets.add(set1);

        RadarData data = new RadarData(sets);
        data.setValueTextSize(8f);
        data.setDrawValues(false);
        data.setValueTextColor(Color.WHITE);

        radarChart.setData(data);
        radarChart.invalidate();
    }

    private RadarEntry getConsistencyValue() {
        float temp = ((float) (habitDetails.getHabitTrackerEntities().size()) /
                HabitsBasicUtil.getDaysFromStart(habitDetails)) * 100;
        temp = temp > 100 ? 100 : temp;
        return new RadarEntry(temp);
    }

    private RadarEntry getCheckInValue() {
        float temp = ((float) (habitDetails.getHabitTrackerEntities().size()) /
                HabitsBasicUtil.getDaysFromStart(habitDetails)) * 100;
        temp = temp > 100 ? 100 : temp;
        return new RadarEntry(temp);
    }

    private RadarEntry getFailureValue() {
        int allowedFailures = HabitsBasicUtil.
                getDefaultSharedPreference(getApplicationContext()).
                getInt(HabitsConstants.PREFERENCE_ALLOWED_FAILURES, 3);
        float temp = ((float) (allowedFailures - habitsStats.getFailureCounter())) /
                allowedFailures * 100;
        temp = temp > 100 ? 100 : temp;
        return new RadarEntry(temp);
    }

    private RadarEntry getJournalValue() {
        float temp = ((float) (habitDetails.getJournalEntryEntities().size())) /
                habitDetails.getHabitTrackerEntities().size() * 100;
        temp = temp > 100 ? 100 : temp;
        return new RadarEntry(temp);
    }

    private RadarEntry getStreakValue() {
        return new RadarEntry(habitsStats.getStreakPercentage());
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}