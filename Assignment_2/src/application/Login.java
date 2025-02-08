package application;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class Login {
	
	@FXML
	private Button button;
	@FXML
	private TextField username;
	@FXML
	private PasswordField password;
	@FXML
	private Label LoginLabel;
	
	
	
	public void btnLoginOnClick(ActionEvent event) throws IOException, SQLException{
		validateLogin();
	}
	
	private void validateLogin() throws IOException, SQLException{
		JDBC conn = new JDBC();
		conn.connectToDB();
		String usernameStr = username.getText().toString();
		String passwordStr = password.getText().toString();
		if(usernameStr.length() < 1 || passwordStr.length() < 1) {
			LoginLabel.setText("Please input a Username and Password!");
		} else {
			boolean isValid = conn.isValidCredentials(usernameStr, passwordStr);
			
			if(isValid) {
				Main main = new Main();
				LoginLabel.setText("Successful Login!");
			
				//Writing to the txt file the current user logged in
				FileWriter currUser = new FileWriter("currentUser.txt");
				currUser.write(usernameStr);
				currUser.close();
				
				boolean isManager = conn.isManager(usernameStr);
				if(isManager) {
					main.changeScene("ManagerPage.fxml");
				} else {
					main.changeScene("StaffPage.fxml");
				}

			} else {
				LoginLabel.setText("Incorrect Login Details!");
			}
		}
		conn.closeConnectionToDB();
	}
}