package com.example.ums_engg1420;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Subject {
    private final StringProperty subjectName;
    private final StringProperty subjectCode;

    public Subject(String name, String code) {
        this.subjectName = new SimpleStringProperty(name);
        this.subjectCode = new SimpleStringProperty(code);
    }

    public String getSubjectName() {
        return subjectName.get();
    }

    public void setSubjectName(String name) {
        this.subjectName.set(name);
    }

    public StringProperty subjectNameProperty() {
        return subjectName;
    }

    public String getSubjectCode() {
        return subjectCode.get();
    }

    public void setSubjectCode(String code) {
        this.subjectCode.set(code);
    }

    public StringProperty subjectCodeProperty() {
        return subjectCode;
    }
}
