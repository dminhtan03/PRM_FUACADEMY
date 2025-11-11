package com.example.fu_academy.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fu_academy.R;
import com.example.fu_academy.dto.ClassInfo;

import java.util.List;

public class ClassListAdapter extends RecyclerView.Adapter<ClassListAdapter.ClassViewHolder> {

    private List<ClassInfo> classList;
    private OnClassClickListener listener;

    public interface OnClassClickListener {
        void onViewStudentsClick(ClassInfo classInfo);
        void onAttendanceClick(ClassInfo classInfo);
        void onGradeClick(ClassInfo classInfo);
        void onMaterialClick(ClassInfo classInfo);
    }

    public ClassListAdapter(List<ClassInfo> classList, OnClassClickListener listener) {
        this.classList = classList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ClassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_class, parent, false);
        return new ClassViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassViewHolder holder, int position) {
        ClassInfo classInfo = classList.get(position);
        holder.bind(classInfo, listener);
    }

    @Override
    public int getItemCount() {
        return classList.size();
    }

    public void updateList(List<ClassInfo> newList) {
        this.classList = newList;
        notifyDataSetChanged();
    }

    static class ClassViewHolder extends RecyclerView.ViewHolder {
        private TextView tvClassCode, tvCourseName, tvRoom, tvSchedule, tvSemester, tvStudentCount;
        private Button btnViewStudents, btnAttendance, btnGrades;

        public ClassViewHolder(@NonNull View itemView) {
            super(itemView);
            tvClassCode = itemView.findViewById(R.id.tv_class_code);
            tvCourseName = itemView.findViewById(R.id.tv_course_name);
            tvRoom = itemView.findViewById(R.id.tv_room);
            tvSchedule = itemView.findViewById(R.id.tv_schedule);
            tvSemester = itemView.findViewById(R.id.tv_semester);
            tvStudentCount = itemView.findViewById(R.id.tv_student_count);
            btnViewStudents = itemView.findViewById(R.id.btn_view_students);
            btnAttendance = itemView.findViewById(R.id.btn_attendance);
            btnGrades = itemView.findViewById(R.id.btn_grades);
        }

        public void bind(ClassInfo classInfo, OnClassClickListener listener) {
            tvClassCode.setText(classInfo.getClassCode());
            tvCourseName.setText(classInfo.getCourseName());
            tvRoom.setText(classInfo.getRoom());
            tvSchedule.setText(classInfo.getSchedule());
            tvSemester.setText(classInfo.getSemester());
            tvStudentCount.setText(String.valueOf(classInfo.getStudentCount()));

            btnViewStudents.setOnClickListener(v -> listener.onViewStudentsClick(classInfo));
            btnAttendance.setOnClickListener(v -> listener.onAttendanceClick(classInfo));
            btnGrades.setOnClickListener(v -> listener.onGradeClick(classInfo));
        }
    }
}
