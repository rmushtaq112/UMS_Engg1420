module com.example.ums_engg1420 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires org.apache.poi.ooxml;
    opens com.example.ums_engg1420.studentsmodule to javafx.fxml; // Add this line
    opens com.example.ums_engg1420.facultymodule to javafx.fxml;


    opens com.example.ums_engg1420 to javafx.fxml;
    exports com.example.ums_engg1420;
    exports com.example.ums_engg1420.dataparsers;
    opens com.example.ums_engg1420.dataparsers to javafx.fxml;
    exports com.example.ums_engg1420.dataclasses;
    opens com.example.ums_engg1420.dataclasses to javafx.fxml;
    exports com.example.ums_engg1420.subjectsmodule;
    opens com.example.ums_engg1420.subjectsmodule to javafx.fxml;
    exports com.example.ums_engg1420.eventsmodule;
    opens com.example.ums_engg1420.eventsmodule to javafx.fxml;
    exports com.example.ums_engg1420.coursesmodule;
    opens com.example.ums_engg1420.coursesmodule to javafx.fxml;

}