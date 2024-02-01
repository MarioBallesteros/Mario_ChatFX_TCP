package com.example.mario_chatfx_tcp.manejador;

import com.example.mario_chatfx_tcp.controlador.Message;
import com.example.mario_chatfx_tcp.controlador.Receiver;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class ComunicationManager implements Runnable{
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private Receiver receiver;
    private InetAddress inetAddress;

    public ComunicationManager(Socket socket, Receiver receiver) {
        try {
            this.oos = new ObjectOutputStream(socket.getOutputStream());
            this.ois = new ObjectInputStream(socket.getInputStream());
            this.receiver = receiver;
            this.inetAddress = socket.getInetAddress();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        System.out.println("Comunication Manager creado correctamente");
    }

    @Override
    public void run() {
        Message message;
        try {
            while (((message = (Message) ois.readObject()) != null)) {
                receiver.recibir(message,this);
                System.out.println("Mensaje de: "+ inetAddress.getHostAddress() + " : "+ message.getTexto());
            }
        } catch (Exception e) {
            System.out.println("Comunication manager eliminado de la lista debido a una excepcion");
            receiver.borrar(this);
        }
    }

    public void enviarMensaje(Message mensaje){
        Thread hiloEnvio = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    oos.writeObject(mensaje);
                    // Joaquin hace oos.flush(),preguntar!
                    System.out.println("Enviando mensaje");
                } catch (IOException e) {
                    System.out.println("Error al enviar mensaje");
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }
        });
        hiloEnvio.start();
    }
}
