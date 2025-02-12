package application;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import application.Objects.Booking;
import application.Objects.Event;
import application.Objects.ItemDetails;
import application.Objects.Orders;
import application.Objects.Venue;
import application.Objects.VenueMatch;
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
	
	public boolean isValidDate(String date) {
		String[] dateSplit = date.split("-");
		if(dateSplit.length < 3 || dateSplit.length > 3) {
			return false;
		} else if(!isNumeric(dateSplit[0]) || !isNumeric(dateSplit[1]) || !isNumeric(dateSplit[2])) {
			return false;
		} else if(Integer.valueOf(dateSplit[1]) < 1 || Integer.valueOf(dateSplit[1]) > 12) {
			return false;
		} else if(Integer.valueOf(dateSplit[0]) < 1 || Integer.valueOf(dateSplit[0]) > 31) {
			return false;
		} else {
			return true;
		}
	}
	
	public boolean isValidVenueAndEvent(String venue, String event) throws SQLException {
		JDBC conn = new JDBC();
		conn.connectToDB();
		if(conn.isValidEvent(event) && conn.isValidVenue(venue)) {
			conn.closeConnectionToDB();
			return true;
		} else {
			conn.closeConnectionToDB();
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
			if(results.getString(10) != null && !results.getString(10).equals("null")) {
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

	public ObservableList<Booking> returnBookings() throws SQLException {
		ObservableList<Booking> bookings = FXCollections.observableArrayList();
		
		JDBC conn = new JDBC();
		conn.connectToDB();
		ResultSet result = conn.returnBookings();
		while(result.next()) {
			Booking newBooking = new Booking(result.getString(1), result.getString(2), result.getString(3), result.getString(4), result.getString(5), result.getString(6));
			bookings.add(newBooking);
		}
		
		conn.closeConnectionToDB();
		return bookings;
	}
	
	public ArrayList<String> returnAvailableTimes(String venueName, String eventName, String eventDate) throws SQLException {
		ArrayList<String> availableTimes = new ArrayList<String>();
		ArrayList<Integer> availableTimeBinary = new ArrayList<Integer>();
		for(int i = 0; i < 25; i++) {
			availableTimeBinary.add(1);
		}
		availableTimes.add("12am");
		for(int i = 1; i < 12; i++) {
			availableTimes.add(i + "am");
		}
		availableTimes.add("12pm");
		for(int i = 1; i < 12; i++) {
			availableTimes.add(i + "pm");
		}
		JDBC conn = new JDBC();
		conn.connectToDB();
		ResultSet plannedEventDuration = conn.returnEventDuration(eventName);
		int eventDuration = Integer.valueOf(plannedEventDuration.getString(1));
		ResultSet result = conn.returnAllTimesUnavailable(venueName, eventName, eventDate);
		while(result.next()) {
			int duration = Integer.valueOf(result.getString(2));
			String time = result.getString(1);
			boolean hasTimeUnavailable = false;
			int c = 0; 
			for(int i = 0; i < availableTimes.size(); i++) {
				if(time.equals(availableTimes.get(i))) {
					c = i;
					hasTimeUnavailable = true;
				}
			}
			
			if(c - (eventDuration - 1) < 0) {
				c = 0;
			} else {
				c = c - (eventDuration - 1);
			}
			
			if(hasTimeUnavailable) {
				for(int i = 0; i < (duration + eventDuration - 1); i++) {
					availableTimeBinary.set(c, 0);
					c++;
				}
			}
		}
		
		ArrayList<String> availableTimesFinal = new ArrayList<String>();
		for(int i = 0; i < availableTimes.size(); i++) {
			if(availableTimeBinary.get(i) == 1) {
				availableTimesFinal.add(availableTimes.get(i));
			}
		}
		conn.closeConnectionToDB();
		return availableTimesFinal;
	}
	
	public boolean makeBooking(String venueName, String eventName, String eventDate, String bookingTime) throws SQLException {
		JDBC conn = new JDBC();
		conn.connectToDB();
		if(conn.makeBookingDB(venueName, eventName, eventDate, bookingTime)) {
			conn.closeConnectionToDB();
			return true;
		} else {
			conn.closeConnectionToDB();
			return false;
		}
	}
	
	public boolean eventBooked(String eventName) throws SQLException {
		JDBC conn = new JDBC();
		conn.connectToDB();
		if(conn.eventBooked(eventName)) {
			conn.closeConnectionToDB();
			return true;
		} else {
			conn.closeConnectionToDB();
			return false;
		}
	}
	
	public boolean isValidBookingID(String id) throws SQLException {
		JDBC conn = new JDBC();
		conn.connectToDB();
		if(conn.isValidBookingIDDB(id)) {
			conn.closeConnectionToDB();
			return true;
		} else {
			conn.closeConnectionToDB();
			return false;
		}
	}
	
	public boolean deleteBooking(String id) throws SQLException {
		JDBC conn = new JDBC();
		conn.connectToDB();
		if(conn.deleteBooking(id)) {
			conn.closeConnectionToDB();
			return true;
		} else {
			conn.closeConnectionToDB();
			return false;
		}
	}
	
	public boolean isValidVenue(String venue) throws SQLException {
		JDBC conn = new JDBC();
		conn.connectToDB();
		if(conn.isValidVenue(venue)) {
			conn.closeConnectionToDB();
			return true;
		} else {
			conn.closeConnectionToDB();
			return false;
		}
	}
	
	public boolean isValidEvent(String event) throws SQLException {
		JDBC conn = new JDBC();
		conn.connectToDB();
		if(conn.isValidEvent(event)) {
			conn.closeConnectionToDB();
			return true;
		} else {
			conn.closeConnectionToDB();
			return false;
		}
	}
	
	public ArrayList<String> returnBookingOnID(String id) throws SQLException{
		ArrayList<String> returnVals = new ArrayList<String>();
		JDBC conn = new JDBC();
		conn.connectToDB();
		ResultSet result = conn.returnBookingOnIDDB(id);
		if(result.next()) {
			for(int i = 1; i <= 4; i++) {
				returnVals.add(result.getString(i));
			}
		}
		conn.closeConnectionToDB();
		return returnVals;
	}
	
	public boolean eventBookedEdit(String eventName, String bookingID) throws SQLException {
		JDBC conn = new JDBC();
		conn.connectToDB();
		ResultSet result = conn.eventBookedEdit(eventName, bookingID);
		if(result.next()) {
			conn.closeConnectionToDB();
			return true;
		} else {
			conn.closeConnectionToDB();
			return false;
		}
	}
	
	public ArrayList<String> returnAllEventNames() throws SQLException {
		ArrayList<String> events = new ArrayList<String>();
		
		JDBC conn = new JDBC();
		conn.connectToDB();
		ResultSet results = conn.returnAllEventNames();
		while(results.next()) {
			events.add(results.getString(1));
		}
		conn.closeConnectionToDB();
		return events;
	}

	public ObservableList<Orders> returnOrders() throws SQLException {
		ObservableList<Orders> Orders = FXCollections.observableArrayList();
		
		JDBC conn = new JDBC();
		conn.connectToDB();
		ResultSet result = conn.returnOrdersDB();
		while(result.next()) {
			String commission = conn.calculateCommission(result.getString(5), result.getString(4)) + "";
			Orders newOrder = new Orders(result.getString(1), result.getString(2), result.getString(3), result.getString(4), result.getString(5), result.getString(6), result.getString(7), result.getString(8), commission);
			Orders.add(newOrder);
		}
		
		conn.closeConnectionToDB();
		return Orders;
	}
	
	public ObservableList<VenueMatch> returnAutoMatchScores(String eventName) throws SQLException {
		ObservableList<VenueMatch> autoMatchList = FXCollections.observableArrayList();
		JDBC conn = new JDBC();
		conn.connectToDB();
		ResultSet result = conn.returnAutoMatch(eventName, 1);
		if(result != null) {
			while(result.next()) {
				VenueMatch newAuto = new VenueMatch(result.getString(1), result.getString(2), result.getString(3), result.getString(4), "100");
				autoMatchList.add(newAuto);
			}
		}

		result = conn.returnAutoMatch(eventName, 2);
		if(result != null) {
			while(result.next()) {
				VenueMatch newAuto = new VenueMatch(result.getString(1), result.getString(2), result.getString(3), result.getString(4), "75");
				autoMatchList.add(newAuto);
			}
		}
		
		result = conn.returnAutoMatch(eventName, 3);
		if(result != null) {
			while(result.next()) {
				VenueMatch newAuto = new VenueMatch(result.getString(1), result.getString(2), result.getString(3), result.getString(4), "50");
				autoMatchList.add(newAuto);
			}
		}
		
		result = conn.returnAutoMatch(eventName, 4);
		if(result != null) {
			while(result.next()) {
				VenueMatch newAuto = new VenueMatch(result.getString(1), result.getString(2), result.getString(3), result.getString(4), "25");
				autoMatchList.add(newAuto);
			}
		}
		
		result = conn.returnAutoMatch(eventName, 5);
		if(result != null) {
			while(result.next()) {
				VenueMatch newAuto = new VenueMatch(result.getString(1), result.getString(2), result.getString(3), result.getString(4), "0");
				autoMatchList.add(newAuto);
			}
		}
		conn.closeConnectionToDB();
		return autoMatchList;
	}
	
	public String getEventDate(String eventName) throws SQLException {
		String eventDate = "";
		JDBC conn = new JDBC();
		conn.connectToDB();
		eventDate = conn.findEventDate(eventName);
		conn.closeConnectionToDB();
		return eventDate;
	}
}