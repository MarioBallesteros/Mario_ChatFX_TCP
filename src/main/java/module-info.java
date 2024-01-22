module com.example.mario_chatfx_tcp {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.mario_chatfx_tcp to javafx.fxml;
    exports com.example.mario_chatfx_tcp;
}