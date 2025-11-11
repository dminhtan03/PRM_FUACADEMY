package com.example.fu_academy.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fu_academy.R;
import com.example.fu_academy.dto.StudentInfo;

import java.util.List;

public class StudentListAdapter extends RecyclerView.Adapter<StudentListAdapter.StudentViewHolder> {

    private List<StudentInfo> studentList;
    private OnStudentClickListener listener;

    public interface OnStudentClickListener {
        void onStudentClick(StudentInfo studentInfo);
        void onAttendanceClick(StudentInfo studentInfo);
        void onGradeClick(StudentInfo studentInfo);
    }

    public StudentListAdapter(List<StudentInfo> studentList, OnStudentClickListener listener) {
        this.studentList = studentList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_student, parent, false);
        return new StudentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        StudentInfo studentInfo = studentList.get(position);
        holder.bind(studentInfo, listener);
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    public void updateList(List<StudentInfo> newList) {
        this.studentList = newList;
        notifyDataSetChanged();
    }

    static class StudentViewHolder extends RecyclerView.ViewHolder {
        private TextView tvStudentName, tvStudentId, tvEmail, tvAttendanceRate, tvGrade;
        private Button btnViewProfile, btnAttendance, btnGrade;

        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvStudentName = itemView.findViewById(R.id.tv_student_name);
            tvStudentId = itemView.findViewById(R.id.tv_student_id);
            tvEmail = itemView.findViewById(R.id.tv_email);
            tvAttendanceRate = itemView.findViewById(R.id.tv_attendance_rate);
            tvGrade = itemView.findViewById(R.id.tv_grade);
            btnViewProfile = itemView.findViewById(R.id.btn_view_profile);
            btnAttendance = itemView.findViewById(R.id.btn_attendance);
            btnGrade = itemView.findViewById(R.id.btn_grade);
        }

        public void bind(StudentInfo studentInfo, OnStudentClickListener listener) {
            tvStudentName.setText(studentInfo.getStudentName());
            tvStudentId.setText(studentInfo.getStudentId_text());
            tvEmail.setText(studentInfo.getStudentEmail());
            tvAttendanceRate.setText(String.format("%.1f%%", studentInfo.getAttendanceRate()));

            if (studentInfo.getAverageGrade() != null) {
                tvGrade.setText(String.format("%.1f", studentInfo.getAverageGrade()));
            } else {
                tvGrade.setText("N/A");
            }

            btnViewProfile.setOnClickListener(v -> listener.onStudentClick(studentInfo));
            btnAttendance.setOnClickListener(v -> listener.onAttendanceClick(studentInfo));
            btnGrade.setOnClickListener(v -> listener.onGradeClick(studentInfo));
        }
    }
}
