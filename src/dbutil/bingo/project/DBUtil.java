package dbutil.bingo.project;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

// resource management util
public class DBUtil {

	public static void close(Connection conn) {
		try { if(conn != null) conn.close(); } catch (SQLException e) { }
	}
	public static void close(Statement stmt) {
		try { if(stmt != null) stmt.close(); } catch (SQLException e) { }
	}
	public static void close(PreparedStatement pstmt) {
		try { if(pstmt != null) pstmt.close(); } catch (SQLException e) { }
	}
	public static void close(ResultSet rs) {
		try { if(rs != null) rs.close(); } catch (SQLException e) { }
	}

}
