package com.example.fu_academy.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fu_academy.R;
import com.example.fu_academy.viewmodel.MonthlyCalendarViewModel;

import java.util.List;

public class CalendarEventAdapter extends RecyclerView.Adapter<CalendarEventAdapter.EventViewHolder> {
    private List<MonthlyCalendarViewModel.CalendarEvent> events;

    public CalendarEventAdapter(List<MonthlyCalendarViewModel.CalendarEvent> events) {
        this.events = events;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_calendar_event, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        MonthlyCalendarViewModel.CalendarEvent event = events.get(position);
        
        // Extract day from date (e.g., "2024-01-15" -> "15")
        String day = event.day != null && event.day.length() >= 10 ? 
                     event.day.substring(8) : "?";
        holder.tvDay.setText(day);
        
        holder.tvCourseName.setText(event.courseName);
        holder.tvType.setText(event.type != null ? event.type.toUpperCase() : "EVENT");
        holder.tvTime.setText(event.time != null ? event.time : "");
        holder.tvRoom.setText("Phòng: " + (event.room != null ? event.room : "N/A"));
        
        String status = event.status != null ? event.status : "Scheduled";
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
        return events != null ? events.size() : 0;
    }

    public void updateData(List<MonthlyCalendarViewModel.CalendarEvent> newEvents) {
        this.events = newEvents;
        notifyDataSetChanged();
    }

    static class EventViewHolder extends RecyclerView.ViewHolder {
        TextView tvDay, tvCourseName, tvType, tvTime, tvRoom, tvStatus;

        EventViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDay = itemView.findViewById(R.id.tvDay);
            tvCourseName = itemView.findViewById(R.id.tvCourseName);
            tvType = itemView.findViewById(R.id.tvType);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvRoom = itemView.findViewById(R.id.tvRoom);
            tvStatus = itemView.findViewById(R.id.tvStatus);
        }
    }
}


