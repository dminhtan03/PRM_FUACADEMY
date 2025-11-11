package com.example.fu_academy.dao;

import androidx.room.*;

import com.example.fu_academy.entity.Assignment;

import java.util.List;

@Dao
public interface AssignmentDao {
    @Insert
    long insert(Assignment a);

    @Query("SELECT * FROM Assignment WHERE course_id = :courseId")
    List<Assignment> getByCourse(long courseId);

    @Query("SELECT * FROM Assignment WHERE assignment_id = :id")
    Assignment findById(long id);

    @Update
    void update(Assignment assignment);

    @Query("SELECT * FROM Assignment WHERE class_id = :classId ORDER BY due_date DESC")
    List<Assignment> getByClass(long classId);
}