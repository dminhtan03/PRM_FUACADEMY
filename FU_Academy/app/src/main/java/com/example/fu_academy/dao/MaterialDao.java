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

    @Query("SELECT * FROM Material WHERE material_id = :id")
    Material getById(long id);

    @Delete
    void delete(Material m);
}