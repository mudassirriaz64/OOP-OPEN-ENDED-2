module com.labtasks.task {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    opens com.labtasks.task to javafx.fxml;
    exports com.labtasks.task;
}