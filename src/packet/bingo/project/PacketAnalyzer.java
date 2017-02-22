package packet.bingo.project;

import java.util.List;

import dao.bingo.project.UserDAO;
import game.bingo.project.BingoGame;
import server.bingo.project.ClientObject;
import server.bingo.project.SenderObject;
import vo.bingo.project.UserVO;

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

		Packet response;

		switch (packet.getProtocol()) {
		/*
		 * MESSAGE, MESSAGE_TO, SIGN_UP, SIGN_IN, BINGO_NUM, CLICKED_NUM,
		 * READY, DUPLICATION_CHECK, REQUEST_USER_LIST, BINGO_COMPLETE, WIN
		 */
		case MESSAGE:
			// forwarding incoming messages to the clients
			response = new Packet(PacketType.MESSAGE, clientObject.getNickName() + " > " + packet.getMainField(), "");
			SenderObject.getInstance().broadcast(response);
			break;
		case MESSAGE_TO:
			System.out.println("MESSAGE: " + packet.getMainField());
			System.out.println("TO: " + packet.getSubField());
			break;
		case SIGN_UP:
			// sign up request
			UserVO userSignUp = new UserVO(packet.getMainField(), packet.getSubField());
			UserDAO.getInstance().insert(userSignUp);
			break;
		case SIGN_IN:
			// login request
			response = null;
			if (UserDAO.getInstance().loginRequest(packet.getMainField(), packet.getSubField())) {
				response = new Packet(PacketType.SIGN_IN, "true", "");
				// 로그인 성공 시 인증된 유저 리스트에 등록
				clientObject.setAuthenticated(true); 				// 인증 허가
				clientObject.setNickName(packet.getMainField());	// 아이디 등록
				SenderObject.getInstance().setAuthenticatedUser(clientObject);	// 인증 리스트에 등록
			} else {
				response = new Packet(PacketType.SIGN_IN, "false", "");
			}
			SenderObject.getInstance().sendTo(clientObject.getHostIP(), response);
			break;
		case BINGO_NUM:
			break;
		case CLICKED_NUM:
//			System.out.println(packet);
			break;
		case READY:
			System.out.println("[!] " + clientObject.getHostIP() + " is joined to the game.");
			boolean flag = clientObject.isReady();
			clientObject.setReady(!flag);
			BingoGame.getInstance().setReadyUser(clientObject);		// it's a bug. fix me later.
			System.out.println("[!] Number of ready user: " + BingoGame.getInstance().numberOfReadyUser());
			System.out.println("[!] Number of auth user: " + SenderObject.getInstance().getNumOfAuthenticatedUser());
			
			if(BingoGame.getInstance().numberOfReadyUser() == SenderObject.getInstance().getNumOfAuthenticatedUser()
					&& SenderObject.getInstance().getNumOfAuthenticatedUser() >= 2) {
				response = new Packet(PacketType.READY, "", "");
				BingoGame task = BingoGame.getInstance();
				SenderObject.getInstance().sendTo(clientObject.getHostIP(), response);
				Thread bingoThread = new Thread(task);
				bingoThread.start();
			}
//			response = new Packet(PacketType.READY, SenderObject.getInstance().sendReadyList(), "");
//			SenderObject.getInstance().broadcast(response);
			break;
		case DUPLICATION_CHECK:
			response = null;
			if (UserDAO.getInstance().duplicationCheck(packet.getMainField())) {
				response = new Packet(PacketType.DUPLICATION_CHECK, "true", "");
			} else {
				response = new Packet(PacketType.DUPLICATION_CHECK, "false", "");
			}
			SenderObject.getInstance().sendTo(clientObject.getHostIP(), response);
			break;
		case REQUEST_USER_LIST:
			// get authenticated user from SenderObject
			List<ClientObject> list = SenderObject.getInstance().getAuthenticatedUser();
			// example: bingo,chiho,babo
			String userList = "";	// user list
			String redyList = "";	// ready list

			for (int i = 0; i < list.size(); i++) {
				if (i > 0) {
					userList += ",";
					redyList += ",";
				}
				userList += list.get(i).getNickName() + "(" + (list.get(i).isReady() ? "ready" : "standby") + ")";
				redyList += list.get(i).isReady();
			}
//			System.out.println(userList);
			response = new Packet(PacketType.REQUEST_USER_LIST, userList, redyList);
			SenderObject.getInstance().sendTo(clientObject.getHostIP(), response);
			break;
		case BINGO_COMPLETE:
			response = new Packet(PacketType.WIN, "니가 이깄으", "");
			SenderObject.getInstance().sendTo(clientObject.getHostIP(), response);
			BingoGame.getInstance().removeReadyUser(clientObject);
			SenderObject.getInstance().killAllGamers(BingoGame.getInstance().getReadyUserList());
			break;
		default:
			System.out.println("Wrong Protocol");
			break;
		}
	}

}