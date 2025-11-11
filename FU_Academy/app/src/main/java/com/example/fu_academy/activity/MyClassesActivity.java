package com.example.fu_academy.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fu_academy.R;
import com.example.fu_academy.adapter.ClassListAdapter;
import com.example.fu_academy.dto.ClassInfo;
import com.example.fu_academy.viewmodel.TeacherDashboardViewModel;

import java.util.ArrayList;
import java.util.List;

public class MyClassesActivity extends AppCompatActivity implements ClassListAdapter.OnClassClickListener {

    private TeacherDashboardViewModel viewModel;
    private ClassListAdapter adapter;
    private RecyclerView recyclerView;
    private long lecturerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_classes);

        lecturerId = getIntent().getLongExtra("lecturer_id", -1);

        initViews();
        setupViewModel();
        setupRecyclerView();
        loadClasses();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recycler_view_classes);
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(TeacherDashboardViewModel.class);

        viewModel.getClassList().observe(this, this::updateClassList);

        viewModel.getErrorMessage().observe(this, errorMessage -> {
            if (errorMessage != null && !errorMessage.isEmpty()) {
                Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setupRecyclerView() {
        adapter = new ClassListAdapter(new ArrayList<>(), this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void loadClasses() {
        viewModel.loadMyClasses(lecturerId);
    }

    private void updateClassList(List<ClassInfo> classList) {
        if (classList != null) {
            adapter.updateList(classList);
        }
    }

    @Override
    public void onViewStudentsClick(ClassInfo classInfo) {
        Intent intent = new Intent(this, StudentListActivity.class);
        intent.putExtra("class_id", classInfo.getClassId());
        intent.putExtra("class_name", classInfo.getCourseName());
        startActivity(intent);
    }

    @Override
    public void onAttendanceClick(ClassInfo classInfo) {
        Intent intent = new Intent(this, AttendanceSheetActivity.class);
        intent.putExtra("class_id", classInfo.getClassId());
        intent.putExtra("class_name", classInfo.getCourseName());
        startActivity(intent);
    }

    @Override
    public void onGradeClick(ClassInfo classInfo) {
        Intent intent = new Intent(this, GradeInputActivity.class);
        intent.putExtra("class_id", classInfo.getClassId());
        intent.putExtra("class_name", classInfo.getCourseName());
        startActivity(intent);
    }

    @Override
    public void onMaterialClick(ClassInfo classInfo) {
        Intent intent = new Intent(this, UploadMaterialActivity.class);
        intent.putExtra("class_id", classInfo.getClassId());
        intent.putExtra("class_name", classInfo.getCourseName());
        startActivity(intent);
    }
}
