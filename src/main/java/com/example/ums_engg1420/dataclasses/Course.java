package com.example.ums_engg1420.dataclasses;

public class Course {
    private String courseName;
    private int courseCode;
    private String subjectCode;
    private String sectionNumber;
    private int capacity;
    private String lectureTime;
    private String finalExamDate;
    private String location;
    private String teacherName;

    // Constructor
    public Course(String courseName, int courseCode, String subjectCode, String sectionNumber,
                  int capacity, String lectureTime, String finalExamDate, String location, String teacherName) {
        this.courseName = courseName;
        this.courseCode = courseCode;
        this.subjectCode = subjectCode;
        this.sectionNumber = sectionNumber;
        this.capacity = capacity;
        this.lectureTime = lectureTime;
        this.finalExamDate = finalExamDate;
        this.location = location;
        this.teacherName = teacherName;
    }

    // Getters and setters
    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public int getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(int courseCode) {
        this.courseCode = courseCode;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public String getSectionNumber() {
        return sectionNumber;
    }

    public void setSectionNumber(String sectionNumber) {
        this.sectionNumber = sectionNumber;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getLectureTime() {
        return lectureTime;
    }

    public void setLectureTime(String lectureTime) {
        this.lectureTime = lectureTime;
    }

    public String getFinalExamDate() {
        return finalExamDate;
    }

    public void setFinalExamDate(String finalExamDate) {
        this.finalExamDate = finalExamDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }
}