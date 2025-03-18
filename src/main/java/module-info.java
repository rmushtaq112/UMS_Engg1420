module com.example.ums_engg1420 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires org.apache.poi.poi;
    requires java.logging;

    opens com.example.ums_engg1420 to javafx.fxml;
    exports com.example.ums_engg1420;
    exports com.example.ums_engg1420.dataparsers;
    opens com.example.ums_engg1420.dataparsers to javafx.fxml;
    exports com.example.ums_engg1420.dataclasses;
    opens com.example.ums_engg1420.dataclasses to javafx.fxml;
    exports com.example.ums_engg1420.subjectsmodule;
    opens com.example.ums_engg1420.subjectsmodule to javafx.fxml;

}