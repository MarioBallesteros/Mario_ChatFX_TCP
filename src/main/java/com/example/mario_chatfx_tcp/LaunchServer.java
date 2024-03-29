package com.example.mario_chatfx_tcp;

import com.example.mario_chatfx_tcp.manejador.ChannelManager;

import java.io.IOException;
import java.net.ServerSocket;

public class LaunchServer {
    public static void main(String[] args) {
        int puerto = 8080;
        ChannelManager channelManager = new ChannelManager();

        try(ServerSocket serverSocket = new ServerSocket(puerto)){
            System.out.println("Servidor escuchando en el puerto: "+ puerto);
            while (true){
                channelManager.add(serverSocket.accept());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
