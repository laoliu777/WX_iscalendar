package servlet;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.*;

public class DBUtil {
	//SQL操作语句
	public static PreparedStatement prepare(Connection conn, String sql) {
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
		} catch (SQLException e) {
			System.out.println("ERR IN SQL QUERY!!");
			e.printStackTrace();
		}
		return ps;
	}
	//重载，关闭数据库连接
	public static void close(Connection conn) {
		
		try {
			conn.close();
			conn = null;
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	//重载，关闭SQL语句的链接
	public static void close(Statement stmt) {
		try {
			stmt.close();
			stmt = null;
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	//重载，关闭结果集的链接
	public static void close(ResultSet rs) {
		try {
			rs.close();
			rs = null;
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public static Connection CreateConn() {
		// TODO Auto-generated method stub
		 String url="jdbc:mysql://0.0.0.0:3306/test?characterEncoding=UTF-8&useSSL=false&serverTimezone=Asia/Shanghai";
		 //String url="jdbc:mysql://localhost:3306/calendar?characterEncoding=UTF-8&setUnicode=true&useSSL=false&serverTimezone=Asia/Shanghai";
		 String driverClass="com.mysql.cj.jdbc.Driver";
		 String username="root";
		 String password="0000000";
		 //String password="123456";

		Connection conn = null;
		try {
			Class.forName(driverClass);
			conn = DriverManager.getConnection(url, username, password);
		} catch (ClassNotFoundException e) {
			System.out.println("ERR IN DB CONNECT!");
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}
}
