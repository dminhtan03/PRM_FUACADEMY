package com.example.fu_academy.dao;

import androidx.room.*;

import com.example.fu_academy.entity.Submission;

import java.util.List;

@Dao
public interface SubmissionDao {
    @Insert
    long insert(Submission s);

    @Update
    void update(Submission s);

    @Query("SELECT * FROM Submission WHERE assignment_id = :assignmentId")
    List<Submission> getByAssignment(long assignmentId);

    @Query("SELECT * FROM Submission WHERE student_id = :studentId")
    List<Submission> getByStudent(long studentId);

    @Query("SELECT s.* FROM Submission s " +
           "INNER JOIN Assignment a ON s.assignment_id = a.assignment_id " +
           "INNER JOIN Class c ON a.class_id = c.class_id " +
           "WHERE c.lecturer_id = :lecturerId AND s.grade IS NULL " +
           "ORDER BY s.submit_date DESC")
    List<Submission> getPendingByLecturer(long lecturerId);

    @Query("SELECT COUNT(*) FROM Submission s " +
           "INNER JOIN Assignment a ON s.assignment_id = a.assignment_id " +
           "INNER JOIN Class c ON a.class_id = c.class_id " +
           "WHERE c.lecturer_id = :lecturerId AND s.grade IS NULL")
    int countPendingByLecturer(long lecturerId);
}