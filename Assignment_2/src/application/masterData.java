package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;

public class masterData {
	@SuppressWarnings("serial")
	static class userData implements Serializable{
		private String userID;
		private String username;
		private String firstName;
		private String lastName;
		private String password;
		private String isManager;
		
		userData(String userID, String username, String firstName, String lastName, String password, String isManager){
			this.userID = userID;
			this.username = username;
			this.firstName = firstName;
			this.lastName = lastName;
			this.password = password;
			this.isManager = isManager;
		}

		public String getUserID() {
			return userID;
		}

		public String getUsername() {
			return username;
		}
		
		public String getFirstName() {
			return firstName;
		}
		
		public String getLastName() {
			return lastName;
		}
		
		public String getPassword() {
			return password;
		}
		
		public String getIsManager() {
			return isManager;
		}
	}
	
	@SuppressWarnings("serial")
	static class clientData implements Serializable {
		private String clientID;
		private String clientName;
		
		clientData(String clientID, String clientName){
			this.clientID = clientID;
			this.clientName = clientName;
		}
		
		public String getClientID() {
			return clientID;
		}
		
		public String getClientName() {
			return clientName;
		}
	}
	
	public boolean backupData(ArrayList<userData> users, ArrayList<clientData> clients) {
		File dataFile = new File("src/Backups/masterdata.lmvm");
	    try(FileOutputStream fos = new FileOutputStream(dataFile); ObjectOutputStream oos = new ObjectOutputStream(fos);){
	    	oos.writeObject(users);
	    	oos.writeObject(clients);
	        oos.close();
	        fos.close();
	        return true;
	    } catch (FileNotFoundException e){
	    	return false;
	    } catch (IOException e){
	    	return false;
	    }
	}
	
	@SuppressWarnings("unchecked")
	public boolean importBackupData() throws ClassNotFoundException, SQLException {
		ArrayList<userData> users = new ArrayList<userData>();
		ArrayList<clientData> clients = new ArrayList<clientData>();
		File dataFile = new File("src/Backups/masterdata.lmvm");
	    try(FileInputStream fos = new FileInputStream(dataFile); ObjectInputStream oos = new ObjectInputStream(fos);){
	    	users = (ArrayList<userData>)oos.readObject();
	    	clients = (ArrayList<clientData>)oos.readObject();
	        oos.close();
	        fos.close();
	        
	        ManagerMenu menuFunctions = new ManagerMenu();
	        if(menuFunctions.importMasterData(users, clients)) {
		        return true;
	        } else {
	        	return false;
	        }    
	    } catch (FileNotFoundException e){
	    	return false;
	    } catch (IOException e){
	    	return false;
	    }
	}

}