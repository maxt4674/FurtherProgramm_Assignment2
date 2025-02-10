package application;

import java.io.IOException;
import java.sql.SQLException;

import application.Objects.masterData;
import application.Objects.transactionData;
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
	
	
	public void btnExit(ActionEvent event) {
		Stage stage = (Stage) exitBtn.getScene().getWindow();
		stage.close();
	}
	
	//Need to go back to either staff or manager page
	public void btnMainMenu(ActionEvent event) throws IOException {
		Main main = new Main();
		main.changeScene("ManagerPage.fxml");
	}
	
	public void btnBackupTransaction(ActionEvent event) throws SQLException {
		ManagerMenu menuFunctions = new ManagerMenu();
		if(menuFunctions.backupTransactionData()) {
			alertLabelbackup.setText("Transaction Data backed up!");
		} else {
			alertLabelbackup.setText("Transaction Data back up failed!");
		}
		
	}
	
	public void btnBackupMaster(ActionEvent event) throws SQLException {
		ManagerMenu menuFunctions = new ManagerMenu();
		if(menuFunctions.backupMasterData()) {
			alertLabelbackup.setText("Master Data backed up!");
		} else {
			alertLabelbackup.setText("Master Data back up failed!");
		}

	}
	
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