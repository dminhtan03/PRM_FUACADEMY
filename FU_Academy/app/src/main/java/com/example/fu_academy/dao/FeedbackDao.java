package com.example.fu_academy.dao;

import androidx.room.*;

import com.example.fu_academy.entity.Feedback;

import java.util.List;

@Dao
public interface FeedbackDao {
    @Insert
    long insert(Feedback f);

    @Query("SELECT * FROM Feedback")
    List<Feedback> getAll();

    @Query("SELECT * FROM Feedback WHERE user_id = :userId")
    List<Feedback> getByUser(long userId);

    @Query("SELECT * FROM Feedback WHERE student_id = :studentId ORDER BY date DESC")
    List<Feedback> getByStudent(long studentId);
}
