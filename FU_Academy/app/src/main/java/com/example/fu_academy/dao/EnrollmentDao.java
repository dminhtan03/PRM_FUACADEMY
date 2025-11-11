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

    @Query("SELECT COUNT(DISTINCT e.student_id) FROM Enrollment e " +
           "INNER JOIN Class c ON e.class_id = c.class_id " +
           "WHERE c.lecturer_id = :lecturerId")
    int countByLecturer(long lecturerId);

    @Query("SELECT AVG(e.average_score) FROM Enrollment e " +
           "INNER JOIN Class c ON e.class_id = c.class_id " +
           "WHERE c.lecturer_id = :lecturerId AND e.average_score IS NOT NULL")
    Double getAverageGradeByLecturer(long lecturerId);

    @Query("SELECT e.*, u.name as student_name, u.email as student_email " +
           "FROM Enrollment e " +
           "INNER JOIN User u ON e.student_id = u.user_id " +
           "WHERE e.class_id = :classId " +
           "ORDER BY u.name")
    List<Enrollment> getStudentsByClass(long classId);
}