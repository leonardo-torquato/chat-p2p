package br.com.chat_peer.chat_p2p.Service;

import br.com.chat_peer.chat_p2p.Model.Peer;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

@Service
public class MessagingService {
    private final ConnectionService connectionService;

    public MessagingService(ConnectionService connectionService) {
        this.connectionService = connectionService;
    }
    
    public void sendMessage(String message) {
        for (Peer peer : connectionService.getPeers()) {
            try {
                PrintWriter out = new PrintWriter(peer.getSocket().getOutputStream(), true);
                out.println(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

        public void receiveMessages(Peer peer) {
        new Thread(() -> {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(peer.getSocket().getInputStream()));
                String message;
                while ((message = in.readLine()) != null) {
                    System.out.println(peer.getUserName() + ": " + message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
