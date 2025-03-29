package com.example.ums_engg1420.studentsmodule;

//Import JavaFX Packages
import com.example.ums_engg1420.dataclasses.Student;
import com.example.ums_engg1420.dataparsers.StudentDataHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import java.io.IOException;

public class AdminStudentController extends StudentModuleInitializer {
    //Initialize GUI Functions
    @FXML private StackPane mainContent;
    @FXML private TableView<Student> tblStudents;
    @FXML private TableColumn<Student, String> colStuName;
    @FXML private TableColumn<Student, String> colStuID;
    @FXML private TableColumn<Student, String> colStuPic;
    @FXML private TableColumn<Student, String> ViewInfoCol;
    @FXML private Button ViewInfo;

        //Initialize Column Names
        public void initialize() {
            setupCoulmns();
            loadStudents();
        }
        //Sets up the different columns with student information
        private void setupCoulmns() {
            // Set up basic columns
            colStuName.setCellValueFactory(new PropertyValueFactory<>("name"));
            colStuID.setCellValueFactory(new PropertyValueFactory<>("studentId"));
            colStuPic.setCellValueFactory(new PropertyValueFactory<>("profilePhoto"));

            // Set up a custom cell factory for the "View Info" column
            ViewInfoCol.setCellFactory(column -> new TableCell<>() {
                private final Button viewButton = new Button("View Info");

                {
                    viewButton.setOnAction(e -> {
                        Student student = getTableView().getItems().get(getIndex());
                        System.out.println("Viewing details for student: " + student.getName());
                        loadStudentDetails(student);
                    });
                }
                //Updates the item
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(viewButton);
                    }
                }
            });
        }

    //Load student
    private void loadStudents() {
        ObservableList<Student> students = FXCollections.observableArrayList(StudentDataHandler.getLoggedInStudent());
        tblStudents.setItems(students);
    }

    //Load student information
    private void loadStudentDetails(Student student) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("StudentModuleInitializer.fxml"));
        try {
            Parent studentView = loader.load();
            StudentModuleInitializer controller = loader.getController();
            controller.setStudent(student);
            mainContent.getChildren().setAll(studentView);
        } catch (IOException e) {
            e.printStackTrace();  // Print error if FXML is not found
            System.out.println("ERROR: Could not load fxml file");
        }
    }

    }//AdminStudentController

