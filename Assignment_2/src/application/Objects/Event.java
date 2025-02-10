package application.Objects;

import javafx.beans.property.SimpleStringProperty;

public class Event{
	private int EventID;
	private SimpleStringProperty Title;
	private SimpleStringProperty Artist;
	private SimpleStringProperty Date;
	private SimpleStringProperty Time;
	private int Duration;
	private int TargetAudience;
	private SimpleStringProperty Type;
	private SimpleStringProperty Category;
	private SimpleStringProperty Client;
	private SimpleStringProperty HasBooking;
	
	public Event(String EventID, String Title, String Artist, String Date, String Time, String Duration, String TargetAudience, String Type, String Category, String HasBooking, String Client) {
		this.EventID = Integer.valueOf(EventID);
		this.Duration = Integer.valueOf(Duration);
		this.TargetAudience = Integer.valueOf(TargetAudience);
		this.Client = new SimpleStringProperty(Client);
		this.HasBooking = new SimpleStringProperty(HasBooking);
		this.Title = new SimpleStringProperty(Title);
		this.Artist = new SimpleStringProperty(Artist);
		this.Category = new SimpleStringProperty(Category);
		this.Date = new SimpleStringProperty(Date);
		this.Time = new SimpleStringProperty(Time);
		this.Type = new SimpleStringProperty(Type);
	}
	
	public int EventIDProperty() {
		return this.EventID;
	}
	
	public int DurationProperty() {
		return this.Duration;
	}
	
	public int TargetAudienceProperty() {
		return this.TargetAudience;
	}
	
	public SimpleStringProperty ClientProperty() {
		return this.Client;
	}
	
	public SimpleStringProperty HasBookingProperty() {
		return this.HasBooking;
	}
	
	
	public SimpleStringProperty TitleProperty() {
		return this.Title;
	}
	
	public SimpleStringProperty ArtistProperty() {
		return this.Artist;
	}
	
	public SimpleStringProperty CategoryProperty() {
		return this.Category;
	}
	
	public SimpleStringProperty DateProperty() {
		return this.Date;
	}
	
	public SimpleStringProperty TimeProperty() {
		return this.Time;
	}
	
	public SimpleStringProperty TypeProperty() {
		return this.Type;
	}
	
}