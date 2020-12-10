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

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

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

import tech.zenex.habits.database.HabitDetails;
import tech.zenex.habits.utils.HabitsBasicUtil;
import tech.zenex.habits.utils.HabitsPreferencesUtil;
import tech.zenex.habits.utils.HabitsStats;
import tech.zenex.habits.views.InsightsCard;
import tech.zenex.habits.views.JournalsCard;

public class InsightsActivity extends AppCompatActivity {
    private RadarChart radarChart;
    private HabitDetails habitDetails;
    private HabitsStats habitsStats;
    private RecyclerView rv;


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
//        rv = findViewById(R.id.insights_rv);
//        rv.setLayoutManager(new GridLayoutManager(this, 3));
//        rv.setAdapter(new InsightsRecyclerViewAdapter(this, habitDetails, getSupportFragmentManager()));
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
//        yAxis.setTextSize(9f);
        yAxis.setAxisMinimum(0f);
        yAxis.setAxisMaximum(100f);
        yAxis.setDrawLabels(false);
    }

    private void setupXAxis() {
        XAxis xAxis = radarChart.getXAxis();
//        xAxis.setTextSize(9f);
        xAxis.setYOffset(0f);
        xAxis.setXOffset(0f);
        xAxis.setValueFormatter(new ValueFormatter() {
            private final String[] mActivities = new String[]{"Streak", "Journal", "Failure", "Check-in",
                    "Consistency"};

            @Override
            public String getFormattedValue(float value) {
                return mActivities[(int) value % mActivities.length];
            }
        });
        xAxis.setTextColor(getColor(R.color.colorPrimaryDark));
    }

    private void defineChart() {
        radarChart = findViewById(R.id.radar);
        radarChart.getDescription().setEnabled(false);
//        radarChart.setRotationEnabled(false);
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
            habitDetails = (HabitDetails) extras.getSerializable("habit");
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
        Log.d("ChartData", String.format("Consistency : %f", temp));
        return new RadarEntry(temp);
    }

    private RadarEntry getCheckInValue() {
        float temp = ((float) (habitDetails.getHabitTrackerEntities().size()) /
                HabitsBasicUtil.getDaysFromStart(habitDetails)) * 100;
        temp = temp > 100 ? 100 : temp;
        Log.d("ChartData", String.format("CheckIn : %f", temp));
        return new RadarEntry(temp);
    }

    private RadarEntry getFailureValue() {
        int allowedFailures = HabitsPreferencesUtil.
                getDefaultSharedPreference(getApplicationContext()).
                getInt("allowed_failures", 3);
        float temp = ((float) (allowedFailures - habitsStats.getFailureCounter())) /
                allowedFailures * 100;
        temp = temp > 100 ? 100 : temp;
        Log.d("ChartData", String.format("Failure : %f", temp));
        return new RadarEntry(temp);
    }

    private RadarEntry getJournalValue() {
        float temp = ((float) (habitDetails.getJournalEntryEntities().size())) /
                habitDetails.getHabitTrackerEntities().size() * 100;
        temp = temp > 100 ? 100 : temp;
        Log.d("ChartData", String.format("Journal : %f", temp));
        return new RadarEntry(temp);
    }

    private RadarEntry getStreakValue() {
        Log.d("ChartData", String.format("Streak : %d", habitsStats.getStreakPercentage()));
        return new RadarEntry(habitsStats.getStreakPercentage());
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

//    protected float getRandom(float range, float start) {
//        return (float) (Math.random() * range) + start;
//    }
}