package com.example.fu_academy.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fu_academy.R;
import com.example.fu_academy.activity.AssignmentListActivity;
import com.example.fu_academy.activity.MaterialListActivity;
import com.example.fu_academy.database.EducationDatabase;
import com.example.fu_academy.entity.Class;
import com.example.fu_academy.entity.Course;
import com.example.fu_academy.entity.User;

import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {
    private List<Course> courseList;
    private EducationDatabase db;
    private android.content.Context context;

    public CourseAdapter(List<Course> courseList) {
        this.courseList = courseList;
    }

    public void setDatabase(EducationDatabase db) {
        this.db = db;
    }

    public void setContext(android.content.Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_course_table, parent, false);
        return new CourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        Course course = courseList.get(position);
        
        // Set STT (sequential number)
        holder.txtSTT.setText(String.valueOf(position + 1));
        
        // Set Course Code
        holder.txtCourseCode.setText(course.getCourse_code() != null ? course.getCourse_code() : "N/A");
        
        // Set Course Name
        holder.txtName.setText(course.getName());
        
        // Set Credit
        holder.txtCredit.setText(String.valueOf(course.getCredit()));
        
        // Get lecturer name
        String lecturerName = "N/A";
        if (db != null) {
            User lecturer = db.userDao().getUserById(course.getLecturer_id());
            if (lecturer != null) {
                lecturerName = lecturer.getName();
            }
        }
        holder.txtLecturer.setText(lecturerName);
        
        // Extract term number from semester string
        String semester = course.getSemester() != null ? course.getSemester() : "";
        String termNo = extractTermNumber(semester);
        holder.txtTermNo.setText(termNo);
        
        // Set Status
        holder.txtStatus.setText(course.getStatus() != null ? course.getStatus() : "N/A");
        
        // Set Type
        holder.txtType.setText(course.getType() != null ? course.getType() : "N/A");
        
        // Get room/class group
        String roomInfo = "N/A";
        if (db != null) {
            List<Class> classes = db.classDao().getByCourse(course.getCourse_id());
            if (classes != null && !classes.isEmpty()) {
                Class classObj = classes.get(0);
                roomInfo = classObj.getRoom() != null ? classObj.getRoom() : "N/A";
            }
        }
        holder.txtRoom.setText(roomInfo);
        
        // Set alternating background color
        if (position % 2 == 0) {
            holder.itemView.setBackgroundColor(android.graphics.Color.parseColor("#FFFFFF"));
        } else {
            holder.itemView.setBackgroundColor(android.graphics.Color.parseColor("#F5F5F5"));
        }
        
        // Set click listener to open Assignment List
        holder.itemView.setOnClickListener(v -> {
            if (context != null) {
                Intent intent = new Intent(context, AssignmentListActivity.class);
                intent.putExtra("course_id", course.getCourse_id());
                context.startActivity(intent);
            }
        });
    }
    
    private String extractTermNumber(String semester) {
        if (semester == null || semester.isEmpty()) {
            return "0";
        }
        String upper = semester.toUpperCase();
        // Extract number from semester string (e.g., "Fall 2024" -> "0", "Spring 2024" -> "1")
        // Or if it contains term number directly
        if (upper.contains("FALL") || upper.contains("HK1") || upper.contains("TERM 0")) {
            return "0";
        } else if (upper.contains("SPRING") || upper.contains("HK2") || upper.contains("TERM 1")) {
            return "1";
        } else if (upper.contains("SUMMER") || upper.contains("HK3") || upper.contains("TERM 2")) {
            return "2";
        } else if (upper.contains("TERM 3")) {
            return "3";
        }
        // Try to extract number from string
        try {
            // Look for patterns like "2024-1", "Term 1", etc.
            java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("(?:TERM|HK|HỌC KỲ)\\s*(\\d+)", java.util.regex.Pattern.CASE_INSENSITIVE);
            java.util.regex.Matcher matcher = pattern.matcher(semester);
            if (matcher.find()) {
                int term = Integer.parseInt(matcher.group(1));
                return String.valueOf(term - 1); // Convert to 0-based
            }
        } catch (Exception e) {
            // Ignore
        }
        return "0"; // Default
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    static class CourseViewHolder extends RecyclerView.ViewHolder {
        TextView txtSTT, txtCourseCode, txtName, txtCredit, txtLecturer, txtTermNo, txtStatus, txtType, txtRoom;

        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            txtSTT = itemView.findViewById(R.id.txtSTT);
            txtCourseCode = itemView.findViewById(R.id.txtCourseCode);
            txtName = itemView.findViewById(R.id.txtCourseName);
            txtCredit = itemView.findViewById(R.id.txtCredit);
            txtLecturer = itemView.findViewById(R.id.txtLecturer);
            txtTermNo = itemView.findViewById(R.id.txtTermNo);
            txtStatus = itemView.findViewById(R.id.txtStatus);
            txtType = itemView.findViewById(R.id.txtType);
            txtRoom = itemView.findViewById(R.id.txtRoom);
        }
    }
}