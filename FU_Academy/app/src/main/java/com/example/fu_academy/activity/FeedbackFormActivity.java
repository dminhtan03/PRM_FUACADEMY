package com.example.fu_academy.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.activity.ComponentActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.fu_academy.R;
import com.example.fu_academy.entity.Feedback;
import com.example.fu_academy.viewmodel.FeedbackFormViewModel;

import java.util.ArrayList;
import java.util.List;

public class FeedbackFormActivity extends ComponentActivity {
    private FeedbackFormViewModel viewModel;
    private EditText edtSubject, edtContent;
    private Spinner spinnerCategory;
    private RatingBar ratingBar;
    private Button btnSubmit;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_form);

        initViews();
        viewModel = new ViewModelProvider(this).get(FeedbackFormViewModel.class);

        // Get user IDs
        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        long userId = prefs.getLong("user_id", 1);
        long studentId = prefs.getLong("student_id", 1);

        // Load existing feedbacks
        viewModel.loadFeedbackList(studentId);

        // Observe data
        viewModel.getFeedbackList().observe(this, feedbacks -> {
            List<String> displayItems = new ArrayList<>();
            for (Feedback f : feedbacks) {
                String display = String.format("%s (%s)\n%s\nĐánh giá: %d/5 | Trạng thái: %s",
                    f.subject != null ? f.subject : "N/A",
                    f.category != null ? f.category : "N/A",
                    f.content != null ? f.content : "",
                    f.rating, f.status != null ? f.status : "pending");
                if (f.response != null && !f.response.isEmpty()) {
                    display += "\nPhản hồi: " + f.response;
                }
                displayItems.add(display);
            }
            listView.setAdapter(new ArrayAdapter<>(this, 
                android.R.layout.simple_list_item_1, displayItems));
        });

        viewModel.getSubmitResult().observe(this, success -> {
            if (success != null && success) {
                Toast.makeText(this, "Gửi phản hồi thành công!", Toast.LENGTH_SHORT).show();
                edtSubject.setText("");
                edtContent.setText("");
                ratingBar.setRating(0);
            } else if (success != null && !success) {
                Toast.makeText(this, "Gửi phản hồi thất bại!", Toast.LENGTH_SHORT).show();
            }
        });

        btnSubmit.setOnClickListener(v -> {
            String subject = edtSubject.getText().toString().trim();
            String content = edtContent.getText().toString().trim();
            String category = spinnerCategory.getSelectedItem().toString();
            int rating = (int) ratingBar.getRating();

            if (subject.isEmpty() || content.isEmpty()) {
                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            } else {
                viewModel.submitFeedback(userId, studentId, subject, content, category, rating);
            }
        });
    }

    private void initViews() {
        edtSubject = findViewById(R.id.edtSubject);
        edtContent = findViewById(R.id.edtContent);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        ratingBar = findViewById(R.id.ratingBar);
        btnSubmit = findViewById(R.id.btnSubmit);
        listView = findViewById(R.id.listViewFeedback);

        // Setup category spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
            this,
            R.array.feedback_categories,
            android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);
    }
}

