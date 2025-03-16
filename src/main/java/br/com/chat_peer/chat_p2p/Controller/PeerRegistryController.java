package br.com.chat_peer.chat_p2p.Controller;

import br.com.chat_peer.chat_p2p.Service.PeerRegistryService;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/registry")
public class PeerRegistryController {
    private final PeerRegistryService peerRegistryService;

    public PeerRegistryController(PeerRegistryService peerRegistryService) {
        this.peerRegistryService = peerRegistryService;
    }

    @PostMapping("/register")
    public String registerPeer(@RequestParam String address) {
        peerRegistryService.registerPeer(address);
        return "Peer registrado: " + address;
    }

    @GetMapping("/peers")
    public List<String> getPeers() {
        return peerRegistryService.getRegisteredPeers();
    }
}
