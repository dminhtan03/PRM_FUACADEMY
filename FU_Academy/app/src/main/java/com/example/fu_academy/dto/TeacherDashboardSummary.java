package com.example.fu_academy.dto;

public class TeacherDashboardSummary {
    private int courseCount;
    private int studentCount;
    private int pendingTasks;
    private Double averageGrade;
    private int feedbackCount;
    private Double attendanceRate;
    private String upcomingClass;

    public TeacherDashboardSummary() {
    }

    public TeacherDashboardSummary(int courseCount, int studentCount, int pendingTasks,
                                 Double averageGrade, int feedbackCount, Double attendanceRate,
                                 String upcomingClass) {
        this.courseCount = courseCount;
        this.studentCount = studentCount;
        this.pendingTasks = pendingTasks;
        this.averageGrade = averageGrade;
        this.feedbackCount = feedbackCount;
        this.attendanceRate = attendanceRate;
        this.upcomingClass = upcomingClass;
    }

    // Getters and setters
    public int getCourseCount() {
        return courseCount;
    }

    public void setCourseCount(int courseCount) {
        this.courseCount = courseCount;
    }

    public int getStudentCount() {
        return studentCount;
    }

    public void setStudentCount(int studentCount) {
        this.studentCount = studentCount;
    }

    public int getPendingTasks() {
        return pendingTasks;
    }

    public void setPendingTasks(int pendingTasks) {
        this.pendingTasks = pendingTasks;
    }

    public Double getAverageGrade() {
        return averageGrade;
    }

    public void setAverageGrade(Double averageGrade) {
        this.averageGrade = averageGrade;
    }

    public int getFeedbackCount() {
        return feedbackCount;
    }

    public void setFeedbackCount(int feedbackCount) {
        this.feedbackCount = feedbackCount;
    }

    public Double getAttendanceRate() {
        return attendanceRate;
    }

    public void setAttendanceRate(Double attendanceRate) {
        this.attendanceRate = attendanceRate;
    }

    public String getUpcomingClass() {
        return upcomingClass;
    }

    public void setUpcomingClass(String upcomingClass) {
        this.upcomingClass = upcomingClass;
    }
}
