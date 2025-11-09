package com.example.fu_academy.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import androidx.activity.ComponentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fu_academy.R;
import com.example.fu_academy.adapter.CalendarEventAdapter;
import com.example.fu_academy.viewmodel.MonthlyCalendarViewModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MonthlyCalendarActivity extends ComponentActivity {
    private MonthlyCalendarViewModel viewModel;
    private RecyclerView recyclerView;
    private CalendarEventAdapter adapter;
    private TextView tvMonth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly_calendar);

        recyclerView = findViewById(R.id.recyclerViewCalendar);
        tvMonth = findViewById(R.id.tvMonth);

        // Setup RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CalendarEventAdapter(null);
        recyclerView.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(MonthlyCalendarViewModel.class);

        // Get student ID from SharedPreferences (saved during login)
        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        long studentId = prefs.getLong("student_id", -1);

        // Get current month
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM", Locale.getDefault());
        String currentMonth = sdf.format(new Date());
        
        // Display month
        SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
        tvMonth.setText(monthFormat.format(new Date()));

        // Load events
        viewModel.loadMonthlyEvents(studentId, currentMonth);

        // Observe data
        viewModel.getMonthlyEvents().observe(this, events -> {
            adapter.updateData(events);
        });
    }
}

