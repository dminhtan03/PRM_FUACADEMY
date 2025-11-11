package com.example.fu_academy.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fu_academy.R;
import com.example.fu_academy.dto.StudentInfo;
import com.example.fu_academy.entity.AttendanceDetail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TeacherAttendanceAdapter extends RecyclerView.Adapter<TeacherAttendanceAdapter.AttendanceViewHolder> {

    private List<StudentInfo> studentList;
    private String selectedDate;
    private Map<Long, String> attendanceStatus = new HashMap<>();

    public TeacherAttendanceAdapter(List<StudentInfo> studentList) {
        this.studentList = studentList;
    }

    @NonNull
    @Override
    public AttendanceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_attendance, parent, false);
        return new AttendanceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AttendanceViewHolder holder, int position) {
        StudentInfo studentInfo = studentList.get(position);
        holder.bind(studentInfo, attendanceStatus);
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    public void updateStudentList(List<StudentInfo> newList, String date) {
        this.studentList = newList;
        this.selectedDate = date;
        // Reset attendance status for new date
        attendanceStatus.clear();
        notifyDataSetChanged();
    }

    public List<AttendanceDetail> getAttendanceList(long classId) {
        List<AttendanceDetail> attendanceList = new ArrayList<>();

        for (StudentInfo student : studentList) {
            String status = attendanceStatus.get(student.getStudentId());
            if (status != null && !status.isEmpty()) {
                AttendanceDetail attendance = new AttendanceDetail();
                attendance.student_id = student.getStudentId();
                attendance.date = selectedDate;
                attendance.status = status;
                attendance.remark = "";
                attendance.duration = 120; // Default 2 hours
                attendance.type = "lecture";
                // schedule_id will be set in AttendanceViewModel

                attendanceList.add(attendance);
            }
        }

        return attendanceList;
    }

    static class AttendanceViewHolder extends RecyclerView.ViewHolder {
        private TextView tvStudentName, tvStudentId;
        private RadioGroup radioGroupStatus;
        private RadioButton rbPresent, rbAbsent, rbLate;

        public AttendanceViewHolder(@NonNull View itemView) {
            super(itemView);
            tvStudentName = itemView.findViewById(R.id.tv_student_name);
            tvStudentId = itemView.findViewById(R.id.tv_student_id);
            radioGroupStatus = itemView.findViewById(R.id.radio_group_status);
            rbPresent = itemView.findViewById(R.id.rb_present);
            rbAbsent = itemView.findViewById(R.id.rb_absent);
            rbLate = itemView.findViewById(R.id.rb_late);
        }

        public void bind(StudentInfo studentInfo, Map<Long, String> attendanceStatus) {
            tvStudentName.setText(studentInfo.getStudentName());
            tvStudentId.setText(studentInfo.getStudentId_text());

            // Set current status
            String currentStatus = attendanceStatus.get(studentInfo.getStudentId());
            radioGroupStatus.clearCheck();

            if ("present".equals(currentStatus)) {
                rbPresent.setChecked(true);
            } else if ("absent".equals(currentStatus)) {
                rbAbsent.setChecked(true);
            } else if ("late".equals(currentStatus)) {
                rbLate.setChecked(true);
            }

            // Set listener for status changes
            radioGroupStatus.setOnCheckedChangeListener((group, checkedId) -> {
                String status = "";
                if (checkedId == R.id.rb_present) {
                    status = "present";
                } else if (checkedId == R.id.rb_absent) {
                    status = "absent";
                } else if (checkedId == R.id.rb_late) {
                    status = "late";
                }
                attendanceStatus.put(studentInfo.getStudentId(), status);
            });
        }
    }
}
