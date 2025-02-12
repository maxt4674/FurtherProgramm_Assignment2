package application;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import application.Objects.ChartData;
import application.Objects.ClientOrders;
import application.Objects.PieGraphData;
import application.Objects.User;
import application.masterData.clientData;
import application.masterData.userData;
import application.transactionData.bookingData;
import application.transactionData.eventData;
import application.transactionData.venuesData;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

//Menu functions class for only manager functions
public class ManagerMenu extends DefaultMenu {
	
	//Returns all users from JDBC and parses them into a list
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
	
	//Deletes a user account
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
	
	//Adds an account based on the params
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
	
	//backs up the master data
	public boolean backupMasterData() throws SQLException {
		ArrayList<userData> users = new ArrayList<userData>();
		ArrayList<clientData> clients = new ArrayList<clientData>();
		masterData data = new masterData();
		JDBC conn = new JDBC();
		conn.connectToDB();
		ResultSet userResults = conn.returnAllUsers();
		while(userResults.next()) {
			userData user = new userData(userResults.getString(1), userResults.getString(2), userResults.getString(3), userResults.getString(4), userResults.getString(5), userResults.getString(6));
			users.add(user);
		}
		
		ResultSet clientResults = conn.returnAllClients();
		while(clientResults.next()) {
			clientData client = new clientData(clientResults.getString(1), clientResults.getString(2));
			clients.add(client);
		}
		
		conn.closeConnectionToDB();
		if(data.backupData(users, clients)) {
			return true;
		} else {
			return false;
		}
	}
	
	//backs up the transaction data
	public boolean backupTransactionData() throws SQLException {
		ArrayList<venuesData> venues = new ArrayList<venuesData>();
		ArrayList<eventData> events = new ArrayList<eventData>();
		ArrayList<bookingData> bookings = new ArrayList<bookingData>();
		transactionData data = new transactionData();
		JDBC conn = new JDBC();
		conn.connectToDB();
		ResultSet venuesResults = conn.returnAllVenues();
		while(venuesResults.next()) {
			venuesData venue = new venuesData(venuesResults.getString(1), venuesResults.getString(2), venuesResults.getString(3), venuesResults.getString(4), venuesResults.getString(5), venuesResults.getString(6));
			venues.add(venue);
		}
		
		ResultSet eventsResults = conn.returnAllEvents();
		while(eventsResults.next()) {
			eventData event = new eventData(eventsResults.getString(1), eventsResults.getString(2), eventsResults.getString(3), eventsResults.getString(4), eventsResults.getString(5), eventsResults.getString(6), eventsResults.getString(7), eventsResults.getString(8), eventsResults.getString(9), eventsResults.getString(10), eventsResults.getString(11));
			events.add(event);
		}
		
		ResultSet bookingsResults = conn.returnAllBookings();
		while(bookingsResults.next()) {
			bookingData booking = new bookingData(bookingsResults.getString(1), bookingsResults.getString(2), bookingsResults.getString(3), bookingsResults.getString(4), bookingsResults.getString(5));
			bookings.add(booking);
		}
		conn.closeConnectionToDB();
		if(data.backupData(venues, events, bookings)) {
			return true;
		} else {
			return false;
		}
	}

	//Imports master data
	public boolean importMasterData(ArrayList<userData> users, ArrayList<clientData> clients) throws SQLException {
		JDBC conn = new JDBC();
		conn.connectToDB();
		if(!conn.importUsersData(users)) {
			conn.closeConnectionToDB();
			return false;
		} 
		
		if(!conn.importClientsData(clients)) {
			conn.closeConnectionToDB();
			return false;
		}
		
		conn.closeConnectionToDB();
		return true;
	}
	
	//Imports transaction data
	public boolean importTransactionData(ArrayList<venuesData> venues, ArrayList<eventData> events, ArrayList<bookingData> bookings) throws SQLException {
		JDBC conn = new JDBC();
		conn.connectToDB();
		if(!conn.importVenuesData(venues)) {
			conn.closeConnectionToDB();
			return false;
		} 
		
		if(!conn.importEventsData(events)) {
			conn.closeConnectionToDB();
			return false;
		}
		
		if(!conn.importBookingsData(bookings)) {
			conn.closeConnectionToDB();
			return false;
		}
		
		conn.closeConnectionToDB();
		return true;
	}
	
	//Returns if the backup file is valid
	public boolean isBackupFileValid(String filename) {
		File myFile = new File("src/Backups/" + filename);
		if(myFile.exists()) {
			return true;
		} else {
			return false;
		}
	}

	//Returns a list of the client order data
	public ObservableList<ClientOrders> returnClientOrders() throws SQLException {
		ObservableList<ClientOrders> orders = FXCollections.observableArrayList();
		JDBC conn = new JDBC();
		conn.connectToDB();
		ResultSet result = conn.returnClientOrders();
		while(result.next()) {
			if(conn.numOfEventsByClient(result.getString(1)) < 2){
				ClientOrders order = new ClientOrders(result.getString(1), result.getString(2));
				orders.add(order);
			} else {
				int editedCommission = Integer.valueOf(result.getString(2));
				editedCommission = editedCommission - (editedCommission / 10);
				ClientOrders order = new ClientOrders(result.getString(1), String.valueOf(editedCommission));
				orders.add(order);
			}

		}
		conn.closeConnectionToDB();
		return orders;
	}

	//Returns a list for the pie graph to use
	public ObservableList<PieGraphData> returnPieGraphData() throws SQLException {
		ObservableList<PieGraphData> data = FXCollections.observableArrayList();
		JDBC conn = new JDBC();
		conn.connectToDB();
		ResultSet result = conn.returnPieGraphData();
		while(result.next()) {
			PieGraphData pie = new PieGraphData(result.getString(1), result.getInt(2));
			data.add(pie);
		}
		conn.closeConnectionToDB();
		return data;
	}

	//Returns a list for the bar chart to use
	public ObservableList<ChartData> returnChartData() throws SQLException {
		ObservableList<ChartData> data = FXCollections.observableArrayList();
		JDBC conn = new JDBC();
		conn.connectToDB();
		ResultSet result = conn.returnBarChartData();
		while(result.next()) {
			if(conn.numOfEventsByClient(conn.returnClientByEvent(result.getString(1))) < 2) {
				ChartData chart = new ChartData(result.getString(1), result.getInt(2), result.getInt(3));
				data.add(chart);
			} else {
				int editedCommission = Integer.valueOf(result.getString(2));
				editedCommission = editedCommission - (editedCommission / 10);
				ChartData chart = new ChartData(result.getString(1), editedCommission, result.getInt(3));
				data.add(chart);
			}
		}
		conn.closeConnectionToDB();
		return data;
	}


	
}