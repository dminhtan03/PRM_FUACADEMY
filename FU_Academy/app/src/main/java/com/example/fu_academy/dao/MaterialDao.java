package com.example.fu_academy.dao;

import androidx.room.*;

import com.example.fu_academy.entity.Material;

import java.util.List;

@Dao
public interface MaterialDao {
    @Insert
    long insert(Material m);

    @Query("SELECT * FROM Material WHERE course_id = :courseId")
    List<Material> getByCourse(long courseId);

    @Delete
    void delete(Material m);
}