module com.example.login_checker_02 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.login_checker_02 to javafx.fxml;
    exports com.example.login_checker_02;
}