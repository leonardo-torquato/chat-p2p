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
    public void sendMessage(@RequestBody MessageRequest request) {
        peerService.sendMessage(request.getSender(), request.getMessage());
    }

    @GetMapping("/history")
    public List<String> getMessageHistory() {
        return peerService.getMessageHistory();
    }

    // Classe interna para representar a requisição JSON
    public static class MessageRequest {
        private String sender;
        private String message;

        public String getSender() {
            return sender;
        }

        public String getMessage() {
            return message;
        }
    }
}

