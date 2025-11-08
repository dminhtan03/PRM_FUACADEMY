package com.example.fu_academy.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ForeignKey;
import androidx.room.ColumnInfo;

@Entity(tableName = "Course",
        foreignKeys = @ForeignKey(
                entity = User.class,
                parentColumns = "user_id",
                childColumns = "lecturer_id",
                onDelete = ForeignKey.CASCADE
        ))
public class Course {
    @PrimaryKey(autoGenerate = true)
    public long course_id;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "lecturer_id", index = true)
    public long lecturer_id;

    @ColumnInfo(name = "credit")
    public int credit;

    @ColumnInfo(name = "semester")
    public String semester;

    @ColumnInfo(name = "status")
    public String status;

    public Course() {
    }

    public Course(long course_id, String name, long lecturer_id, int credit, String semester, String status) {
        this.course_id = course_id;
        this.name = name;
        this.lecturer_id = lecturer_id;
        this.credit = credit;
        this.semester = semester;
        this.status = status;
    }

    public long getCourse_id() {
        return course_id;
    }

    public void setCourse_id(long course_id) {
        this.course_id = course_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getLecturer_id() {
        return lecturer_id;
    }

    public void setLecturer_id(long lecturer_id) {
        this.lecturer_id = lecturer_id;
    }

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}