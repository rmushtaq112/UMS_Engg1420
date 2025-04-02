package com.example.ums_engg1420.studentsmodule;

//Import JavaFX Packages
import com.example.ums_engg1420.dataclasses.Student;
import com.example.ums_engg1420.dataparsers.StudentDataHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.IOException;

public class AdminStudentController extends StudentModuleInitializer{
    //Initialize GUI Functions
    @FXML private StackPane mainContent;
    @FXML private TableView<Student> tblStudents;
    @FXML private TableColumn<Student, String> colStuName;
    @FXML private TableColumn<Student, String> colStuID;
    @FXML private TableColumn<Student, String> colStuPic;
    @FXML private TableColumn<Student, String> ViewInfoCol;
    @FXML private Button ViewInfo;
    @FXML private Button btnAddStudent;
    @FXML private Button btnEditStudent;
    @FXML private Button btnDeleteStudent;
    @FXML private TextField txtName;
    @FXML private TextField txtID;
    @FXML private TextField txtPP;
    @FXML private TextField txtEmail;
    @FXML private TextField txtTelephone;
    @FXML private TextField txtTuitionStatus;
    @FXML private TextField txtAcademicProg;
    @FXML private TextField txtTuition;



    //Initialize Column Names
    @FXML
        public void initialize() {
            setupCoulmns();
            loadStudents();
            handleAddStudent();
        }
        //Sets up the different columns with student information
        private void setupCoulmns() {
            // Set up basic columns
            colStuName.setCellValueFactory(new PropertyValueFactory<>("name"));
            colStuID.setCellValueFactory(new PropertyValueFactory<>("studentId"));
            colStuPic.setCellValueFactory(new PropertyValueFactory<>("profilePhoto"));

            // Set up a custom cell factory for the "View Info" column - Supposed to be a button that loads the students info
            ViewInfoCol.setCellFactory(column -> new TableCell<>() {
                private final Button viewInfo = new Button("View Info");

                {
                    viewInfo.setOnAction(e -> {
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
                        setGraphic(viewInfo);
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

    @FXML
    private void handleAddStudent() {
        System.out.println("Adding a new student...");

        // Logic for adding a student
        // Example: Open a dialog box or form to collect student details
        openAddStudentDialog();
    }

    private void openAddStudentDialog() {
        // Example: Load a separate FXML file for the add-student form
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AddStudent.fxml"));
            Parent addStudentView = loader.load();

            // Create a dialog or new scene for the add-student form
            Stage stage = new Stage();
            stage.setTitle("Add Student");
            stage.setScene(new Scene(addStudentView));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("ERROR: Could not load AddStudent.fxml");
        }
    }

}//AdminStudentController

