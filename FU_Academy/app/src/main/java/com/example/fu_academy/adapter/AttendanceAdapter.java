package com.example.fu_academy.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fu_academy.R;
import com.example.fu_academy.viewmodel.AttendanceDetailViewModel;

import java.util.List;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.AttendanceViewHolder> {
    private List<AttendanceDetailViewModel.AttendanceItem> attendanceItems;

    public AttendanceAdapter(List<AttendanceDetailViewModel.AttendanceItem> attendanceItems) {
        this.attendanceItems = attendanceItems;
    }

    @NonNull
    @Override
    public AttendanceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_attendance, parent, false);
        return new AttendanceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AttendanceViewHolder holder, int position) {
        AttendanceDetailViewModel.AttendanceItem item = attendanceItems.get(position);
        holder.tvDate.setText(item.date);
        holder.tvCourseName.setText(item.courseName);
        holder.tvLecturer.setText(item.lecturerName);
        holder.tvRoom.setText(item.room);
        holder.tvRemark.setText(item.remark != null ? item.remark : "Không có ghi chú");
        
        String status = item.status != null ? item.status : "N/A";
        String statusText = getStatusText(status);
        holder.tvStatus.setText(statusText);
        
        // Set status color
        int statusColor;
        int statusBgColor;
        if ("present".equalsIgnoreCase(status)) {
            statusColor = 0xFF4CAF50;
            statusBgColor = 0xFFE8F5E9;
        } else if ("absent".equalsIgnoreCase(status)) {
            statusColor = 0xFFF44336;
            statusBgColor = 0xFFFFEBEE;
        } else if ("late".equalsIgnoreCase(status)) {
            statusColor = 0xFFFF9800;
            statusBgColor = 0xFFFFF3E0;
        } else {
            statusColor = 0xFF757575;
            statusBgColor = 0xFFF5F5F5;
        }
        holder.tvStatus.setTextColor(statusColor);
        holder.tvStatus.setBackgroundColor(statusBgColor);
    }

    private String getStatusText(String status) {
        if (status == null) return "N/A";
        switch (status.toLowerCase()) {
            case "present": return "Có mặt";
            case "absent": return "Vắng";
            case "late": return "Muộn";
            default: return status;
        }
    }

    @Override
    public int getItemCount() {
        return attendanceItems != null ? attendanceItems.size() : 0;
    }

    public void updateData(List<AttendanceDetailViewModel.AttendanceItem> newItems) {
        this.attendanceItems = newItems;
        notifyDataSetChanged();
    }

    static class AttendanceViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate, tvCourseName, tvLecturer, tvRoom, tvStatus, tvRemark;

        AttendanceViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvCourseName = itemView.findViewById(R.id.tvCourseName);
            tvLecturer = itemView.findViewById(R.id.tvLecturer);
            tvRoom = itemView.findViewById(R.id.tvRoom);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvRemark = itemView.findViewById(R.id.tvRemark);
        }
    }
}


