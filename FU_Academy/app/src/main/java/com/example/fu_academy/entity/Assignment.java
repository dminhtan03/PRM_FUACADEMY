package com.example.fu_academy.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ForeignKey;
import androidx.room.ColumnInfo;

@Entity(tableName = "Assignment",
        foreignKeys = @ForeignKey(
                entity = Course.class,
                parentColumns = "course_id",
                childColumns = "course_id",
                onDelete = ForeignKey.CASCADE
        ))
public class Assignment {
    @PrimaryKey(autoGenerate = true)
    public long assignment_id;

    @ColumnInfo(name = "course_id", index = true)
    public long course_id;

    @ColumnInfo(name = "title")
    public String title;

    @ColumnInfo(name = "deadline")
    public String deadline;

    @ColumnInfo(name = "description")
    public String description;

    public Assignment() {
    }

    public Assignment(long assignment_id, long course_id, String title, String deadline, String description) {
        this.assignment_id = assignment_id;
        this.course_id = course_id;
        this.title = title;
        this.deadline = deadline;
        this.description = description;
    }

    public long getAssignment_id() {
        return assignment_id;
    }

    public void setAssignment_id(long assignment_id) {
        this.assignment_id = assignment_id;
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

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}