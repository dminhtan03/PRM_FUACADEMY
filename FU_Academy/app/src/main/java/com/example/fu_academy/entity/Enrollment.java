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
}