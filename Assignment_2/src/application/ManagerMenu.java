package application;

import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ManagerMenu extends DefaultMenu {
	
	public ObservableList<User> returnUsers() throws SQLException{
		ObservableList<User> users = FXCollections.observableArrayList();
		
		JDBC conn = new JDBC();
		conn.connectToDB();
		
		ResultSet usrData = conn.usrReturnDB();
		
		while(usrData.next()) {
			User inputtedUser = new User(usrData.getString("userID"), usrData.getString("username"), usrData.getString("firstName"), usrData.getString("lastName"), usrData.getString("IsManager"));
			users.add(inputtedUser);
		}
		
		conn.closeConnectionToDB();
		return users;
	}
	
	public boolean delAccount(String userID) throws SQLException {
		JDBC conn = new JDBC();
		conn.connectToDB();
		
		if(isNumeric(userID)) {
			if(conn.findID(Integer.valueOf(userID))) {
				if(conn.deleteAccount(Integer.valueOf(userID))) {
					conn.closeConnectionToDB();
					return true;
				} else {
					conn.closeConnectionToDB();
					return false;
				}
			} else {
				conn.closeConnectionToDB();
				return false;
			}
		} else {
			conn.closeConnectionToDB();
			return false;
		}
	}
	
	public boolean addAccount(String username, String password, String firstName, String lastName, boolean isManager) throws SQLException {
		JDBC conn = new JDBC();
		conn.connectToDB();
		int id = conn.validID();
		String manager = "";
		if(isManager) {
			manager = "Yes";
		} else {
			manager = "No";
		}
		String query = "INSERT INTO Users (userID, username, firstName, lastName, password, IsManager) Values ('" + id + "', '" + username + "', '" + firstName + "', '" + lastName + "', '" + password + "', '" + manager + "');";
		if(conn.addUserToDB(query)) {
			conn.closeConnectionToDB();
			return true;
		} else {
			conn.closeConnectionToDB();
			return false;
		}
	}
	
}