package com.example.fu_academy.dao;

import androidx.room.*;

import com.example.fu_academy.entity.Class;

import java.util.List;

@Dao
public interface ClassDao {
    @Insert
    long insert(Class c);

    @Update
    void update(Class c);

    @Delete
    void delete(Class c);

    @Query("SELECT * FROM Class")
    List<Class> getAll();

    @Query("SELECT * FROM Class WHERE class_id = :id")
    Class findById(long id);

    @Query("SELECT * FROM Class WHERE course_id = :courseId")
    List<Class> getByCourse(long courseId);

    @Query("SELECT * FROM Class WHERE lecturer_id = :lecturerId")
    List<Class> getByLecturer(long lecturerId);

    @Query("SELECT c.*, co.name as course_name, co.course_code " +
           "FROM Class c " +
           "INNER JOIN Course co ON c.course_id = co.course_id " +
           "WHERE c.lecturer_id = :lecturerId " +
           "ORDER BY c.semester DESC")
    List<Class> getClassesWithCourseByLecturer(long lecturerId);
}