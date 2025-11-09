package com.example.fu_academy.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fu_academy.R;
import com.example.fu_academy.viewmodel.ExamScheduleViewModel;

import java.util.List;

public class ExamAdapter extends RecyclerView.Adapter<ExamAdapter.ExamViewHolder> {
    private List<ExamScheduleViewModel.ExamItem> examItems;

    public ExamAdapter(List<ExamScheduleViewModel.ExamItem> examItems) {
        this.examItems = examItems;
    }

    @NonNull
    @Override
    public ExamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_exam, parent, false);
        return new ExamViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExamViewHolder holder, int position) {
        ExamScheduleViewModel.ExamItem item = examItems.get(position);
        holder.tvCourseName.setText(item.courseName);
        holder.tvDate.setText(item.date);
        holder.tvTime.setText(item.time);
        holder.tvRoom.setText(item.room);
        holder.tvSeat.setText(item.seat != null ? item.seat : "N/A");
        holder.tvDuration.setText(item.duration + " phút");
        
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
            statusColor = 0xFFFF9800;
            statusBgColor = 0xFFFFF3E0;
        }
        holder.tvStatus.setTextColor(statusColor);
        holder.tvStatus.setBackgroundColor(statusBgColor);
    }

    @Override
    public int getItemCount() {
        return examItems != null ? examItems.size() : 0;
    }

    public void updateData(List<ExamScheduleViewModel.ExamItem> newItems) {
        this.examItems = newItems;
        notifyDataSetChanged();
    }

    static class ExamViewHolder extends RecyclerView.ViewHolder {
        TextView tvCourseName, tvDate, tvTime, tvRoom, tvSeat, tvDuration, tvStatus;

        ExamViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCourseName = itemView.findViewById(R.id.tvCourseName);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvRoom = itemView.findViewById(R.id.tvRoom);
            tvSeat = itemView.findViewById(R.id.tvSeat);
            tvDuration = itemView.findViewById(R.id.tvDuration);
            tvStatus = itemView.findViewById(R.id.tvStatus);
        }
    }
}



