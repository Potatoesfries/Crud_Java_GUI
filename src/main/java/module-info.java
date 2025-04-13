module com.example.finalproject {
    requires transitive javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires javafx.graphics;
    requires javafx.base;

    opens com.example.finalproject to javafx.fxml;
    opens com.example.finalproject.Model to javafx.base; // Add this line
    exports com.example.finalproject;
    exports com.example.finalproject.Controller;
    opens com.example.finalproject.Controller to javafx.fxml;
}