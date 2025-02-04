package application;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DefaultMenu{
	public boolean editAccount(String userID, String username, String password, String firstName, String lastName, boolean isManager) throws NumberFormatException, SQLException {
		JDBC conn = new JDBC();
		conn.connectToDB();
		
		if(conn.findID(Integer.valueOf(userID))) {
			ResultSet result = conn.returnAllUsrDetails(userID);
			if(result.next()) {
				String usernameFinal;
				String passwordFinal;
				String firstNameFinal;
				String lastNameFinal;
				String isManagerFinal;
				String isManagerIn;
				if(isManager) {
					isManagerIn = "Yes";
				} else {
					isManagerIn = "No";
				}
				
				if(result.getString("username").equals(username) || username.length() < 1) {
					usernameFinal = result.getString("username");
				} else {
					usernameFinal = username;
				}
				
				if(result.getString("password").equals(password) || password.length() < 1) {
					passwordFinal = result.getString("password");
				} else {
					passwordFinal = password;
				}
				
				if(result.getString("firstName").equals(firstName) || firstName.length() < 1) {
					firstNameFinal = result.getString("firstName");
				} else {
					firstNameFinal = firstName;
				}
				
				if(result.getString("lastName").equals(lastName) || lastName.length() < 1) {
					lastNameFinal = result.getString("lastName");
				} else {
					lastNameFinal = lastName;
				}
				
				if(result.getString("isManager").equals(isManagerIn) || isManagerIn.length() < 1) {
					isManagerFinal = result.getString("isManager");
				} else {
					isManagerFinal = isManagerIn;
				}
				
				if(conn.deleteAccount(Integer.valueOf(userID))) {
					String query = "INSERT INTO Users (userID, username, firstName, lastName, password, IsManager) Values ('" + userID + "', '" + usernameFinal + "', '" + firstNameFinal + "', '" + lastNameFinal + "', '" + passwordFinal + "', '" + isManagerFinal + "');";
					if(conn.addUserToDB(query)) {
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
		} else {
			conn.closeConnectionToDB();
			return false;
		}
	}
	
	public String getID(String username) throws SQLException {
		JDBC conn = new JDBC();
		conn.connectToDB();
		ResultSet result = conn.returnUserID(username);
		String resultStr = result.getString(1);
		conn.closeConnectionToDB();
		return resultStr;
	}
	
	public boolean isNumeric(String value) {
		try {
			Integer.valueOf(value);
			return true;
		} catch(NumberFormatException e) {
			return false;
		}
	}
}