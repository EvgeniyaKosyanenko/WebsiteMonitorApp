module com.example.testurl {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.testurl to javafx.fxml;
    exports com.example.testurl;
}