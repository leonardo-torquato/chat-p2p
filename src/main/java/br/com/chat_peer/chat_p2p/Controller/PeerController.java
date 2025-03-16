package br.com.chat_peer.chat_p2p.Controller;

import br.com.chat_peer.chat_p2p.Service.ConnectionService;
import br.com.chat_peer.chat_p2p.Service.MessagingService;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/peer")
public class PeerController {
    private final ConnectionService connectionService;
    private final MessagingService messagingService;

    public PeerController(ConnectionService connectionService, MessagingService messagingService) {
        this.connectionService = connectionService;
        this.messagingService = messagingService;
    }

    @PostMapping("/start/{port}")
    public String startServer(@PathVariable int port) {
        String address = "localhost:" + port;
        
        // Registrar este peer
        connectionService.registerSelf(address);

        // Descobrir outros peers
        List<String> peers = connectionService.discoverPeers();
        for (String peer : peers) {
            if (!peer.equals(address)) {
                String[] parts = peer.split(":");
                connectionService.connectToPeer(parts[0], Integer.parseInt(parts[1]));
            }
        }
        
        connectionService.startServer(port);
        return "Servidor iniciado e conectado a peers conhecidos!";
    }


    @PostMapping("/connect")
    public String connectToPeer(@RequestParam String host, @RequestParam int port) {
        connectionService.connectToPeer(host, port);
        return "Conectado a " + host + ":" + port;
    }

    @PostMapping("/send")
    public String sendMessage(@RequestParam String message) {
        messagingService.sendMessage(message);
        return "Mensagem enviada!";
    }
}
