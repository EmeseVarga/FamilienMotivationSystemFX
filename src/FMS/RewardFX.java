package FMS;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class RewardFX {
	
	private Reward modellObject;
	private StringProperty name;
	private IntegerProperty ownerID;
	private StringProperty ownerName;
	private IntegerProperty point;
	private BooleanProperty isFullfield;
	private IntegerProperty IDNummber;
	
	//Constructor 
	public RewardFX(Reward r) {
		super();
		modellObject = r;
		name = new SimpleStringProperty(r.getName());
		ownerID =  new SimpleIntegerProperty(r.getOwnerID());
		ownerName = new SimpleStringProperty(r.getOwnerName());
		point =  new SimpleIntegerProperty(r.getPoint());
		isFullfield = new SimpleBooleanProperty(r.isFullfield());
		IDNummber =  new SimpleIntegerProperty(r.getIDNummber());
	}
	
	
	//Generate Getters and Setters
	public Reward getModellObject() {
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

}
