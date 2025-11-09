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
import com.example.fu_academy.adapter.GradeAdapter;
import com.example.fu_academy.database.EducationDatabase;
import com.example.fu_academy.entity.Class;
import com.example.fu_academy.entity.Course;
import com.example.fu_academy.entity.Enrollment;

import java.util.ArrayList;
import java.util.List;

public class GradePerSemesterActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private GradeAdapter adapter;
    private EducationDatabase db;
    private Spinner spinnerSemester;
    private List<GradeItem> allGradeItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grade_per_semester);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Điểm Theo Kỳ");
        }

        recyclerView = findViewById(R.id.recyclerViewGrades);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        spinnerSemester = findViewById(R.id.spinnerSemester);
        
        db = EducationDatabase.getInstance(this);

        try {
            SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
            long studentId = prefs.getLong("student_id", -1);
            
            // Fallback: try to get from user_id if student_id not found
            if (studentId <= 0) {
                long userId = prefs.getLong("user_id", -1);
                if (userId > 0) {
                    studentId = userId;
                }
            }

            allGradeItems = new ArrayList<>();
            java.util.Set<String> semesterSet = new java.util.HashSet<>();
            
            if (studentId > 0) {
                List<Enrollment> enrollments = db.enrollmentDao().getByStudent(studentId);
                
                if (enrollments != null && !enrollments.isEmpty()) {
                    for (Enrollment enrollment : enrollments) {
                        try {
                            com.example.fu_academy.entity.Class classObj = db.classDao().findById(enrollment.getClass_id());
                            if (classObj != null) {
                                Course course = db.courseDao().findById(classObj.getCourse_id());
                                if (course != null) {
                                    GradeItem item = new GradeItem();
                                    item.courseName = course.getName() != null ? course.getName() : "N/A";
                                    item.attendanceScore = enrollment.getAttendance_score() != null ? enrollment.getAttendance_score() : 0.0;
                                    item.assignmentScore = enrollment.getAssignment_score() != null ? enrollment.getAssignment_score() : 0.0;
                                    item.midtermScore = enrollment.getMidterm_score() != null ? enrollment.getMidterm_score() : 0.0;
                                    item.finalScore = enrollment.getFinal_score() != null ? enrollment.getFinal_score() : 0.0;
                                    item.averageScore = enrollment.getAverage_score() != null ? enrollment.getAverage_score() : 0.0;
                                    item.status = enrollment.getStatus() != null ? enrollment.getStatus() : "N/A";
                                    item.credit = course.getCredit();
                                    
                                    // Store full semester string (e.g., "Fall 2024")
                                    String fullSemester = course.getSemester() != null ? course.getSemester() : "Fall 2024";
                                    item.semester = fullSemester;
                                    semesterSet.add(fullSemester);
                                    
                                    item.courseStatus = course.getStatus() != null ? course.getStatus() : "N/A";
                                    allGradeItems.add(item);
                                }
                            }
                        } catch (Exception e) {
                            android.util.Log.e("GradePerSemester", "Error processing enrollment: " + e.getMessage());
                        }
                    }
                }
            }
            
            // Sort semesters by year and type (Fall, Spring, Summer)
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
            
            // Setup semester spinner with full semester names
            if (semesterList.isEmpty()) {
                // Get current semester as default
                java.util.Calendar cal = java.util.Calendar.getInstance();
                int currentYear = cal.get(java.util.Calendar.YEAR);
                int currentMonth = cal.get(java.util.Calendar.MONTH) + 1;
                String currentSemesterType = "Fall";
                if (currentMonth >= 1 && currentMonth <= 4) {
                    currentSemesterType = "Spring";
                } else if (currentMonth >= 5 && currentMonth <= 8) {
                    currentSemesterType = "Summer";
                }
                semesterList.add(currentSemesterType + " " + currentYear);
            }
            ArrayAdapter<String> semesterAdapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_spinner_item, semesterList);
            semesterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerSemester.setAdapter(semesterAdapter);
            
            // Filter by default semester (first one - newest/current)
            if (!semesterList.isEmpty()) {
                filterBySemester(semesterList.get(0));
            }
            
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
            android.util.Log.e("GradePerSemester", "Error loading grades: " + e.getMessage(), e);
            // Show empty adapter on error
            adapter = new GradeAdapter(new ArrayList<>());
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
        if (semesterString == null) return 2024;
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
        if (semesterString == null) return 0;
        String upper = semesterString.toUpperCase();
        if (upper.contains("FALL")) return 0;
        if (upper.contains("SPRING")) return 1;
        if (upper.contains("SUMMER")) return 2;
        return 0; // Default
    }

    private void filterBySemester(String semester) {
        List<GradeItem> filteredItems = new ArrayList<>();
        
        for (GradeItem originalItem : allGradeItems) {
            // Match exact semester string (e.g., "Fall 2024")
            if (semester != null && semester.equals(originalItem.semester)) {
                // Create a copy to avoid modifying original
                GradeItem item = new GradeItem();
                item.courseName = originalItem.courseName;
                item.attendanceScore = originalItem.attendanceScore;
                item.assignmentScore = originalItem.assignmentScore;
                item.midtermScore = originalItem.midtermScore;
                item.finalScore = originalItem.finalScore;
                item.averageScore = originalItem.averageScore;
                item.credit = originalItem.credit;
                item.semester = originalItem.semester;
                item.courseStatus = originalItem.courseStatus;
                
                // For current semester (no grades yet), show "Not Pass" in red
                // For past semesters, show actual grades
                if ("Đang học".equals(item.courseStatus) && 
                    (item.averageScore == null || item.averageScore == 0.0)) {
                    item.status = "Not Pass";
                } else {
                    item.status = originalItem.status != null ? originalItem.status : "N/A";
                }
                
                filteredItems.add(item);
            }
        }
        
        // Limit to maximum 6 courses per semester
        if (filteredItems.size() > 6) {
            filteredItems = filteredItems.subList(0, 6);
        }
        
        adapter = new GradeAdapter(filteredItems);
        recyclerView.setAdapter(adapter);
    }

    public static class GradeItem {
        public String courseName;
        public Double attendanceScore;
        public Double assignmentScore;
        public Double midtermScore;
        public Double finalScore;
        public Double averageScore;
        public String status;
        public int credit;
        public String semester;
        public String courseStatus;
    }
}

