module com.example.checkers {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.checkers to javafx.fxml;
    exports com.example.checkers;
    exports com.example.checkers.models;
    opens com.example.checkers.models to javafx.fxml;
    exports com.example.checkers.enums;
    opens com.example.checkers.enums to javafx.fxml;
}