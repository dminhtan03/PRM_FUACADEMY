package com.example.fu_academy.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ForeignKey;
import androidx.room.ColumnInfo;

@Entity(tableName = "Material",
        foreignKeys = @ForeignKey(
                entity = Course.class,
                parentColumns = "course_id",
                childColumns = "course_id",
                onDelete = ForeignKey.CASCADE
        ))
public class Material {
    @PrimaryKey(autoGenerate = true)
    public long material_id;

    @ColumnInfo(name = "course_id", index = true)
    public long course_id;

    @ColumnInfo(name = "title")
    public String title;

    @ColumnInfo(name = "file_url")
    public String file_url;

    @ColumnInfo(name = "upload_date")
    public String upload_date;

    public Material() {
    }

    public Material(long material_id, long course_id, String title, String file_url, String upload_date) {
        this.material_id = material_id;
        this.course_id = course_id;
        this.title = title;
        this.file_url = file_url;
        this.upload_date = upload_date;
    }

    public long getMaterial_id() {
        return material_id;
    }

    public void setMaterial_id(long material_id) {
        this.material_id = material_id;
    }

    public long getCourse_id() {
        return course_id;
    }

    public void setCourse_id(long course_id) {
        this.course_id = course_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFile_url() {
        return file_url;
    }

    public void setFile_url(String file_url) {
        this.file_url = file_url;
    }

    public String getUpload_date() {
        return upload_date;
    }

    public void setUpload_date(String upload_date) {
        this.upload_date = upload_date;
    }
}