package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

import application.Objects.Booking;
import application.Objects.ChartData;
import application.Objects.ClientOrders;
import application.Objects.Event;
import application.Objects.ItemDetails;
import application.Objects.Orders;
import application.Objects.PieGraphData;
import application.Objects.User;
import application.Objects.Venue;
import application.Objects.VenueMatch;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.chart.XYChart;
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

public class Manager {
	@FXML
	private Button exitBtn;
	@FXML
	private Button csvFilesBtn;
	@FXML
	private Button dataBackUpBtn;
	@FXML
	private Label nameLabel;
	@FXML
	private Label alertLabel;
	
	//All of the Account Admin Variables
	@FXML
	private TableView<User> userView;
	@FXML 
	private TableColumn<User, String> userIDCol, usernameCol, firstNameCol, lastNameCol, isManagerCol;
	@FXML
	private TextField addAccountUsernameField, addAccountPasswordField, addAccountPinField, addAccountFirstNameField, addAccountLastNameField;
	@FXML
	private Button addAccountBtn;
	
	//Deleting Account Admin
	@FXML
	private TextField deleteAccountField;
	@FXML
	private Button delAccountBtn;
	
	//Editing Account Admin
	@FXML
	private TextField editAccountUserIDField, editAccountUsernameField, editAccountPasswordField, editAccountPINField, editAccountFirstNameField, editAccountLastNameField;
	@FXML
	private Button editAccountBtn;
	
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
	private TableColumn<VenueMatch, String> venueNameACol, venueCategoryACol, venueCapacityACol, venueSuitableForACol, venueScoreACol;
	@FXML
	private MenuButton venueMatchMenu;
	@FXML
	private Button autoMatchBtn;
	
	//Orders
	@FXML
	private TableView<Orders> ordersView;
	@FXML
	private TableColumn<Orders, String> orderIDCol, venueIDCol, eventIDCol, venueNameOCol, eventNameOCol, commissionsCol, clientCol, dateCol, timeCol; 
	
	//Order Statistics
	@FXML
	private TableView<Orders> orderStatsPerJobView;
	@FXML
	private TableColumn<Orders, String> orderStatsIDCol, orderStatsEventNameCol, orderStatsCommissionsCol;
	
	@FXML
	private Label totalCommissionsLabel;
	
	@FXML
	private TableView<ClientOrders> clientOrdersView;
	@FXML
	private TableColumn<ClientOrders, String> clientOrdersClientNameCol, clientOrdersCommissionsCol;
	
	//Report Tab
	@FXML 
	private PieChart venueUtilGraph;
	@FXML
	private CategoryAxis eventAxis;
	@FXML
	private NumberAxis incomeAxis;
	@FXML
	private BarChart<String, Number> incomeVsCommissionChart;
	
	//Exit btn
	public void btnExit(ActionEvent event) {
		Stage stage = (Stage) exitBtn.getScene().getWindow();
		stage.close();
	}
	
	//CSV page btn
	public void btnCSVFiles(ActionEvent event) throws IOException {
		Main main = new Main();
		main.changeScene("CSVPage.fxml");
	}
	
	//Backup page btn
	public void btnDataBackUp(ActionEvent event) throws IOException {
		Main main = new Main();
		main.changeScene("DataBackupPage.fxml");
	}
	
	//Add user btn
	public void btnAddAccount(ActionEvent event) throws SQLException {
		String username = addAccountUsernameField.getText().toString();
		String password = addAccountPasswordField.getText().toString();
		String pin = addAccountPinField.getText().toString();
		String firstName = addAccountFirstNameField.getText().toString();
		String lastName = addAccountLastNameField.getText().toString();
		
		boolean wasSuccessful = false;
		if(username.length() < 1 || password.length() < 1 || firstName.length() < 1 || lastName.length() < 1) {
			alertLabel.setText("Please input all necessary fields to add account!");
		} else {
			ManagerMenu menuFunctions = new ManagerMenu();
			if(pin.equals("909")) {
				wasSuccessful = menuFunctions.addAccount(username, password, firstName, lastName, true);
			} else {
				wasSuccessful = menuFunctions.addAccount(username, password, firstName, lastName, false);
			}
			
			if(!wasSuccessful) {
				alertLabel.setText("Error, please input a unique account!");
			} else {
				alertLabel.setText("Account added!");
			}
			
			populateUserTable(menuFunctions.returnUsers());
		}
		
		addAccountUsernameField.clear();
		addAccountPasswordField.clear();
		addAccountPinField.clear();
		addAccountFirstNameField.clear();
		addAccountLastNameField.clear();
		
	}
	
