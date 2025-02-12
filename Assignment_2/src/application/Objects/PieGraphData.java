package application.Objects;

import javafx.beans.property.SimpleStringProperty;

public class PieGraphData{
	private SimpleStringProperty Venue;
	private int NumBookings;
	
	public PieGraphData(String Venue, int NumBookings) {
		this.Venue = new SimpleStringProperty(Venue);
		this.NumBookings = NumBookings;
	}
	
	public SimpleStringProperty VenueProperty() {
		return this.Venue;
	}
	
	public int NumBookingsProperty() {
		return this.NumBookings;
	}
}