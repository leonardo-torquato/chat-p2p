package br.com.chat_peer.chat_p2p.Service;

import br.com.chat_peer.chat_p2p.Model.Peer;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class MessagingService {
    private final ConnectionService connectionService;
    private final List<String> messageHistory = new CopyOnWriteArrayList<>();

    public MessagingService(ConnectionService connectionService) {
        this.connectionService = connectionService;
    }

    public void sendMessage(String sender, String message) {
        String timestampedMessage = LocalDateTime.now() + " " + sender + ": " + message; // Removido os colchetes
        messageHistory.add(timestampedMessage);
        System.out.println(timestampedMessage);
    
        for (Peer peer : connectionService.getPeers().values()) {
            try {
                PrintWriter out = new PrintWriter(peer.getSocket().getOutputStream(), true);
                out.println(timestampedMessage); // Envia a mensagem formatada
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
                // Processa a mensagem recebida
                System.out.println("Mensagem recebida de " + peer.getUserName() + ": " + message);
                sendMessage(peer.getUserName(), message); // Envia a mensagem para todos os peers
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }).start();
}

    public synchronized List<String> getMessageHistory() {
        return new ArrayList<>(messageHistory);
    }
}