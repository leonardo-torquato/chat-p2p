package br.com.chat_peer.chat_p2p.Controller;

import br.com.chat_peer.chat_p2p.Service.PeerService;

import java.util.List;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/messages")
public class MessageController {
    private final PeerService peerService;

    public MessageController(PeerService peerService) {
        this.peerService = peerService;
    }

    @PostMapping("/send")
    public void sendMessage(@RequestParam String sender, @RequestParam String message) {
        peerService.sendMessage(sender, message);
    }

    @GetMapping("/history")
    public List<String> getMessageHistory() {
        return peerService.getMessageHistory();
    }
}
