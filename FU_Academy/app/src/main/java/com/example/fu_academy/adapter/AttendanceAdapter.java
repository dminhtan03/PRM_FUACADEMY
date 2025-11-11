package com.example.fu_academy.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fu_academy.R;
import com.example.fu_academy.viewmodel.AttendanceDetailViewModel;

import java.util.ArrayList;
import java.util.List;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.AttendanceViewHolder> {

    private List<AttendanceDetailViewModel.AttendanceItem> attendanceList;

    public AttendanceAdapter(List<AttendanceDetailViewModel.AttendanceItem> attendanceList) {
        this.attendanceList = attendanceList != null ? attendanceList : new ArrayList<>();
    }

    @NonNull
    @Override
    public AttendanceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_attendance_detail, parent, false);
        return new AttendanceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AttendanceViewHolder holder, int position) {
        AttendanceDetailViewModel.AttendanceItem item = attendanceList.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return attendanceList.size();
    }

    public void updateData(List<AttendanceDetailViewModel.AttendanceItem> newList) {
        this.attendanceList = newList != null ? newList : new ArrayList<>();
        notifyDataSetChanged();
    }

    static class AttendanceViewHolder extends RecyclerView.ViewHolder {
        private TextView tvDate, tvStatus, tvSubject, tvTime;

        public AttendanceViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvStatus = itemView.findViewById(R.id.tv_status);
            tvSubject = itemView.findViewById(R.id.tv_subject);
            tvTime = itemView.findViewById(R.id.tv_time);
        }

        public void bind(AttendanceDetailViewModel.AttendanceItem item) {
            if (item != null) {
                tvDate.setText(item.date);
                tvStatus.setText(item.status);
                tvSubject.setText(item.courseName);
                tvTime.setText(item.room + " - " + item.duration + " mins");

                // Set status color
                if ("present".equals(item.status)) {
                    tvStatus.setTextColor(0xFF2ECC71);
                } else if ("absent".equals(item.status)) {
                    tvStatus.setTextColor(0xFFE74C3C);
                } else if ("late".equals(item.status)) {
                    tvStatus.setTextColor(0xFFF39C12);
                }
            }
        }
    }
}
