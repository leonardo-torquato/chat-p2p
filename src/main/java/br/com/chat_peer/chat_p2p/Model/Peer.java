package br.com.chat_peer.chat_p2p.Model;

import java.net.Socket;

public class Peer {
    private String userName;
    private Socket socket;

    public Peer(String userName, Socket socket) {
        this.userName = userName;
        this.socket = socket;
    }

    public String getUserName() {
        return this.userName;
    }

    public Socket getSocket() {
        return this.socket;
    }

}
