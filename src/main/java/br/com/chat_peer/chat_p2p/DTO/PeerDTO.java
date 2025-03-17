package br.com.chat_peer.chat_p2p.DTO;

public class PeerDTO {
    private String userName;
    private String peerAddress;

    public PeerDTO(String userName, String peerAddress) {
        this.userName = userName;
        this.peerAddress = peerAddress;
    }

    public String getUserName() {
        return userName;
    }

    public String getPeerAddress() {
        return peerAddress;
    }
}