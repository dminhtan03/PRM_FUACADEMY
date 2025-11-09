package com.example.fu_academy.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import androidx.activity.ComponentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fu_academy.R;
import com.example.fu_academy.adapter.ScheduleAdapter;
import com.example.fu_academy.viewmodel.WeeklyScheduleViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class WeeklyScheduleActivity extends ComponentActivity {
    private WeeklyScheduleViewModel viewModel;
    private RecyclerView recyclerView;
    private ScheduleAdapter adapter;
    private TextView tvWeekRange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly_schedule);

        recyclerView = findViewById(R.id.recyclerViewSchedule);
        tvWeekRange = findViewById(R.id.tvWeekRange);

        // Setup RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ScheduleAdapter(null);
        recyclerView.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(WeeklyScheduleViewModel.class);
        
        // Đảm bảo database được khởi tạo và schedules được cập nhật cho tuần hiện tại
        com.example.fu_academy.database.EducationDatabase db = 
            com.example.fu_academy.database.EducationDatabase.getInstance(this);
        // Đảm bảo schedules luôn được tạo cho tuần hiện tại
        com.example.fu_academy.helper.DatabaseInitializer.ensureSchedulesForCurrentWeek(db);

        // Get student ID from SharedPreferences (saved during login)
        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        long studentId = prefs.getLong("student_id", -1);
        
        android.util.Log.d("WeeklySchedule", "Initial studentId from prefs: " + studentId);
        
        // Nếu không có student_id, thử lấy từ user_id
        if (studentId <= 0) {
            long userId = prefs.getLong("user_id", -1);
            android.util.Log.d("WeeklySchedule", "No student_id, trying user_id: " + userId);
            if (userId > 0) {
                // Kiểm tra xem user có phải là student không (sử dụng db đã khai báo ở trên)
                com.example.fu_academy.entity.User user = db.userDao().getUserById(userId);
                if (user != null && "student".equalsIgnoreCase(user.role)) {
                    studentId = userId;
                    prefs.edit().putLong("student_id", studentId).apply();
                    android.util.Log.d("WeeklySchedule", "Set studentId from user_id: " + studentId);
                } else {
                    android.util.Log.w("WeeklySchedule", "User is not a student: " + (user != null ? user.role : "null"));
                }
            }
        }

        // Get current week start date (thứ 2 của tuần hiện tại)
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Calendar cal = Calendar.getInstance();
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        int daysFromMonday = (dayOfWeek == Calendar.SUNDAY) ? 6 : dayOfWeek - Calendar.MONDAY;
        cal.add(Calendar.DAY_OF_MONTH, -daysFromMonday);
        String startDate = sdf.format(cal.getTime());
        
        android.util.Log.d("WeeklySchedule", "Current date: " + sdf.format(Calendar.getInstance().getTime()));
        android.util.Log.d("WeeklySchedule", "Week start date (Monday): " + startDate);
        
        // Display week range
        Calendar endCal = (Calendar) cal.clone();
        endCal.add(Calendar.DAY_OF_MONTH, 6);
        String endDate = sdf.format(endCal.getTime());
        tvWeekRange.setText(startDate + " - " + endDate);

        // Load schedule
        viewModel.loadWeeklySchedule(studentId, startDate);

        // Observe data
        viewModel.getWeeklySchedule().observe(this, scheduleItems -> {
            adapter.updateData(scheduleItems);
        });
    }
}

