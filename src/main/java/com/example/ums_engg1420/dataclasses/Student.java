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
    private final StringProperty password;
    private final StringProperty subjectsRegistered;

    // Full constructor to initialize all fields
    public Student(String studentId, String name, String email, String address, String telephone, String tuitionStatus,
                   int currentSemester, String academicLevel, String thesisTitle, double progress,
                   String password, String profilePhoto, String subjectsRegistered) {
        this.studentId = new SimpleStringProperty(studentId);
        this.name = new SimpleStringProperty(name);
        this.email = new SimpleStringProperty(email);
        this.profilePhoto = new SimpleStringProperty(profilePhoto);
        this.address = new SimpleStringProperty(address);
        this.telephone = new SimpleStringProperty(telephone);
        this.tuitionStatus = new SimpleStringProperty(tuitionStatus);
        this.currentSemester = new SimpleIntegerProperty(currentSemester);
        this.academicLevel = new SimpleStringProperty(academicLevel);
        this.thesisTitle = new SimpleStringProperty(thesisTitle);
        this.progress = new SimpleDoubleProperty(progress);
        this.password = new SimpleStringProperty(password);
        this.subjectsRegistered = new SimpleStringProperty(subjectsRegistered);
    }

    // Updated constructor with handling for null values
    public Student(String placeholder, Object name) {
        // Initialize all properties
        this.studentId = new SimpleStringProperty(placeholder);

        // Handle name being null
        if (name == null) {
            this.name = new SimpleStringProperty("Unknown");  // Default value for name
        } else {
            this.name = new SimpleStringProperty(name.toString());  // Convert to String if name is not null
        }

        // Initialize other properties with default values or empty strings
        this.email = new SimpleStringProperty("");
        this.profilePhoto = new SimpleStringProperty("");
        this.address = new SimpleStringProperty("");
        this.telephone = new SimpleStringProperty("");
        this.tuitionStatus = new SimpleStringProperty("");
        this.currentSemester = new SimpleIntegerProperty(0); // Default value of 0 for semester
        this.academicLevel = new SimpleStringProperty("");
        this.thesisTitle = new SimpleStringProperty(""); // Default value
        this.progress = new SimpleDoubleProperty(0.0); // Default progress to 0.0
        this.password = new SimpleStringProperty("");
        this.subjectsRegistered = new SimpleStringProperty("");
    }

    // Getter and setter methods for all fields

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

    public String getPassword() {
        return password.get();
    }

    public void setPassword(String password) {
        this.password.set(password);
    }

    public StringProperty passwordProperty() {
        return password;
    }

    public String getSubjectsRegistered() {
        return subjectsRegistered.get();
    }

    public void setSubjectsRegistered(String subjectsRegistered) {
        this.subjectsRegistered.set(subjectsRegistered);
    }

    public StringProperty subjectsRegisteredProperty() {
        return subjectsRegistered;
    }
}



