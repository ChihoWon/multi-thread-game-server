package dao.bingo.project;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import dbutil.bingo.project.ConnectionFactory;
import dbutil.bingo.project.DBUtil;
import vo.bingo.project.RankVO;


public class RankDAO {
	
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	
	private static RankDAO instance = new RankDAO();
	private RankDAO() {
		System.out.println("[!] RankDAO instance is ready.");
	}
	public static RankDAO getInstance() {
		return instance;
	}
	
	public void insert(RankVO rank) {
		conn = ConnectionFactory.getInstance().getConnection();
		
		try {
			String sql = "insert into user_rank(user_id, user_time) vlaues (?, ?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, rank.getUserId());
			pstmt.setDouble(2, rank.getUserTime());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("[!] Unable to insert user rank into database.");
		}
	}
	
	public ArrayList<RankVO> select() {
		ArrayList<RankVO> list = null;
		conn = ConnectionFactory.getInstance().getConnection();
		
		try {
			String sql = "select * from user_rank order by idx";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				list = new ArrayList<>();
				do {
					RankVO rank = new RankVO(
							rs.getInt("idx"), 
							rs.getString("user_id"),
							rs.getDouble("user_time"));
					list.add(rank);
				} while(rs.next());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		DBUtil.close(rs);
		DBUtil.close(pstmt);
		DBUtil.close(conn);
		return list;
	}
	
	public void delete(int idx) {
		
		conn = ConnectionFactory.getInstance().getConnection();
		
		try {
			
			String sql = "delete from user_rank where idx = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, idx);
			pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		DBUtil.close(pstmt);
		DBUtil.close(conn);
		
	}
	
	
	public void update(int idx, double timelaps) {
		
		conn = ConnectionFactory.getInstance().getConnection();
		
		try {
			
			String sql = "update user_rank set user_time = ? where idx = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setDouble(1, timelaps);
			pstmt.setInt(2, idx);
			pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("[!] Unable to change timestamp. Please check out datebase settings.");
		}
		
		DBUtil.close(pstmt);
		DBUtil.close(conn);
		
	}
}
