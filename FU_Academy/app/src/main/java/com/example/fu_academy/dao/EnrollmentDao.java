package com.example.fu_academy.dao;

import androidx.room.*;

import com.example.fu_academy.entity.Enrollment;

import java.util.List;

@Dao
public interface EnrollmentDao {
    @Insert
    void insert(Enrollment e);

    @Update
    void update(Enrollment e);

    @Delete
    void delete(Enrollment e);

    @Query("SELECT * FROM Enrollment WHERE student_id = :studentId")
    List<Enrollment> getByStudent(long studentId);

    @Query("SELECT * FROM Enrollment WHERE class_id = :classId")
    List<Enrollment> getByClass(long classId);
}