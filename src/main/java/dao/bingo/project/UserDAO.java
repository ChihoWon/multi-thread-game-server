package dao.bingo.project;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import dbutil.bingo.project.ConnectionFactory;
import dbutil.bingo.project.DBUtil;
import vo.bingo.project.UserVO;

public class UserDAO {

	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;

	private static UserDAO instance = new UserDAO();

	private UserDAO() {
		System.out.println("[!] UserDAO instance is ready.");
	}

	public static UserDAO getInstance() {
		return instance;
	}

	public void insert(UserVO user) {
		conn = ConnectionFactory.getInstance().getConnection();

		try {
			String sql = "insert into user_table(user_id, user_pw) values (?, ?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, user.getUserId());
			pstmt.setString(2, user.getUserPw());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("[!] Unable to insert user data into database.");
		}

		DBUtil.close(pstmt);
		DBUtil.close(conn);
	}

	public ArrayList<UserVO> select() {
		ArrayList<UserVO> list = null;
		conn = ConnectionFactory.getInstance().getConnection();

		try {
			String sql = "select * from user_table order by idx";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				list = new ArrayList<>();
				do {
					UserVO user = new UserVO(rs.getInt("idx"), rs.getString("user_id"), rs.getString("user_pw"),
							rs.getTimestamp("write_date"));
					list.add(user);
				} while (rs.next());
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("[!] Unable to get user list from database.");
		}

		DBUtil.close(rs);
		DBUtil.close(pstmt);
		DBUtil.close(conn);
		return list;
	}

	// true - userId가 db에 존재함
	// false - userId가 db에 존재하지 않음
	public boolean duplicationCheck(String userId) {
		boolean ret = false;

		conn = ConnectionFactory.getInstance().getConnection();

		try {
			String sql = "select * from user_table where user_id = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userId);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				ret = rs.getString("user_id").equals(userId);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("[!] Unable to get user list from database.");
		}

		DBUtil.close(rs);
		DBUtil.close(pstmt);
		DBUtil.close(conn);

		return ret;
	}

	public boolean loginRequest(String userId, String userPw) {
		boolean ret = false;

		conn = ConnectionFactory.getInstance().getConnection();

		try {
			String sql = "select * from user_table where user_id = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userId);
			rs = pstmt.executeQuery();
			
			String id = null;
			String pw = null;

			if (rs.next()) {
				id = rs.getString("user_id");
				pw = rs.getString("user_pw");
				if(id.equals(userId) && pw.equals(userPw)) {
					ret = true;
				}
			}
			

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("[!] Unable to get user list from database.");
		}

		DBUtil.close(rs);
		DBUtil.close(pstmt);
		DBUtil.close(conn);

		return ret;
	}

	public void delete(int idx) {

		conn = ConnectionFactory.getInstance().getConnection();

		try {

			String sql = "delete from user_table where idx = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, idx);
			pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		DBUtil.close(pstmt);
		DBUtil.close(conn);

	}

	public void update(int idx, String password) {
		conn = ConnectionFactory.getInstance().getConnection();

		try {

			String sql = "update user_table set password = ? where idx = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, password);
			pstmt.setInt(2, idx);
			pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("[!] Unable to change password. Please check out database settings.");
		}

		DBUtil.close(pstmt);
		DBUtil.close(conn);
	}

}
