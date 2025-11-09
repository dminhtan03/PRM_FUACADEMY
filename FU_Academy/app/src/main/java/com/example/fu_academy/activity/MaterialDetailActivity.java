package com.example.fu_academy.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.fu_academy.R;
import com.example.fu_academy.database.EducationDatabase;
import com.example.fu_academy.entity.Material;
import com.example.fu_academy.entity.User;

public class MaterialDetailActivity extends AppCompatActivity {
    private TextView txtTitle, txtDescription, txtType, txtFileSize, txtUploadDate, txtOwner;
    private Button btnDownload;
    private EducationDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Chi Tiết Tài Liệu");
        }

        try {
            db = EducationDatabase.getInstance(this);
            
            txtTitle = findViewById(R.id.txtTitle);
            txtDescription = findViewById(R.id.txtDescription);
            txtType = findViewById(R.id.txtType);
            txtFileSize = findViewById(R.id.txtFileSize);
            txtUploadDate = findViewById(R.id.txtUploadDate);
            txtOwner = findViewById(R.id.txtOwner);
            btnDownload = findViewById(R.id.btnDownload);

            long materialId = getIntent().getLongExtra("material_id", -1);
            if (materialId > 0) {
                Material material = db.materialDao().getById(materialId);
                if (material != null) {
                    txtTitle.setText("Tiêu đề: " + (material.getTitle() != null ? material.getTitle() : "N/A"));
                    txtDescription.setText("Mô tả: " + (material.getDescription() != null ? material.getDescription() : "N/A"));
                    txtType.setText("Loại: " + (material.getType() != null ? material.getType() : "N/A"));
                    txtFileSize.setText("Kích thước: " + (material.getFile_size() != null ? material.getFile_size() : "N/A"));
                    txtUploadDate.setText("Ngày tải lên: " + (material.getUpload_date() != null ? material.getUpload_date() : "N/A"));
                    
                    String ownerName = "N/A";
                    if (material.getOwner_id() > 0) {
                        try {
                            User owner = db.userDao().getUserById(material.getOwner_id());
                            if (owner != null) {
                                ownerName = owner.getName() != null ? owner.getName() : "N/A";
                            }
                        } catch (Exception e) {
                            android.util.Log.e("MaterialDetail", "Error getting owner: " + e.getMessage());
                        }
                    }
                    txtOwner.setText("Người tải: " + ownerName);
                    
                    btnDownload.setOnClickListener(v -> {
                        // Handle download
                    });
                } else {
                    // Material not found
                    txtTitle.setText("Tiêu đề: Không tìm thấy tài liệu");
                    txtDescription.setText("Mô tả: N/A");
                    txtType.setText("Loại: N/A");
                    txtFileSize.setText("Kích thước: N/A");
                    txtUploadDate.setText("Ngày tải lên: N/A");
                    txtOwner.setText("Người tải: N/A");
                }
            } else {
                // Invalid material ID
                txtTitle.setText("Tiêu đề: Lỗi");
                txtDescription.setText("Mô tả: ID tài liệu không hợp lệ");
            }
        } catch (Exception e) {
            android.util.Log.e("MaterialDetail", "Error loading material: " + e.getMessage(), e);
            if (txtTitle != null) {
                txtTitle.setText("Tiêu đề: Lỗi tải dữ liệu");
            }
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

