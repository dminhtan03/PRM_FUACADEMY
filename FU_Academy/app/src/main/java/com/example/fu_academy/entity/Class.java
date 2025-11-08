package com.example.fu_academy.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ForeignKey;
import androidx.room.ColumnInfo;

@Entity(tableName = "Class",
        foreignKeys = {
                @ForeignKey(entity = Course.class,
                        parentColumns = "course_id",
                        childColumns = "course_id",
                        onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = User.class,
                        parentColumns = "user_id",
                        childColumns = "lecturer_id",
                        onDelete = ForeignKey.CASCADE)
        })
public class Class {
    @PrimaryKey(autoGenerate = true)
    public long class_id;

    @ColumnInfo(name = "course_id", index = true)
    public long course_id;

    @ColumnInfo(name = "lecturer_id", index = true)
    public long lecturer_id;

    @ColumnInfo(name = "room")
    public String room;

    @ColumnInfo(name = "schedule")
    public String schedule;

    @ColumnInfo(name = "semester")
    public String semester;

    public Class() {
    }

    public Class(long class_id, long course_id, long lecturer_id, String room, String schedule, String semester) {
        this.class_id = class_id;
        this.course_id = course_id;
        this.lecturer_id = lecturer_id;
        this.room = room;
        this.schedule = schedule;
        this.semester = semester;
    }

    public long getClass_id() {
        return class_id;
    }

    public void setClass_id(long class_id) {
        this.class_id = class_id;
    }

    public long getCourse_id() {
        return course_id;
    }

    public void setCourse_id(long course_id) {
        this.course_id = course_id;
    }

    public long getLecturer_id() {
        return lecturer_id;
    }

    public void setLecturer_id(long lecturer_id) {
        this.lecturer_id = lecturer_id;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }
}
