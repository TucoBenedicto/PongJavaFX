module com.example.tuto_pong {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.tuto_pong to javafx.fxml;
    exports com.example.tuto_pong;
}