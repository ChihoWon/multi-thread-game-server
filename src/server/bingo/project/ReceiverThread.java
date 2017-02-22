package server.bingo.project;

import packet.bingo.project.Packet;
import packet.bingo.project.PacketAnalyzer;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.SocketException;

/**
 * Created by incognito on 2016-12-02.
 */
public class ReceiverThread implements Runnable {

    private ClientObject clientObject;

    public ReceiverThread(ClientObject clientObject) {
        this.clientObject = clientObject;
    }

    @Override
    public void run() {
        while (true) {
            try (InputStream is = clientObject.getSocket().getInputStream(); ObjectInputStream reader = new ObjectInputStream(is)) {
                Packet packet;
                while ((packet = (Packet) reader.readObject()) != null) {
//                    System.out.println(Thread.currentThread().getName() + " : " + packet);
                    PacketAnalyzer.getInstance().analyzePacket(clientObject, packet);
                }
            }catch (SocketException e) {
//                e.printStackTrace();
                break;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                break;
            } catch (EOFException e) {
//                e.printStackTrace();
                break;
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
        // These message will be replaced with slf4j.
        System.out.printf("[!] %s(receiver) is going to be terminated.\n", Thread.currentThread().getName());
        SenderObject.getInstance().removeClientObject(clientObject);
        SenderObject.getInstance().removeAutheticatedUser(clientObject);
    }
}