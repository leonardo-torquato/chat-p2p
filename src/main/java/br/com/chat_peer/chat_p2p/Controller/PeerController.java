

package br.com.chat_peer.chat_p2p.Controller;

import br.com.chat_peer.chat_p2p.DTO.PeerDTO;
import br.com.chat_peer.chat_p2p.Model.Peer;
import br.com.chat_peer.chat_p2p.Service.ConnectionService;
import br.com.chat_peer.chat_p2p.Service.PeerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("/peer")
public class PeerController {
    private final ConnectionService connectionService;
    private final PeerService peerService;

    public PeerController(ConnectionService connectionService, PeerService peerService) {
        this.connectionService = connectionService;
        this.peerService = peerService;
    }

    @PostMapping("/start/{port}")
    public String startServer(@PathVariable int port) {
        String address = "localhost:" + port;

        // Registrar este peer
        connectionService.registerSelf(address);

        // Descobrir e conectar a outros peers
        List<String> peers = connectionService.discoverPeers();
        for (String peer : peers) {
            if (!peer.equals(address)) {
                String[] parts = peer.split(":");
                connectionService.connectToPeer(parts[0], Integer.parseInt(parts[1]), address); // Passa o endereço do peer
            }
        }

        connectionService.startServer(port);
        return "Servidor iniciado e conectado a peers conhecidos!";
    }

    @PostMapping("/connect")
    public String connectToPeer(
        @RequestParam String host, 
        @RequestParam int port,
        @RequestParam String myAddress // Adiciona o endereço do peer atual
    ) {
        connectionService.connectToPeer(host, port, myAddress); // Passa o endereço do peer
        return "Conectado a " + host + ":" + port;
    }

    @PostMapping("/disconnectAll")
    public String disconnectAll() {
        connectionService.disconnectAll();
        return "Todos os peers foram desconectados!";
    }

    @PostMapping("/disconnect/{peerAddress}")
    public ResponseEntity<String> disconnectPeer(@PathVariable String peerAddress) {
        connectionService.disconnectPeer(peerAddress);
        return ResponseEntity.ok("Peer desconectado: " + peerAddress);
    }

    @GetMapping("/connected")
    public List<PeerDTO> getConnectedPeers() {
        Map<String, Peer> peers = connectionService.getPeers();
        List<PeerDTO> peerDTOs = new ArrayList<>();

        for (Map.Entry<String, Peer> entry : peers.entrySet()) {
            String peerAddress = entry.getKey();
            Peer peer = entry.getValue();
            peerDTOs.add(new PeerDTO(peer.getUserName(), peerAddress));
        }

        return peerDTOs;
    }
}