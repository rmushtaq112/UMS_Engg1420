package com.example.ums_engg1420.coursesmodule;

import com.example.ums_engg1420.dataclasses.Course;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.apache.commons.compress.compressors.zstandard.ZstdCompressorOutputStream;

import java.util.List;

import static com.example.ums_engg1420.dataparsers.CourseDataHandler.readCourses;

public class CourseModuleInitializer {

    protected List<Course> courseData = readCourses();
    protected ObservableList<Course> courses = FXCollections.observableArrayList(courseData);

    public void tableInitialize(TableColumn<Course, String> colCourseName,
                                TableColumn<Course, String> colCourseCode,
                                TableColumn<Course, String> colSubjectCode,
                                TableColumn<Course, String> colSectionNumber,
                                TableColumn<Course, String> colCapacity,
                                TableColumn<Course, String> colLectureTime,
                                TableColumn<Course, String> colFinalExamDate,
                                TableColumn<Course, String> colLocation,
                                TableColumn<Course, String>colTeacherName) {
        colCourseName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCourseName()));
        colCourseCode.setCellValueFactory(cellData -> new SimpleStringProperty("" + cellData.getValue().getCourseCode()));
        colSubjectCode.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSubjectCode()));
        colSectionNumber.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSectionNumber()));
        colCapacity.setCellValueFactory(cellData -> new SimpleStringProperty("" + cellData.getValue().getCapacity()));
        colLectureTime.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLectureTime()));
        colFinalExamDate.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFinalExamDate()));
        colLocation.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLocation()));
        colTeacherName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTeacherName()));
    }

    public void refreshEntries(TableView<Course> table){
        courseData = readCourses();
        courses = FXCollections.observableArrayList(courseData);
        table.setItems(courses);
    }
}