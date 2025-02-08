package application;

import javafx.beans.property.SimpleStringProperty;

public class Booking{
	private SimpleStringProperty BookingID;
	private SimpleStringProperty Event;
	private SimpleStringProperty Venue;
	private SimpleStringProperty Date;
	private SimpleStringProperty Time;
	private SimpleStringProperty Duration;
	
	public Booking(String BookingID, String Event, String Venue, String Date, String Time, String Duration) {
		this.BookingID = new SimpleStringProperty(BookingID);
		this.Event = new SimpleStringProperty(Event);
		this.Venue = new SimpleStringProperty(Venue);
		this.Date = new SimpleStringProperty(Date);
		this.Time = new SimpleStringProperty(Time);
		this.Duration = new SimpleStringProperty(Duration);
	}
	
	public SimpleStringProperty BookingIDProperty() {
		return this.BookingID;
	}
	
	public SimpleStringProperty EventProperty() {
		return this.Event;
	}
	
	public SimpleStringProperty VenueProperty() {
		return this.Venue;
	}
	
	public SimpleStringProperty DateProperty() {
		return this.Date;
	}
	
	public SimpleStringProperty TimeProperty() {
		return this.Time;
	}
	
	public SimpleStringProperty DurationProperty() {
		return this.Duration;
	}
}