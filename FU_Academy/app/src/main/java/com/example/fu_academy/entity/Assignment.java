package com.example.fu_academy.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ForeignKey;
import androidx.room.ColumnInfo;

@Entity(tableName = "Assignment",
        foreignKeys = {
                @ForeignKey(entity = Course.class,
                        parentColumns = "course_id",
                        childColumns = "course_id",
                        onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = Class.class,
                        parentColumns = "class_id",
                        childColumns = "class_id",
                        onDelete = ForeignKey.CASCADE)
        })
public class Assignment {
    @PrimaryKey(autoGenerate = true)
    public long assignment_id;

    @ColumnInfo(name = "course_id", index = true)
    public long course_id;

    @ColumnInfo(name = "class_id", index = true)
    public long class_id;

    @ColumnInfo(name = "title")
    public String title;

    @ColumnInfo(name = "deadline")
    public String deadline;

    @ColumnInfo(name = "due_date")
    public String due_date;

    @ColumnInfo(name = "description")
    public String description;

    @ColumnInfo(name = "status")
    public String status; // "Chưa nộp", "Đã nộp", "Quá hạn"

    @ColumnInfo(name = "grade")
    public Double grade;

    @ColumnInfo(name = "type")
    public String type; // "Bài tập", "Dự án", etc.

    @ColumnInfo(name = "file_required")
    public boolean file_required;

    @ColumnInfo(name = "max_score")
    public Double max_score;

    @ColumnInfo(name = "feedback")
    public String feedback;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getGrade() {
        return grade;
    }

    public void setGrade(Double grade) {
        this.grade = grade;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isFile_required() {
        return file_required;
    }

    public void setFile_required(boolean file_required) {
        this.file_required = file_required;
    }

    public Double getMax_score() {
        return max_score;
    }

    public void setMax_score(Double max_score) {
        this.max_score = max_score;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
}