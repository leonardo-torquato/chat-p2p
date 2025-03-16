package br.com.chat_peer.chat_p2p.Service;

import br.com.chat_peer.chat_p2p.Model.Peer;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

@Service
public class ConnectionService {
    private static final String REGISTRY_URL = "http://localhost:8080/registry";
    private final RestTemplate restTemplate = new RestTemplate();
    private List<Peer> peers = new ArrayList<>();
    private ServerSocket serverSocket;

    public void registerSelf(String address) {
        String url = REGISTRY_URL + "/register?address=" + address;
        restTemplate.postForObject(url, null, String.class);
    }

    @SuppressWarnings("unchecked")
    public List<String> discoverPeers() {
        String url = REGISTRY_URL + "/peers";
        return restTemplate.getForObject(url, List.class);
    }

    public void startServer(int port) {
        try{
            serverSocket = new ServerSocket(port);
            System.out.println("Servidor P2P ouvindo na porta: " + port);

            new Thread(this::listenForConnections).start();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    private void listenForConnections() {
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                System.out.println("Novo peer conectado: " + socket.getInetAddress());

                Peer peer = new Peer("Desconhecido", socket);
                peers.add(peer);
                new Thread(() -> handleConnection(peer)).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleConnection(Peer peer) {
        System.out.println("Gerenciando conexão com: " + peer.getSocket().getInetAddress());
    }

    public void connectToPeer(String host, int port) {
        try {
            Socket socket = new Socket(host, port);
            Peer peer = new Peer("Peer Remoto", socket);
            peers.add(peer);
            System.out.println("Conectado ao peer em " + host + ":" + port);
        } catch (IOException e) {
            System.out.println("Erro ao conectar ao peer em " + host + ":" + port);
            e.printStackTrace();
        }
    }

    public List<Peer> getPeers() {
        return peers;
    }

}
