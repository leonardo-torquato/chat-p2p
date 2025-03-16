package br.com.chat_peer.chat_p2p.Service;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class MessageService {
    private final List<String> messageHistory = new ArrayList<>();

    public synchronized void saveMessage(String sender, String message) {
        String timestampedMessage = "[" + LocalDateTime.now() + "] " + sender + ": " + message;
        messageHistory.add(timestampedMessage);
        System.out.println(timestampedMessage);
    }

    public synchronized List<String> getMessageHistory() {
        return new ArrayList<>(messageHistory);
    }

    public void sendMessage(String sender, String message, List<Socket> connections) {
        saveMessage(sender, message);  // Salva no histórico

        for (Socket socket : connections) {
            try {
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                out.println(sender + ": " + message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}