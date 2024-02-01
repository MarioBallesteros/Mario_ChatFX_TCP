package com.example.mario_chatfx_tcp.controlador;

import java.io.Serializable;

public class Message implements Serializable {
    private String emisor;
    private String texto;

    public Message(String emisor, String texto) {
        this.emisor = emisor;
        this.texto = texto;
    }

    public String getTexto() {
        return texto;
    }

    public String getEmisor() {
        return emisor;
    }
}
