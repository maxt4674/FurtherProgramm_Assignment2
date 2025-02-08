package application;

import javafx.beans.property.SimpleStringProperty;

public class VenueMatch{
	private SimpleStringProperty venueName;
	private SimpleStringProperty category;
	private SimpleStringProperty capacity;
	private SimpleStringProperty suitableFor;
	private SimpleStringProperty score;
	
	public VenueMatch(String venueName, String category, String capacity, String suitableFor, String score) {
		this.venueName = new SimpleStringProperty(venueName);
		this.category = new SimpleStringProperty(category);
		this.capacity = new SimpleStringProperty(capacity);
		this.suitableFor = new SimpleStringProperty(suitableFor);
		this.score = new SimpleStringProperty(score);
	}
	
	public SimpleStringProperty venueNameProperty() {
		return this.venueName;
	}
	
	public SimpleStringProperty categoryProperty() {
		return this.category;
	}
	
	public SimpleStringProperty capacityProperty() {
		return this.capacity;
	}
	
	public SimpleStringProperty suitableForProperty() {
		return this.suitableFor;
	}
	
	public SimpleStringProperty scoreProperty() {
		return this.score;
	}
}