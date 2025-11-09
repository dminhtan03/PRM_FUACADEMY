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
}