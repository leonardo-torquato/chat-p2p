
package br.com.chat_peer.chat_p2p.Service;

import org.springframework.stereotype.Service;

import br.com.chat_peer.chat_p2p.Model.Peer;

import java.net.Socket;
import java.util.List;

@Service
public class PeerService {
    private final ConnectionService connectionService;
    private final MessagingService messagingService;

    public PeerService(ConnectionService connectionService, MessagingService messagingService) {
        this.connectionService = connectionService;
        this.messagingService = messagingService;
    }

    public void addConnection(Socket socket) {
        String peerAddress = socket.getInetAddress().getHostAddress() + ":" + socket.getPort();
        Peer peer = new Peer("Desconhecido", peerAddress, socket); // Passa o endereço do peer
        connectionService.getPeers().put(peerAddress, peer);
    }

    public void sendMessage(String sender, String message) {
        // Envia a mensagem para todos os peers conectados
        for (Peer peer : connectionService.getPeers().values()) {
            messagingService.sendMessage(sender, message);
        }
    }

    public List<String> getMessageHistory() {
        return messagingService.getMessageHistory();
    }

    public void notifyDisconnection(Socket socket) {
        String message = "Peer " + socket.getInetAddress().getHostAddress() + " se desconectou.";
        // Notifica todos os peers sobre a desconexão
        for (Peer peer : connectionService.getPeers().values()) {
            try {
                messagingService.sendMessage("Sistema", message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

