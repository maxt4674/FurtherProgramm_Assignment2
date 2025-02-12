package application;

import java.io.IOException;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class DataBackup {
	
	@FXML
	private Button exitBtn;
	@FXML
	private Button MainMenuBtn;
	@FXML
	private Button backupTransactionBtn, backupMasterBtn, importTransactionBtn, importMasterBtn;
	@FXML
	private Label alertLabelImport, alertLabelbackup;
	
	//Exit button
	public void btnExit(ActionEvent event) {
		Stage stage = (Stage) exitBtn.getScene().getWindow();
		stage.close();
	}
	
	//returns to user menu depending on user
	public void btnMainMenu(ActionEvent event) throws IOException {
		Main main = new Main();
		main.changeScene("ManagerPage.fxml");
	}
	
	//Backups up transactions
	public void btnBackupTransaction(ActionEvent event) throws SQLException {
		ManagerMenu menuFunctions = new ManagerMenu();
		if(menuFunctions.backupTransactionData()) {
			alertLabelbackup.setText("Transaction Data backed up!");
		} else {
			alertLabelbackup.setText("Transaction Data back up failed!");
		}
		
	}
	
	//backups up master data
	public void btnBackupMaster(ActionEvent event) throws SQLException {
		ManagerMenu menuFunctions = new ManagerMenu();
		if(menuFunctions.backupMasterData()) {
			alertLabelbackup.setText("Master Data backed up!");
		} else {
			alertLabelbackup.setText("Master Data back up failed!");
		}

	}
	
	//imports transaction data
	public void btnImportTransaction(ActionEvent event) throws ClassNotFoundException, SQLException {
		transactionData data = new transactionData();
		ManagerMenu menuFunctions = new ManagerMenu();
		if(menuFunctions.isBackupFileValid("transactiondata.lmvm")){
			if(data.importBackupData()) {
				alertLabelImport.setText("Transaction Data backup imported!");
			} else {
				alertLabelImport.setText("Transaction Data backup import failed!");
			}
		} else {
			alertLabelImport.setText("Transaction Data file not found!");
		}
	}
	
	//imports master data
	public void btnImportMaster(ActionEvent event) throws ClassNotFoundException, SQLException {
		masterData data = new masterData();
		ManagerMenu menuFunctions = new ManagerMenu();
		if(menuFunctions.isBackupFileValid("masterdata.lmvm")){
			if(data.importBackupData()) {
				alertLabelImport.setText("Master Data backup imported!");
			} else {
				alertLabelImport.setText("Master Data backup import failed!");
			}
		} else {
			alertLabelImport.setText("Master Data file not found!");
		}
	}
}