package com.example.ums_engg1420.subjectsmodule;

import com.example.ums_engg1420.dataclasses.Subject;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.List;

import static com.example.ums_engg1420.dataparsers.SubjectDataHandler.readSubjects;

public class SubjectModuleInitializer {

    protected List<Subject> subjectData = readSubjects();
    protected ObservableList<Subject> subjects = FXCollections.observableArrayList(subjectData);

    public void tableInitialize(TableColumn<Subject, String> codeCol, TableColumn<Subject, String> nameCol) {
        codeCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSubjectCode()));
        nameCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSubjectName()));
    }

    public void refreshEntries(TableView<Subject> table){
        subjectData = readSubjects();
        subjects = FXCollections.observableArrayList(subjectData);
        table.setItems(subjects);
    }
}
