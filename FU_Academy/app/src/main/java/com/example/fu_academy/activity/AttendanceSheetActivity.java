package com.example.fu_academy.activity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fu_academy.R;
import com.example.fu_academy.adapter.TeacherAttendanceAdapter;
import com.example.fu_academy.dto.StudentInfo;
import com.example.fu_academy.entity.AttendanceDetail;
import com.example.fu_academy.viewmodel.AttendanceViewModel;
import com.example.fu_academy.viewmodel.TeacherDashboardViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AttendanceSheetActivity extends BaseTeacherActivity {

    private TeacherDashboardViewModel teacherViewModel;
    private AttendanceViewModel attendanceViewModel;
    private TeacherAttendanceAdapter adapter;
    private RecyclerView recyclerView;
    private TextView tvClassName, tvSelectedDate;
    private Button btnSelectDate, btnSaveAttendance;

    private long classId;
    private String className;
    private String selectedDate;
    private List<StudentInfo> studentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_sheet);

        classId = getIntent().getLongExtra("class_id", -1);
        className = getIntent().getStringExtra("class_name");

        // Set today as default date
        selectedDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        initViews();
        setupViewModels();
        setupRecyclerView();
        setupClickListeners();
        loadStudents();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recycler_view_attendance);
        tvClassName = findViewById(R.id.tv_class_name);
        tvSelectedDate = findViewById(R.id.tv_selected_date);
        btnSelectDate = findViewById(R.id.btn_select_date);
        btnSaveAttendance = findViewById(R.id.btn_save_attendance);
        
        // Setup back button
        android.widget.ImageButton btnBack = findViewById(R.id.btn_back);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        if (className != null) {
            tvClassName.setText(className);
        }
        tvSelectedDate.setText(selectedDate);
    }

    private void setupViewModels() {
        teacherViewModel = new ViewModelProvider(this).get(TeacherDashboardViewModel.class);
        attendanceViewModel = new ViewModelProvider(this).get(AttendanceViewModel.class);

        teacherViewModel.getStudentList().observe(this, this::updateStudentList);

        teacherViewModel.getErrorMessage().observe(this, errorMessage -> {
            if (errorMessage != null && !errorMessage.isEmpty()) {
                Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
            }
        });

        attendanceViewModel.getIsLoading().observe(this, isLoading -> {
            btnSaveAttendance.setEnabled(!isLoading);
        });

        attendanceViewModel.getErrorMessage().observe(this, errorMessage -> {
            if (errorMessage != null && !errorMessage.isEmpty()) {
                Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
            }
        });

        attendanceViewModel.getSuccessMessage().observe(this, successMessage -> {
            if (successMessage != null && !successMessage.isEmpty()) {
                Toast.makeText(this, successMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupRecyclerView() {
        adapter = new TeacherAttendanceAdapter(new ArrayList<>());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void setupClickListeners() {
        btnSelectDate.setOnClickListener(v -> showDatePicker());
        btnSaveAttendance.setOnClickListener(v -> saveAttendance());
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
            this,
            (view, year, month, dayOfMonth) -> {
                calendar.set(year, month, dayOfMonth);
                selectedDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.getTime());
                tvSelectedDate.setText(selectedDate);
                // Reload attendance data for the new date
                loadAttendanceForDate();
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void loadStudents() {
        teacherViewModel.loadStudentsByClass(classId);
    }

    private void updateStudentList(List<StudentInfo> students) {
        if (students != null) {
            this.studentList = students;
            adapter.updateStudentList(students, selectedDate);
            loadAttendanceForDate();
        }
    }

    private void loadAttendanceForDate() {
        // Load existing attendance data for the selected date
        // This would be implemented in AttendanceViewModel
        // For now, we'll just update the adapter with empty attendance
        adapter.updateStudentList(studentList, selectedDate);
    }

    private void saveAttendance() {
        List<AttendanceDetail> attendanceList = adapter.getAttendanceList(classId);
        if (attendanceList.isEmpty()) {
            Toast.makeText(this, "No attendance data to save", Toast.LENGTH_SHORT).show();
            return;
        }

        // Save attendance through ViewModel
        attendanceViewModel.saveAttendance(attendanceList, classId, selectedDate);
    }
}
