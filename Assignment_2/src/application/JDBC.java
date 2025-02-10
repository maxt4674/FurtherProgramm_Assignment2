package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import application.Objects.masterData.clientData;
import application.Objects.masterData.userData;
import application.Objects.transactionData.bookingData;
import application.Objects.transactionData.eventData;
import application.Objects.transactionData.venuesData;

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
	
	public boolean importVenueDataCSV(ArrayList<ArrayList<String>> venueData) {
		Statement venueDataImport;
		try {
			venueDataImport = connection.createStatement();
			venueDataImport.execute("DELETE FROM Venues;");
			
			for(int i = 0; i < venueData.size(); i++) {
				int VenueID = i + 1;
				venueDataImport.execute("INSERT INTO Venues (VenueID, Name, Capacity, Suitable_For, Category, Booking_Price) VALUES ('" + VenueID + "','" + venueData.get(i).get(0) + "','" + venueData.get(i).get(1) + "','"+venueData.get(i).get(2)+"','"+venueData.get(i).get(3)+"','"+venueData.get(i).get(4)+"');");
			}
			
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public ResultSet venueReturnDB(String name, String category) throws SQLException {
		Statement venueReturn = connection.createStatement();
		ResultSet venues = null;
		
		if(name.length() < 1 && category.length() < 1) {
			venues = venueReturn.executeQuery("SELECT * FROM Venues;");
		} else if(name.length() < 1 && category.length() > 0) {
			venues = venueReturn.executeQuery("SELECT * FROM Venues WHERE Category = '"+category+"';");
		} else if(name.length() > 0 && category.length() < 1) {
			venues = venueReturn.executeQuery("SELECT * FROM Venues WHERE Name LIKE '%"+name+"%';");
		} else if(name.length() > 0 && category.length() > 0) {
			venues = venueReturn.executeQuery("SELECT * FROM Venues WHERE Category = '"+category+"' AND Name LIKE '%"+name+"%';");
		}
		
		return venues;
	}
	
	public ResultSet returnCategories() throws SQLException {
		Statement categoryReturn = connection.createStatement();
		ResultSet categories = categoryReturn.executeQuery("SELECT DISTINCT Category FROM Venues;");
		return categories;
	}
	
	public ResultSet returnVenueDetails(String name) throws SQLException {
		Statement venueDetReturn = connection.createStatement();
		ResultSet venueDetails = venueDetReturn.executeQuery("SELECT Capacity, Suitable_For, Category, Booking_Price FROM Venues WHERE Name = '"+name+"'");
		return venueDetails;
	}
	
	public ResultSet returnEvents() throws SQLException {
		Statement eventReturn = connection.createStatement();
		ResultSet events = eventReturn.executeQuery("SELECT Events.EventID, Events.Title, Events.Artist, Events.Date, Events.Time, Events.Duration, Events.Target_Audience, Events.Type, Events.Category, Events.BookingID, Clients.ClientName FROM Events INNER JOIN Clients ON Events.ClientID = Clients.ClientID");
		return events;
	}
	
	public ResultSet returnEventDetails(String name) throws SQLException {
		Statement eventDetReturn = connection.createStatement();
		ResultSet eventDetails = eventDetReturn.executeQuery("SELECT Events.Artist, Events.Date, Events.Time, Events.Duration, Events.Target_Audience, Events.Type, Events.Category, Clients.ClientName FROM Events INNER JOIN Clients ON Events.ClientID = Clients.ClientID WHERE Events.Title = '"+name+"'");
		return eventDetails;
	}

	public ResultSet returnBookings() throws SQLException {
		Statement bookingsReturn = connection.createStatement();
		ResultSet bookings = bookingsReturn.executeQuery("SELECT Bookings.BookingID, Events.Title, Venues.Name, Bookings.Date, Bookings.Time, Events.Duration FROM Bookings INNER JOIN Events ON Events.EventID = Bookings.EventID INNER JOIN Venues ON Venues.VenueID = Bookings.VenueID;");
		return bookings;
	}
	
	public boolean isValidEvent(String event) throws SQLException {
		Statement eventReturn = connection.createStatement();
		ResultSet eventR = eventReturn.executeQuery("SELECT * FROM Events WHERE Title = '"+event+"';");
		if(eventR.next()) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isValidVenue(String venue) throws SQLException {
		Statement venueReturn = connection.createStatement();
		ResultSet venueR = venueReturn.executeQuery("SELECT * FROM Venues WHERE Name = '"+venue+"';");
		if(venueR.next()) {
			return true;
		} else {
			return false;
		}
	}

	public ResultSet returnAllTimesUnavailable(String venueName, String eventName, String eventDate) throws SQLException {
		Statement dateReturn = connection.createStatement();
		ResultSet result = dateReturn.executeQuery("SELECT Bookings.Time, Events.Duration FROM Bookings INNER JOIN Events ON Events.EventID = Bookings.EventID INNER JOIN Venues ON Venues.VenueID = Bookings.VenueID WHERE Venues.Name = '"+venueName+"' AND Bookings.Date = '"+eventDate+"';");
		return result;
	}

	public boolean makeBookingDB(String venueName, String eventName, String eventDate, String bookingTime) throws SQLException {
		Statement makeBooking = connection.createStatement();
		int VenueID;
		int EventID;
		ResultSet result = makeBooking.executeQuery("SELECT VenueID FROM Venues WHERE Name = '"+venueName+"';");
		if(result.next()) {
			VenueID = Integer.valueOf(result.getString(1));
		} else {
			return false;
		}
		
		result = null;
		result = makeBooking.executeQuery("SELECT EventID FROM Events WHERE Title = '"+eventName+"';");
		if(result.next()) {
			EventID = Integer.valueOf(result.getString(1));
		} else {
			return false;
		}
		
		int BookingID = validBookingID();
		try {
			makeBooking.execute("INSERT INTO Bookings (BookingID, Date, Time, VenueID, EventID) VALUES ('"+BookingID+"','"+eventDate+"','"+bookingTime+"','"+VenueID+"','"+EventID+"');");
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		

		try {
			makeBooking.execute("UPDATE Events SET BookingID = '"+BookingID+"' WHERE EventID = '"+EventID+"';");
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public ResultSet returnEventDuration(String eventName) throws SQLException {
		Statement findDuration = connection.createStatement();
		ResultSet result = findDuration.executeQuery("SELECT Duration FROM Events WHERE Title = '"+eventName+"';");
		return result;
	}
	
	public int validBookingID() throws SQLException {
		Statement findID = connection.createStatement();
		
		int id = 1;
		boolean isFound = false;
		
		while(!isFound) {
			ResultSet results = findID.executeQuery("SELECT * FROM Bookings WHERE BookingID = '" + id + "'");
			if(!results.next()) {
				isFound = true;
				break;
			}
			id++;
		}
		
		return id;
	}

	public boolean eventBooked(String eventName) throws SQLException {
		Statement eventBooked = connection.createStatement();
		
		ResultSet result = eventBooked.executeQuery("SELECT BookingID FROM Events WHERE Title = '"+eventName+"';");
		if(result.getString(1) == null) {
			return false;
		} else {
			return true;
		}
	}

	public boolean isValidBookingIDDB(String id) throws SQLException {
		Statement bID = connection.createStatement();
		
		ResultSet result = bID.executeQuery("SELECT * FROM Bookings WHERE BookingID = '"+id+"'");
		if(result.next()) {
			return true;
		} else {
			return false;
		}
	}

	public boolean deleteBooking(String id) throws SQLException {
		Statement delBooking;
		try {
			delBooking = connection.createStatement();
			delBooking.execute("UPDATE Events SET BookingID = NULL WHERE BookingID = '"+id+"';");
			delBooking.execute("DELETE FROM Bookings WHERE BookingID = '" + id + "';");
			return true;
		} catch (SQLException e) {
			return false;
		}
	}

	public ResultSet returnBookingOnIDDB(String id) throws SQLException {
		Statement returnBooking = connection.createStatement();
		ResultSet result = returnBooking.executeQuery("SELECT Venues.Name, Events.Title, Bookings.Date, Bookings.Time FROM Bookings INNER JOIN Venues ON Venues.VenueID = Bookings.VenueID INNER JOIN Events ON Events.EventID = Bookings.EventID WHERE Bookings.BookingID = '"+id+"';");
		return result;
	}

	public ResultSet eventBookedEdit(String eventName, String bookingID) throws SQLException {
		Statement eventBooked = connection.createStatement();
		
		ResultSet result = eventBooked.executeQuery("SELECT BookingID FROM Events WHERE Title = '"+eventName+"' AND BookingID <> '"+bookingID+"';");
		return result;
	}

	public ResultSet returnAllEventNames() throws SQLException {
		Statement events = connection.createStatement();
		
		ResultSet result = events.executeQuery("SELECT Title FROM Events;");
		return result;
	}

	public ResultSet returnOrdersDB() throws SQLException {
		Statement orders = connection.createStatement();
		ResultSet result = orders.executeQuery("SELECT Bookings.BookingID, Bookings.VenueID, Bookings.EventID, Venues.Name, Events.Title, Clients.ClientName, Bookings.Date, Bookings.time FROM Bookings INNER JOIN Events ON Events.EventID = Bookings.EventID INNER JOIN Clients ON Events.ClientID = Clients.ClientID INNER JOIN Venues ON Venues.VenueID = Bookings.VenueID;");
		return result;
	}
	
	public int calculateCommission(String eventName, String venueName) throws SQLException {
		Statement comm = connection.createStatement();
		ResultSet result = comm.executeQuery("SELECT Venues.Booking_Price, Events.Duration, Clients.ClientName FROM Bookings INNER JOIN Venues ON Venues.VenueID = Bookings.VenueID INNER JOIN Events ON Events.EventID = Bookings.EventID INNER JOIN Clients ON Events.ClientID = Clients.ClientID WHERE Events.Title = '"+eventName+"' AND Venues.Name = '"+venueName+"';");
		if(result.next()) {
			int bookingPrice = Integer.valueOf(result.getString(1));
			int duration = Integer.valueOf(result.getString(2));
			String clientName = result.getString(3);
			int total = bookingPrice * duration;
			int commission = 0;
			
			result = comm.executeQuery("SELECT COUNT(Clients.ClientName) FROM Bookings INNER JOIN Events ON Events.EventID = Bookings.EventID INNER JOIN Clients ON Clients.ClientID = Events.ClientID WHERE Clients.ClientName = '"+clientName+"';");
			if(result.next()) {
				if(Integer.valueOf(result.getString(1)) > 1) {
					commission = (int) (total * 0.09);
				} else {
					commission = (int) (total * 0.1);
				}
				return commission;
			}
		}
		
		return 0;
	}

	public ResultSet returnAutoMatch(String eventName, int stage) throws SQLException {
		Statement auto = connection.createStatement();
		ResultSet result = null;
		ResultSet eventResult = auto.executeQuery("SELECT Target_Audience, Category, Type, Date, Time FROM Events WHERE Title = '"+eventName+"'");
		String eventAudience = eventResult.getString(1);
		String eventCategory = eventResult.getString(2);
		String eventType = eventResult.getString(3);
		String eventDate = eventResult.getString(4);
		String eventTime = eventResult.getString(5);
		ResultSet targetAudienceResult = auto.executeQuery("SELECT VenueID FROM Venues WHERE Capacity < "+eventAudience+";");
		ArrayList<String> targetAudience = new ArrayList<String>();
		while(targetAudienceResult.next()) {
			targetAudience.add(targetAudienceResult.getString(1));
		}
		
		ResultSet categoryResult = auto.executeQuery("SELECT VenueID FROM Venues WHERE Category <> '"+eventCategory+"';");
		ArrayList<String> category = new ArrayList<String>();
		while(categoryResult.next()) {
			category.add(categoryResult.getString(1));
		}
		
		ResultSet typeResult = auto.executeQuery("SELECT VenueID FROM Venues WHERE Suitable_For NOT LIKE '%"+eventType+"%';");
		ArrayList<String> type = new ArrayList<String>();
		while(typeResult.next()) {
			type.add(typeResult.getString(1));
		}
		
		
		ResultSet dateResult = auto.executeQuery("SELECT Venues.VenueID FROM Venues INNER JOIN Bookings ON Bookings.VenueID = Venues.VenueID AND Bookings.Date = '"+eventDate+"' AND Bookings.Time = '"+eventTime+"';");
		ArrayList<String> date = new ArrayList<String>();
		while(dateResult.next()) {
			date.add(dateResult.getString(1));
		}
		
		ResultSet allVenuesResult = auto.executeQuery("SELECT VenueID FROM Venues;");
		ArrayList<Integer> intScore = new ArrayList<Integer>();
		ArrayList<String> allVenues = new ArrayList<String>();
		while(allVenuesResult.next()) {
			allVenues.add(allVenuesResult.getString(1));
		}
		
		for(int i = 0; i < allVenues.size(); i++) {
			int score = 4;
			for(int j = 0; j < date.size(); j++) {
				if(allVenues.get(i).equals(date.get(j))) {
					score -= 1;
				}
			}
			
			for(int j = 0; j < targetAudience.size(); j++) {
				if(allVenues.get(i).equals(targetAudience.get(j))) {
					score -= 1;
				}
			}
			
			for(int j = 0; j < category.size(); j++) {
				if(allVenues.get(i).equals(category.get(j))) {
					score -= 1;
				}
			}
			
			for(int j = 0; j < type.size(); j++) {
				if(allVenues.get(i).equals(type.get(j))) {
					score -= 1;
				}
			}
			
			intScore.add(score);
		}
		
		if(stage == 1) {
			ArrayList<Integer> locations = new ArrayList<Integer>();
			for(int i = 0; i < intScore.size(); i++) {
				if(intScore.get(i) == 4) {
					locations.add(i);
				}
			}
			
			if(locations.size() > 0) {
				String query = "SELECT Name, Category, Capacity, Suitable_For FROM Venues WHERE VenueID IN (";
				for(int i = 0; i < locations.size(); i++) {
					query += allVenues.get(locations.get(i));
					if(i < locations.size() - 1) {
						query += ",";
					}
				}
				query += ");";
				result = auto.executeQuery(query);
			} else {
				result = null;
			}
		} else if(stage == 2) {
			ArrayList<Integer> locations = new ArrayList<Integer>();
			for(int i = 0; i < intScore.size(); i++) {
				if(intScore.get(i) == 3) {
					locations.add(i);
				}
			}
			
			if(locations.size() > 0) {
				String query = "SELECT Name, Category, Capacity, Suitable_For FROM Venues WHERE VenueID IN (";
				for(int i = 0; i < locations.size(); i++) {
					query += allVenues.get(locations.get(i));
					if(i < locations.size() - 1) {
						query += ",";
					}
				}
				query += ");";
				result = auto.executeQuery(query);
			} else {
				result = null;
			}
		} else if(stage == 3) {
			ArrayList<Integer> locations = new ArrayList<Integer>();
			for(int i = 0; i < intScore.size(); i++) {
				if(intScore.get(i) == 2) {
					locations.add(i);
				}
			}
			
			if(locations.size() > 0) {
				String query = "SELECT Name, Category, Capacity, Suitable_For FROM Venues WHERE VenueID IN (";
				for(int i = 0; i < locations.size(); i++) {
					query += allVenues.get(locations.get(i));
					if(i < locations.size() - 1) {
						query += ",";
					}
				}
				query += ");";
				result = auto.executeQuery(query);
			} else {
				result = null;
			}
		} else if(stage == 4) {
			ArrayList<Integer> locations = new ArrayList<Integer>();
			for(int i = 0; i < intScore.size(); i++) {
				if(intScore.get(i) == 1) {
					locations.add(i);
				}
			}
			
			if(locations.size() > 0) {
				String query = "SELECT Name, Category, Capacity, Suitable_For FROM Venues WHERE VenueID IN (";
				for(int i = 0; i < locations.size(); i++) {
					query += allVenues.get(locations.get(i));
					if(i < locations.size() - 1) {
						query += ",";
					}
				}
				query += ");";
				result = auto.executeQuery(query);
			} else {
				result = null;
			}
		} else if(stage == 5) {
			ArrayList<Integer> locations = new ArrayList<Integer>();
			for(int i = 0; i < intScore.size(); i++) {
				if(intScore.get(i) == 0) {
					locations.add(i);
				}
			}
			
			if(locations.size() > 0) {
				String query = "SELECT Name, Category, Capacity, Suitable_For FROM Venues WHERE VenueID IN (";
				for(int i = 0; i < locations.size(); i++) {
					query += allVenues.get(locations.get(i));
					if(i < locations.size() - 1) {
						query += ",";
					}
				}
				query += ");";
				result = auto.executeQuery(query);
			} else {
				result = null;
			}
		}
		
		return result;
	}

	public String findEventDate(String eventName) throws SQLException {
		Statement conn = connection.createStatement();
		ResultSet result = conn.executeQuery("SELECT Date FROM Events WHERE Title = '"+eventName+"'");
		String date = result.getString(1);
		return date;
	}

	public ResultSet returnAllUsers() throws SQLException {
		Statement conn = connection.createStatement();
		ResultSet result = conn.executeQuery("SELECT * FROM Users;");
		return result;
	}

	public ResultSet returnAllClients() throws SQLException {
		Statement conn = connection.createStatement();
		ResultSet result = conn.executeQuery("SELECT * FROM Clients;");
		return result;
	}

	public ResultSet returnAllVenues() throws SQLException {
		Statement conn = connection.createStatement();
		ResultSet result = conn.executeQuery("SELECT * FROM Venues;");
		return result;
	}

	public ResultSet returnAllEvents() throws SQLException {
		Statement conn = connection.createStatement();
		ResultSet result = conn.executeQuery("SELECT * FROM Events;");
		return result;
	}

	public ResultSet returnAllBookings() throws SQLException {
		Statement conn = connection.createStatement();
		ResultSet result = conn.executeQuery("SELECT * FROM Bookings;");
		return result;
	}

	public boolean importUsersData(ArrayList<userData> users) {
		try {
			Statement stmt = connection.createStatement();
			stmt.execute("DELETE FROM Users;");
			for(int i = 0; i < users.size(); i++) {
				stmt.execute("INSERT INTO Users (userID, username, firstName, lastName, password, IsManager) Values ('" + users.get(i).getUserID() + "', '" + users.get(i).getUsername() + "', '" + users.get(i).getFirstName() + "', '" + users.get(i).getLastName() + "', '" + users.get(i).getPassword() + "', '" + users.get(i).getIsManager() + "');");
			}
			return true;
		} catch (SQLException e) {
			return false;
		}
	}

	public boolean importClientsData(ArrayList<clientData> clients) {
		try {
			Statement stmt = connection.createStatement();
			stmt.execute("DELETE FROM Clients;");
			for(int i = 0; i < clients.size(); i++) {
				stmt.execute("INSERT INTO Clients (ClientID, ClientName) VALUES ('" + clients.get(i).getClientID() + "','" + clients.get(i).getClientName() + "');");
			}
			return true;
		} catch (SQLException e) {
			return false;
		}
	}

	public boolean importVenuesData(ArrayList<venuesData> venues) {
		try {
			Statement stmt = connection.createStatement();
			stmt.execute("DELETE FROM Venues;");
			for(int i = 0; i < venues.size(); i++) {
				stmt.execute("INSERT INTO Venues (VenueID, Name, Capacity, Suitable_For, Category, Booking_Price) VALUES ('" + venues.get(i).getVenueID() + "','" + venues.get(i).getName() + "','" + venues.get(i).getCapacity() + "','"+venues.get(i).getSuitable_For()+"','"+venues.get(i).getCategory()+"','"+venues.get(i).getBooking_Price()+"');");
			}
			return true;
		} catch (SQLException e) {
			return false;
		}
	}

	public boolean importEventsData(ArrayList<eventData> events) {
		try {
			Statement stmt = connection.createStatement();
			stmt.execute("DELETE FROM Events;");
			for(int i = 0; i < events.size(); i++) {
				stmt.execute("INSERT INTO Events (EventID, Title, Artist, Date, Time, Duration, Target_Audience, Type, Category, ClientID, BookingID) VALUES ('"+events.get(i).getEventID()+"','"+events.get(i).getTitle()+"','"+events.get(i).getArtist()+"','"+events.get(i).getDate()+"','"+events.get(i).getTime()+"','"+events.get(i).getDuration()+"','"+events.get(i).getTarget_Audience()+"','"+events.get(i).getType()+"','"+events.get(i).getCategory()+"','"+events.get(i).getClientID()+"','"+events.get(i).getBookingID()+"');");
			}
			return true;
		} catch (SQLException e) {
			return false;
		}
	}

	public boolean importBookingsData(ArrayList<bookingData> bookings) {
		try {
			Statement stmt = connection.createStatement();
			stmt.execute("DELETE FROM Bookings;");
			for(int i = 0; i < bookings.size(); i++) {
				stmt.execute("INSERT INTO Bookings (BookingID, Date, Time, VenueID, EventID) VALUES ('"+bookings.get(i).getBookingID()+"','"+bookings.get(i).getDate()+"','"+bookings.get(i).getTime()+"','"+bookings.get(i).getVenueID()+"','"+bookings.get(i).getEventID()+"');");
			}
			return true;
		} catch (SQLException e) {
			return false;
		}
	}

	public ResultSet returnClientOrders() throws SQLException {
		Statement stmt = connection.createStatement();
		ResultSet result = stmt.executeQuery("SELECT Clients.ClientName, SUM((Venues.Booking_Price * Events.Duration)/10) FROM Clients INNER JOIN Events ON Events.ClientID = Clients.ClientID INNER JOIN Bookings ON Events.EventID = Bookings.EventID INNER JOIN Venues ON Bookings.VenueID = Venues.VenueID GROUP BY Clients.ClientName;");
		return result;
	}

	public int numOfEventsByClient(String clientname) throws SQLException {
		Statement stmt = connection.createStatement();
		ResultSet result = stmt.executeQuery("SELECT COUNT(Bookings.BookingID) FROM Clients INNER JOIN Events ON Events.ClientID = Clients.ClientID INNER JOIN Bookings ON Bookings.EventID = Events.EventID WHERE ClientName = '"+clientname+"';");
		int count = Integer.valueOf(result.getString(1));
		return count;
	}
	
}