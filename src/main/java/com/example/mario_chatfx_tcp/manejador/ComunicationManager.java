package com.example.mario_chatfx_tcp.manejador;

import com.example.mario_chatfx_tcp.controlador.Message;
import com.example.mario_chatfx_tcp.controlador.Receiver;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ComunicationManager implements Runnable {
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private Receiver receiver;

    public ComunicationManager(Socket socket, Receiver receiver) {
        try {
            this.oos = new ObjectOutputStream(socket.getOutputStream());
            // hacer flush por si hay algo pendiente en el output stream
            this.oos.flush();
            this.ois = new ObjectInputStream(socket.getInputStream());
            this.receiver = receiver;
        } catch (IOException e) {
            System.out.println("Error al crear ComunicationManager: " + e.getMessage());
            return; // salir para no printear el creado correcto
        }
        System.out.println("Comunication Manager creado correctamente");
    }

    @Override
    public void run() {
        try {
            Message message;
            while ((message = (Message) ois.readObject()) != null) {
                receiver.recibir(message, this);
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error de ComunicationManager: " + e.getMessage());
        } finally {
            receiver.borrar(this);
        }
    }

    public void enviarMensaje(Message mensaje) {
        try {
            oos.writeObject(mensaje);
            oos.reset();
        } catch (IOException e) {
            System.out.println("Error al enviar mensaje: " + e.getMessage());
            receiver.borrar(this);
        }
    }
}
