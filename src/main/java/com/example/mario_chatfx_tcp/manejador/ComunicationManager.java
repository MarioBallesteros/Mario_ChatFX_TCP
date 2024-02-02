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
            this.oos.flush();
            this.ois = new ObjectInputStream(socket.getInputStream());
            this.receiver = receiver;
        } catch (IOException e) {
            System.out.println("Error al crear ComunicationManager: " + e.getMessage());
            closeResources();
            return; // Retornar para evitar seguir ejecutando el constructor.
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
        } catch (IOException e) {
            System.out.println("Error de E/S en ComunicationManager: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("Clase no encontrada en ComunicationManager: " + e.getMessage());
        } finally {
            closeResources();
            receiver.borrar(this);
        }
    }

    public void enviarMensaje(Message mensaje) {
        try {
            oos.writeObject(mensaje);
            oos.reset(); // Resetear el ObjectOutputStream para manejar objetos modificados.
        } catch (IOException e) {
            System.out.println("Error al enviar mensaje: " + e.getMessage());
            closeResources();
            receiver.borrar(this);
        }
    }

    private void closeResources() {
        try {
            if (ois != null) ois.close();
            if (oos != null) oos.close();
        } catch (IOException e) {
            System.out.println("Error al cerrar recursos en ComunicationManager: " + e.getMessage());
        }
    }
}
