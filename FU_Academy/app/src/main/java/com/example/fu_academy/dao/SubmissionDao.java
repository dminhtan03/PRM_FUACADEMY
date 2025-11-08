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
}