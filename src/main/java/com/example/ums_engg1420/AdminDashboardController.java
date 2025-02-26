package com.example.ums_engg1420;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import java.util.Optional;

public class AdminDashboardController {

    @FXML
    private TextField subjectCodeField, subjectNameField;

    @FXML
    private TableView<Subject> subjectsTable;

    @FXML
    private TableColumn<Subject, String> codeColumn, nameColumn;

    private ObservableList<Subject> subjectList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        codeColumn.setCellValueFactory(new PropertyValueFactory<>("code"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        subjectsTable.setItems(subjectList);
    }

    @FXML
    private void addSubject() {
        String code = subjectCodeField.getText();
        String name = subjectNameField.getText();
        if (!code.isEmpty() && !name.isEmpty()) {
            subjectList.add(new Subject(code, name));
            subjectCodeField.clear();
            subjectNameField.clear();
        }
    }

    @FXML
    private void editSubject() {
        Subject selected = subjectsTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            TextInputDialog codeDialog = new TextInputDialog(selected.getCode());
            codeDialog.setTitle("Edit Subject Code");
            codeDialog.setHeaderText("Modify Subject Code");
            codeDialog.setContentText("New Code:");
            Optional<String> codeResult = codeDialog.showAndWait();

            TextInputDialog nameDialog = new TextInputDialog(selected.getName());
            nameDialog.setTitle("Edit Subject Name");
            nameDialog.setHeaderText("Modify Subject Name");
            nameDialog.setContentText("New Name:");
            Optional<String> nameResult = nameDialog.showAndWait();

            codeResult.ifPresent(selected::setCode);
            nameResult.ifPresent(selected::setName);

            subjectsTable.refresh();
        }
    }

    @FXML
    private void deleteSubject() {
        Subject selected = subjectsTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            subjectList.remove(selected);
        }
    }
}
