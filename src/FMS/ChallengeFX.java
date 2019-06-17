package FMS;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


//Konstruktor
public class ChallengeFX {
	
	private Challenge modellObject;
	private IntegerProperty IDNummber;
	private StringProperty name;
	private IntegerProperty ownerID ;
	private StringProperty ownerName;
	private IntegerProperty point;
	private BooleanProperty isFullfield;
	private IntegerProperty frequency;
	private IntegerProperty actualPoint;
	
	public ChallengeFX(Challenge c) {
		super();
		modellObject = c;
		IDNummber =  new SimpleIntegerProperty(c.getIDNummber());
		name = new SimpleStringProperty(c.getName());
		ownerID =  new SimpleIntegerProperty(c.getOwnerID());
		ownerName = new SimpleStringProperty(c.getOwnerName());
		point =  new SimpleIntegerProperty(c.getPoint());
		isFullfield = new SimpleBooleanProperty(c.isFullfield());
		frequency = new SimpleIntegerProperty(c.getFrequency());
		actualPoint = new SimpleIntegerProperty(c.getActualPoint());	
	}
	
	//Generate Getters and Setters
		public Challenge getModellObject() {
			return modellObject;
		}
		
		public String getName() {
			return name.get();
		}
		
		public void setName(String n) {
			name.set(n);
			modellObject.setName(n);
		}
		
		public StringProperty nameStringProperty() {
			return name;
		}
		
		public String getOwnerName() {
			return ownerName.get();
		}
		
		public void setOwnerName(String n) {
			ownerName.set(n);
			modellObject.setOwnerName(n);
		}
		
		public StringProperty ownerNameStringProperty() {
			return ownerName;
		}
		
		public int getOwnerID() {
			return ownerID.get();
		}
		
		public void setOwnerID(int ownerId) {
			ownerID.set(ownerId);
			modellObject.setOwnerID(ownerId);
		}
		
		public IntegerProperty ownerIDIntegerProperty() {
			return ownerID;
		}
		
		public int getPoint() {
			return point.get();
		}
		
		public void setPoint(int p) {
			point.set(p);
			modellObject.setPoint(p);
		}
		
		public IntegerProperty pointIntegerProperty() {
			return point;
		}
		
		public boolean isIsFullfield() {
			return isFullfield.get();
		}
		
		public void setIsFullfield(boolean p) {
			isFullfield.set(p);
			modellObject.setFullfield(p);
		}
		
		public BooleanProperty isFullfieldBooleanProperty() {
			return isFullfield;
		}
		
		public int getIDNummber() {
			return IDNummber.get();
		}
		
		public void setIDNummber(int p) {
			IDNummber.set(p);
			modellObject.setIDNummber(p);
		}
		
		public IntegerProperty IDNummberIntegerProperty() {
			return IDNummber;
		}
		
		public int getFrequency() {
			return frequency.get();
		}
		
		public void setFrequency(int p) {
			frequency.set(p);
			modellObject.setFrequency(p);
		}
		
		public IntegerProperty frequencyIntegerProperty() {
			return frequency;
		}
		

		public int getActualPoint() {
			return actualPoint.get();
		}
		
		public void setActualPoint(int p) {
			actualPoint.set(p);
			modellObject.setActualPoint(p);;
		}
		
		public IntegerProperty actualPointIntegerProperty() {
			return actualPoint;
		}


}
