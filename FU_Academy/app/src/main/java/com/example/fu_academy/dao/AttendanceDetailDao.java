package com.example.fu_academy.dao;

import androidx.room.*;

import com.example.fu_academy.entity.AttendanceDetail;

import java.util.List;

@Dao
public interface AttendanceDetailDao {
    @Insert
    long insert(AttendanceDetail attendance);

    @Update
    void update(AttendanceDetail attendance);

    @Delete
    void delete(AttendanceDetail attendance);

    @Query("SELECT * FROM AttendanceDetail WHERE student_id = :studentId ORDER BY date DESC")
    List<AttendanceDetail> getByStudent(long studentId);

    @Query("SELECT * FROM AttendanceDetail WHERE student_id = :studentId AND date = :date")
    List<AttendanceDetail> getByStudentAndDate(long studentId, String date);

    @Query("SELECT * FROM AttendanceDetail WHERE schedule_id = :scheduleId")
    List<AttendanceDetail> getBySchedule(long scheduleId);

    @Query("SELECT COUNT(*) FROM AttendanceDetail WHERE student_id = :studentId AND status = 'present'")
    int getPresentCount(long studentId);

    @Query("SELECT COUNT(*) FROM AttendanceDetail WHERE student_id = :studentId")
    int getTotalCount(long studentId);

    @Query("SELECT AVG(CASE WHEN a.status = 'present' THEN 1.0 ELSE 0.0 END) * 100 " +
           "FROM AttendanceDetail a " +
           "INNER JOIN Schedule s ON a.schedule_id = s.id " +
           "INNER JOIN Class c ON s.class_id = c.class_id " +
           "WHERE c.lecturer_id = :lecturerId")
    Double getAttendanceRateByLecturer(long lecturerId);

    @Query("SELECT a.*, u.name as student_name " +
           "FROM AttendanceDetail a " +
           "INNER JOIN User u ON a.student_id = u.user_id " +
           "INNER JOIN Schedule s ON a.schedule_id = s.id " +
           "WHERE s.class_id = :classId AND a.date = :date " +
           "ORDER BY u.name")
    List<AttendanceDetail> getByClassAndDate(long classId, String date);
}




