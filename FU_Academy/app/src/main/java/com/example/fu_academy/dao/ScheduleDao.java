package com.example.fu_academy.dao;

import androidx.room.*;

import com.example.fu_academy.entity.Schedule;

import java.util.List;

@Dao
public interface ScheduleDao {
    @Insert
    long insert(Schedule s);

    @Update
    void update(Schedule s);

    @Delete
    void delete(Schedule s);

    @Query("DELETE FROM Schedule")
    void deleteAll();

    @Query("SELECT * FROM Schedule WHERE class_id = :classId")
    List<Schedule> getByClass(long classId);

    @Query("SELECT * FROM Schedule")
    List<Schedule> getAll();

    @Query("SELECT s.*, c.room as class_room, co.name as course_name " +
           "FROM Schedule s " +
           "INNER JOIN Class c ON s.class_id = c.class_id " +
           "INNER JOIN Course co ON c.course_id = co.course_id " +
           "WHERE c.lecturer_id = :lecturerId AND s.date >= date('now') " +
           "ORDER BY s.date, s.time " +
           "LIMIT 5")
    List<Schedule> getUpcomingByLecturer(long lecturerId);
}