package com.example.fu_academy.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ForeignKey;
import androidx.room.ColumnInfo;

@Entity(tableName = "Feedback",
        foreignKeys = @ForeignKey(
                entity = User.class,
                parentColumns = "user_id",
                childColumns = "user_id",
                onDelete = ForeignKey.CASCADE
        ))
public class Feedback {
    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "user_id", index = true)
    public long user_id;

    @ColumnInfo(name = "content")
    public String content;

    @ColumnInfo(name = "rating")
    public int rating;

    @ColumnInfo(name = "status")
    public String status;

    @ColumnInfo(name = "response")
    public String response;

    public Feedback() {
    }

    public Feedback(long id, long user_id, String content, int rating, String status, String response) {
        this.id = id;
        this.user_id = user_id;
        this.content = content;
        this.rating = rating;
        this.status = status;
        this.response = response;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}