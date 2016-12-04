package server.bingo.project;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by incognito on 2016-12-02.
 * ClientObject represents the client's object
 */
@Getter
@Setter
@ToString
public class ClientObject {

    private Socket socket;              // client socket
    private String hostIP;              // client's ip address
    private boolean isAuthenticated;    // is it login-user?
    private ObjectOutputStream sender;

    ClientObject(Socket socket) {
        this.socket = socket;

        try {
            sender = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("[!] Unable to get output stream.");
            System.out.println("[>] Object signature: " + socket);
        }

        hostIP = socket.getInetAddress().getHostAddress();
    }

}
