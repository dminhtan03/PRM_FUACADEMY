package com.example.fu_academy.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ForeignKey;
import androidx.room.ColumnInfo;

@Entity(tableName = "Submission",
        foreignKeys = {
                @ForeignKey(entity = Assignment.class,
                        parentColumns = "assignment_id",
                        childColumns = "assignment_id",
                        onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = User.class,
                        parentColumns = "user_id",
                        childColumns = "student_id",
                        onDelete = ForeignKey.CASCADE)
        })
public class Submission {
    @PrimaryKey(autoGenerate = true)
    public long submission_id;

    @ColumnInfo(name = "assignment_id", index = true)
    public long assignment_id;

    @ColumnInfo(name = "student_id", index = true)
    public long student_id;

    @ColumnInfo(name = "file_url")
    public String file_url;

    @ColumnInfo(name = "grade")
    public Double grade;

    @ColumnInfo(name = "submit_date")
    public String submit_date;

    @ColumnInfo(name = "file_name")
    public String file_name;

    @ColumnInfo(name = "file_size")
    public String file_size;

    @ColumnInfo(name = "status")
    public String status; // "On-time" or "Late"

    @ColumnInfo(name = "feedback")
    public String feedback;

    public Submission() {
    }

    public Submission(long submission_id, long assignment_id, long student_id, String file_url, Double grade) {
        this.submission_id = submission_id;
        this.assignment_id = assignment_id;
        this.student_id = student_id;
        this.file_url = file_url;
        this.grade = grade;
    }

    public long getSubmission_id() {
        return submission_id;
    }

    public void setSubmission_id(long submission_id) {
        this.submission_id = submission_id;
    }

    public long getAssignment_id() {
        return assignment_id;
    }

    public void setAssignment_id(long assignment_id) {
        this.assignment_id = assignment_id;
    }

    public long getStudent_id() {
        return student_id;
    }

    public void setStudent_id(long student_id) {
        this.student_id = student_id;
    }

    public String getFile_url() {
        return file_url;
    }

    public void setFile_url(String file_url) {
        this.file_url = file_url;
    }

    public Double getGrade() {
        return grade;
    }

    public void setGrade(Double grade) {
        this.grade = grade;
    }

    public String getSubmit_date() {
        return submit_date;
    }

    public void setSubmit_date(String submit_date) {
        this.submit_date = submit_date;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public String getFile_size() {
        return file_size;
    }

    public void setFile_size(String file_size) {
        this.file_size = file_size;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
}