	//Delete user btn
	public void btnDeleteAccount(ActionEvent event) throws SQLException {
		String userIDtoDel = deleteAccountField.getText().toString();
		boolean wasSuccessful = false;
		if(userIDtoDel.length() < 1) {
			alertLabel.setText("Please input a UserID to delete an Account!");
		} else {
			ManagerMenu menuFunctions = new ManagerMenu();
			wasSuccessful = menuFunctions.delAccount(userIDtoDel);
			if(wasSuccessful) {
				alertLabel.setText("Account Deleted!");
				populateUserTable(menuFunctions.returnUsers());
			} else {
				alertLabel.setText("Error, Please input a valid ID!");
			}
		}
		
		deleteAccountField.clear();
	}
	
	//On click in the venue table to display venue details
	public void onClickVenueRow(MouseEvent event) throws SQLException {
	    if (venueViewMain.getSelectionModel().getSelectedItem() != null) {
	        String venueName = venueViewMain.getSelectionModel().getSelectedItem().NameProperty().get();
	        ManagerMenu menuFunctions = new ManagerMenu();
	        populateVenueViewDetailed(menuFunctions.returnVenueDetailed(venueName));
	        selectedVenueLabel.setText(venueName);
	    }
	}
	
	//Search for venue btn
	public void btnSearchVenue(ActionEvent event) throws SQLException {
		String nameSearch = venueNameField.getText().toString();
		String category = venueCategoryMenu.getText().toString();

		ManagerMenu menuFunctions = new ManagerMenu();
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
	
	//Edit account btn
	public void btnEditAccount(ActionEvent event) throws SQLException{
		String userIDtoEdit = editAccountUserIDField.getText().toString();
		String username = editAccountUsernameField.getText().toString();
		String password = editAccountPasswordField.getText().toString();
		String pin = editAccountPINField.getText().toString();
		String firstName = editAccountFirstNameField.getText().toString();
		String lastName = editAccountLastNameField.getText().toString();
		
		if(userIDtoEdit.length() < 1) {
			alertLabel.setText("Error, Please input an ID!");
		} else {
			if(username.length() < 1 && password.length() < 1 && pin.length() < 1 && firstName.length() < 1 && lastName.length() < 1) {
				alertLabel.setText("Error, Please input details to edit!");
			} else {
				ManagerMenu menuFunctions = new ManagerMenu();
				
				if(menuFunctions.isNumeric(userIDtoEdit)) {
					boolean wasSuccessful = false;
					
					if(pin.equals("909")) {
						wasSuccessful = menuFunctions.editAccount(userIDtoEdit, username, password, firstName, lastName, true);
					} else {
						wasSuccessful = menuFunctions.editAccount(userIDtoEdit, username, password, firstName, lastName, false);
					}
					
					
					if(wasSuccessful) {
						alertLabel.setText("Account Edited!");
						populateUserTable(menuFunctions.returnUsers());
					} else {
						alertLabel.setText("Error, account not edited!");
					}
				} else {
					alertLabel.setText("Error, Please input a valid ID!");
				}
			}
		}
		
		editAccountUserIDField.clear();
		editAccountUsernameField.clear();
		editAccountPasswordField.clear();
		editAccountPINField.clear();
		editAccountFirstNameField.clear();
		editAccountLastNameField.clear();
	}
	
	//on click on the events table to reveal event details
	public void onClickEventRow(MouseEvent event) throws SQLException {
	    if (eventViewMain.getSelectionModel().getSelectedItem() != null) {
	        String eventName = eventViewMain.getSelectionModel().getSelectedItem().TitleProperty().get();
	        ManagerMenu menuFunctions = new ManagerMenu();
	        populateEventViewDetailed(menuFunctions.returnEventDetailed(eventName));
	        selectedEventLabel.setText(eventName);
	    }	
	}
	
	//MenuButton on click to return available times for booking
	public void onClickMakeBookingTimeSelection(MouseEvent event) throws SQLException {
		makeBookingTimeMenu.getItems().clear();
		String venueName = makeBookingVenueField.getText().toString();
		String eventName = makeBookingEventField.getText().toString();
		String eventDate = makeBookingDateField.getText().toString();
		ManagerMenu menuFunctions = new ManagerMenu();
		
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
	
	//MenuButton on click to return available times for editing a booking
	public void onClickEditBookingTimeSelection(MouseEvent event) throws SQLException {
		editBookingTimeMenu.getItems().clear();
		String bookingID = editBookingIDField.getText().toString();
		String venueName = editBookingVenueField.getText().toString();
		String eventName = editBookingEventField.getText().toString();
		String eventDate = editBookingDateField.getText().toString();
		ManagerMenu menuFunctions = new ManagerMenu();
		
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
	
	//Make booking btn
	public void btnMakeBooking(ActionEvent event) throws SQLException {
		String venueName = makeBookingVenueField.getText().toString();
		String eventName = makeBookingEventField.getText().toString();
		String eventDate = makeBookingDateField.getText().toString();
		String bookingTime = makeBookingTimeMenu.getText().toString();
		ManagerMenu menuFunctions = new ManagerMenu();
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
				populateOrderStatsPerJobView(menuFunctions.returnOrders());
				populateTotalCommissionsLabel(menuFunctions.returnOrders());
				populateClientCommissionsTable(menuFunctions.returnClientOrders());
				populatePieGraph(menuFunctions.returnPieGraphData());
				populateBarChart(menuFunctions.returnChartData());
			} else {
				alertBookingLabel.setText("Booking Unsuccessful!");
			}
		}
		
		makeBookingVenueField.clear();
		makeBookingEventField.clear();
		makeBookingDateField.clear();
		makeBookingTimeMenu.setText("Times Available");
	}
	
	//Delete a booking btn
	public void btnDeleteBooking(ActionEvent event) throws SQLException {
		String bookingID = delBookingIDField.getText().toString();
		ManagerMenu menuFunctions = new ManagerMenu();
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
				populateOrderStatsPerJobView(menuFunctions.returnOrders());
				populateTotalCommissionsLabel(menuFunctions.returnOrders());
				populateClientCommissionsTable(menuFunctions.returnClientOrders());
				populatePieGraph(menuFunctions.returnPieGraphData());
				populateBarChart(menuFunctions.returnChartData());
			} else {
				alertBookingLabel.setText("Booking not Deleted!");
			}
		}
		
