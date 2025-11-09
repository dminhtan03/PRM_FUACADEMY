package com.example.fu_academy.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fu_academy.R;
import com.example.fu_academy.database.EducationDatabase;
import com.example.fu_academy.entity.Assignment;
import com.example.fu_academy.entity.Submission;

import java.util.List;

public class SubmissionListAdapter extends RecyclerView.Adapter<SubmissionListAdapter.SubmissionViewHolder> {
    private List<Submission> submissionList;
    private EducationDatabase db;

    public SubmissionListAdapter(List<Submission> submissionList, EducationDatabase db) {
        this.submissionList = submissionList;
        this.db = db;
    }

    @NonNull
    @Override
    public SubmissionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_submission_list, parent, false);
        return new SubmissionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubmissionViewHolder holder, int position) {
        Submission submission = submissionList.get(position);
        
        String assignmentTitle = "N/A";
        if (submission.getAssignment_id() > 0) {
            Assignment assignment = db.assignmentDao().findById(submission.getAssignment_id());
            if (assignment != null) {
                assignmentTitle = assignment.getTitle();
            }
        }
        holder.txtAssignmentTitle.setText(assignmentTitle);
        
        holder.txtSubmitDate.setText("Ngày nộp: " + (submission.getSubmit_date() != null ? submission.getSubmit_date() : "N/A"));
        holder.txtFileName.setText("File: " + (submission.getFile_name() != null ? submission.getFile_name() : "N/A"));
        holder.txtFileSize.setText("Kích thước: " + (submission.getFile_size() != null ? submission.getFile_size() : "N/A"));
        holder.txtStatus.setText("Trạng thái: " + (submission.getStatus() != null ? submission.getStatus() : "N/A"));
        
        // Format grade display
        String gradeText = submission.getGrade() != null ? String.valueOf(submission.getGrade()) : "Chưa có";
        holder.txtGrade.setText(gradeText);
        if (submission.getGrade() != null) {
            holder.txtGrade.setTextColor(android.graphics.Color.parseColor("#4CAF50"));
        } else {
            holder.txtGrade.setTextColor(android.graphics.Color.parseColor("#757575"));
        }
        
        holder.txtFeedback.setText(submission.getFeedback() != null ? submission.getFeedback() : "Chưa có nhận xét");
        
        holder.btnDownload.setOnClickListener(v -> {
            // Handle download
        });
    }

    @Override
    public int getItemCount() {
        return submissionList.size();
    }

    static class SubmissionViewHolder extends RecyclerView.ViewHolder {
        TextView txtAssignmentTitle, txtSubmitDate, txtFileName, txtFileSize, txtStatus, txtGrade, txtFeedback;
        Button btnDownload;

        public SubmissionViewHolder(@NonNull View itemView) {
            super(itemView);
            txtAssignmentTitle = itemView.findViewById(R.id.txtAssignmentTitle);
            txtSubmitDate = itemView.findViewById(R.id.txtSubmitDate);
            txtFileName = itemView.findViewById(R.id.txtFileName);
            txtFileSize = itemView.findViewById(R.id.txtFileSize);
            txtStatus = itemView.findViewById(R.id.txtStatus);
            txtGrade = itemView.findViewById(R.id.txtGrade);
            txtFeedback = itemView.findViewById(R.id.txtFeedback);
            btnDownload = itemView.findViewById(R.id.btnDownload);
        }
    }
}

