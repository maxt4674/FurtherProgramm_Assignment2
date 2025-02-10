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

public class transactionData {
	@SuppressWarnings("serial")
	static class venuesData implements Serializable{
		private String VenueID;
		private String Name;
		private String Capacity;
		private String Suitable_For;
		private String Category;
		private String Booking_Price;
		
		venuesData(String VenueID, String Name, String Capacity, String Suitable_For, String Category, String Booking_Price){
			this.VenueID = VenueID;
			this.Name = Name;
			this.Capacity = Capacity;
			this.Suitable_For = Suitable_For;
			this.Category = Category;
			this.Booking_Price = Booking_Price;
		}

		public String getVenueID() {
			return VenueID;
		}

		public String getName() {
			return Name;
		}
		
		public String getCapacity() {
			return Capacity;
		}
		
		public String getSuitable_For() {
			return Suitable_For;
		}
		
		public String getCategory() {
			return Category;
		}
		
		public String getBooking_Price() {
			return Booking_Price;
		}
	}
	
	@SuppressWarnings("serial")
	static class eventData implements Serializable {
		private String EventID;
		private String Title;
		private String Artist;
		private String Date;
		private String Time;
		private String Duration;
		private String Target_Audience;
		private String Type;
		private String Category;
		private String ClientID;
		private String BookingID;
		
		eventData(String EventID, String Title, String Artist, String Date, String Time, String Duration, String Target_Audience, String Type, String Category, String ClientID, String BookingID){
			this.EventID = EventID;
			this.Title = Title;
			this.Artist = Artist;
			this.Date = Date;
			this.Time = Time;
			this.Duration = Duration;
			this.Target_Audience = Target_Audience;
			this.Type = Type;
			this.Category = Category;
			this.ClientID = ClientID;
			this.BookingID = BookingID;
		}
		
		public String getEventID() {
			return EventID;
		}
		
		public String getTitle() {
			return Title;
		}
		
		public String getArtist() {
			return Artist;
		}
		
		public String getDate() {
			return Date;
		}
		
		public String getTime() {
			return Time;
		}
		
		public String getDuration() {
			return Duration;
		}
		
		public String getTarget_Audience() {
			return Target_Audience;
		}
		
		public String getType() {
			return Type;
		}
		
		public String getCategory() {
			return Category;
		}
		
		public String getClientID() {
			return ClientID;
		}
		
		public String getBookingID() {
			return BookingID;
		}
	}
	
	@SuppressWarnings("serial")
	static class bookingData implements Serializable {
		private String BookingID;
		private String Date;
		private String Time;
		private String VenueID;
		private String EventID;
		
		bookingData(String clientID, String Date, String Time, String VenueID, String EventID){
			this.BookingID = clientID;
			this.Date = Date;
			this.Time = Time;
			this.EventID = EventID;
			this.VenueID = VenueID;
		}
		
		public String getBookingID() {
			return BookingID;
		}
		
		public String getDate() {
			return Date;
		}
		
		public String getTime() {
			return Time;
		}
		
		public String getEventID() {
			return EventID;
		}
		
		public String getVenueID() {
			return VenueID;
		}
	}
	
	public boolean backupData(ArrayList<venuesData> venues, ArrayList<eventData> events, ArrayList<bookingData> bookings) {
		File dataFile = new File("src/Backups/transactiondata.lmvm");
	    try(FileOutputStream fos = new FileOutputStream(dataFile); ObjectOutputStream oos = new ObjectOutputStream(fos);){
	    	oos.writeObject(venues);
	    	oos.writeObject(events);
	    	oos.writeObject(bookings);
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
		ArrayList<venuesData> venues = new ArrayList<venuesData>();
		ArrayList<eventData> events = new ArrayList<eventData>();
		ArrayList<bookingData> bookings = new ArrayList<bookingData>();
		File dataFile = new File("src/Backups/transactiondata.lmvm");
	    try(FileInputStream fos = new FileInputStream(dataFile); ObjectInputStream oos = new ObjectInputStream(fos);){
	    	venues = (ArrayList<venuesData>)oos.readObject();
	    	events = (ArrayList<eventData>)oos.readObject();
	    	bookings = (ArrayList<bookingData>)oos.readObject();
	        oos.close();
	        fos.close();
	        
	        ManagerMenu menuFunctions = new ManagerMenu();
	        if(menuFunctions.importTransactionData(venues, events, bookings)) {
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