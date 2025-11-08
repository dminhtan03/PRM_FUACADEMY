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

    @ColumnInfo(name = "student_id", index = true)
    public long student_id;

    @ColumnInfo(name = "subject")
    public String subject;

    @ColumnInfo(name = "content")
    public String content;

    @ColumnInfo(name = "category")
    public String category; // course, lecturer, system

    @ColumnInfo(name = "date")
    public String date;

    @ColumnInfo(name = "rating")
    public int rating;

    @ColumnInfo(name = "status")
    public String status;

    @ColumnInfo(name = "response")
    public String response;

    public Feedback() {
    }

    public Feedback(long id, long user_id, long student_id, String subject, String content, 
                    String category, String date, int rating, String status, String response) {
        this.id = id;
        this.user_id = user_id;
        this.student_id = student_id;
        this.subject = subject;
        this.content = content;
        this.category = category;
        this.date = date;
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

    public long getStudent_id() {
        return student_id;
    }

    public void setStudent_id(long student_id) {
        this.student_id = student_id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}