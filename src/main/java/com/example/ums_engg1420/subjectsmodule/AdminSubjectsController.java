package com.example.ums_engg1420.subjectsmodule;

import com.example.ums_engg1420.dataclasses.Subject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import static com.example.ums_engg1420.dataparsers.SubjectDataHandler.*;

public class AdminSubjectsController extends SubjectModuleInitializer{

    @FXML private TextField txtSubjectName;
    @FXML private TextField txtSubjectCode;
    @FXML private TextField txtSearch;
    @FXML private TableView<Subject> tblSubjects;
    @FXML private TableColumn<Subject, String> colSubjectName;
    @FXML private TableColumn<Subject, String> colSubjectCode;
    @FXML private Button btnAddSubject;
    @FXML private Button btnSearch;
    @FXML private Button btnEditSubject;
    @FXML private Button btnDeleteSubject;


    @FXML
    public void initialize() {
        tableInitialize(colSubjectCode, colSubjectName);

        tblSubjects.setItems(subjects);

        if (btnSearch != null) {
            btnSearch.setOnAction(e -> searchSubjects());
        }

        btnAddSubject.setOnAction(e -> addSubject());
        btnDeleteSubject.setOnAction(e -> deleteSubject());
//        btnEditSubject.setOnAction(e -> editSubject());
    }

    private void addSubject() {
        String name = txtSubjectName.getText();
        String code = txtSubjectCode.getText();

        if (name.isEmpty() || code.isEmpty()) {
            showAlert("Error", "Subject Name and Code cannot be empty!");
            return;
        }

        Subject newSubject = new Subject(name, code);
        try {
            writeNewSubject(newSubject);
        } catch (Exception DuplicateEntryException) {
            showAlert("Error", "Subject already exists!");
        }
        refreshEntries(tblSubjects);

        txtSubjectName.clear();
        txtSubjectCode.clear();
    }

    private void deleteSubject() {
        Subject selected = tblSubjects.getSelectionModel().getSelectedItem();
        if (selected != null) {
            removeSubject(subjects.indexOf(selected) + 1);
            refreshEntries(tblSubjects);
        } else {
            showAlert("Error", "No subject selected for deletion.");
        }
    }

//    private void editSubject() {
//        Subject selected = tblSubjects.getSelectionModel().getSelectedItem();
//        if (selected != null) {
//            selected.setSubjectName(txtSubjectName.getText());
//            selected.setSubjectCode(txtSubjectCode.getText());
//        } else {
//            showAlert("Error", "No subject selected for editing.");
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
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.show();
    }
}
