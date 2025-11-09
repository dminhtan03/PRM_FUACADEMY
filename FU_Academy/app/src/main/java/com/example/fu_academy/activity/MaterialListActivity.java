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
import com.example.fu_academy.adapter.MaterialListAdapter;
import com.example.fu_academy.database.EducationDatabase;
import com.example.fu_academy.entity.Material;

import java.util.List;

public class MaterialListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MaterialListAdapter adapter;
    private EducationDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Danh Sách Tài Liệu");
        }

        recyclerView = findViewById(R.id.recyclerViewMaterials);
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

            List<Material> materials = new java.util.ArrayList<>();
            
            if (courseId > 0) {
                List<Material> courseMaterials = db.materialDao().getByCourse(courseId);
                if (courseMaterials != null) {
                    materials.addAll(courseMaterials);
                }
            } else {
                // Show all materials for student's courses
                if (studentId > 0) {
                    List<com.example.fu_academy.entity.Course> courses = db.courseDao().getCoursesByStudent(studentId);
                    if (courses != null) {
                        for (com.example.fu_academy.entity.Course course : courses) {
                            List<Material> courseMaterials = db.materialDao().getByCourse(course.getCourse_id());
                            if (courseMaterials != null) {
                                materials.addAll(courseMaterials);
                            }
                        }
                    }
                }
            }
            
            adapter = new MaterialListAdapter(materials, material -> {
                Intent intent = new Intent(MaterialListActivity.this, MaterialDetailActivity.class);
                intent.putExtra("material_id", material.getMaterial_id());
                startActivity(intent);
            }, db);
            recyclerView.setAdapter(adapter);
            
        } catch (Exception e) {
            android.util.Log.e("MaterialList", "Error loading materials: " + e.getMessage(), e);
            // Show empty adapter on error
            adapter = new MaterialListAdapter(new java.util.ArrayList<>(), material -> {}, db);
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

