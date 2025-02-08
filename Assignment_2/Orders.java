package application;

import javafx.beans.property.SimpleStringProperty;

public class Orders{
	private SimpleStringProperty orderNum;
	private SimpleStringProperty venueNum;
	private SimpleStringProperty eventNum;
	private SimpleStringProperty venueName;
	private SimpleStringProperty eventName;
	private SimpleStringProperty commission;
	private SimpleStringProperty client;
	private SimpleStringProperty date;
	private SimpleStringProperty time;
	
	public Orders(String orderNum, String venueNum, String eventNum, String venueName, String eventName, String client, String date, String time, String commission) {
		this.orderNum = new SimpleStringProperty(orderNum);
		this.venueNum = new SimpleStringProperty(venueNum);
		this.eventNum = new SimpleStringProperty(eventNum);
		this.venueName = new SimpleStringProperty(venueName);
		this.eventName = new SimpleStringProperty(eventName);
		this.commission = new SimpleStringProperty(commission);
		this.client = new SimpleStringProperty(client);
		this.date = new SimpleStringProperty(date);
		this.time = new SimpleStringProperty(time);
	}
	
	public SimpleStringProperty orderNumProperty() {
		return this.orderNum;
	}
	
	public SimpleStringProperty venueNumProperty() {
		return this.venueNum;
	}
	
	public SimpleStringProperty eventNumProperty() {
		return this.eventNum;
	}
	
	public SimpleStringProperty venueNameProperty() {
		return this.venueName;
	}
	
	public SimpleStringProperty eventNameProperty() {
		return this.eventName;
	}
	
	public SimpleStringProperty commissionProperty() {
		return this.commission;
	}
	
	public SimpleStringProperty clientProperty() {
		return this.client;
	}
	
	public SimpleStringProperty dateProperty() {
		return this.date;
	}
	
	public SimpleStringProperty timeProperty() {
		return this.time;
	}
}