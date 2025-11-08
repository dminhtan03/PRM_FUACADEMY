package com.example.fu_academy.dao;

import androidx.room.*;

import com.example.fu_academy.entity.Course;

import java.util.List;

@Dao
public interface CourseDao {
    @Insert
    long insert(Course course);

    @Update
    void update(Course course);

    @Delete
    void delete(Course course);

    @Query("SELECT * FROM Course")
    List<Course> getAll();

    @Query("SELECT * FROM Course WHERE course_id = :id")
    Course findById(long id);

    @Query("SELECT * FROM Course WHERE lecturer_id = :lecturerId")
    List<Course> getCoursesByLecturer(long lecturerId);
}