		delBookingIDField.clear();
	}
	
	//Edit booking btn
	public void btnEditBooking(ActionEvent event) throws SQLException {
		String bookingID = editBookingIDField.getText().toString();
		String venueName = editBookingVenueField.getText().toString();
		String eventName = editBookingEventField.getText().toString();
		String eventDate = editBookingDateField.getText().toString();
		String bookingTime = editBookingTimeMenu.getText().toString();
		ManagerMenu menuFunctions = new ManagerMenu();
		
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
					populateOrderStatsPerJobView(menuFunctions.returnOrders());
					populateTotalCommissionsLabel(menuFunctions.returnOrders());
					populateClientCommissionsTable(menuFunctions.returnClientOrders());
					populatePieGraph(menuFunctions.returnPieGraphData());
					populateBarChart(menuFunctions.returnChartData());
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
	
	//Auto match venue to eventName btn
	public void btnAutoMatch(ActionEvent event) throws SQLException {
		String eventName = venueMatchMenu.getText().toString();
		
		if(eventName.length() < 1 || eventName.equals("Event")) {
			alertBookingLabel.setText("Please select an event to match!");
		} else {
			ManagerMenu menuFunctions = new ManagerMenu();
			populateVenueMatchTable(menuFunctions.returnAutoMatchScores(eventName));
		}
	}
	
	//Returns list of events to select from
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
	
	//On click on automatch table row to fill in the make booking fields
	public void onClickAutoMatchRow(MouseEvent event) throws SQLException {
	    if (venueMatchView.getSelectionModel().getSelectedItem() != null) {
	    	makeBookingEventField.setText(venueMatchMenu.getText());
	    	makeBookingVenueField.setText(venueMatchView.getSelectionModel().getSelectedItem().venueNameProperty().get());
	    	ManagerMenu menuFunctions = new ManagerMenu();
	    	String eventDate = menuFunctions.getEventDate(venueMatchMenu.getText());
	    	makeBookingDateField.setText(eventDate);
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
			String username = currUserFile.nextLine();
			currUserFile.close();
			conn.connectToDB();
			String name = conn.returnName(username);
			conn.closeConnectionToDB();
			nameLabel.setText(name);
		}
		
		ManagerMenu menuFunctions = new ManagerMenu();
		populateUserTable(menuFunctions.returnUsers());
		populateVenueTable(menuFunctions.returnVenues("", ""));
		populateVenueCategoryMenu(menuFunctions.returnVenueCategories());
		populateEventTable(menuFunctions.returnEvents());
		populateBookingTable(menuFunctions.returnBookings());
		populateOrderTable(menuFunctions.returnOrders());
		populateOrderStatsPerJobView(menuFunctions.returnOrders());
		populateTotalCommissionsLabel(menuFunctions.returnOrders());
		populateClientCommissionsTable(menuFunctions.returnClientOrders());
		populatePieGraph(menuFunctions.returnPieGraphData());
		populateBarChart(menuFunctions.returnChartData());
		
		conn.closeConnectionToDB();
	}
	
	//Populate user table
	private void populateUserTable(ObservableList<User> users) {
		userIDCol.setCellValueFactory(new PropertyValueFactory<>("UserID"));
		usernameCol.setCellValueFactory(new PropertyValueFactory<>("Username"));
		firstNameCol.setCellValueFactory(new PropertyValueFactory<>("FirstName"));
		lastNameCol.setCellValueFactory(new PropertyValueFactory<>("LastName"));
		isManagerCol.setCellValueFactory(new PropertyValueFactory<>("Manager"));
		userView.setItems(users);
	}
	
	//populates venue table
	private void populateVenueTable(ObservableList<Venue> venues) {
		venueNameCol.setCellValueFactory(new PropertyValueFactory<>("Name"));
		venueCategoryCol.setCellValueFactory(new PropertyValueFactory<>("Category"));
		venueViewMain.setItems(venues);
	}
	
	//populates venue catgeory menu
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
	
	//populates venue detailed table
	private void populateVenueViewDetailed(ObservableList<ItemDetails> venueDetails) {
		venueItemCol.setCellValueFactory(new PropertyValueFactory<>("Item"));
		venueInfoCol.setCellValueFactory(new PropertyValueFactory<>("Info"));
		venueViewDetailed.setItems(venueDetails);
	}
	
	//populate event table
	private void populateEventTable(ObservableList<Event> events) {
		eventNameCol.setCellValueFactory(new PropertyValueFactory<>("Title"));
		eventArtistCol.setCellValueFactory(new PropertyValueFactory<>("Artist"));
		eventTypeCol.setCellValueFactory(new PropertyValueFactory<>("Type"));;
		eventClientCol.setCellValueFactory(new PropertyValueFactory<>("Client"));;
		eventBookingStatusCol.setCellValueFactory(new PropertyValueFactory<>("HasBooking"));;
		eventViewMain.setItems(events);
	}
	
	//populate event detailed table
	private void populateEventViewDetailed(ObservableList<ItemDetails> eventDetails) {
		eventItemCol.setCellValueFactory(new PropertyValueFactory<>("Item"));
		eventInfoCol.setCellValueFactory(new PropertyValueFactory<>("Info"));
		eventViewDetailed.setItems(eventDetails);
	}
	
	//populate bookings
	private void populateBookingTable(ObservableList<Booking> bookings) {
		bookingIDCol.setCellValueFactory(new PropertyValueFactory<>("BookingID"));
		bookingEventCol.setCellValueFactory(new PropertyValueFactory<>("Event"));
		bookingVenueCol.setCellValueFactory(new PropertyValueFactory<>("Venue"));
		bookingDateCol.setCellValueFactory(new PropertyValueFactory<>("Date"));
		bookingTimeCol.setCellValueFactory(new PropertyValueFactory<>("Time"));
		bookingDurationCol.setCellValueFactory(new PropertyValueFactory<>("Duration"));
		bookingView.setItems(bookings);
	}
	
	//populate order table
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
	
	//populate venue match table
	private void populateVenueMatchTable(ObservableList<VenueMatch> venueMatchs) {
		venueNameACol.setCellValueFactory(new PropertyValueFactory<>("venueName"));
		venueCategoryACol.setCellValueFactory(new PropertyValueFactory<>("category"));
		venueCapacityACol.setCellValueFactory(new PropertyValueFactory<>("capacity"));
		venueSuitableForACol.setCellValueFactory(new PropertyValueFactory<>("suitableFor"));
		venueScoreACol.setCellValueFactory(new PropertyValueFactory<>("score"));
		venueMatchView.setItems(venueMatchs);
	}
	
	//Orders stats per job table
	private void populateOrderStatsPerJobView(ObservableList<Orders> orders) {
		orderStatsIDCol.setCellValueFactory(new PropertyValueFactory<>("orderNum"));
		orderStatsEventNameCol.setCellValueFactory(new PropertyValueFactory<>("eventName"));
		orderStatsCommissionsCol.setCellValueFactory(new PropertyValueFactory<>("commission"));
		orderStatsPerJobView.setItems(orders);
	}
	
	//Total commissions label
	private void populateTotalCommissionsLabel(ObservableList<Orders> orders) {
		int total = 0;
		for(int i = 0; i < orders.size(); i++) {
			total += Integer.valueOf(orders.get(i).commissionProperty().get());
		}
		
		totalCommissionsLabel.setText("Total Commission Earnt: $" + total);
	}
	
	//Client and commissions table
	private void populateClientCommissionsTable(ObservableList<ClientOrders> orders) {
		clientOrdersClientNameCol.setCellValueFactory(new PropertyValueFactory<>("clientName"));
		clientOrdersCommissionsCol.setCellValueFactory(new PropertyValueFactory<>("commissions"));
		clientOrdersView.setItems(orders);
	}
	
	//Pie graph
	private void populatePieGraph(ObservableList<PieGraphData> data) {
		ObservableList<Data> pieData = FXCollections.observableArrayList();
		for(int i = 0; i < data.size(); i++) {
			Data dataSingular = new Data(data.get(i).VenueProperty().get(), data.get(i).NumBookingsProperty());
			pieData.add(dataSingular);
		}
		
		venueUtilGraph.setData(pieData);
		venueUtilGraph.setLabelsVisible(false);
	}
	
	//Bar chart
	private void populateBarChart(ObservableList<ChartData> data) {
		incomeVsCommissionChart.getData().clear();
		ObservableList<XYChart.Series<String, Number>> series = FXCollections.observableArrayList();
		for(int i = 0; i < data.size(); i++) {
			XYChart.Series<String, Number> seriesInput = new XYChart.Series<>(); 
			seriesInput.setName(data.get(i).eventNameProperty().get());
			seriesInput.getData().add(new XYChart.Data<>("commission", data.get(i).commissionProperty()));
			seriesInput.getData().add(new XYChart.Data<>("income", data.get(i).incomeProperty()));
			series.add(seriesInput);
		}
		incomeVsCommissionChart.getData().addAll(series);
	}

}