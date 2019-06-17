package FMS;

import java.util.ArrayList;


public class Familymembers {
	
	private int personID;
	private String personName;
	private String passworld;
	private int actualPoint;
	private ArrayList<Reward> reawardList;
	private ArrayList<Challenge> challengeList;
	private boolean isAdministrator;
	
	
	//Constructor
	public Familymembers(int personID, String personName, String passworld,	int actualPoint,
							ArrayList<Reward> reawardList, ArrayList<Challenge> challengeList, boolean isAdministrator) {
		super();
		this.personID = personID;
		this.personName = personName;
		this.passworld = passworld;
		this.actualPoint = actualPoint;
		this.reawardList = reawardList;
		this.challengeList = challengeList;
		this.isAdministrator = isAdministrator;
	}

	
	//Getters and Setters
	public int getPersonID() {
		return personID;
	}

	public void setPersonID(int personID) {
		this.personID = personID;
	}

	public String getPersonName() {
		return personName;
	}

	public void setPersonName(String personName) {
		this.personName = personName;
	}

	public String getPassworld() {
		return passworld;
	}

	public void setPassworld(String passworld) {
		this.passworld = passworld;
	}

	public int getActualPoint() {
		return actualPoint;
	}

	public void setActualPoint(int actualPoint) {
		this.actualPoint = actualPoint;
	}

	public ArrayList<Reward> getReawardList() {
		return reawardList;
	}

	public void setReawardList(ArrayList<Reward> reawardList) {
		this.reawardList = reawardList;
	}

	public ArrayList<Challenge> getChallengeList() {
		return challengeList;
	}

	public void setChallengeList(ArrayList<Challenge> challengeList) {
		this.challengeList = challengeList;
	}

	public boolean isAdministrator() {
		return isAdministrator;
	}

	public void setAdministrator(boolean isAdministrator) {
		this.isAdministrator = isAdministrator;
	}
	
	

}

