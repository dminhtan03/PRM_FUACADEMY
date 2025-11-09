package com.example.fu_academy.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ForeignKey;
import androidx.room.ColumnInfo;

@Entity(tableName = "Exam",
        foreignKeys = @ForeignKey(
                entity = Course.class,
                parentColumns = "course_id",
                childColumns = "course_id",
                onDelete = ForeignKey.CASCADE
        ))
public class Exam {
    @PrimaryKey(autoGenerate = true)
    public long exam_id;

    @ColumnInfo(name = "course_id", index = true)
    public long course_id;

    @ColumnInfo(name = "date")
    public String date;

    @ColumnInfo(name = "time")
    public String time;

    @ColumnInfo(name = "room")
    public String room;

    @ColumnInfo(name = "seat")
    public String seat;

    @ColumnInfo(name = "duration")
    public int duration; // in minutes

    @ColumnInfo(name = "type")
    public String type; // midterm, final, quiz, etc.

    @ColumnInfo(name = "status")
    public String status; // scheduled, cancelled, completed

    public Exam() {
    }

    public Exam(long exam_id, long course_id, String date, String time, String room, 
                String seat, int duration, String type, String status) {
        this.exam_id = exam_id;
        this.course_id = course_id;
        this.date = date;
        this.time = time;
        this.room = room;
        this.seat = seat;
        this.duration = duration;
        this.type = type;
        this.status = status;
    }

    public long getExam_id() {
        return exam_id;
    }

    public void setExam_id(long exam_id) {
        this.exam_id = exam_id;
    }

    public long getCourse_id() {
        return course_id;
    }

    public void setCourse_id(long course_id) {
        this.course_id = course_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getSeat() {
        return seat;
    }

    public void setSeat(String seat) {
        this.seat = seat;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}




