package com.example.ums_engg1420.dataclasses;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.DoubleProperty;

public class Student {
    private final StringProperty studentId;
    private final StringProperty name;
    private final StringProperty email;
    private final StringProperty profilePhoto;
    private final StringProperty address;
    private final StringProperty telephone;
    private final StringProperty tuitionStatus;
    private final IntegerProperty currentSemester;
    private final StringProperty academicLevel;
    private final StringProperty thesisTitle;
    private final DoubleProperty progress;

    public Student(String studentId, String name) {
        this.studentId = new SimpleStringProperty(studentId);
        this.name = new SimpleStringProperty(name);
        this.email = new SimpleStringProperty("");
        this.profilePhoto = new SimpleStringProperty("");
        this.address = new SimpleStringProperty("");
        this.telephone = new SimpleStringProperty("");
        this.tuitionStatus = new SimpleStringProperty("");
        this.currentSemester = new SimpleIntegerProperty(0);
        this.academicLevel = new SimpleStringProperty("");
        this.thesisTitle = new SimpleStringProperty("");
        this.progress = new SimpleDoubleProperty(0.0);
    }

    public String getStudentId() {
        return studentId.get();
    }

    public void setStudentId(String studentId) {
        this.studentId.set(studentId);
    }

    public StringProperty studentIdProperty() {
        return studentId;
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public StringProperty nameProperty() {
        return name;
    }

    public String getEmail() {
        return email.get();
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public StringProperty emailProperty() {
        return email;
    }

    public String getProfilePhoto() {
        return profilePhoto.get();
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto.set(profilePhoto);
    }

    public StringProperty profilePhotoProperty() {
        return profilePhoto;
    }

    public String getAddress() {
        return address.get();
    }

    public void setAddress(String address) {
        this.address.set(address);
    }

    public StringProperty addressProperty() {
        return address;
    }

    public String getTelephone() {
        return telephone.get();
    }

    public void setTelephone(String telephone) {
        this.telephone.set(telephone);
    }

    public StringProperty telephoneProperty() {
        return telephone;
    }

    public String getTuitionStatus() {
        return tuitionStatus.get();
    }

    public void setTuitionStatus(String tuitionStatus) {
        this.tuitionStatus.set(tuitionStatus);
    }

    public StringProperty tuitionStatusProperty() {
        return tuitionStatus;
    }

    public int getCurrentSemester() {
        return currentSemester.get();
    }

    public void setCurrentSemester(int currentSemester) {
        this.currentSemester.set(currentSemester);
    }

    public IntegerProperty currentSemesterProperty() {
        return currentSemester;
    }

    public String getAcademicLevel() {
        return academicLevel.get();
    }

    public void setAcademicLevel(String academicLevel) {
        this.academicLevel.set(academicLevel);
    }

    public StringProperty academicLevelProperty() {
        return academicLevel;
    }

    public String getThesisTitle() {
        return thesisTitle.get();
    }

    public void setThesisTitle(String thesisTitle) {
        this.thesisTitle.set(thesisTitle);
    }

    public StringProperty thesisTitleProperty() {
        return thesisTitle;
    }

    public double getProgress() {
        return progress.get();
    }

    public void setProgress(double progress) {
        this.progress.set(progress);
    }

    public DoubleProperty progressProperty() {
        return progress;
    }
}
