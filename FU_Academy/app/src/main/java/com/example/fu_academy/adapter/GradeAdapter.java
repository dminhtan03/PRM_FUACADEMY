package com.example.fu_academy.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fu_academy.R;
import com.example.fu_academy.activity.GradePerSemesterActivity;

import java.util.List;

public class GradeAdapter extends RecyclerView.Adapter<GradeAdapter.GradeViewHolder> {
    private List<GradePerSemesterActivity.GradeItem> gradeList;

    public GradeAdapter(List<GradePerSemesterActivity.GradeItem> gradeList) {
        this.gradeList = gradeList;
    }

    @NonNull
    @Override
    public GradeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_grade, parent, false);
        return new GradeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GradeViewHolder holder, int position) {
        GradePerSemesterActivity.GradeItem item = gradeList.get(position);
        holder.txtCourseName.setText(item.courseName);
        holder.txtAttendanceScore.setText(item.attendanceScore != null ? String.format("%.1f", item.attendanceScore) : "N/A");
        holder.txtAssignmentScore.setText(item.assignmentScore != null ? String.format("%.1f", item.assignmentScore) : "N/A");
        holder.txtMidtermScore.setText(item.midtermScore != null ? String.format("%.1f", item.midtermScore) : "N/A");
        holder.txtFinalScore.setText(item.finalScore != null ? String.format("%.1f", item.finalScore) : "N/A");
        holder.txtAverageScore.setText(item.averageScore != null ? String.format("%.1f", item.averageScore) : "N/A");
        holder.txtStatus.setText(item.status != null ? item.status : "N/A");
        holder.txtCredit.setText("Tín chỉ: " + item.credit);
        
        // Set color red for "Not Pass" status
        if ("Not Pass".equals(item.status)) {
            holder.txtStatus.setTextColor(android.graphics.Color.RED);
        } else if ("Pass".equals(item.status) || "Đạt".equals(item.status)) {
            holder.txtStatus.setTextColor(android.graphics.Color.parseColor("#4CAF50"));
        } else {
            holder.txtStatus.setTextColor(android.graphics.Color.parseColor("#555555"));
        }
    }

    @Override
    public int getItemCount() {
        return gradeList.size();
    }

    static class GradeViewHolder extends RecyclerView.ViewHolder {
        TextView txtCourseName, txtAttendanceScore, txtAssignmentScore, txtMidtermScore,
                txtFinalScore, txtAverageScore, txtStatus, txtCredit;

        public GradeViewHolder(@NonNull View itemView) {
            super(itemView);
            txtCourseName = itemView.findViewById(R.id.txtCourseName);
            txtAttendanceScore = itemView.findViewById(R.id.txtAttendanceScore);
            txtAssignmentScore = itemView.findViewById(R.id.txtAssignmentScore);
            txtMidtermScore = itemView.findViewById(R.id.txtMidtermScore);
            txtFinalScore = itemView.findViewById(R.id.txtFinalScore);
            txtAverageScore = itemView.findViewById(R.id.txtAverageScore);
            txtStatus = itemView.findViewById(R.id.txtStatus);
            txtCredit = itemView.findViewById(R.id.txtCredit);
        }
    }
}

