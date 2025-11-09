package com.example.fu_academy.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fu_academy.R;
import com.example.fu_academy.adapter.AssignmentListAdapter;
import com.example.fu_academy.database.EducationDatabase;
import com.example.fu_academy.entity.Assignment;

import java.util.List;

public class AssignmentListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private AssignmentListAdapter adapter;
    private EducationDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Danh Sách Bài Tập");
        }

        recyclerView = findViewById(R.id.recyclerViewAssignments);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        
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
            
            long courseId = getIntent().getLongExtra("course_id", -1);

            List<Assignment> assignments = new java.util.ArrayList<>();
            
            // If courseId is -1, show all assignments for the student's courses
            if (courseId > 0) {
                List<Assignment> courseAssignments = db.assignmentDao().getByCourse(courseId);
                if (courseAssignments != null) {
                    assignments.addAll(courseAssignments);
                }
            } else {
                // Show all assignments for student's courses
                if (studentId > 0) {
                    // Get all courses for student, then get assignments
                    List<com.example.fu_academy.entity.Course> courses = db.courseDao().getCoursesByStudent(studentId);
                    if (courses != null) {
                        for (com.example.fu_academy.entity.Course course : courses) {
                            List<Assignment> courseAssignments = db.assignmentDao().getByCourse(course.getCourse_id());
                            if (courseAssignments != null) {
                                assignments.addAll(courseAssignments);
                            }
                        }
                    }
                }
            }
            
            // Filter: Only show assignments that haven't been submitted yet
            List<Assignment> unsubmittedAssignments = new java.util.ArrayList<>();
            if (studentId > 0) {
                for (Assignment assignment : assignments) {
                    // Check if student has submitted this assignment
                    List<com.example.fu_academy.entity.Submission> submissions = 
                        db.submissionDao().getByAssignment(assignment.getAssignment_id());
                    boolean hasSubmitted = false;
                    if (submissions != null) {
                        for (com.example.fu_academy.entity.Submission sub : submissions) {
                            if (sub.getStudent_id() == studentId) {
                                hasSubmitted = true;
                                break;
                            }
                        }
                    }
                    // Only add if not submitted
                    if (!hasSubmitted) {
                        unsubmittedAssignments.add(assignment);
                    }
                }
            } else {
                unsubmittedAssignments = assignments;
            }
            
            adapter = new AssignmentListAdapter(unsubmittedAssignments, assignment -> {
                Intent intent = new Intent(AssignmentListActivity.this, AssignmentDetailActivity.class);
                intent.putExtra("assignment_id", assignment.getAssignment_id());
                startActivity(intent);
            });
            recyclerView.setAdapter(adapter);
            
        } catch (Exception e) {
            android.util.Log.e("AssignmentList", "Error loading assignments: " + e.getMessage(), e);
            // Show empty adapter on error
            adapter = new AssignmentListAdapter(new java.util.ArrayList<>(), assignment -> {});
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
}

