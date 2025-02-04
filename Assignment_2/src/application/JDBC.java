package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class JDBC {
	private Connection connection = null;
	
	public void connectToDB() throws SQLException {
		connection = DriverManager.getConnection("jdbc:sqlite:Database/Assignment2DB.db");
	}
	
	public void closeConnectionToDB() throws SQLException {
		if(connection != null) {
			connection.close();
		}
		connection = null;
	}
	
	public boolean isValidCredentials(String username, String password) throws SQLException {
		Statement usrValidation = connection.createStatement();
		ResultSet results = usrValidation.executeQuery("SELECT * FROM Users WHERE Username = '" + username + "' AND Password = '"+ password +"'");
		if(results.next()) {
			return true;
		}
		
		return false;
	}
	
	public boolean isManager(String username) throws SQLException {
		Statement managerValidation = connection.createStatement();
		
		ResultSet results = managerValidation.executeQuery("SELECT IsManager FROM Users WHERE Username = '" + username + "'");
		
		if(results.next()) {
			if(results.getString(1).equals("Yes")) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}
	
	public String returnName(String username) throws SQLException {
		String firstAndLast = "";
		Statement nameRetrieval = connection.createStatement();
		ResultSet firstName = nameRetrieval.executeQuery("SELECT firstName FROM Users WHERE Username = '" + username + "'");
		firstAndLast += firstName.getString(1) + " ";
		ResultSet lastName = nameRetrieval.executeQuery("SELECT lastName FROM Users WHERE Username = '" + username + "'");
		firstAndLast += lastName.getString(1);
		return firstAndLast;
	}
	
	public ResultSet usrReturnDB() throws SQLException{
		Statement userRetrieval = connection.createStatement();
		ResultSet results = userRetrieval.executeQuery("SELECT userID, username, firstName, lastName, IsManager FROM Users");
		
		return results;
		
	}
	
	public boolean addUserToDB(String query) throws SQLException {
		Statement addUser = connection.createStatement();
		int returnVal = 0;
		try {
			returnVal = addUser.executeUpdate(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			returnVal = 3;
			e.printStackTrace();
		}
		
		if(returnVal != 1 && returnVal != 0) {
			return false;
		} else {
			return true;
		}
	}
	
	public int validID() throws SQLException {
		Statement findID = connection.createStatement();
		
		int id = 1;
		boolean isFound = false;
		
		while(!isFound) {
			ResultSet results = findID.executeQuery("SELECT * FROM Users WHERE userID = '" + id + "'");
			if(!results.next()) {
				isFound = true;
				break;
			}
			id++;
		}
		
		return id;
	}
	
	public boolean findID(int id) throws SQLException {
		Statement findID = connection.createStatement();
		
		ResultSet result = findID.executeQuery("SELECT * FROM Users WHERE userID = '" + id + "';");
		
		if(result.next()) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean deleteAccount(int id) {
		Statement delAccount;
		try {
			delAccount = connection.createStatement();
			delAccount.execute("DELETE FROM Users WHERE userID = '" + id + "';");
			return true;
		} catch (SQLException e) {
			return false;
		}
	}
	
	public ResultSet returnAllUsrDetails(String id) throws SQLException {
		Statement userRetrieval = connection.createStatement();
		ResultSet results = userRetrieval.executeQuery("SELECT username, firstName, lastName, password, IsManager FROM Users WHERE userID = '" + id + "'");
		
		return results;
	}
	
	public ResultSet returnUserID(String username) throws SQLException {
		Statement userRetrieval = connection.createStatement();
		ResultSet result = userRetrieval.executeQuery("SELECT userID FROM Users WHERE username = '" + username + "';");
		
		return result;
	}
	
	public ResultSet returnUserDetails(String username) throws SQLException {
		Statement userRetrieval = connection.createStatement();
		ResultSet result = userRetrieval.executeQuery("SELECT password, firstName, lastName FROM Users WHERE username = '"+username+"';");
		return result;
	}
	
	public boolean importRequestDataCSV(ArrayList<ArrayList<String>> requestData) {
		Statement requestDataImport;
		try {
			requestDataImport = connection.createStatement();
			ResultSet result = requestDataImport.executeQuery("SELECT * FROM Events;");
			if(result.next()) {
				requestDataImport.execute("DELETE FROM Events;");
			}

			ArrayList<String> listOfClients = new ArrayList<String>();
			
			for(int i = 0; i < requestData.size(); i++) {
				listOfClients.add(requestData.get(i).get(0));
			}
			
			editClientList(listOfClients);
			
			for(int i = 0; i < requestData.size(); i++) {
				int ClientID = returnClientID(requestData.get(i).get(0));
				int EventID = i + 1;
				requestDataImport.execute("INSERT INTO Events (EventID, Title, Artist, Date, Time, Duration, Target_Audience, Type, Category, ClientID) VALUES ('" + EventID + "','" + requestData.get(i).get(1) + "','" + requestData.get(i).get(2) + "','"+requestData.get(i).get(3)+"','"+requestData.get(i).get(4)+"','"+requestData.get(i).get(5)+"','"+requestData.get(i).get(6)+"','"+requestData.get(i).get(7)+"','"+requestData.get(i).get(8)+"','" + ClientID + "');");
			}
			
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	public void editClientList(ArrayList<String> clients) throws SQLException {
		Statement clientsRequest = connection.createStatement();
		for(int i = 0; i < clients.size(); i++) {
			ResultSet result = clientsRequest.executeQuery("SELECT * FROM Clients WHERE ClientName = '" + clients.get(i) + "';");
			
			if(!result.next()) {
				int idNum = sizeOfClientsTable();
				clientsRequest.execute("INSERT INTO Clients (ClientID, ClientName) VALUES ('" + idNum + "','" + clients.get(i) + "');");
			}
		}
	}
	
	public int sizeOfClientsTable() throws SQLException {
		Statement clients = connection.createStatement();
		ResultSet results = clients.executeQuery("SELECT * FROM Clients");
		int c = 1;
		
		while(results.next()) {
			c++;
		}
		
		return c;
	}
	
	public int returnClientID(String clientName) throws SQLException {
		Statement client = connection.createStatement();
		
		ResultSet result = client.executeQuery("SELECT ClientID FROM Clients WHERE ClientName = '" + clientName + "'");
		
		int clientID = result.getInt(1);
		
		return clientID;
	}
}