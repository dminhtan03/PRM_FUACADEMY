package com.example.fu_academy.dto;

public class StudentInfo {
    private long studentId;
    private String studentName;
    private String studentEmail;
    private String studentId_text;
    private double attendanceRate;
    private Double averageGrade;
    private String remark;

    public StudentInfo() {
    }

    public StudentInfo(long studentId, String studentName, String studentEmail,
                      String studentId_text, double attendanceRate, Double averageGrade, String remark) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.studentEmail = studentEmail;
        this.studentId_text = studentId_text;
        this.attendanceRate = attendanceRate;
        this.averageGrade = averageGrade;
        this.remark = remark;
    }

    // Getters and setters
    public long getStudentId() {
        return studentId;
    }

    public void setStudentId(long studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentEmail() {
        return studentEmail;
    }

    public void setStudentEmail(String studentEmail) {
        this.studentEmail = studentEmail;
    }

    public String getStudentId_text() {
        return studentId_text;
    }

    public void setStudentId_text(String studentId_text) {
        this.studentId_text = studentId_text;
    }

    public double getAttendanceRate() {
        return attendanceRate;
    }

    public void setAttendanceRate(double attendanceRate) {
        this.attendanceRate = attendanceRate;
    }

    public Double getAverageGrade() {
        return averageGrade;
    }

    public void setAverageGrade(Double averageGrade) {
        this.averageGrade = averageGrade;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
