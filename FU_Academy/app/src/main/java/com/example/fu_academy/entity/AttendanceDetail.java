package com.example.fu_academy.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ForeignKey;
import androidx.room.ColumnInfo;

@Entity(tableName = "AttendanceDetail",
        foreignKeys = {
                @ForeignKey(entity = User.class,
                        parentColumns = "user_id",
                        childColumns = "student_id",
                        onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = Schedule.class,
                        parentColumns = "id",
                        childColumns = "schedule_id",
                        onDelete = ForeignKey.CASCADE)
        })
public class AttendanceDetail {
    @PrimaryKey(autoGenerate = true)
    public long attendance_id;

    @ColumnInfo(name = "student_id", index = true)
    public long student_id;

    @ColumnInfo(name = "schedule_id", index = true)
    public long schedule_id;

    @ColumnInfo(name = "date")
    public String date;

    @ColumnInfo(name = "status")
    public String status; // present, absent, late

    @ColumnInfo(name = "remark")
    public String remark;

    @ColumnInfo(name = "duration")
    public int duration; // in minutes

    @ColumnInfo(name = "type")
    public String type; // lecture, lab, etc.

    public AttendanceDetail() {
    }

    public AttendanceDetail(long attendance_id, long student_id, long schedule_id, 
                            String date, String status, String remark, int duration, String type) {
        this.attendance_id = attendance_id;
        this.student_id = student_id;
        this.schedule_id = schedule_id;
        this.date = date;
        this.status = status;
        this.remark = remark;
        this.duration = duration;
        this.type = type;
    }

    public long getAttendance_id() {
        return attendance_id;
    }

    public void setAttendance_id(long attendance_id) {
        this.attendance_id = attendance_id;
    }

    public long getStudent_id() {
        return student_id;
    }

    public void setStudent_id(long student_id) {
        this.student_id = student_id;
    }

    public long getSchedule_id() {
        return schedule_id;
    }

    public void setSchedule_id(long schedule_id) {
        this.schedule_id = schedule_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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
}



