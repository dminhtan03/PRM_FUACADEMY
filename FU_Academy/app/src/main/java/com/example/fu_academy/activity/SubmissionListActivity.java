package com.example.fu_academy.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fu_academy.R;
import com.example.fu_academy.adapter.SubmissionListAdapter;
import com.example.fu_academy.database.EducationDatabase;
import com.example.fu_academy.entity.Assignment;
import com.example.fu_academy.entity.Course;
import com.example.fu_academy.entity.Submission;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SubmissionListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private SubmissionListAdapter adapter;
    private EducationDatabase db;
    private Spinner spinnerSemester;
    private List<Submission> allSubmissions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submission_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Danh Sách Bài Đã Nộp");
        }

        recyclerView = findViewById(R.id.recyclerViewSubmissions);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        spinnerSemester = findViewById(R.id.spinnerSemester);
        
        try {
            db = EducationDatabase.getInstance(this);

            SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
            long studentId = prefs.getLong("student_id", -1);
            
            // Fallback: try to get from user_id if student_id not found
            if (studentId <= 0) {
                long userId = prefs.getLong("user_id", -1);
                if (userId > 0) {
                    studentId = userId;
                }
            }

            allSubmissions = new ArrayList<>();
            
            if (studentId > 0) {
                List<Submission> studentSubmissions = db.submissionDao().getByStudent(studentId);
                if (studentSubmissions != null) {
                    allSubmissions.addAll(studentSubmissions);
                }
            }
            
            // Get unique semesters from submissions
            Set<String> semesterSet = new HashSet<>();
            for (Submission submission : allSubmissions) {
                Assignment assignment = db.assignmentDao().findById(submission.getAssignment_id());
                if (assignment != null) {
                    Course course = db.courseDao().findById(assignment.getCourse_id());
                    if (course != null && course.semester != null) {
                        semesterSet.add(course.semester);
                    }
                }
            }
            
            // Sort semesters by year and type
            List<String> semesterList = new ArrayList<>(semesterSet);
            semesterList.sort((s1, s2) -> {
                // Extract year and semester type
                int year1 = extractYear(s1);
                int year2 = extractYear(s2);
                if (year1 != year2) {
                    return Integer.compare(year2, year1); // Descending order (newest first)
                }
                // Same year, sort by semester type
                int type1 = getSemesterTypeOrder(s1);
                int type2 = getSemesterTypeOrder(s2);
                return Integer.compare(type1, type2);
            });
            
            // Add "Tất cả" option at the beginning
            semesterList.add(0, "Tất cả");
            
            // Setup semester spinner
            ArrayAdapter<String> semesterAdapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_spinner_item, semesterList);
            semesterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerSemester.setAdapter(semesterAdapter);
            
            // Filter by default (all)
            filterBySemester("Tất cả");
            
            // Setup spinner listener
            spinnerSemester.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String selectedSemester = semesterList.get(position);
                    filterBySemester(selectedSemester);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {}
            });
            
        } catch (Exception e) {
            android.util.Log.e("SubmissionList", "Error loading submissions: " + e.getMessage(), e);
            // Show empty adapter on error
            adapter = new SubmissionListAdapter(new ArrayList<>(), db);
            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    private int extractYear(String semesterString) {
        if (semesterString == null || semesterString.equals("Tất cả")) return 2024;
        try {
            // Extract year from string like "Fall 2024", "Spring 2023", etc.
            java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("\\d{4}");
            java.util.regex.Matcher matcher = pattern.matcher(semesterString);
            if (matcher.find()) {
                return Integer.parseInt(matcher.group());
            }
        } catch (Exception e) {
            // Ignore
        }
        return 2024; // Default
    }
    
    private int getSemesterTypeOrder(String semesterString) {
        if (semesterString == null || semesterString.equals("Tất cả")) return 0;
        String upper = semesterString.toUpperCase();
        if (upper.contains("FALL")) return 0;
        if (upper.contains("SPRING")) return 1;
        if (upper.contains("SUMMER")) return 2;
        return 0; // Default
    }
    
    private void filterBySemester(String semester) {
        List<Submission> filteredSubmissions = new ArrayList<>();
        
        if ("Tất cả".equals(semester)) {
            filteredSubmissions.addAll(allSubmissions);
        } else {
            for (Submission submission : allSubmissions) {
                Assignment assignment = db.assignmentDao().findById(submission.getAssignment_id());
                if (assignment != null) {
                    Course course = db.courseDao().findById(assignment.getCourse_id());
                    if (course != null && course.semester != null && course.semester.equals(semester)) {
                        filteredSubmissions.add(submission);
                    }
                }
            }
        }
        
        adapter = new SubmissionListAdapter(filteredSubmissions, db);
        recyclerView.setAdapter(adapter);
    }
}

