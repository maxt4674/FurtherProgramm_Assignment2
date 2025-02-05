package application;

import javafx.beans.property.SimpleStringProperty;

public class Venue{
	private int VenueID;
	private SimpleStringProperty Name;
	private int Capacity;
	private SimpleStringProperty suitableFor;
	private SimpleStringProperty Category;
	private int bookingPrice;
	
	public Venue(String VenueID, String Name, String Capacity, String suitableFor, String Category, String bookingPrice) {
		this.VenueID = Integer.valueOf(VenueID);
		this.Name = new SimpleStringProperty(Name);
		this.Capacity = Integer.valueOf(Capacity);
		this.suitableFor = new SimpleStringProperty(suitableFor);
		this.Category = new SimpleStringProperty(Category);
		this.bookingPrice = Integer.valueOf(bookingPrice);
	}
	
	public int VenueIDProperty() {
		return this.VenueID;
	}
	
	public SimpleStringProperty NameProperty() {
		return this.Name;
	}
	
	public int CapacityProperty() {
		return this.Capacity;
	}
	
	public SimpleStringProperty suitableForProperty() {
		return this.suitableFor;
	}
	
	public SimpleStringProperty CategoryProperty() {
		return this.Category;
	}
	
	public int bookingPriceProperty() {
		return this.bookingPrice;
	}
}