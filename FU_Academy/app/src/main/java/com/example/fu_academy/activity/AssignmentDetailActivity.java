package com.example.fu_academy.activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.fu_academy.R;
import com.example.fu_academy.database.EducationDatabase;
import com.example.fu_academy.entity.Assignment;
import com.example.fu_academy.entity.Submission;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AssignmentDetailActivity extends AppCompatActivity {
    private static final int PICK_FILE_REQUEST = 1001;
    private TextView txtTitle, txtDescription, txtDeadline, txtMaxScore, txtGrade, txtFeedback, txtSubmissionStatus;
    private Button btnUpload;
    private EducationDatabase db;
    private long assignmentId;
    private long studentId;
    private Uri selectedFileUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Chi Tiết Bài Tập");
        }

        try {
            db = EducationDatabase.getInstance(this);
            
            txtTitle = findViewById(R.id.txtTitle);
            txtDescription = findViewById(R.id.txtDescription);
            txtDeadline = findViewById(R.id.txtDeadline);
            txtMaxScore = findViewById(R.id.txtMaxScore);
            txtGrade = findViewById(R.id.txtGrade);
            txtFeedback = findViewById(R.id.txtFeedback);
            txtSubmissionStatus = findViewById(R.id.txtSubmissionStatus);
            btnUpload = findViewById(R.id.btnUpload);

            SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
            studentId = prefs.getLong("student_id", -1);
            if (studentId <= 0) {
                studentId = prefs.getLong("user_id", -1);
            }

            assignmentId = getIntent().getLongExtra("assignment_id", -1);
            if (assignmentId > 0) {
                Assignment assignment = db.assignmentDao().findById(assignmentId);
                if (assignment != null) {
                    txtTitle.setText("Tiêu đề: " + (assignment.getTitle() != null ? assignment.getTitle() : "N/A"));
                    txtDescription.setText("Mô tả: " + (assignment.getDescription() != null ? assignment.getDescription() : "N/A"));
                    txtDeadline.setText("Hạn nộp: " + (assignment.getDeadline() != null ? assignment.getDeadline() : "N/A"));
                    txtMaxScore.setText("Điểm tối đa: " + (assignment.getMax_score() != null ? assignment.getMax_score() : "N/A"));
                    txtGrade.setText("Điểm: " + (assignment.getGrade() != null ? assignment.getGrade() : "Chưa có"));
                    txtFeedback.setText("Nhận xét: " + (assignment.getFeedback() != null ? assignment.getFeedback() : "Chưa có"));
                    
                    // Check if already submitted
                    List<Submission> existingSubmissions = db.submissionDao().getByStudent(studentId);
                    Submission existingSubmission = null;
                    if (existingSubmissions != null) {
                        for (Submission sub : existingSubmissions) {
                            if (sub.getAssignment_id() == assignmentId) {
                                existingSubmission = sub;
                                txtSubmissionStatus.setText("Trạng thái nộp: Đã nộp (" + (sub.getSubmit_date() != null ? sub.getSubmit_date() : "N/A") + ")");
                                break;
                            }
                        }
                    }
                    
                    // Check if assignment is graded (has grade)
                    boolean isGraded = assignment.getGrade() != null;
                    
                    // Check if deadline has passed
                    boolean isDeadlinePassed = false;
                    if (assignment.getDeadline() != null) {
                        try {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                            Date deadlineDate = sdf.parse(assignment.getDeadline());
                            Date currentDate = new Date();
                            if (deadlineDate != null && currentDate.after(deadlineDate)) {
                                isDeadlinePassed = true;
                            }
                        } catch (Exception e) {
                            android.util.Log.e("AssignmentDetail", "Error parsing deadline: " + e.getMessage());
                        }
                    }
                    
                    // Determine if submission/update is allowed
                    boolean canSubmit = !isGraded && !isDeadlinePassed;
                    
                    if (existingSubmission != null) {
                        if (isGraded) {
                            btnUpload.setText("Đã chấm điểm - Không thể cập nhật");
                            btnUpload.setEnabled(false);
                            btnUpload.setAlpha(0.5f);
                        } else if (isDeadlinePassed) {
                            btnUpload.setText("Đã quá hạn - Không thể cập nhật");
                            btnUpload.setEnabled(false);
                            btnUpload.setAlpha(0.5f);
                        } else {
                            btnUpload.setText("Cập nhật bài nộp");
                            btnUpload.setEnabled(true);
                            btnUpload.setAlpha(1.0f);
                        }
                    } else {
                        if (isGraded) {
                            btnUpload.setText("Đã chấm điểm - Không thể nộp");
                            btnUpload.setEnabled(false);
                            btnUpload.setAlpha(0.5f);
                        } else if (isDeadlinePassed) {
                            btnUpload.setText("Đã quá hạn - Không thể nộp");
                            btnUpload.setEnabled(false);
                            btnUpload.setAlpha(0.5f);
                        } else {
                            btnUpload.setText("Nộp bài");
                            btnUpload.setEnabled(true);
                            btnUpload.setAlpha(1.0f);
                        }
                        if (existingSubmission == null) {
                            txtSubmissionStatus.setText("Trạng thái nộp: Chưa nộp");
                        }
                    }
                    
                    btnUpload.setOnClickListener(v -> {
                        if (canSubmit) {
                            openFilePicker();
                        } else {
                            String message = isGraded ? "Bài tập đã được chấm điểm, không thể cập nhật!" : "Đã quá hạn nộp bài!";
                            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    // Assignment not found
                    txtTitle.setText("Tiêu đề: Không tìm thấy bài tập");
                    txtDescription.setText("Mô tả: N/A");
                    txtDeadline.setText("Hạn nộp: N/A");
                    txtMaxScore.setText("Điểm tối đa: N/A");
                    txtGrade.setText("Điểm: N/A");
                    txtFeedback.setText("Nhận xét: N/A");
                    txtSubmissionStatus.setText("Trạng thái nộp: N/A");
                }
            } else {
                // Invalid assignment ID
                txtTitle.setText("Tiêu đề: Lỗi");
                txtDescription.setText("Mô tả: ID bài tập không hợp lệ");
            }
        } catch (Exception e) {
            android.util.Log.e("AssignmentDetail", "Error loading assignment: " + e.getMessage(), e);
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

    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent, "Chọn file để nộp"), PICK_FILE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if (requestCode == PICK_FILE_REQUEST && resultCode == RESULT_OK && data != null) {
            selectedFileUri = data.getData();
            if (selectedFileUri != null) {
                submitAssignment(selectedFileUri);
            }
        }
    }

    private void submitAssignment(Uri fileUri) {
        try {
            // Check assignment again to ensure it's still valid
            Assignment assignment = db.assignmentDao().findById(assignmentId);
            if (assignment == null) {
                Toast.makeText(this, "Không tìm thấy bài tập!", Toast.LENGTH_SHORT).show();
                return;
            }
            
            // Check if already graded
            if (assignment.getGrade() != null) {
                Toast.makeText(this, "Bài tập đã được chấm điểm, không thể cập nhật!", Toast.LENGTH_SHORT).show();
                return;
            }
            
            // Check deadline
            boolean isDeadlinePassed = false;
            if (assignment.getDeadline() != null) {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    Date deadlineDate = sdf.parse(assignment.getDeadline());
                    Date currentDate = new Date();
                    if (deadlineDate != null && currentDate.after(deadlineDate)) {
                        isDeadlinePassed = true;
                    }
                } catch (Exception e) {
                    android.util.Log.e("AssignmentDetail", "Error parsing deadline: " + e.getMessage());
                }
            }
            
            if (isDeadlinePassed) {
                Toast.makeText(this, "Đã quá hạn nộp bài!", Toast.LENGTH_SHORT).show();
                return;
            }
            
            String fileName = getFileName(fileUri);
            String fileSize = getFileSize(fileUri);
            String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
            
            // Check if submission already exists
            List<Submission> existingSubmissions = db.submissionDao().getByStudent(studentId);
            Submission existingSubmission = null;
            if (existingSubmissions != null) {
                for (Submission sub : existingSubmissions) {
                    if (sub.getAssignment_id() == assignmentId) {
                        existingSubmission = sub;
                        break;
                    }
                }
            }
            
            // Determine if submission is on-time or late
            String status = "On-time";
            if (assignment.getDeadline() != null) {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    Date deadlineDate = sdf.parse(assignment.getDeadline());
                    Date submitDate = sdf.parse(currentDate);
                    if (deadlineDate != null && submitDate != null && submitDate.after(deadlineDate)) {
                        status = "Late";
                    }
                } catch (Exception e) {
                    // Keep as "On-time" if parsing fails
                }
            }
            
            if (existingSubmission != null) {
                // Update existing submission
                existingSubmission.setFile_url(fileUri.toString());
                existingSubmission.setFile_name(fileName);
                existingSubmission.setFile_size(fileSize);
                existingSubmission.setSubmit_date(currentDate);
                existingSubmission.setStatus(status);
                db.submissionDao().update(existingSubmission);
                Toast.makeText(this, "Đã cập nhật bài nộp thành công!", Toast.LENGTH_SHORT).show();
            } else {
                // Create new submission
                Submission submission = new Submission();
                submission.setAssignment_id(assignmentId);
                submission.setStudent_id(studentId);
                submission.setFile_url(fileUri.toString());
                submission.setFile_name(fileName);
                submission.setFile_size(fileSize);
                submission.setSubmit_date(currentDate);
                submission.setStatus(status);
                db.submissionDao().insert(submission);
                Toast.makeText(this, "Đã nộp bài thành công!", Toast.LENGTH_SHORT).show();
            }
            
            // Update assignment status
            assignment.setStatus("Đã nộp");
            db.assignmentDao().update(assignment);
            
            // Refresh UI
            txtSubmissionStatus.setText("Trạng thái nộp: Đã nộp (" + currentDate + ")");
            btnUpload.setText("Cập nhật bài nộp");
            
        } catch (Exception e) {
            android.util.Log.e("AssignmentDetail", "Error submitting assignment: " + e.getMessage(), e);
            Toast.makeText(this, "Lỗi khi nộp bài: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            ContentResolver cr = getContentResolver();
            android.database.Cursor cursor = cr.query(uri, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int nameIndex = cursor.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME);
                if (nameIndex >= 0) {
                    result = cursor.getString(nameIndex);
                }
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result != null ? result : "file.pdf";
    }

    private String getFileSize(Uri uri) {
        try {
            ContentResolver cr = getContentResolver();
            android.database.Cursor cursor = cr.query(uri, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int sizeIndex = cursor.getColumnIndex(android.provider.OpenableColumns.SIZE);
                if (sizeIndex >= 0) {
                    long size = cursor.getLong(sizeIndex);
                    cursor.close();
                    if (size < 1024) {
                        return size + " B";
                    } else if (size < 1024 * 1024) {
                        return String.format(Locale.getDefault(), "%.2f KB", size / 1024.0);
                    } else {
                        return String.format(Locale.getDefault(), "%.2f MB", size / (1024.0 * 1024.0));
                    }
                }
                cursor.close();
            }
        } catch (Exception e) {
            android.util.Log.e("AssignmentDetail", "Error getting file size: " + e.getMessage());
        }
        return "N/A";
    }
}

