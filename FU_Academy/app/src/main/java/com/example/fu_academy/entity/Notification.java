package com.example.fu_academy.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ForeignKey;
import androidx.room.ColumnInfo;

@Entity(tableName = "Notification",
        foreignKeys = @ForeignKey(
                entity = User.class,
                parentColumns = "user_id",
                childColumns = "sender_id",
                onDelete = ForeignKey.CASCADE
        ))
public class Notification {
    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "title")
    public String title;

    @ColumnInfo(name = "content")
    public String content;

    @ColumnInfo(name = "sender_id", index = true)
    public long sender_id;

    @ColumnInfo(name = "role_target")
    public String role_target;

    @ColumnInfo(name = "date")
    public String date;

    public Notification() {
    }

    public Notification(long id, String title, String content, long sender_id, String role_target, String date) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.sender_id = sender_id;
        this.role_target = role_target;
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getSender_id() {
        return sender_id;
    }

    public void setSender_id(long sender_id) {
        this.sender_id = sender_id;
    }

    public String getRole_target() {
        return role_target;
    }

    public void setRole_target(String role_target) {
        this.role_target = role_target;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}