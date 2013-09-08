import java.sql.*;

public class DatabaseConnector {

	private static final String dburl = "jdbc:mysql://purdueinhand.heliohost.org/purduein_main";
	private Connection con;
	private boolean isConnected = false;

	public DatabaseConnector() {}

	private boolean Connect() {
		if(isConnected) {
			return true;
		}

		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection(dburl, "cs408_server", "cs408");
			isConnected = true;
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private void Disconnect() {
		if(!isConnected) {
			return;
		}
		try {
			con.close();
			isConnected = false;
		} catch(Exception e) {}
	}

	public boolean addUser(String name, String password) {

	}

	public boolean isValidLogin(String name, String password) {

	}

	public boolean updateUserData(String slotNo, String gameProcess) {

	}

	public String getUserData(String name) {

	}
}