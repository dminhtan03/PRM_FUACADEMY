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
}



