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

    @Query("SELECT f.* FROM Feedback f " +
           "INNER JOIN Enrollment e ON f.student_id = e.student_id " +
           "INNER JOIN Class c ON e.class_id = c.class_id " +
           "WHERE c.lecturer_id = :lecturerId " +
           "ORDER BY f.date DESC")
    List<Feedback> getByLecturer(long lecturerId);

    @Query("SELECT COUNT(*) FROM Feedback f " +
           "INNER JOIN Enrollment e ON f.student_id = e.student_id " +
           "INNER JOIN Class c ON e.class_id = c.class_id " +
           "WHERE c.lecturer_id = :lecturerId")
    int countByLecturer(long lecturerId);
}
