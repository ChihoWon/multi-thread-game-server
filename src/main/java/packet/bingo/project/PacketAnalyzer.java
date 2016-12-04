package packet.bingo.project;

import server.bingo.project.ClientObject;
import server.bingo.project.SenderObject;

/**
 * Created by incognito on 2016-12-02.
 */
public class PacketAnalyzer {

    private static PacketAnalyzer instance = new PacketAnalyzer();

    private PacketAnalyzer() {
        System.out.println("[!] PacketAnalyzer instance is ready.");
    }

    public static PacketAnalyzer getInstance() {
        return instance;
    }

    public void analyzePacket(ClientObject clientObject, Packet packet) {
        switch (packet.getProtocol()) {
            case SIGN_UP:
                System.out.println("ID      : " + packet.getMainField());
                System.out.println("Password: " + packet.getSubField());
                break;
            case MESSAGE:
                System.out.println("MESSAGE: " + packet.getMainField());
                SenderObject.getInstance().broadcast(new Packet(PacketType.MESSAGE, "y'all hi", "nice to meet you."));
                break;
            case MESSAGE_TO:
                System.out.println("MESSAGE: " + packet.getMainField());
                System.out.println("TO: " + packet.getSubField());
                break;
        }
    }

}
