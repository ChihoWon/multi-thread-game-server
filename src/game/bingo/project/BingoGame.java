package game.bingo.project;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import packet.bingo.project.Packet;
import packet.bingo.project.PacketType;
import server.bingo.project.ClientObject;
import server.bingo.project.SenderObject;

// game thread
public class BingoGame implements Runnable {

	private static final int timeout = 5000;		// default timeout
	private static BingoGame instance = new BingoGame();	// singleton object
	private static List<ClientObject> readyList;	// user list
	private static String[] numberList;				// numbers for game

	private BingoGame() {

		Random rg = new Random();
		readyList = new ArrayList<>();
		numberList = new String[50];

		for (int i = 0; i < 50; i++) {
			numberList[i] = i + 1 + "";
		}

		for (int i = 0; i < 50; i++) {
			int r = rg.nextInt(50);
			String temp = numberList[r];
			numberList[r] = numberList[i];
			numberList[i] = temp;
		}
		System.out.println("[!] BingoGame instance is ready. :)");
	}

	@Override
	public void run() {
		System.out.println("[!] Gamer list");
		for (ClientObject co : readyList) {
			System.out.println("[+] " + co.getNickName());
		}

		int i = 0;
		while (true) {
			try {
				String num = numberList[i++];
				if(readyList.size() < 2) {
					System.out.println("bye~");
					SenderObject.getInstance().sendTo(readyList.get(1).getHostIP(), new Packet(PacketType.WIN, "", ""));
					SenderObject.getInstance().broadcast(new Packet(PacketType.WIN, "", ""));
					break;
				}
				System.out.printf("Number(%d): %s\n", i + 1, num);
				Thread.sleep(timeout);
				SenderObject.getInstance().sendToGamers(readyList, num);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	// thread trigger
	public int numberOfReadyUser() {
		int ret = 0;
		for (ClientObject co : readyList) {
			if (co.isReady()) {
				ret++;
			}
		}
		return ret;
	}

	public static BingoGame getInstance() {
		return instance;
	}
	
	public List<ClientObject> getReadyUserList() {
		return readyList;
	}

	public void setReadyUser(ClientObject clientObject) {
		readyList.add(clientObject);
	}

	public void removeReadyUser(ClientObject clientObject) {
		if (readyList.contains(clientObject)) {
			readyList.remove(clientObject);
		}
	}

}
