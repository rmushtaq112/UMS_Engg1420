package com.example.ums_engg1420.dataclasses;

public class FacultySubject {
    private String subjectCode;
    private String subjectName;
    private String facultyName;

    public FacultySubject(String subjectCode, String subjectName, String facultyName) {
        this.subjectCode = subjectCode;
        this.subjectName = subjectName;
        this.facultyName = facultyName;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public String getFacultyName() {
        return facultyName;
    }
}
