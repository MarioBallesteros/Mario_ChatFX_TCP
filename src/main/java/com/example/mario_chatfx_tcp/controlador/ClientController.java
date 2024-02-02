package com.example.mario_chatfx_tcp.controlador;

import com.example.mario_chatfx_tcp.controlador.Message;
import com.example.mario_chatfx_tcp.controlador.Receiver;
import com.example.mario_chatfx_tcp.manejador.ComunicationManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

import java.io.IOException;
import java.net.Socket;

public class ClientController implements Receiver {
    private ComunicationManager comunicationManager;
    @FXML
    private Button connectButton;
    @FXML
    private Button sendButton;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField ipTxtField;

    @FXML
    private TextField portTxtField;

    @FXML
    private TextField messageTxtField;

    @FXML
    private VBox messageTextArea;
    private String nombreCliente;

    @FXML
    public void onConnectButtonClick() {
        try {
            String ipCliente = ipTxtField.getText();
            int puertoCliente = Integer.parseInt(portTxtField.getText());
            nombreCliente = nameTextField.getText();

            Socket socket = new Socket(ipCliente,puertoCliente);
            comunicationManager = new ComunicationManager(socket,this);

            Thread hiloCliente = new Thread(comunicationManager);
            hiloCliente.setDaemon(true);
            hiloCliente.start();
            sendButton.setDisable(false);
            connectButton.setDisable(true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void setSendButtonClick() {
        // codigo Joaquin para crear los label del mensaje
        if (comunicationManager != null) {
            comunicationManager.enviarMensaje(new Message(nombreCliente, messageTxtField.getText()));
            Label label = new Label(" Tú: \n" + messageTxtField.getText());
            label.setTextAlignment(TextAlignment.LEFT);
            label.setBackground(new Background(new BackgroundFill(Color.rgb(85, 190, 83), new CornerRadii(5.0), Insets.EMPTY)));

            HBox hBox = new HBox(label);
            hBox.setAlignment(Pos.CENTER_RIGHT);
            HBox.setMargin(label, new Insets(5, 10, 5, 50));

            messageTextArea.getChildren().add(hBox);
            messageTxtField.setText("");
        }
    }


    @Override
    public void recibir(Message mensaje, ComunicationManager comunicationManager) {
        Platform.runLater(() -> {
            // codigo Joaquin para rellenar los label
            Label label = new Label(mensaje.getEmisor() + ": \n" + mensaje.getTexto());
            label.setBackground(new Background(new BackgroundFill(Color.rgb(74, 137, 190), new CornerRadii(5.0), new Insets(-5.0))));
            HBox hBox = new HBox(label);
            hBox.setAlignment(Pos.CENTER_LEFT);
            messageTextArea.getChildren().add(hBox);
        });
    }


    @Override
    public void borrar(ComunicationManager comunicationManager) {
        this.comunicationManager = null;
        ipTxtField.setEditable(true);
        portTxtField.setEditable(true);
        nameTextField.setEditable(true);
        connectButton.setDisable(false);
        sendButton.setDisable(true);
    }
}
