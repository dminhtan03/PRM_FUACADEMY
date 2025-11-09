package com.example.fu_academy.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fu_academy.R;
import com.example.fu_academy.viewmodel.WeeklyScheduleViewModel;

import java.util.List;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder> {
    private List<WeeklyScheduleViewModel.ScheduleItem> scheduleItems;

    public ScheduleAdapter(List<WeeklyScheduleViewModel.ScheduleItem> scheduleItems) {
        this.scheduleItems = scheduleItems;
    }

    @NonNull
    @Override
    public ScheduleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_schedule, parent, false);
        return new ScheduleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScheduleViewHolder holder, int position) {
        WeeklyScheduleViewModel.ScheduleItem item = scheduleItems.get(position);
        holder.tvDate.setText(item.date);
        holder.tvTime.setText(item.time);
        holder.tvCourseName.setText(item.courseName);
        holder.tvLecturer.setText(item.lecturerName);
        holder.tvRoom.setText(item.room);
        
        String status = item.status != null ? item.status : "Scheduled";
        holder.tvStatus.setText(status);
        
        // Set status color
        int statusColor;
        int statusBgColor;
        if ("cancelled".equalsIgnoreCase(status)) {
            statusColor = 0xFFF44336;
            statusBgColor = 0xFFEFEBE9;
        } else if ("completed".equalsIgnoreCase(status)) {
            statusColor = 0xFF4CAF50;
            statusBgColor = 0xFFE8F5E9;
        } else {
            statusColor = 0xFF4CAF50;
            statusBgColor = 0xFFE8F5E9;
        }
        holder.tvStatus.setTextColor(statusColor);
        holder.tvStatus.setBackgroundColor(statusBgColor);
    }

    @Override
    public int getItemCount() {
        return scheduleItems != null ? scheduleItems.size() : 0;
    }

    public void updateData(List<WeeklyScheduleViewModel.ScheduleItem> newItems) {
        this.scheduleItems = newItems;
        notifyDataSetChanged();
    }

    static class ScheduleViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate, tvTime, tvCourseName, tvLecturer, tvRoom, tvStatus;

        ScheduleViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvCourseName = itemView.findViewById(R.id.tvCourseName);
            tvLecturer = itemView.findViewById(R.id.tvLecturer);
            tvRoom = itemView.findViewById(R.id.tvRoom);
            tvStatus = itemView.findViewById(R.id.tvStatus);
        }
    }
}



