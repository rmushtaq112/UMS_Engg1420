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
            TextInputDialog dialog = new TextInputDialog(selected.getName());
            dialog.setTitle("Edit Subject");
            dialog.setHeaderText("Modify Subject Name");
            dialog.setContentText("New Name:");
            Optional<String> result = dialog.showAndWait();
            result.ifPresent(selected::setName);
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
