package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CSV {
	
	@FXML
	private Button exitBtn;
	@FXML
	private Button MainMenuBtn;
	@FXML
	private TextField requestsFileNameField, venuesFileNameField;
	@FXML
	private Button requestsImportBtn, venuesImportBtn;
	@FXML
	private Label alertLabel;
	
	
	public void btnExit(ActionEvent event) {
		Stage stage = (Stage) exitBtn.getScene().getWindow();
		stage.close();
	}
	
	//Need to go back to either staff or manager page
	public void btnMainMenu(ActionEvent event) throws IOException, SQLException {
		Main main = new Main();
		boolean isManager = false;
		File currUser = new File("currentUser.txt");
		Scanner currUserFile = new Scanner(currUser);
		if(currUserFile.hasNextLine()) {
			String username = currUserFile.nextLine();
			
			JDBC conn = new JDBC();
			conn.connectToDB();
			isManager = conn.isManager(username);
			conn.closeConnectionToDB();
		}
		currUserFile.close();
		
		if(isManager) {
			main.changeScene("ManagerPage.fxml");
		} else {
			main.changeScene("StaffPage.fxml");
		}
		
	}
	
	public void btnImportRequests(ActionEvent event) throws SQLException {
		String filename = requestsFileNameField.getText().toString();
		if(filename.length() > 0) {
			if(isFileValid(filename)) {
				File reqFile = new File("src/CSVFiles/" + filename);
				Scanner fs;
				
				try {
					fs = new Scanner(reqFile);
					@SuppressWarnings("unused")
					String heading = fs.nextLine();
					ArrayList<ArrayList<String>> requestData = new ArrayList<ArrayList<String>>();
					
					while(fs.hasNextLine()) {
						String reqData = fs.nextLine();
						String[] seperatedData = reqData.split(",");
						ArrayList<String> row = new ArrayList<String>();
						
						for(int i = 0; i < seperatedData.length; i++) {
							row.add(seperatedData[i]);
						}
						
						requestData.add(row);
					}
					
					JDBC conn = new JDBC();
					conn.connectToDB();
					if(conn.importRequestDataCSV(requestData)) {
						alertLabel.setText("Requests Data Imported!");
					} else {
						alertLabel.setText("Requests Data Failed to import!");
					}
					
					conn.closeConnectionToDB();
					
					fs.close();
					
					
				} catch (FileNotFoundException e) {

				}
			} else {
				alertLabel.setText("Must be a valid requests file!");
			}
		} else {
			alertLabel.setText("Please input a filename to import requests!");
		}
	}
	
	public void btnImportVenues(ActionEvent event) throws SQLException {
		String filename = venuesFileNameField.getText().toString();
		if(filename.length() > 0) {
			if(isFileValid(filename)) {
				File venFile = new File("src/CSVFiles/" + filename);
				Scanner fs;
				
				try {
					fs = new Scanner(venFile);
					@SuppressWarnings("unused")
					String heading = fs.nextLine();
					ArrayList<ArrayList<String>> venueData = new ArrayList<ArrayList<String>>();
					
					while(fs.hasNextLine()) {
						String reqData = fs.nextLine();
						String[] seperatedData = reqData.split(",");
						ArrayList<String> row = new ArrayList<String>();
						
						for(int i = 0; i < seperatedData.length; i++) {
							row.add(seperatedData[i]);
						}
						
						venueData.add(row);
					}
					
					JDBC conn = new JDBC();
					conn.connectToDB();
					if(conn.importVenueDataCSV(venueData)) {
						alertLabel.setText("Venues Data Imported!");
					} else {
						alertLabel.setText("Venues Data Failed to import!");
					}
					
					conn.closeConnectionToDB();
					
					fs.close();
					
					
				} catch (FileNotFoundException e) {

				}
			} else {
				alertLabel.setText("Must be a valid venues file!");
			}
		} else {
			alertLabel.setText("Please input a filename to import venues!");
		}
	}
	
	public boolean isFileValid(String filename) {
		File myFile = new File("src/CSVFiles/" + filename);
		if(myFile.exists()) {
			return true;
		} else {
			return false;
		}
	}
}