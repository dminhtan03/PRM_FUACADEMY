package com.example.fu_academy.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import androidx.activity.ComponentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fu_academy.R;
import com.example.fu_academy.adapter.ScheduleAdapter;
import com.example.fu_academy.viewmodel.WeeklyScheduleViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class WeeklyScheduleActivity extends ComponentActivity {
    private WeeklyScheduleViewModel viewModel;
    private RecyclerView recyclerView;
    private ScheduleAdapter adapter;
    private TextView tvWeekRange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly_schedule);

        recyclerView = findViewById(R.id.recyclerViewSchedule);
        tvWeekRange = findViewById(R.id.tvWeekRange);

        // Setup RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ScheduleAdapter(null);
        recyclerView.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(WeeklyScheduleViewModel.class);

        // Get student ID
        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        long studentId = prefs.getLong("student_id", 1);

        // Get current week start date
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        String startDate = sdf.format(cal.getTime());
        
        // Display week range
        Calendar endCal = (Calendar) cal.clone();
        endCal.add(Calendar.DAY_OF_MONTH, 6);
        String endDate = sdf.format(endCal.getTime());
        tvWeekRange.setText(startDate + " - " + endDate);

        // Load schedule
        viewModel.loadWeeklySchedule(studentId, startDate);

        // Observe data
        viewModel.getWeeklySchedule().observe(this, scheduleItems -> {
            adapter.updateData(scheduleItems);
        });
    }
}

