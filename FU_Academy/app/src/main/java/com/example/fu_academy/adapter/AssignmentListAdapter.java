package com.example.fu_academy.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fu_academy.R;
import com.example.fu_academy.entity.Assignment;

import java.util.List;

public class AssignmentListAdapter extends RecyclerView.Adapter<AssignmentListAdapter.AssignmentViewHolder> {
    private List<Assignment> assignmentList;
    private OnAssignmentClickListener listener;

    public interface OnAssignmentClickListener {
        void onAssignmentClick(Assignment assignment);
    }

    public AssignmentListAdapter(List<Assignment> assignmentList, OnAssignmentClickListener listener) {
        this.assignmentList = assignmentList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AssignmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_assignment_list, parent, false);
        return new AssignmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AssignmentViewHolder holder, int position) {
        Assignment assignment = assignmentList.get(position);
        holder.txtTitle.setText(assignment.getTitle());
        holder.txtDeadline.setText("Hạn nộp: " + (assignment.getDeadline() != null ? assignment.getDeadline() : "N/A"));
        holder.txtStatus.setText("Trạng thái: " + (assignment.getStatus() != null ? assignment.getStatus() : "Chưa nộp"));
        holder.txtGrade.setText("Điểm: " + (assignment.getGrade() != null ? assignment.getGrade() : "Chưa có"));
        
        // Set type badge
        String type = assignment.getType() != null ? assignment.getType() : "N/A";
        holder.txtType.setText(type);
        
        holder.txtDescription.setText(assignment.getDescription() != null ? assignment.getDescription() : "N/A");
        holder.txtFileRequired.setText("File: " + (assignment.isFile_required() ? "Bắt buộc" : "Không bắt buộc"));
        
        holder.btnUpload.setOnClickListener(v -> {
            if (listener != null) {
                listener.onAssignmentClick(assignment);
            }
        });
    }

    @Override
    public int getItemCount() {
        return assignmentList.size();
    }

    static class AssignmentViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle, txtDeadline, txtStatus, txtGrade, txtType, txtDescription, txtFileRequired;
        Button btnUpload;

        public AssignmentViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtDeadline = itemView.findViewById(R.id.txtDeadline);
            txtStatus = itemView.findViewById(R.id.txtStatus);
            txtGrade = itemView.findViewById(R.id.txtGrade);
            txtType = itemView.findViewById(R.id.txtType);
            txtDescription = itemView.findViewById(R.id.txtDescription);
            txtFileRequired = itemView.findViewById(R.id.txtFileRequired);
            btnUpload = itemView.findViewById(R.id.btnUpload);
        }
    }
}

