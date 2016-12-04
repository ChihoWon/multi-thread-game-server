package server.bingo.project;

import packet.bingo.project.Packet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by incognito on 2016-12-02.
 */
public class SenderObject {

    private static SenderObject instance = new SenderObject();
    private static List<ClientObject> clientList;

    private SenderObject() {
        System.out.println("[!] SenderObject instance is ready.");
        clientList = new ArrayList<>();
    }

    public static SenderObject getInstance() {
        return instance;
    }

    public int getNumOfClients() { return clientList.size(); }

    public void setClientObject(ClientObject clientObject) {
        clientList.add(clientObject);
    }

    public boolean removeClientObject(ClientObject clientObject) {
        boolean ret = false;

        if(clientList.contains(clientObject)) {
            clientList.remove(clientObject);
            ret = true;
        }

        return ret;
    }

    public void broadcast(Packet packet) {
        for (ClientObject co : clientList) {
            try {
                co.getSender().writeObject(packet);
                co.getSender().flush();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("[!] Unable to send packet to " + co.getHostIP());
            }
        }
    }

    public boolean sendTo(String hostIP, Packet packet) {
        boolean ret = false;

        for (ClientObject co : clientList) {
            if (co.getHostIP().equals(hostIP)) {
                try {
                    co.getSender().writeObject(packet);
                    co.getSender().flush();
                    ret = true;
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("[!] Unable to send packet to " + co.getHostIP());
                }
                break;
            }
        }

        return ret;
    }


}
