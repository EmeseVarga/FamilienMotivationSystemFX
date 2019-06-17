package FMS;


public abstract class Motivation_Element {
	
	private int IDNummber;
	private String name;
	private int ownerID ;
	private String ownerName;
	private int point;
	private boolean isFullfield;
	
	
	//Constructor
	public Motivation_Element(int iDNummber, String name, int ownerID, String ownerName, int point,
			boolean isFullfield) {
		super();
		IDNummber = iDNummber;
		this.name = name;
		this.ownerID = ownerID;
		this.ownerName = ownerName;
		this.point = point;
		this.isFullfield = isFullfield;
	}

	
	//Getters and Setters
	public int getIDNummber() {
		return IDNummber;
	}

	public void setIDNummber(int iDNummber) {
		IDNummber = iDNummber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getOwnerID() {
		return ownerID;
	}

	public void setOwnerID(int ownerID) {
		this.ownerID = ownerID;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public int getPoint() {
		return point;
	}

	public void setPoint(int point) {
		this.point = point;
	}

	public boolean isFullfield() {
		return isFullfield;
	}

	public void setFullfield(boolean isFullfield) {
		this.isFullfield = isFullfield;
	}
	

}
