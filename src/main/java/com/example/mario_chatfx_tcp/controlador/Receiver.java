package com.example.mario_chatfx_tcp.controlador;

import com.example.mario_chatfx_tcp.manejador.ComunicationManager;

public interface Receiver {

    void recibir(Message mensaje, ComunicationManager comunicationManager);

    void borrar(ComunicationManager comunicationManager);
}
