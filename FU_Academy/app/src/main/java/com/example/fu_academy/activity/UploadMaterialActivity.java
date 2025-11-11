package com.example.fu_academy.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;

import com.example.fu_academy.R;
import com.example.fu_academy.entity.Material;
import com.example.fu_academy.viewmodel.UploadMaterialViewModel;

public class UploadMaterialActivity extends BaseTeacherActivity {

    private static final int PICK_FILE_REQUEST = 1;

    private UploadMaterialViewModel viewModel;
    private EditText etTitle, etDescription;
    private TextView tvSelectedFile, tvClassName;
    private Button btnSelectFile, btnUpload;

    private long classId;
    private String className;
    private Uri selectedFileUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_material);

        classId = getIntent().getLongExtra("class_id", -1);
        className = getIntent().getStringExtra("class_name");

        initViews();
        setupViewModel();
        setupClickListeners();
    }

    private void initViews() {
        etTitle = findViewById(R.id.et_title);
        etDescription = findViewById(R.id.et_description);
        tvSelectedFile = findViewById(R.id.tv_selected_file);
        tvClassName = findViewById(R.id.tv_class_name);
        btnSelectFile = findViewById(R.id.btn_select_file);
        btnUpload = findViewById(R.id.btn_upload);

        if (className != null) {
            tvClassName.setText(className);
        }
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(UploadMaterialViewModel.class);

        viewModel.getErrorMessage().observe(this, errorMessage -> {
            if (errorMessage != null && !errorMessage.isEmpty()) {
                Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
            }
        });

        viewModel.getSuccessMessage().observe(this, successMessage -> {
            if (successMessage != null && !successMessage.isEmpty()) {
                Toast.makeText(this, successMessage, Toast.LENGTH_SHORT).show();
                finish(); // Close activity after successful upload
            }
        });

        viewModel.getIsLoading().observe(this, isLoading -> {
            btnUpload.setEnabled(!isLoading);
            btnSelectFile.setEnabled(!isLoading);
        });
    }

    private void setupClickListeners() {
        btnSelectFile.setOnClickListener(v -> selectFile());
        btnUpload.setOnClickListener(v -> uploadMaterial());
    }

    private void selectFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent, "Select File"), PICK_FILE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_FILE_REQUEST && resultCode == RESULT_OK && data != null) {
            selectedFileUri = data.getData();
            if (selectedFileUri != null) {
                String fileName = getFileName(selectedFileUri);
                tvSelectedFile.setText(fileName);
            }
        }
    }

    private String getFileName(Uri uri) {
        String path = uri.getPath();
        if (path != null) {
            int index = path.lastIndexOf('/');
            if (index != -1) {
                return path.substring(index + 1);
            }
        }
        return "Selected File";
    }

    private void uploadMaterial() {
        String title = etTitle.getText().toString().trim();
        String description = etDescription.getText().toString().trim();

        if (title.isEmpty()) {
            Toast.makeText(this, "Please enter a title", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedFileUri == null) {
            Toast.makeText(this, "Please select a file", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create Material object
        Material material = new Material();
        material.class_id = classId;
        material.title = title;
        material.description = description;
        material.file_url = selectedFileUri.toString(); // In a real app, you'd upload to server first
        material.file_name = getFileName(selectedFileUri);
        material.upload_date = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                java.util.Locale.getDefault()).format(new java.util.Date());
        material.file_type = getFileType(selectedFileUri);

        viewModel.uploadMaterial(material);
    }

    private String getFileType(Uri uri) {
        String fileName = getFileName(uri);
        int lastDot = fileName.lastIndexOf('.');
        if (lastDot != -1 && lastDot < fileName.length() - 1) {
            return fileName.substring(lastDot + 1).toLowerCase();
        }
        return "unknown";
    }
}
