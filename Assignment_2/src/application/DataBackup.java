package application;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class DataBackup {
	
	@FXML
	private Button exitBtn;
	@FXML
	private Button MainMenuBtn;
	
	
	public void btnExit(ActionEvent event) {
		Stage stage = (Stage) exitBtn.getScene().getWindow();
		stage.close();
	}
	
	//Need to go back to either staff or manager page
	public void btnMainMenu(ActionEvent event) throws IOException {
		Main main = new Main();
		main.changeScene("ManagerPage.fxml");
	}
}