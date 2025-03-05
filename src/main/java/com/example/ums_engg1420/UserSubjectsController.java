package com.example.ums_engg1420;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class UserSubjectsController {

    @FXML private TableView<Subject> tblSubjects;
    @FXML private TableColumn<Subject, String> colSubjectName;
    @FXML private TableColumn<Subject, String> colSubjectCode;
    @FXML private TableColumn<Subject, String> colEnroll;  // FIXED TYPE
    @FXML private TextField txtSearch;
    @FXML private Button btnSearch;

    private static ObservableList<Subject> subjectList = FXCollections.observableArrayList();
    private static ObservableList<Subject> enrolledSubjects = FXCollections.observableArrayList();

    @FXML
    public void initialize() {

        colSubjectName.setCellValueFactory(cellData -> cellData.getValue().subjectNameProperty());
        colSubjectCode.setCellValueFactory(cellData -> cellData.getValue().subjectCodeProperty());

        addEnrollButtons();
        tblSubjects.setItems(subjectList);

        if (btnSearch != null) {  // Prevents NullPointerException
            btnSearch.setOnAction(e -> searchSubjects());
        }
    }

    private void addEnrollButtons() {
        colEnroll.setCellFactory(param -> new TableCell<>() {
            private final Button enrollButton = new Button("Enroll");

            {
                enrollButton.setOnAction(e -> {
                    Subject subject = getTableView().getItems().get(getIndex());
                    enrollSubject(subject);
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {  // FIXED TYPE
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(enrollButton);
                }
            }
        });
    }

    private void enrollSubject(Subject subject) {
        if (!enrolledSubjects.contains(subject)) {
            enrolledSubjects.add(subject);
            showAlert("Success", "You have enrolled in " + subject.getSubjectName());
        } else {
            showAlert("Info", "You are already enrolled in this subject!");
        }
    }

    private void searchSubjects() {
        String keyword = txtSearch.getText().toLowerCase();
        if (keyword.isEmpty()) {
            tblSubjects.setItems(subjectList);
            return;
        }

        ObservableList<Subject> filteredList = FXCollections.observableArrayList();
        for (Subject subject : subjectList) {
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


