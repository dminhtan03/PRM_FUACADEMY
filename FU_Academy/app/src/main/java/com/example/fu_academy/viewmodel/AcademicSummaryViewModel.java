package com.example.fu_academy.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.fu_academy.database.EducationDatabase;
import com.example.fu_academy.entity.Enrollment;
import com.example.fu_academy.entity.Class;
import com.example.fu_academy.entity.Course;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AcademicSummaryViewModel extends AndroidViewModel {
    private final EducationDatabase db;
    private final MutableLiveData<List<SemesterSummary>> academicSummary = new MutableLiveData<>();

    public AcademicSummaryViewModel(Application application) {
        super(application);
        db = EducationDatabase.getInstance(application);
    }

    public void loadAcademicSummary(long studentId) {
        List<SemesterSummary> summaries = new ArrayList<>();
        Map<String, List<Enrollment>> semesterMap = new HashMap<>();
        
        // Group enrollments by semester
        List<Enrollment> enrollments = db.enrollmentDao().getByStudent(studentId);
        for (Enrollment e : enrollments) {
            Class classObj = db.classDao().findById(e.class_id);
            if (classObj != null && classObj.semester != null) {
                if (!semesterMap.containsKey(classObj.semester)) {
                    semesterMap.put(classObj.semester, new ArrayList<>());
                }
                semesterMap.get(classObj.semester).add(e);
            }
        }
        
        // Calculate summary for each semester
        for (Map.Entry<String, List<Enrollment>> entry : semesterMap.entrySet()) {
            String semester = entry.getKey();
            List<Enrollment> semesterEnrollments = entry.getValue();
            
            double totalPoints = 0;
            int totalCredits = 0;
            int passed = 0;
            int failed = 0;
            
            for (Enrollment e : semesterEnrollments) {
                Class classObj = db.classDao().findById(e.class_id);
                if (classObj != null) {
                    Course course = db.courseDao().findById(classObj.course_id);
                    if (course != null) {
                        int credits = course.credit;
                        totalCredits += credits;
                        
                        if (e.grade != null && e.grade > 0) {
                            totalPoints += e.grade * credits;
                            if (e.grade >= 5.0) {
                                passed++;
                            } else {
                                failed++;
                            }
                        }
                    }
                }
            }
            
            double gpa = totalCredits > 0 ? totalPoints / totalCredits : 0.0;
            
            SemesterSummary summary = new SemesterSummary();
            summary.semester = semester;
            summary.gpa = gpa;
            summary.credits = totalCredits;
            summary.passed = passed;
            summary.failed = failed;
            summary.rank = 1; // Placeholder
            summary.year = extractYear(semester);
            summary.remark = generateRemark(gpa);
            
            summaries.add(summary);
        }
        
        academicSummary.postValue(summaries);
    }

    private String extractYear(String semester) {
        // Extract year from semester string (e.g., "2024-2025 HK1" -> "2024-2025")
        if (semester != null && semester.contains("-")) {
            String[] parts = semester.split("-");
            if (parts.length >= 2) {
                return parts[0] + "-" + parts[1].split(" ")[0];
            }
        }
        return "N/A";
    }

    private String generateRemark(double gpa) {
        if (gpa >= 8.0) return "Xuất sắc";
        if (gpa >= 7.0) return "Giỏi";
        if (gpa >= 6.0) return "Khá";
        if (gpa >= 5.0) return "Trung bình";
        return "Cần cải thiện";
    }

    public LiveData<List<SemesterSummary>> getAcademicSummary() {
        return academicSummary;
    }

    public static class SemesterSummary {
        public String semester;
        public double gpa;
        public int credits;
        public int passed;
        public int failed;
        public int rank;
        public String year;
        public String remark;
    }
}




