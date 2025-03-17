
package br.com.chat_peer.chat_p2p.Service;

import br.com.chat_peer.chat_p2p.Model.Peer;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ConnectionService {
    private static final String REGISTRY_URL = "http://localhost:8080/registry";
    private final RestTemplate restTemplate = new RestTemplate();
    private final ConcurrentHashMap<String, Peer> peers = new ConcurrentHashMap<>();
    private ServerSocket serverSocket;

    public void registerSelf(String address) {
        String url = REGISTRY_URL + "/register?address=" + address;
        restTemplate.postForObject(url, null, String.class);
    }

    public List<String> discoverPeers() {
        String url = REGISTRY_URL + "/peers";
        return restTemplate.getForObject(url, List.class);
    }

    public void startServer(int port) {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Servidor P2P ouvindo na porta: " + port);
            new Thread(this::listenForConnections).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void listenForConnections() {
    while (true) {
        try {
            Socket socket = serverSocket.accept();

            // Lê o endereço enviado pelo peer
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String peerAddress = in.readLine(); // Lê o endereço do peer

            // Cria o objeto Peer com o endereço enviado
            Peer peer = new Peer("Desconhecido", peerAddress, socket);
            peers.put(peerAddress, peer);
            System.out.println("Novo peer conectado: " + peerAddress);

            new Thread(() -> handleConnection(peer)).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    }

    private void handleConnection(Peer peer) {
        System.out.println("Gerenciando conexão com: " + peer.getSocket().getInetAddress());
    }

    public void connectToPeer(String host, int port, String myAddress) {
        try {
            Socket socket = new Socket(host, port);

            // Envia o endereço do peer como parte do handshake
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(myAddress); // Envia o endereço do peer

            // Cria o objeto Peer com o endereço enviado
            Peer peer = new Peer("Peer Remoto", myAddress, socket);
            peers.put(myAddress, peer);
            System.out.println("Conectado ao peer em " + host + ":" + port);
        } catch (IOException e) {
            System.out.println("Erro ao conectar ao peer em " + host + ":" + port);
            e.printStackTrace();
        }
    }

    public ConcurrentHashMap<String, Peer> getPeers() {
        return peers;
    }

    public void disconnectPeer(String peerAddress) {
        Peer peer = peers.remove(peerAddress);
        if (peer != null) {
            try {
                peer.getSocket().close();
                System.out.println("Desconectado peer: " + peerAddress);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void disconnectAll() {
        for (String peerAddress : peers.keySet()) {
            disconnectPeer(peerAddress);
        }
    }
}