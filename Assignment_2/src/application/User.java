package application;

import javafx.beans.property.SimpleStringProperty;

public class User{
	private SimpleStringProperty UserID;
	private SimpleStringProperty Username;
	private SimpleStringProperty FirstName;
	private SimpleStringProperty LastName;
	private SimpleStringProperty Manager;
	
	public User(String userID, String username, String firstName, String lastName, String manager) {
		this.UserID = new SimpleStringProperty(userID);
		this.Username = new SimpleStringProperty(username);
		this.FirstName = new SimpleStringProperty(firstName);
		this.LastName = new SimpleStringProperty(lastName);
		this.Manager = new SimpleStringProperty(manager);
	}
	
	public SimpleStringProperty UserIDProperty() {
		return this.UserID;
	}
	
	public SimpleStringProperty UsernameProperty() {
		return this.Username;
	}
	
	public SimpleStringProperty FirstNameProperty() {
		return this.FirstName;
	}
	
	public SimpleStringProperty LastNameProperty() {
		return this.LastName;
	}
	
	public SimpleStringProperty ManagerProperty() {
		return this.Manager;
	}
}