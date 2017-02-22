package server.bingo.project;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by incognito on 2016-12-02.
 */
public class ServerTask implements Runnable {

	private static ServerTask instance = new ServerTask();
	private ServerSocket serverSocket = null;
	private String port = "7000"; // default server port

	// Constructor
	private ServerTask() {
		try {
			serverSocket = new ServerSocket(Integer.parseInt(port));
			System.out.println("[!] Server task has begun.");
			System.out.printf("[!] Our server is running on %s:%s!\n", InetAddress.getLocalHost().getHostAddress(),
					port);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("[!] Unable to create server socket.");
		}
	}

	public static ServerTask getInstance() {
		return instance;
	}

	@Override
	public void run() {
		while (true) {
			Socket socket;
			ClientObject co;
			try {
				socket = serverSocket.accept();
				co = new ClientObject(socket);
				SenderObject.getInstance().setClientObject(co);
				ReceiverThread task = new ReceiverThread(co);
				Thread thread = new Thread(task);
				thread.start();
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("[!] Unable to accept client connection.");
			}

		}
	}

}