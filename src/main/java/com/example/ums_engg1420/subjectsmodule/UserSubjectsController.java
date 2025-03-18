package com.example.ums_engg1420.subjectsmodule;

import com.example.ums_engg1420.dataclasses.Subject;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.beans.property.SimpleStringProperty;

import java.util.List;

import static com.example.ums_engg1420.dataparsers.SubjectDataHandler.readSubjects;

public class UserSubjectsController extends SubjectModuleInitializer {

    @FXML private TableView<Subject> tblSubjects;
    @FXML private TableColumn<Subject, String> colSubjectName;
    @FXML private TableColumn<Subject, String> colSubjectCode;
    @FXML private TableColumn<Subject, String> colEnroll;
    @FXML private TextField txtSearch;
    @FXML private Button btnSearch;


    @FXML
    public void initialize() {
        tableInitialize(colSubjectCode, colSubjectName);

        tblSubjects.setItems(subjects);

        if (btnSearch != null) {
            btnSearch.setOnAction(e -> searchSubjects());
        }
    }

//    private void addEnrollButtons() {
//        colEnroll.setCellFactory(param -> new TableCell<Subject, String>() {
//            private final Button enrollButton = new Button("Enroll");
//
//            {
//                enrollButton.setOnAction(e -> {
//                    Subject subject = getTableView().getItems().get(getIndex());
//                    enrollSubject(subject);
//                });
//            }
//
//            @Override
//            protected void updateItem(String item, boolean empty) {
//                super.updateItem(item, empty);
//                if (empty) {
//                    setGraphic(null);
//                } else {
//                    setGraphic(enrollButton);
//                }
//            }
//        });
//    }
//
//    private void enrollSubject(Subject subject) {
//        if (!enrolledSubjects.contains(subject)) {
//            enrolledSubjects.add(subject);
//            DataPersistence.saveEnrolledSubjects(enrolledSubjects);  // Save enrolled subjects to file
//            showAlert("Success", "You have enrolled in " + subject.getSubjectName());
//        } else {
//            showAlert("Info", "You are already enrolled in this subject!");
//        }
//    }

    private void searchSubjects() {
        String keyword = txtSearch.getText().toLowerCase();
        if (keyword.isEmpty()) {
            tblSubjects.setItems(subjects);
            return;
        }

        ObservableList<Subject> filteredList = FXCollections.observableArrayList();
        for (Subject subject : subjects) {
            if (subject.getSubjectName().toLowerCase().contains(keyword) ||
                    subject.getSubjectCode().toLowerCase().contains(keyword)) {
                filteredList.add(subject);
            }
        }
        tblSubjects.setItems(filteredList);
    }

    private void showAlert(String title, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }
}
