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

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import tech.zenex.habits.database.HabitDetails;
import tech.zenex.habits.utils.HabitsBasicUtil;
import tech.zenex.habits.utils.HabitsPreferencesUtil;

public class InsightsActivity extends AppCompatActivity {
    private RadarChart radarChart;
    private LineChart lineChart;
    private HabitDetails habitDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parseExtras();
        setupToolBar();
        displayRadarChart();
        displayLineChart();
    }

    private void displayLineChart() {
        setupLineChart();
        setLineChartData();
        setupLineChartLegend();
        setupLineChartXAxis();
        setupLineChartYAxis();
//        lineChart.animateX(1000);
    }

    private void setupLineChartYAxis() {
        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        leftAxis.setTextColor(ColorTemplate.getHoloBlue());
        leftAxis.setDrawGridLines(true);
        leftAxis.setZeroLineColor(Color.BLACK);
        leftAxis.setDrawZeroLine(true);
        leftAxis.setGranularityEnabled(true);
        leftAxis.setAxisMinimum(-5f);
        leftAxis.setAxisMaximum(15f);
        leftAxis.setYOffset(-9f);
        leftAxis.setTextColor(Color.BLACK);

        YAxis rightAxis = lineChart.getAxisRight();
        rightAxis.setEnabled(false);
    }

    private void setupLineChartXAxis() {
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);
        xAxis.setTextSize(10f);
        xAxis.setDrawAxisLine(true);
        xAxis.setAxisLineColor(Color.BLACK);
        xAxis.setDrawGridLines(true);
        xAxis.setTextColor(Color.BLACK);
        xAxis.setCenterAxisLabels(true);
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(new ValueFormatter() {

            private final SimpleDateFormat mFormat = new SimpleDateFormat("dd/MM", Locale.ENGLISH);

            @Override
            public String getFormattedValue(float value) {
                long millis = TimeUnit.DAYS.toMillis((long) value);
                return mFormat.format(new Date(millis));
            }
        });
        xAxis.setSpaceMax(1f);
        xAxis.setSpaceMin(1f);
    }

    private void setupLineChartLegend() {
        Legend l = lineChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setEnabled(true);
    }

    private void setupLineChart() {
        lineChart = findViewById(R.id.line_chart);

        // no description text
        lineChart.getDescription().setEnabled(false);

        // enable touch gestures
        lineChart.setTouchEnabled(true);

        lineChart.setDragDecelerationFrictionCoef(0.9f);

        // enable scaling and dragging
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);
        lineChart.setDrawGridBackground(false);
        lineChart.setHighlightPerDragEnabled(true);
        lineChart.setAutoScaleMinMaxEnabled(true);

        // set an alternative background color
        lineChart.setBackgroundColor(Color.WHITE);
        lineChart.setViewPortOffsets(0f, 0f, 0f, 0f);
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
        yAxis.setTextSize(9f);
        yAxis.setAxisMinimum(0f);
        yAxis.setAxisMaximum(100f);
        yAxis.setDrawLabels(false);
    }

    private void setupXAxis() {
        XAxis xAxis = radarChart.getXAxis();
        xAxis.setTextSize(9f);
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
        xAxis.setTextColor(Color.BLACK);
    }

    private void defineChart() {
        radarChart = findViewById(R.id.radar);
        radarChart.getDescription().setEnabled(false);
        radarChart.setRotationEnabled(false);
        radarChart.setWebLineWidth(1f);
        radarChart.setWebColor(Color.BLACK);
        radarChart.setWebLineWidthInner(1f);
        radarChart.setWebColorInner(Color.BLACK);
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
            actionBar.setTitle("Insights");
        }
    }

    private void parseExtras() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            habitDetails = (HabitDetails) extras.getSerializable("habit");
            for (String key : extras.keySet()) {
                Object value = extras.get(key);
                Log.d("IntentData", "Key: " + key + " Value: " + value);
            }
        }
    }

    private void setLineChartData() {

        /*long creationTime = TimeUnit.MILLISECONDS.toDays(
                habitDetails.getHabitEntity().getCreationDate().toDate().getTime()
        );


        for (float x = now; x < to; x++) {
            float y1 = getRandom(range, 0);
            float y2 = getRandom(range, 0);
            float y3 = getRandom(range, 0);
            entries1.add(new Entry(x, y1)); // add one entry per hour
            entries2.add(new Entry(x, y2)); // add one entry per hour
            entries3.add(new Entry(x, y3)); // add one entry per hour
        }*/

        // create a dataset and give it a type
        ArrayList<Entry> entries = new ArrayList<>();
        for (Map.Entry<Long, Integer> entry : HabitsBasicUtil.getCheckInsByDays(habitDetails).entrySet()) {
            Log.d("Entry", String.format("Key : %d, Value : %d", entry.getKey(), entry.getValue()));
            entries.add(new Entry(entry.getKey(), entry.getValue()));
        }

        LineDataSet set1 = new LineDataSet(entries, "CheckIn");
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);
        set1.setColor(Color.RED);
        set1.setValueTextColor(ColorTemplate.getHoloBlue());
        set1.setLineWidth(1.5f);
        set1.setDrawCircles(true);
        set1.setDrawValues(true);
        set1.setFillAlpha(65);
        set1.setFillColor(Color.RED);
        set1.setHighLightColor(Color.RED);
        set1.setDrawCircleHole(false);
        set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);

        /*LineDataSet set2 = new LineDataSet(entries2, "Failure");
        set2.setAxisDependency(YAxis.AxisDependency.LEFT);
        set2.setColor(Color.MAGENTA);
        set2.setValueTextColor(ColorTemplate.getHoloBlue());
        set2.setLineWidth(1.5f);
        set2.setDrawCircles(false);
        set2.setDrawValues(false);
        set2.setFillAlpha(65);
        set2.setFillColor(Color.MAGENTA);
        set2.setHighLightColor(Color.MAGENTA);
        set2.setDrawCircleHole(false);
        set2.setMode(LineDataSet.Mode.CUBIC_BEZIER);

        LineDataSet set3 = new LineDataSet(entries3, "Journal");
        set3.setAxisDependency(YAxis.AxisDependency.LEFT);
        set3.setColor(Color.GREEN);
        set3.setValueTextColor(ColorTemplate.getHoloBlue());
        set3.setLineWidth(1.5f);
        set3.setDrawCircles(false);
        set3.setDrawValues(false);
        set3.setFillAlpha(65);
        set3.setFillColor(Color.GREEN);
        set3.setHighLightColor(Color.GREEN);
        set3.setDrawCircleHole(false);
        set3.setMode(LineDataSet.Mode.CUBIC_BEZIER);
*/
        // create a data object with the data sets
        LineData data = new LineData(set1/*, set2, set3*/);
        data.setValueTextColor(Color.BLACK);
        data.setValueTextSize(9f);

        // set data
        lineChart.setData(data);
    }


    private void setRadarChartData() {
        ArrayList<RadarEntry> entries = new ArrayList<>();
        entries.add(getStreakValue());
        entries.add(getJournalValue());
        entries.add(getFailureValue());
        entries.add(getCheckInValue());
        entries.add(getConsistencyValue());

        RadarDataSet set1 = new RadarDataSet(entries, "insight");
        set1.setColor(Color.rgb(103, 110, 129));
        set1.setFillColor(Color.rgb(103, 110, 129));
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
        float temp = ((float) (allowedFailures - habitDetails.getHabitEntity().getFailureCounter())) /
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
        float temp = ((float) (HabitsBasicUtil.getStreak(habitDetails))) /
                habitDetails.getHabitEntity().getStreakDays() * 100;
        temp = temp > 100 ? 100 : temp;
        Log.d("ChartData", String.format("Streak : %f", temp));
        return new RadarEntry(temp);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    protected float getRandom(float range, float start) {
        return (float) (Math.random() * range) + start;
    }
}