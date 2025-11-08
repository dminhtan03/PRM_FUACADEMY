package com.example.fu_academy.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.activity.ComponentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fu_academy.R;
import com.example.fu_academy.adapter.AcademicSummaryAdapter;
import com.example.fu_academy.viewmodel.AcademicSummaryViewModel;

public class AcademicSummaryActivity extends ComponentActivity {
    private AcademicSummaryViewModel viewModel;
    private RecyclerView recyclerView;
    private AcademicSummaryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_academic_summary);

        recyclerView = findViewById(R.id.recyclerViewSummary);

        // Setup RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AcademicSummaryAdapter(null);
        recyclerView.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(AcademicSummaryViewModel.class);

        // Get student ID
        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        long studentId = prefs.getLong("student_id", 1);

        // Load academic summary
        viewModel.loadAcademicSummary(studentId);

        // Observe data
        viewModel.getAcademicSummary().observe(this, summaries -> {
            adapter.updateData(summaries);
        });
    }
}

