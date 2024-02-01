package com.example.mario_chatfx_tcp.manejador;

import com.example.mario_chatfx_tcp.controlador.Message;
import com.example.mario_chatfx_tcp.controlador.Receiver;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ChannelManager implements Receiver {
    List<ComunicationManager> comunications;

    public ChannelManager() {
        this.comunications = new ArrayList<>();
    }


    public void add(Socket socket) {
        ComunicationManager comunicationManager = new ComunicationManager(socket,this);
        comunications.add(comunicationManager);
        // y lanzamos el hilo del comunication manager
        Thread thread = new Thread(comunicationManager);
        thread.setDaemon(true);
        thread.start();
    }

    @Override
    public void recibir(Message mensaje, ComunicationManager comunicationManager) {
        System.out.println(mensaje.getEmisor()+": "+ mensaje.getTexto());
        for (ComunicationManager comunication: comunications) {
            if (comunication != comunicationManager){
                comunication.enviarMensaje(mensaje);
            }
        }
    }

    @Override
    public void borrar(ComunicationManager comunicationManager) {
        comunications.remove(comunicationManager);
    }
}
