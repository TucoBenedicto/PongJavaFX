module com.example.Pong {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.Pong to javafx.fxml;
    exports com.example.Pong;
}