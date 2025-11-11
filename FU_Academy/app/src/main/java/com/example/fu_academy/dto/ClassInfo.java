package com.example.fu_academy.dto;

public class ClassInfo {
    private long classId;
    private String classCode;
    private String courseName;
    private String courseCode;
    private String room;
    private String schedule;
    private String semester;
    private int studentCount;
    private String status;

    public ClassInfo() {
    }

    public ClassInfo(long classId, String classCode, String courseName, String courseCode,
                    String room, String schedule, String semester, int studentCount, String status) {
        this.classId = classId;
        this.classCode = classCode;
        this.courseName = courseName;
        this.courseCode = courseCode;
        this.room = room;
        this.schedule = schedule;
        this.semester = semester;
        this.studentCount = studentCount;
        this.status = status;
    }

    // Getters and setters
    public long getClassId() {
        return classId;
    }

    public void setClassId(long classId) {
        this.classId = classId;
    }

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
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

    public int getStudentCount() {
        return studentCount;
    }

    public void setStudentCount(int studentCount) {
        this.studentCount = studentCount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
