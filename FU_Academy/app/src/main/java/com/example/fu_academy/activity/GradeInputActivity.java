package com.example.fu_academy.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fu_academy.R;
import com.example.fu_academy.adapter.GradeInputAdapter;
import com.example.fu_academy.entity.Assignment;
import com.example.fu_academy.entity.Submission;
import com.example.fu_academy.viewmodel.GradeInputViewModel;

import java.util.ArrayList;
import java.util.List;

public class GradeInputActivity extends BaseTeacherActivity {

    private GradeInputViewModel viewModel;
    private GradeInputAdapter adapter;
    private RecyclerView recyclerView;
    private TextView tvClassName;
    private Spinner spinnerAssignments;
    private Button btnSaveGrades;

    private long classId;
    private String className;
    private List<Assignment> assignmentList = new ArrayList<>();
    private Assignment selectedAssignment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grade_input);

        classId = getIntent().getLongExtra("class_id", -1);
        className = getIntent().getStringExtra("class_name");

        initViews();
        setupViewModel();
        setupRecyclerView();
        setupSpinner();
        setupClickListeners();
        loadAssignments();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recycler_view_grades);
        tvClassName = findViewById(R.id.tv_class_name);
        spinnerAssignments = findViewById(R.id.spinner_assignments);
        btnSaveGrades = findViewById(R.id.btn_save_grades);
        
        // Setup back button
        android.widget.ImageButton btnBack = findViewById(R.id.btn_back);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        if (className != null) {
            tvClassName.setText(className);
        }
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(GradeInputViewModel.class);

        viewModel.getAssignmentList().observe(this, this::updateAssignmentSpinner);
        viewModel.getSubmissionList().observe(this, this::updateSubmissionList);

        viewModel.getErrorMessage().observe(this, errorMessage -> {
            if (errorMessage != null && !errorMessage.isEmpty()) {
                Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
            }
        });

        viewModel.getSuccessMessage().observe(this, successMessage -> {
            if (successMessage != null && !successMessage.isEmpty()) {
                Toast.makeText(this, successMessage, Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.getIsLoading().observe(this, isLoading -> {
            btnSaveGrades.setEnabled(!isLoading);
        });
    }

    private void setupRecyclerView() {
        adapter = new GradeInputAdapter(new ArrayList<>());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void setupSpinner() {
        spinnerAssignments.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0 && position <= assignmentList.size()) {
                    selectedAssignment = assignmentList.get(position - 1);
                    loadSubmissions();
                } else {
                    selectedAssignment = null;
                    adapter.updateList(new ArrayList<>());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedAssignment = null;
            }
        });
    }

    private void setupClickListeners() {
        btnSaveGrades.setOnClickListener(v -> saveGrades());
    }

    private void loadAssignments() {
        viewModel.loadAssignments(classId);
    }

    private void updateAssignmentSpinner(List<Assignment> assignments) {
        if (assignments != null) {
            assignmentList = assignments;

            List<String> assignmentNames = new ArrayList<>();
            assignmentNames.add("Select Assignment");

            for (Assignment assignment : assignments) {
                assignmentNames.add(assignment.title);
            }

            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, assignmentNames);
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerAssignments.setAdapter(spinnerAdapter);
        }
    }

    private void loadSubmissions() {
        if (selectedAssignment != null) {
            viewModel.loadSubmissions(selectedAssignment.assignment_id);
        }
    }

    private void updateSubmissionList(List<Submission> submissions) {
        if (submissions != null) {
            adapter.updateList(submissions);
        }
    }

    private void saveGrades() {
        if (selectedAssignment == null) {
            Toast.makeText(this, "Please select an assignment", Toast.LENGTH_SHORT).show();
            return;
        }

        List<Submission> updatedSubmissions = adapter.getUpdatedSubmissions();
        if (updatedSubmissions.isEmpty()) {
            Toast.makeText(this, "No grades to save", Toast.LENGTH_SHORT).show();
            return;
        }

        viewModel.saveGrades(updatedSubmissions, classId);
    }
}
