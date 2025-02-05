package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
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
import javafx.scene.input.KeyEvent;
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
	
	
	
	public void btnExit(ActionEvent event) {
		Stage stage = (Stage) exitBtn.getScene().getWindow();
		stage.close();
	}
	
	public void btnCSVFiles(ActionEvent event) throws IOException {
		Main main = new Main();
		main.changeScene("CSVPage.fxml");
	}
	
	public void btnDataBackUp(ActionEvent event) throws IOException {
		Main main = new Main();
		main.changeScene("DataBackupPage.fxml");
	}
	
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
	
	public void onClickVenueRow(MouseEvent event) throws SQLException {
	    if (venueViewMain.getSelectionModel().getSelectedItem() != null) {
	        String venueName = venueViewMain.getSelectionModel().getSelectedItem().NameProperty().get();
	        ManagerMenu menuFunctions = new ManagerMenu();
	        populateVenueViewDetailed(menuFunctions.returnVenueDetailed(venueName));
	        selectedVenueLabel.setText(venueName);
	    }
	}
	
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
	
	public void onClickEventRow() throws SQLException {
	    if (eventViewMain.getSelectionModel().getSelectedItem() != null) {
	        String eventName = eventViewMain.getSelectionModel().getSelectedItem().TitleProperty().get();
	        ManagerMenu menuFunctions = new ManagerMenu();
	        populateEventViewDetailed(menuFunctions.returnEventDetailed(eventName));
	        selectedEventLabel.setText(eventName);
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
		
		conn.closeConnectionToDB();
	}
	
	private void populateUserTable(ObservableList<User> users) {
		userIDCol.setCellValueFactory(new PropertyValueFactory<>("UserID"));
		usernameCol.setCellValueFactory(new PropertyValueFactory<>("Username"));
		firstNameCol.setCellValueFactory(new PropertyValueFactory<>("FirstName"));
		lastNameCol.setCellValueFactory(new PropertyValueFactory<>("LastName"));
		isManagerCol.setCellValueFactory(new PropertyValueFactory<>("Manager"));
		userView.setItems(users);
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
		eventTypeCol.setCellValueFactory(new PropertyValueFactory<>("Type"));;
		eventClientCol.setCellValueFactory(new PropertyValueFactory<>("Client"));;
		eventBookingStatusCol.setCellValueFactory(new PropertyValueFactory<>("HasBooking"));;
		eventViewMain.setItems(events);
	}
	
	private void populateEventViewDetailed(ObservableList<ItemDetails> eventDetails) {
		eventItemCol.setCellValueFactory(new PropertyValueFactory<>("Item"));
		eventInfoCol.setCellValueFactory(new PropertyValueFactory<>("Info"));
		eventViewDetailed.setItems(eventDetails);
	}

}