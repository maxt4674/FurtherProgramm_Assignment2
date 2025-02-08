package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class Staff {
	private String currUser;
	
	@FXML
	private Button exitBtn;
	@FXML
	private Button csvFilesBtn;
	@FXML
	private Label nameLabel;
	@FXML
	private Label alertLabel;
	
	//Account Admin
	//Editing Account Admin
	@FXML
	private TextField editAccountUsernameField, editAccountPasswordField, editAccountFirstNameField, editAccountLastNameField;
	@FXML
	private Button editAccountBtn;
	@FXML
	private Label usernameLabel, passwordLabel, firstNameLabel, lastNameLabel;
	
	//Venue Page
	@FXML
	private TextField venueNameField;
	@FXML
	private Button venueSearchBtn;
	@FXML
	private TableView<Venue> venueViewMain;
	@FXML
	private TableColumn<Venue, String> venueNameCol, venueCategoryCol;
	@FXML
	private Label selectedVenueLabel;
	@FXML
	private MenuButton venueCategoryMenu;
	@FXML
	private TableView<ItemDetails> venueViewDetailed;
	@FXML
	private TableColumn<ItemDetails, String> venueItemCol, venueInfoCol;
	
	//Event Page
	@FXML
	private TableView<Event> eventViewMain;
	@FXML
	private TableColumn<Event, String> eventNameCol, eventArtistCol, eventTypeCol, eventClientCol, eventBookingStatusCol;
	@FXML
	private Label selectedEventLabel;
	@FXML
	private TableView<ItemDetails> eventViewDetailed;
	@FXML
	private TableColumn<ItemDetails, String> eventItemCol, eventInfoCol;
	
	//Booking page
	@FXML
	private TableView<Booking> bookingView;
	@FXML
	private TableColumn<Booking, String> bookingIDCol, bookingEventCol, bookingVenueCol, bookingDateCol, bookingTimeCol, bookingDurationCol;
	@FXML
	private Label alertBookingLabel;
	
	//Making a booking
	@FXML
	private TextField makeBookingEventField, makeBookingVenueField, makeBookingDateField;
	@FXML
	private MenuButton makeBookingTimeMenu;
	@FXML
	private Button makeBookingBtn;
	
	//Deleting a booking
	@FXML
	private TextField delBookingIDField;
	@FXML
	private Button delBookingBtn;
	
	//Editing a booking
	@FXML
	private TextField editBookingIDField, editBookingEventField, editBookingVenueField, editBookingDateField;
	@FXML
	private MenuButton editBookingTimeMenu;
	@FXML
	private Button editBookingBtn;
	
	//Auto-Match Feature
	@FXML
	private TableView<VenueMatch> venueMatchView;
	@FXML
	private MenuButton venueMatchMenu;
	@FXML
	private Button autoMatchBtn;
	
	//Orders
	@FXML
	private TableView<Orders> ordersView;
	@FXML
	private TableColumn<Orders, String> orderIDCol, venueIDCol, eventIDCol, venueNameOCol, eventNameOCol, commissionsCol, clientCol, dateCol, timeCol; 
	
	
	public Staff() {
		
	}
	
	public void btnExit(ActionEvent event) {
		Stage stage = (Stage) exitBtn.getScene().getWindow();
		stage.close();
	}
	
	public void btnCSVFiles(ActionEvent event) throws IOException {
		Main main = new Main();
		main.changeScene("CSVPage.fxml");
	}
	
	public void btnEditAccount(ActionEvent event) throws SQLException{
		DefaultMenu menuFunctions = new DefaultMenu();
		String userIDtoEdit = menuFunctions.getID(this.currUser);
		String username = editAccountUsernameField.getText().toString();
		String password = editAccountPasswordField.getText().toString();
		String firstName = editAccountFirstNameField.getText().toString();
		String lastName = editAccountLastNameField.getText().toString();
		
		if(userIDtoEdit.length() < 1) {
			alertLabel.setText("Error, Please input an ID!");
		} else {
			if(username.length() < 1 && password.length() < 1 && firstName.length() < 1 && lastName.length() < 1) {
				alertLabel.setText("Error, Please input details to edit!");
			} else {
				
				
				if(menuFunctions.isNumeric(userIDtoEdit)) {
					boolean wasSuccessful = menuFunctions.editAccount(userIDtoEdit, username, password, firstName, lastName, false);
						
					if(wasSuccessful) {
						alertLabel.setText("Account Edited!");
						if(!username.equals(this.currUser) && username.length() >= 1) {
							this.currUser = username;
						}
						populateAccountDetails();
					} else {
						alertLabel.setText("Error, account not edited!");
					}
				} else {
					alertLabel.setText("Error, Please input a valid ID!");
				}
			}
		}
		
		editAccountUsernameField.clear();
		editAccountPasswordField.clear();
		editAccountFirstNameField.clear();
		editAccountLastNameField.clear();
	}
	
	public void onClickVenueRow(MouseEvent event) throws SQLException {
	    if (venueViewMain.getSelectionModel().getSelectedItem() != null) {
	        String venueName = venueViewMain.getSelectionModel().getSelectedItem().NameProperty().get();
	        DefaultMenu menuFunctions = new DefaultMenu();
	        populateVenueViewDetailed(menuFunctions.returnVenueDetailed(venueName));
	        selectedVenueLabel.setText(venueName);
	    }		
	}
	
	public void btnSearchVenue(ActionEvent event) throws SQLException {
		String nameSearch = venueNameField.getText().toString();
		String category = venueCategoryMenu.getText().toString();

		DefaultMenu menuFunctions = new DefaultMenu();
		if(category.equals("Category") && nameSearch.length() < 1) {
			populateVenueTable(menuFunctions.returnVenues("", ""));
		} else if(!category.equals("Category") && nameSearch.length() < 1) {
			populateVenueTable(menuFunctions.returnVenues(category, ""));
		} else if(category.equals("Category") && nameSearch.length() > 0) {
			populateVenueTable(menuFunctions.returnVenues("", nameSearch));
		} else if(!category.equals("Category") && nameSearch.length() > 0) {
			populateVenueTable(menuFunctions.returnVenues(category, nameSearch));
		}
		
		venueNameField.clear();
		venueCategoryMenu.setText("Category");
	}
	
	public void onClickEventRow(MouseEvent event) throws SQLException {
	    if (eventViewMain.getSelectionModel().getSelectedItem() != null) {
	        String eventName = eventViewMain.getSelectionModel().getSelectedItem().TitleProperty().get();
	        DefaultMenu menuFunctions = new DefaultMenu();
	        populateEventViewDetailed(menuFunctions.returnEventDetailed(eventName));
	        selectedEventLabel.setText(eventName);
	    }	
	}
	
	public void onClickMakeBookingTimeSelection(MouseEvent event) throws SQLException {
		makeBookingTimeMenu.getItems().clear();
		String venueName = makeBookingVenueField.getText().toString();
		String eventName = makeBookingEventField.getText().toString();
		String eventDate = makeBookingDateField.getText().toString();
		DefaultMenu menuFunctions = new DefaultMenu();
		
		if(venueName.length() < 1 || eventName.length() < 1 || eventDate.length() < 1) {
			alertBookingLabel.setText("To select the time, all other fields must be inputted");
		} else if(!menuFunctions.isValidDate(eventDate)) {
			alertBookingLabel.setText("Must be a valid date!");
		} else if(!menuFunctions.isValidVenueAndEvent(venueName, eventName)) {
			alertBookingLabel.setText("Must be a valid venue and event!");
		} else if(menuFunctions.eventBooked(eventName)) {
			alertBookingLabel.setText("Event already has a booking!");		
		} else {
			ArrayList<String> availableDates = menuFunctions.returnAvailableTimes(venueName, eventName, eventDate);
			for(int i = 0; i < availableDates.size(); i++) {
				MenuItem item = new MenuItem(availableDates.get(i));
				String itemName = availableDates.get(i);
				item.setOnAction(a->{
					makeBookingTimeMenu.setText(itemName);
				});
				makeBookingTimeMenu.getItems().add(item);
			}
		}
	}
	
	public void onClickEditBookingTimeSelection(MouseEvent event) throws SQLException {
		editBookingTimeMenu.getItems().clear();
		String bookingID = editBookingIDField.getText().toString();
		String venueName = editBookingVenueField.getText().toString();
		String eventName = editBookingEventField.getText().toString();
		String eventDate = editBookingDateField.getText().toString();
		DefaultMenu menuFunctions = new DefaultMenu();
		
		if(bookingID.length() < 0 || !menuFunctions.isValidBookingID(bookingID)) {
			alertBookingLabel.setText("To edit the time, the bookingID must be entered and valid!");
		} else if(!menuFunctions.isValidDate(eventDate) && !(eventDate.length() < 1)) {
			alertBookingLabel.setText("Must be a valid date!");
		} else if(!menuFunctions.isValidVenue(venueName) && !(venueName.length() < 1)) {
			alertBookingLabel.setText("Must be a valid venue!");
		} else if(!menuFunctions.isValidEvent(eventName) && !(eventName.length() < 1)) {
			alertBookingLabel.setText("Must be a valid event!");
		} else {
			ArrayList<String> returnVals = menuFunctions.returnBookingOnID(bookingID);
			if(venueName.length() < 1) {
				venueName = returnVals.get(0);
			}
			
			if(eventName.length() < 1) {
				eventName = returnVals.get(1);
			}
			
			if(eventDate.length() < 1) {
				eventDate = returnVals.get(2);
			}
			ArrayList<String> availableDates = menuFunctions.returnAvailableTimes(venueName, eventName, eventDate);
			for(int i = 0; i < availableDates.size(); i++) {
				MenuItem item = new MenuItem(availableDates.get(i));
				String itemName = availableDates.get(i);
				item.setOnAction(a->{
					editBookingTimeMenu.setText(itemName);
				});
				editBookingTimeMenu.getItems().add(item);
			}
		}
	}
	
	public void btnMakeBooking(ActionEvent event) throws SQLException {
		String venueName = makeBookingVenueField.getText().toString();
		String eventName = makeBookingEventField.getText().toString();
		String eventDate = makeBookingDateField.getText().toString();
		String bookingTime = makeBookingTimeMenu.getText().toString();
		DefaultMenu menuFunctions = new DefaultMenu();
		if(venueName.length() < 1 || eventName.length() < 1 || eventDate.length() < 1 || bookingTime.equals("Times Available")) {
			alertBookingLabel.setText("To make a booking, all fields must be inputted");
		} else if(!menuFunctions.isValidDate(eventDate)) {
			alertBookingLabel.setText("Must be a valid date!");
		} else if(!menuFunctions.isValidVenueAndEvent(venueName, eventName)) {
			alertBookingLabel.setText("Must be a valid venue and event!");
		} else if(menuFunctions.eventBooked(eventName)) {
			alertBookingLabel.setText("Event already has a booking!");
		} else {
			if(menuFunctions.makeBooking(venueName, eventName, eventDate, bookingTime)) {
				alertBookingLabel.setText("Booking made!");
				populateEventTable(menuFunctions.returnEvents());
				populateBookingTable(menuFunctions.returnBookings());
				populateOrderTable(menuFunctions.returnOrders());
			} else {
				alertBookingLabel.setText("Booking Unsuccessful!");
			}
		}
		
		makeBookingVenueField.clear();
		makeBookingEventField.clear();
		makeBookingDateField.clear();
		makeBookingTimeMenu.setText("Times Available");
	}
	
	public void btnDeleteBooking(ActionEvent event) throws SQLException {
		String bookingID = delBookingIDField.getText().toString();
		DefaultMenu menuFunctions = new DefaultMenu();
		if(!menuFunctions.isNumeric(bookingID)) {
			alertBookingLabel.setText("BookingID must be numeric!");
		} else if(!menuFunctions.isValidBookingID(bookingID)) {
			alertBookingLabel.setText("BookingID must be valid!");
		} else {
			if(menuFunctions.deleteBooking(bookingID)) {
				alertBookingLabel.setText("Booking Deleted!");
				populateEventTable(menuFunctions.returnEvents());
				populateBookingTable(menuFunctions.returnBookings());
				populateOrderTable(menuFunctions.returnOrders());
			} else {
				alertBookingLabel.setText("Booking not Deleted!");
			}
		}
		
		delBookingIDField.clear();
	}
	
	public void btnEditBooking(ActionEvent event) throws SQLException {
		String bookingID = editBookingIDField.getText().toString();
		String venueName = editBookingVenueField.getText().toString();
		String eventName = editBookingEventField.getText().toString();
		String eventDate = editBookingDateField.getText().toString();
		String bookingTime = editBookingTimeMenu.getText().toString();
		DefaultMenu menuFunctions = new DefaultMenu();
		
		if(bookingID.length() < 1) {
			alertBookingLabel.setText("Must insert a bookingID to edit a booking!");
		} else if(venueName.length() < 1 && eventName.length() < 1 && eventDate.length() < 1 && bookingTime.equals("Times Available")) {
			alertBookingLabel.setText("Must enter something into at least one field to edit a booking!");
		} else if(!menuFunctions.isNumeric(bookingID) || !menuFunctions.isValidBookingID(bookingID)) {
			alertBookingLabel.setText("Must be a valid BookingID!");
		} else if((!(venueName.length() < 1) && !menuFunctions.isValidVenue(venueName)) || (!(eventName.length() < 1) && !menuFunctions.isValidEvent(eventName))) {
			alertBookingLabel.setText("Must be a valid Event and/or Venues!");
		} else if(!(eventName.length() < 1) && menuFunctions.eventBookedEdit(eventName, bookingID)) { 
			alertBookingLabel.setText("Event must not be booked in another booking!");
		} else if(!(eventDate.length() < 1) && !menuFunctions.isValidDate(eventDate)){
			alertBookingLabel.setText("Must be a valid Date!");
		} else {
			ArrayList<String> returnVals = menuFunctions.returnBookingOnID(bookingID);
			if(venueName.length() < 1) {
				venueName = returnVals.get(0);
			}
			
			if(eventName.length() < 1) {
				eventName = returnVals.get(1);
			}
			
			if(eventDate.length() < 1) {
				eventDate = returnVals.get(2);
			}
			
			if(bookingTime.equals("Times Available")) {
				bookingTime = returnVals.get(3);
			}
			
			if(menuFunctions.deleteBooking(bookingID)) {
				if(menuFunctions.makeBooking(venueName, eventName, eventDate, bookingTime)) {
					alertBookingLabel.setText("Booking edited!");
					populateEventTable(menuFunctions.returnEvents());
					populateBookingTable(menuFunctions.returnBookings());
					populateOrderTable(menuFunctions.returnOrders());
				} else {
					alertBookingLabel.setText("Edit booking failed!");
				}
			} else {
				alertBookingLabel.setText("Edit booking failed!");
			}
		}
		
		editBookingVenueField.clear();
		editBookingEventField.clear();
		editBookingDateField.clear();
		editBookingTimeMenu.setText("Times Available");
		editBookingIDField.clear();
	}
	
	public void btnAutoMatch() {
		String event = venueMatchMenu.getText().toString();
		
		if(event.length() < 1 || event.equals("Event")) {
			alertBookingLabel.setText("Please select an event to match!");
		} else {
			
		}
	}
	
	public void onClickAutoMatchVenueSelection(MouseEvent event) throws SQLException {
		venueMatchMenu.getItems().clear();
		ManagerMenu menuFunctions = new ManagerMenu();
		ArrayList<String> events = menuFunctions.returnAllEventNames();
		for(int i = 0; i < events.size(); i++) {
			MenuItem item = new MenuItem(events.get(i));
			String itemName = events.get(i);
			item.setOnAction(a->{
				venueMatchMenu.setText(itemName);
			});
			venueMatchMenu.getItems().add(item);
		}
	}
	
	//For initialising all default variables
	@FXML
	public void initialize() throws SQLException, FileNotFoundException {
		JDBC conn = new JDBC();
		conn.connectToDB();
		File currUser = new File("currentUser.txt");
		Scanner currUserFile = new Scanner(currUser);
		if(currUserFile.hasNextLine()) {
			this.currUser = currUserFile.nextLine();
			currUserFile.close();
			conn.connectToDB();
			String name = conn.returnName(this.currUser);
			conn.closeConnectionToDB();
			nameLabel.setText(name);
			populateAccountDetails();
		}
		
		DefaultMenu menuFunctions = new DefaultMenu();
		populateVenueTable(menuFunctions.returnVenues("", ""));
		populateVenueCategoryMenu(menuFunctions.returnVenueCategories());
		populateEventTable(menuFunctions.returnEvents());
		populateBookingTable(menuFunctions.returnBookings());
		populateOrderTable(menuFunctions.returnOrders());
		conn.closeConnectionToDB();
	}
	
	private void populateAccountDetails() throws SQLException {
		JDBC conn = new JDBC();
		conn.connectToDB();
		ResultSet result = conn.returnUserDetails(this.currUser);
		usernameLabel.setText(currUser);
		passwordLabel.setText(result.getString("password"));
		firstNameLabel.setText(result.getString("firstName"));
		lastNameLabel.setText(result.getString("lastName"));
		conn.closeConnectionToDB();
	}
	
	private void populateVenueTable(ObservableList<Venue> venues) {
		venueNameCol.setCellValueFactory(new PropertyValueFactory<>("Name"));
		venueCategoryCol.setCellValueFactory(new PropertyValueFactory<>("Category"));
		venueViewMain.setItems(venues);
	}
	
	private void populateVenueCategoryMenu(ArrayList<String> categories) {
		venueCategoryMenu.getItems().clear();
		for(int i = 0; i < categories.size(); i++) {
			MenuItem item = new MenuItem(categories.get(i));
			String itemName = categories.get(i);
			item.setOnAction(a->{
				venueCategoryMenu.setText(itemName);
			});
			venueCategoryMenu.getItems().add(item);
		}
	}
	
	private void populateVenueViewDetailed(ObservableList<ItemDetails> venueDetails) {
		venueItemCol.setCellValueFactory(new PropertyValueFactory<>("Item"));
		venueInfoCol.setCellValueFactory(new PropertyValueFactory<>("Info"));
		venueViewDetailed.setItems(venueDetails);
	}
	
	private void populateEventTable(ObservableList<Event> events) {
		eventNameCol.setCellValueFactory(new PropertyValueFactory<>("Title"));
		eventArtistCol.setCellValueFactory(new PropertyValueFactory<>("Artist"));
		eventTypeCol.setCellValueFactory(new PropertyValueFactory<>("Type"));
		eventClientCol.setCellValueFactory(new PropertyValueFactory<>("Client"));
		eventBookingStatusCol.setCellValueFactory(new PropertyValueFactory<>("HasBooking"));
		eventViewMain.setItems(events);
	}
	
	private void populateEventViewDetailed(ObservableList<ItemDetails> eventDetails) {
		eventItemCol.setCellValueFactory(new PropertyValueFactory<>("Item"));
		eventInfoCol.setCellValueFactory(new PropertyValueFactory<>("Info"));
		eventViewDetailed.setItems(eventDetails);
	}
	
	private void populateBookingTable(ObservableList<Booking> bookings) {
		bookingIDCol.setCellValueFactory(new PropertyValueFactory<>("BookingID"));
		bookingEventCol.setCellValueFactory(new PropertyValueFactory<>("Event"));
		bookingVenueCol.setCellValueFactory(new PropertyValueFactory<>("Venue"));
		bookingDateCol.setCellValueFactory(new PropertyValueFactory<>("Date"));
		bookingTimeCol.setCellValueFactory(new PropertyValueFactory<>("Time"));
		bookingDurationCol.setCellValueFactory(new PropertyValueFactory<>("Duration"));
		bookingView.setItems(bookings);
	}
	
	private void populateOrderTable(ObservableList<Orders> orders) {
		orderIDCol.setCellValueFactory(new PropertyValueFactory<>("orderNum"));
		venueIDCol.setCellValueFactory(new PropertyValueFactory<>("venueNum"));
		eventIDCol.setCellValueFactory(new PropertyValueFactory<>("eventNum"));
		venueNameOCol.setCellValueFactory(new PropertyValueFactory<>("venueName"));
		eventNameOCol.setCellValueFactory(new PropertyValueFactory<>("eventName"));
		commissionsCol.setCellValueFactory(new PropertyValueFactory<>("commission"));
		clientCol.setCellValueFactory(new PropertyValueFactory<>("client"));
		dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
		timeCol.setCellValueFactory(new PropertyValueFactory<>("time"));
		ordersView.setItems(orders);
	}
}