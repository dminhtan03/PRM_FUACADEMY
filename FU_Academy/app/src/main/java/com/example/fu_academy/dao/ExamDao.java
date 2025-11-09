package com.example.fu_academy.dao;

import androidx.room.*;

import com.example.fu_academy.entity.Exam;

import java.util.List;

@Dao
public interface ExamDao {
    @Insert
    long insert(Exam exam);

    @Update
    void update(Exam exam);

    @Delete
    void delete(Exam exam);

    @Query("SELECT * FROM Exam WHERE course_id = :courseId ORDER BY date, time")
    List<Exam> getByCourse(long courseId);

    @Query("SELECT * FROM Exam ORDER BY date, time")
    List<Exam> getAll();

    @Query("SELECT * FROM Exam WHERE date >= :date ORDER BY date, time")
    List<Exam> getUpcoming(String date);

    @Query("SELECT * FROM Exam WHERE status = :status ORDER BY date, time")
    List<Exam> getByStatus(String status);
}



