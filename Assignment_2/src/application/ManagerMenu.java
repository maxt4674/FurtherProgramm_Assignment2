package application;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import application.Objects.ClientOrders;
import application.Objects.User;
import application.Objects.masterData;
import application.Objects.transactionData;
import application.Objects.masterData.clientData;
import application.Objects.masterData.userData;
import application.Objects.transactionData.bookingData;
import application.Objects.transactionData.eventData;
import application.Objects.transactionData.venuesData;
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
	
	public boolean isBackupFileValid(String filename) {
		File myFile = new File("src/Backups/" + filename);
		if(myFile.exists()) {
			return true;
		} else {
			return false;
		}
	}

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


	
}