package server.bingo.project;

import packet.bingo.project.Packet;
import packet.bingo.project.PacketType;

import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

import game.bingo.project.BingoGame;

/**
 * Created by incognito on 2016-12-02.
 */
public class SenderObject {

	private static SenderObject instance = new SenderObject();
	private static List<ClientObject> clientList;
	private static List<ClientObject> authenticatedList;

	private SenderObject() {
		System.out.println("[!] SenderObject instance is ready.");
		clientList = new ArrayList<>();
		authenticatedList = new ArrayList<>();
	}

	public String sendReadyList() {
		String ret = "";

		for (int i = 0; i < authenticatedList.size(); i++) {
			if (i > 0) {
				ret += ",";
			}
			if (authenticatedList.get(i).isReady()) {
				ret += authenticatedList.get(i).getNickName();
			}
		}

		return ret;
	}

	public void printAllList() {
		String str = "Client: ";
		for (int i = 0; i < clientList.size(); i++) {
			if (i > 0) {
				str += ",";
			}
			str += clientList.get(i).getHostIP();
		}
		str += "\nAuthenticated: ";

		for (int i = 0; i < authenticatedList.size(); i++) {
			if (i > 0) {
				str += ",";
			}
			str += authenticatedList.get(i).getNickName() + "(" + authenticatedList.get(i).getHostIP() + ")";
		}
		str += "\n";
		System.out.println(str);
	}

	public static SenderObject getInstance() {
		return instance;
	}

	public int getNumOfClients() {
		return clientList.size();
	}
	
	public int getNumOfAuthenticatedUser() {
		return authenticatedList.size();
	}

	// set client object
	public void setClientObject(ClientObject clientObject) {
		// 클라이언트 리스트에 존재하지 않는 경우 추가
		if (!clientList.contains(clientObject)) {
			clientList.add(clientObject);
		}
	}

	// remove client object
	public boolean removeClientObject(ClientObject clientObject) {
		boolean ret = false;

		if (clientList.contains(clientObject)) {
			clientList.remove(clientObject);
			ret = true;
		}

		return ret;
	}

	// set authenticated user
	public void setAuthenticatedUser(ClientObject clientObject) {
		// 중복 제거
		if (!authenticatedList.contains(clientObject)) {
			authenticatedList.add(clientObject);
		}
	}

	// get authenticated user list
	public List<ClientObject> getAuthenticatedUser() {
		return authenticatedList;
	}

	// remove authenticated user from list
	public boolean removeAutheticatedUser(ClientObject clientObject) {
		boolean ret = false;
		if (authenticatedList.contains(clientObject)) {
			authenticatedList.remove(clientObject);
			ret = true;
		}
		return ret;
	}

	public boolean broadcast(Packet packet) {
		boolean ret = false;
		for (ClientObject co : clientList) {
			try {
				co.getSender().writeObject(packet);
				co.getSender().flush();
				ret = true;
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("[!] Unable to send packet to " + co.getHostIP());
				ret = false;
			}
		}
		return ret;
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

	public void sendToGamers(List<ClientObject> readyList, String number) {
		Packet packet = new Packet(PacketType.BINGO_NUM, number, "");

		for (ClientObject co : readyList) {
			try {
				co.getSender().writeObject(packet);
				co.getSender().flush();
			} catch (SocketException e) {
				BingoGame.getInstance().removeReadyUser(co);
				SenderObject.getInstance().removeClientObject(co);
				SenderObject.getInstance().removeAutheticatedUser(co);
				e.printStackTrace();
			} catch (IOException e) {
				System.out.println("[!] Unable to send packet to " + co.getHostIP());
				BingoGame.getInstance().removeReadyUser(co);
				SenderObject.getInstance().removeClientObject(co);
				SenderObject.getInstance().removeAutheticatedUser(co);
				e.printStackTrace();
			} catch (ConcurrentModificationException e) {
				BingoGame.getInstance().removeReadyUser(co);
				SenderObject.getInstance().removeClientObject(co);
				SenderObject.getInstance().removeAutheticatedUser(co);
			}
		}
	}

	public void killAllGamers(List<ClientObject> readyList) {
		Packet packet = new Packet(PacketType.BINGO_COMPLETE, "", "");

		for (ClientObject co : readyList) {
			try {
				co.getSender().writeObject(packet);
				co.getSender().flush();
			} catch (IOException e) {
				System.out.println("[!] Unable to send packet to " + co.getHostIP());
				e.printStackTrace();
			}
		}
	}

}