
 package FMS;



public class Challenge extends Motivation_Element { 
	
	private int frequency;
	private int actualPoint;

	
	//Konsturctor
	public Challenge(int iDNummber, String name, int ownerID, String ownerName, int point,
			boolean isFullfield, int frequency, int actualPoint) {
		super(iDNummber, name, ownerID, ownerName, point, isFullfield);
		this.frequency = frequency;
		this.actualPoint = actualPoint;
		
	}

	
	//Getters and Setters
	public int getActualPoint() {
		return actualPoint;
	}

	public void setActualPoint(int actualPoint) {
		this.actualPoint = actualPoint;
	}

	public int getFrequency() {
		return frequency;
	}

	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}

}
