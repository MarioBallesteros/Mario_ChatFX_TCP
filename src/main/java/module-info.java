module com.example.mario_chatfx_tcp {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.mario_chatfx_tcp.manejador to javafx.fxml;
    exports com.example.mario_chatfx_tcp;
    opens com.example.mario_chatfx_tcp.controlador to javafx.fxml;
}