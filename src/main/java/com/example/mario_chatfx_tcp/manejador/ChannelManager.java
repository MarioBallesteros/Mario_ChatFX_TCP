package com.example.mario_chatfx_tcp.manejador;

import com.example.mario_chatfx_tcp.controlador.Message;
import com.example.mario_chatfx_tcp.controlador.Receiver;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ChannelManager implements Receiver {
    private List<ComunicationManager> comunications;
    private List<Message> historialMensajes;

    public ChannelManager() {
        this.comunications = new ArrayList<>();
        this.historialMensajes = new ArrayList<>();
    }

    public synchronized void add(Socket socket) {
        ComunicationManager comunicationManager = new ComunicationManager(socket, this);
        comunications.add(comunicationManager);
        Thread thread = new Thread(comunicationManager);
        thread.setDaemon(true);
        thread.start();

        // Envía el historial de mensajes al nuevo cliente
        for (Message mensaje : historialMensajes) {
            comunicationManager.enviarMensaje(mensaje);
        }
    }

    @Override
    public synchronized void recibir(Message mensaje, ComunicationManager comunicationManager) {
        historialMensajes.add(mensaje);
        for (ComunicationManager com : comunications) {
            if (com != comunicationManager) {
                com.enviarMensaje(mensaje);
            }
        }
    }

    @Override
    public synchronized void borrar(ComunicationManager comunicationManager) {
        comunications.remove(comunicationManager);
    }
}
