package br.com.chat_peer.chat_p2p.Service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PeerRegistryService {
    private final List<String> registeredPeers = new ArrayList<>();

    public synchronized void registerPeer(String address) {
        if (!registeredPeers.contains(address)) {
            registeredPeers.add(address);
            System.out.println("Peer registrado: " + address);
        }
    }

    public synchronized List<String> getRegisteredPeers() {
        return new ArrayList<>(registeredPeers);
    }
}

