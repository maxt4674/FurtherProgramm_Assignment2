package application.Objects;

import javafx.beans.property.SimpleStringProperty;

public class ChartData{
	private SimpleStringProperty eventName;
	private int commission;
	private int income;
	
	public ChartData(String eventName, int commission, int income) {
		this.eventName = new SimpleStringProperty(eventName);
		this.commission = commission;
		this.income = income;
	}
	
	public SimpleStringProperty eventNameProperty() {
		return this.eventName;
	}
	
	public int commissionProperty() {
		return this.commission;
	}
	
	public int incomeProperty() {
		return this.income;
	}
}