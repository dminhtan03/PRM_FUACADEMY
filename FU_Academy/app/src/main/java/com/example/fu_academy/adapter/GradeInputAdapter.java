package com.example.fu_academy.adapter;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fu_academy.R;
import com.example.fu_academy.entity.Submission;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GradeInputAdapter extends RecyclerView.Adapter<GradeInputAdapter.GradeViewHolder> {

    private List<Submission> submissionList;
    private Map<Long, Double> gradeUpdates = new HashMap<>();
    private Map<Long, String> feedbackUpdates = new HashMap<>();
    private Map<Long, String> studentNamesMap = new HashMap<>();

    public GradeInputAdapter(List<Submission> submissionList) {
        this.submissionList = submissionList;
    }

    public void setStudentNamesMap(Map<Long, String> studentNamesMap) {
        this.studentNamesMap = studentNamesMap != null ? studentNamesMap : new HashMap<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public GradeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grade_input, parent, false);
        return new GradeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GradeViewHolder holder, int position) {
        Submission submission = submissionList.get(position);
        holder.bind(submission, gradeUpdates, feedbackUpdates, studentNamesMap);
    }

    @Override
    public int getItemCount() {
        return submissionList.size();
    }

    public void updateList(List<Submission> newList) {
        this.submissionList = newList;
        gradeUpdates.clear();
        feedbackUpdates.clear();
        notifyDataSetChanged();
    }

    public List<Submission> getUpdatedSubmissions() {
        List<Submission> updatedSubmissions = new ArrayList<>();

        for (Submission submission : submissionList) {
            // Skip placeholder submissions (submission_id = 0 means not submitted yet)
            if (submission.submission_id == 0) {
                continue;
            }
            
            boolean hasUpdates = false;

            if (gradeUpdates.containsKey(submission.submission_id)) {
                submission.grade = gradeUpdates.get(submission.submission_id);
                hasUpdates = true;
            }

            if (feedbackUpdates.containsKey(submission.submission_id)) {
                submission.feedback = feedbackUpdates.get(submission.submission_id);
                hasUpdates = true;
            }

            if (hasUpdates) {
                updatedSubmissions.add(submission);
            }
        }

        return updatedSubmissions;
    }

    class GradeViewHolder extends RecyclerView.ViewHolder {
        private TextView tvStudentName, tvFileName, tvSubmitDate;
        private EditText etGrade, etFeedback;

        public GradeViewHolder(@NonNull View itemView) {
            super(itemView);
            tvStudentName = itemView.findViewById(R.id.tv_student_name);
            tvFileName = itemView.findViewById(R.id.tv_file_name);
            tvSubmitDate = itemView.findViewById(R.id.tv_submit_date);
            etGrade = itemView.findViewById(R.id.et_grade);
            etFeedback = itemView.findViewById(R.id.et_feedback);
        }

        public void bind(Submission submission, Map<Long, Double> gradeUpdates, Map<Long, String> feedbackUpdates, Map<Long, String> studentNamesMap) {
            // Get student name from map
            String studentName = studentNamesMap != null ? studentNamesMap.get(submission.student_id) : null;
            if (studentName == null || studentName.isEmpty()) {
                studentName = "Student " + submission.student_id;
            }
            tvStudentName.setText(studentName);
            
            // Show file name or status
            if (submission.submit_date == null || submission.submit_date.isEmpty()) {
                tvFileName.setText("Chưa nộp bài");
                tvSubmitDate.setText("N/A");
            } else {
                tvFileName.setText(submission.file_name != null ? submission.file_name : "Đã nộp");
                tvSubmitDate.setText(submission.submit_date);
            }

            // Set current grade and feedback
            if (submission.grade != null) {
                etGrade.setText(String.valueOf(submission.grade));
            } else {
                etGrade.setText("");
            }

            if (submission.feedback != null) {
                etFeedback.setText(submission.feedback);
            } else {
                etFeedback.setText("");
            }

            // Set up text watchers for grade input
            etGrade.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}

                @Override
                public void afterTextChanged(Editable s) {
                    try {
                        if (!s.toString().isEmpty()) {
                            double grade = Double.parseDouble(s.toString());
                            gradeUpdates.put(submission.submission_id, grade);
                        } else {
                            gradeUpdates.remove(submission.submission_id);
                        }
                    } catch (NumberFormatException e) {
                        gradeUpdates.remove(submission.submission_id);
                    }
                }
            });

            // Set up text watcher for feedback
            etFeedback.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}

                @Override
                public void afterTextChanged(Editable s) {
                    feedbackUpdates.put(submission.submission_id, s.toString());
                }
            });
        }
    }
}
