module com.example.ums_engg1420 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens com.example.ums_engg1420 to javafx.fxml;
    exports com.example.ums_engg1420;
}