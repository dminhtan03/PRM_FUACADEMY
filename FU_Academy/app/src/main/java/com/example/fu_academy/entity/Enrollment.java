package com.example.fu_academy.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.ColumnInfo;

@Entity(tableName = "Enrollment",
        primaryKeys = {"student_id", "class_id"},
        foreignKeys = {
                @ForeignKey(entity = User.class,
                        parentColumns = "user_id",
                        childColumns = "student_id",
                        onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = Class.class,
                        parentColumns = "class_id",
                        childColumns = "class_id",
                        onDelete = ForeignKey.CASCADE)
        })
public class Enrollment {
    @ColumnInfo(name = "student_id", index = true)
    public long student_id;

    @ColumnInfo(name = "class_id", index = true)
    public long class_id;

    @ColumnInfo(name = "grade")
    public Double grade;

    @ColumnInfo(name = "attendance")
    public int attendance;

    @ColumnInfo(name = "attendance_score")
    public Double attendance_score;

    @ColumnInfo(name = "assignment_score")
    public Double assignment_score;

    @ColumnInfo(name = "midterm_score")
    public Double midterm_score;

    @ColumnInfo(name = "final_score")
    public Double final_score;

    @ColumnInfo(name = "average_score")
    public Double average_score;

    @ColumnInfo(name = "status")
    public String status; // "Pass" or "Fail"

    @ColumnInfo(name = "remark")
    public String remark;

    public Enrollment() {
    }

    public Enrollment(long student_id, long class_id, Double grade, int attendance) {
        this.student_id = student_id;
        this.class_id = class_id;
        this.grade = grade;
        this.attendance = attendance;
    }

    public long getStudent_id() {
        return student_id;
    }

    public void setStudent_id(long student_id) {
        this.student_id = student_id;
    }

    public long getClass_id() {
        return class_id;
    }

    public void setClass_id(long class_id) {
        this.class_id = class_id;
    }

    public Double getGrade() {
        return grade;
    }

    public void setGrade(Double grade) {
        this.grade = grade;
    }

    public int getAttendance() {
        return attendance;
    }

    public void setAttendance(int attendance) {
        this.attendance = attendance;
    }

    public Double getAttendance_score() {
        return attendance_score;
    }

    public void setAttendance_score(Double attendance_score) {
        this.attendance_score = attendance_score;
    }

    public Double getAssignment_score() {
        return assignment_score;
    }

    public void setAssignment_score(Double assignment_score) {
        this.assignment_score = assignment_score;
    }

    public Double getMidterm_score() {
        return midterm_score;
    }

    public void setMidterm_score(Double midterm_score) {
        this.midterm_score = midterm_score;
    }

    public Double getFinal_score() {
        return final_score;
    }

    public void setFinal_score(Double final_score) {
        this.final_score = final_score;
    }

    public Double getAverage_score() {
        return average_score;
    }

    public void setAverage_score(Double average_score) {
        this.average_score = average_score;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}