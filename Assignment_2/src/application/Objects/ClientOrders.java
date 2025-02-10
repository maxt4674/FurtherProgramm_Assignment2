package application.Objects;

import javafx.beans.property.SimpleStringProperty;

public class ClientOrders{
	private SimpleStringProperty clientName;
	private SimpleStringProperty commissions;
	
	public ClientOrders(String clientName, String commissions){
		this.clientName = new SimpleStringProperty(clientName);
		this.commissions = new SimpleStringProperty(commissions);
	}
	
	public SimpleStringProperty clientNameProperty() {
		return clientName;
	}
	
	public SimpleStringProperty commissionsProperty() {
		return commissions;
	}
	
}