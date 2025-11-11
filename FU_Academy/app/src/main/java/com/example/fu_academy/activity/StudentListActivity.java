package com.example.fu_academy.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fu_academy.R;
import com.example.fu_academy.adapter.StudentListAdapter;
import com.example.fu_academy.dto.StudentInfo;
import com.example.fu_academy.viewmodel.TeacherDashboardViewModel;

import java.util.ArrayList;
import java.util.List;

public class StudentListActivity extends AppCompatActivity implements StudentListAdapter.OnStudentClickListener {

    private TeacherDashboardViewModel viewModel;
    private StudentListAdapter adapter;
    private RecyclerView recyclerView;
    private TextView tvClassName;
    private long classId;
    private String className;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);

        classId = getIntent().getLongExtra("class_id", -1);
        className = getIntent().getStringExtra("class_name");

        initViews();
        setupViewModel();
        setupRecyclerView();
        loadStudents();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recycler_view_students);
        tvClassName = findViewById(R.id.tv_class_name);

        if (className != null) {
            tvClassName.setText(className);
        }
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(TeacherDashboardViewModel.class);

        viewModel.getStudentList().observe(this, this::updateStudentList);

        viewModel.getErrorMessage().observe(this, errorMessage -> {
            if (errorMessage != null && !errorMessage.isEmpty()) {
                Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setupRecyclerView() {
        adapter = new StudentListAdapter(new ArrayList<>(), this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void loadStudents() {
        viewModel.loadStudentsByClass(classId);
    }

    private void updateStudentList(List<StudentInfo> studentList) {
        if (studentList != null) {
            adapter.updateList(studentList);
        }
    }

    @Override
    public void onStudentClick(StudentInfo studentInfo) {
        Intent intent = new Intent(this, ProfileOverviewActivity.class);
        intent.putExtra("user_id", studentInfo.getStudentId());
        startActivity(intent);
    }

    @Override
    public void onAttendanceClick(StudentInfo studentInfo) {
        Intent intent = new Intent(this, AttendanceSheetActivity.class);
        intent.putExtra("class_id", classId);
        intent.putExtra("student_id", studentInfo.getStudentId());
        intent.putExtra("student_name", studentInfo.getStudentName());
        startActivity(intent);
    }

    @Override
    public void onGradeClick(StudentInfo studentInfo) {
        Intent intent = new Intent(this, GradeInputActivity.class);
        intent.putExtra("class_id", classId);
        intent.putExtra("student_id", studentInfo.getStudentId());
        intent.putExtra("student_name", studentInfo.getStudentName());
        startActivity(intent);
    }
}
