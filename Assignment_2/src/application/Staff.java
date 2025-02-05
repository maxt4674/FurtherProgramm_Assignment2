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
	
	public void onClickEventRow() throws SQLException {
	    if (eventViewMain.getSelectionModel().getSelectedItem() != null) {
	        String eventName = eventViewMain.getSelectionModel().getSelectedItem().TitleProperty().get();
	        DefaultMenu menuFunctions = new DefaultMenu();
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
}