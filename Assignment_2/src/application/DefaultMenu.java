package application;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

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
	
	public ObservableList<Venue> returnVenues(String category, String name) throws SQLException {
		ObservableList<Venue> venues = FXCollections.observableArrayList();
		JDBC conn = new JDBC();
		conn.connectToDB();
		ResultSet result = null;
		if(name.length() < 1 && category.length() < 1) {
			//If no params are in use
			result = conn.venueReturnDB("", "");
		} else if(name.length() < 1 && category.length() > 0) {
			//If only category param
			result = conn.venueReturnDB("", category);
		} else if(name.length() > 0 && category.length() < 1) {
			//If only name param
			result = conn.venueReturnDB(name, "");
		} else if(name.length() > 0 && category.length() > 0) {
			//If both search param are in use
			result = conn.venueReturnDB(name, category);
		}
		
		while(result.next()) {
			Venue inputVenue = new Venue(result.getString(1), result.getString(2), result.getString(3), result.getString(4), result.getString(5), result.getString(6));
			venues.add(inputVenue);
		}
		
		conn.closeConnectionToDB();
		return venues;
	}
	
	public ArrayList<String> returnVenueCategories() throws SQLException{
		ArrayList<String> categories = new ArrayList<String>();
		JDBC conn = new JDBC();
		conn.connectToDB();
		ResultSet result = conn.returnCategories();
		while(result.next()) {
			categories.add(result.getString(1));
		}
		
		conn.closeConnectionToDB();
		return categories;
	}
	
	public ObservableList<ItemDetails> returnVenueDetailed(String venueName) throws SQLException {
		ObservableList<ItemDetails> venueDetails = FXCollections.observableArrayList();
		ArrayList<String> listOfItems = new ArrayList<String>();
		listOfItems.add("Capacity:");
		listOfItems.add("Suitable For:");
		listOfItems.add("Category:");
		listOfItems.add("Booking Price:");
		JDBC conn = new JDBC();
		conn.connectToDB();
		
		ResultSet result = conn.returnVenueDetails(venueName);
		if(result.next()) {
			for(int i = 0; i < 4; i++) {
				ItemDetails venue = new ItemDetails(listOfItems.get(i), result.getString(i+1));
				venueDetails.add(venue);
			}
		}
		
		conn.closeConnectionToDB();
		return venueDetails;
	}
	
	public ObservableList<Event> returnEvents() throws SQLException{
		ObservableList<Event> events = FXCollections.observableArrayList();
		
		JDBC conn = new JDBC();
		conn.connectToDB();
		ResultSet results = conn.returnEvents();
		while(results.next()) {
			String hasBooking = "No";
			if(results.getString(10) != null) {
				hasBooking = "Yes";
			}
			Event newEvent = new Event(results.getString(1), results.getString(2), results.getString(3), results.getString(4), results.getString(5), results.getString(6), results.getString(7), results.getString(8), results.getString(9), hasBooking, results.getString(11));
			events.add(newEvent);
		}
		conn.closeConnectionToDB();
		
		return events;
	}
	
	public ObservableList<ItemDetails> returnEventDetailed(String eventName) throws SQLException {
		ObservableList<ItemDetails> venueDetails = FXCollections.observableArrayList();
		ArrayList<String> listOfItems = new ArrayList<String>();
		listOfItems.add("Artist:");
		listOfItems.add("Date:");
		listOfItems.add("Time:");
		listOfItems.add("Duration:");
		listOfItems.add("Target Audience:");
		listOfItems.add("Type:");
		listOfItems.add("Category:");
		listOfItems.add("Client Name:");
		JDBC conn = new JDBC();
		conn.connectToDB();
		
		ResultSet result = conn.returnEventDetails(eventName);
		if(result.next()) {
			for(int i = 0; i < 8; i++) {
				ItemDetails venue = new ItemDetails(listOfItems.get(i), result.getString(i+1));
				venueDetails.add(venue);
			}
		}
		
		conn.closeConnectionToDB();
		return venueDetails;
	}
}