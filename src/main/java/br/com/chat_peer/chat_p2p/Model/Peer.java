package br.com.chat_peer.chat_p2p.Model;

import java.net.Socket;

import com.fasterxml.jackson.annotation.JsonIgnore;
public class Peer {
    private String userName;
    private String peerAddress; // Armazena o endereço do peer

    @JsonIgnore
    private Socket socket;

    public Peer(String userName, String peerAddress, Socket socket) {
        this.userName = userName;
        this.peerAddress = peerAddress;
        this.socket = socket;
    }

    public String getUserName() {
        return this.userName;
    }

    public String getPeerAddress() {
        return this.peerAddress;
    }

    public Socket getSocket() {
        return this.socket;
    }
}