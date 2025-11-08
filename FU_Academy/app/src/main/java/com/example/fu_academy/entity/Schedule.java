package com.example.fu_academy.entity;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ForeignKey;
import androidx.room.ColumnInfo;

@Entity(tableName = "Schedule",
        foreignKeys = @ForeignKey(
                entity = Class.class,
                parentColumns = "class_id",
                childColumns = "class_id",
                onDelete = ForeignKey.CASCADE
        ))
public class Schedule {
    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "class_id", index = true)
    public long class_id;

    @ColumnInfo(name = "date")
    public String date;

    @ColumnInfo(name = "time")
    public String time;

    @ColumnInfo(name = "room")
    public String room;

    @ColumnInfo(name = "type")
    public String type; // lecture, lab, exam, etc.

    @ColumnInfo(name = "status")
    public String status;

    public Schedule() {
    }

    public Schedule(long id, long class_id, String date, String time, String room, String type, String status) {
        this.id = id;
        this.class_id = class_id;
        this.date = date;
        this.time = time;
        this.room = room;
        this.type = type;
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getClass_id() {
        return class_id;
    }

    public void setClass_id(long class_id) {
        this.class_id = class_id;
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