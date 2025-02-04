package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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
}