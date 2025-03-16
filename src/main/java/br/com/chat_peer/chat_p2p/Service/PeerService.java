package br.com.chat_peer.chat_p2p.Service;

import org.springframework.stereotype.Service;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

@Service
public class PeerService {
    private final List<Socket> connections = new ArrayList<>();
    private final MessageService messageService;

    public PeerService(MessageService messageService) {
        this.messageService = messageService;
    }

    public void addConnection(Socket socket) {
        connections.add(socket);
    }

    public void sendMessage(String sender, String message) {
        messageService.sendMessage(sender, message, connections);
    }
    public List<String> getMessageHistory() {
        return messageService.getMessageHistory();
    }
}
