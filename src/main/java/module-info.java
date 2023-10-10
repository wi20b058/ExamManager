module com.example.exammanager {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.exammanager to javafx.fxml;
    exports com.example.exammanager;
}