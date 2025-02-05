package application;

import javafx.beans.property.SimpleStringProperty;

public class ItemDetails{
	private SimpleStringProperty Item;
	private SimpleStringProperty Info;
	
	public ItemDetails(String Item, String Info) {
		this.Item = new SimpleStringProperty(Item);
		this.Info = new SimpleStringProperty(Info);
	}
	
	public SimpleStringProperty ItemProperty() {
		return this.Item;
	}
	
	public SimpleStringProperty InfoProperty() {
		return this.Info;
	}
}