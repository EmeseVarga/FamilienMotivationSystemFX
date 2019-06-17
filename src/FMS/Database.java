package FMS;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Database {

	// Instance variables for database connection
	public static final String DBlocation = "Database";
	public static final String connString = "jdbc:derby:" + DBlocation + ";create=true";

	// Instance variables for person table
	public static final String personTable = "Familymember";
	public static final String personID = "PersonID";
	public static final String personName = "Name";
	public static final String passworld = "passworld";
	public static final String actulaPoint = "actulaPoint";
	public static final String isAdministrator = "Administrator";

	// Instance variables for reward table
	public static final String rewardTable = "Reward";
	public static final String rewardID = "RewardID";
	public static final String rewardName = "Name";
	public static final String rewardPersonID = "Reward_OwnerID";
	public static final String rewardPersonName = "Reward_Owner_Name";
	public static final String rewardPoint = "Reward_Point";
	public static final String isFullfield = "Fullfield";

	// Instance variables for challenge table
	public static final String challengeTable = "Challenge";
	public static final String challengeID = "ChallengeID";
	public static final String challengeName = "Name";
	public static final String challengePersonID = "Challenge_OwnerID";
	public static final String challengePersonName = "Challenge_Owner_Name";
	public static final String challengePoint = "Challenge_Point";
	public static final String ischallengeFullfield = "Fullfield";
	public static final String challengefrequency = "Frequency";
	public static final String challengeActualPoint = "Actual_Point";

	
	// Create person table
	public static void createPersons() throws SQLException {

		String create = "CREATE TABLE " + personTable + "(" + personID + " INTEGER GENERATED ALWAYS AS IDENTITY, "
				+ personName + " VARCHAR(200), " + passworld + " CHAR(13) NOT NULL, " + actulaPoint + " INTEGER, "
				+ isAdministrator + " BOOLEAN, " + "PRIMARY KEY(" + personID + ")" + ")";
		Connection conn = null;
		Statement stmt = null;

		try {
			conn = DriverManager.getConnection(connString);
			ResultSet rs = conn.getMetaData().getTables(null, null, personTable.toUpperCase(),
					new String[] { "TABLE" });
			if (rs.next()) {
				return;
			}
			stmt = conn.createStatement();
			stmt.executeUpdate(create);
		} catch (SQLException e) {
			throw e;
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}

				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				throw e;
			}
		}

	}

	
	// Insert new person
	public static void insertPerson(Familymembers member) throws SQLException {

		String insert = "INSERT INTO " + personTable + "(" + personName + "," + passworld + "," + actulaPoint + ","
				+ isAdministrator + ")" + "VALUES(?, ?, ?, ?)";
		Connection conn = null;
		PreparedStatement pstmt = null;

		try {
			conn = DriverManager.getConnection(connString);
			pstmt = conn.prepareStatement(insert);
			pstmt.setString(1, member.getPersonName());
			pstmt.setString(2, member.getPassworld());
			pstmt.setInt(3, member.getActualPoint());
			pstmt.setBoolean(4, member.isAdministrator());

			pstmt.executeUpdate();
			System.out.println("Es gibt eine neue Familienmitglieder in der Person Tabelle");
		}

		catch (SQLException e) {
			throw e;
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}

				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				throw e;
			}
		}

		// Testing person table

		Statement stmt = null;
		ResultSet rs = null;
		try {
			conn = DriverManager.getConnection(connString);
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT * FROM " + personTable);

			while (rs.next()) {

				System.out.println(rs.getInt(personID) + ", " 
									+ rs.getString(personName) + ", " 
									+ rs.getString(passworld)	+ ", " 
									+ rs.getInt(actulaPoint) + ", " 
									+ rs.getBoolean(isAdministrator));

			}
			rs.close();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	
	// Create ArrayList from details of person table
	public static ArrayList<Familymembers> readFamilymembers() throws SQLException {

		Connection conn = null;
		Statement stmt = null;

		try {
			conn = DriverManager.getConnection(connString);
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM " + personTable);

			ArrayList<Familymembers> fm = new ArrayList<>();

			while (rs.next()) {
				fm.add(new Familymembers(rs.getInt(personID), rs.getString(personName), rs.getString(passworld),
						rs.getInt(actulaPoint), null, null, rs.getBoolean(isAdministrator)));
			}

			rs.close();
			return fm;
		} catch (SQLException e) {
			throw e;
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}

				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				throw e;
			}
		}

	}

	
	//Delete person from database
	public static void deletePerson(Familymembers f) throws SQLException {
		String delete = "DELETE FROM " + personTable + " WHERE " + personID + " = " + f.getPersonID();
		
		Connection conn = null;
		PreparedStatement pstmt = null;

		try {
			conn = DriverManager.getConnection(connString);
			pstmt = conn.prepareStatement(delete);

			pstmt.executeUpdate();
			System.out.println
			( f.getPersonID()  + " mit " + f.getPersonID() + " wurde gelöscht." );
		}

		catch (SQLException e) {
			throw e;
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				throw e;
			}
		}
	}
	
	
	//Update actual point in person table
	public static void updatePersonPoints(Familymembers f, int newPoint) throws SQLException {
		String update = " UPDATE " + personTable + " SET " + actulaPoint  + " = " + newPoint 
				+ " WHERE " + personID + " = " + f.getPersonID();
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			conn = DriverManager.getConnection(connString);
			pstmt = conn.prepareStatement(update);

			pstmt.executeUpdate();
		}

		catch (SQLException e) {
			throw e;
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}

				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				throw e;
			}
		}
	}
	
	
	// Create reward table
	public static void createReward() throws SQLException {

		String create = "CREATE TABLE " + rewardTable + "(" + rewardID + " INTEGER GENERATED ALWAYS AS IDENTITY, "
				+ rewardName + " VARCHAR(200), " 
				+ rewardPersonID + " INTEGER, " 
				+ rewardPersonName + " VARCHAR(200), " 
				+ rewardPoint + " INTEGER, "
				+ isFullfield + " BOOLEAN, " 
				+ "PRIMARY KEY(" + rewardID + ")" + ")";
		Connection conn = null;
		Statement stmt = null;

		try {
			conn = DriverManager.getConnection(connString);
			ResultSet rs = conn.getMetaData().getTables(null, null, rewardTable.toUpperCase(),
					new String[] { "TABLE" });
			if (rs.next()) {
				return;
			}
			stmt = conn.createStatement();
			stmt.executeUpdate(create);
		} catch (SQLException e) {
			throw e;
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}

				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				throw e;
			}
		}

	}

	
	// Insert new reward
	public static void insertReward(Reward reward) throws SQLException {

		String insert = "INSERT INTO " + rewardTable + "(" + rewardName + "," 
						+ rewardPersonID + "," 
						+ rewardPersonName + "," 
						+ rewardPoint + ","
						+ isFullfield + ")" + "VALUES(?, ?, ?, ?, ?)";
		Connection conn = null;
		PreparedStatement pstmt = null;

		try {
			conn = DriverManager.getConnection(connString);
			pstmt = conn.prepareStatement(insert);
			pstmt.setString(1, reward.getName());
			pstmt.setInt(2, reward.getOwnerID());
			pstmt.setString(3, reward.getOwnerName());
			pstmt.setInt(4, reward.getPoint());
			pstmt.setBoolean(5, reward.isFullfield());

			pstmt.executeUpdate();
			System.out.println("Es gibt eine neue Belohnung in der Belohnungen Tabelle");
		}

		catch (SQLException e) {
			throw e;
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}

				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				throw e;
			}
		}

		// Testing reward table

		Statement stmt = null;
		ResultSet rs = null;
		try {
			conn = DriverManager.getConnection(connString);
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT * FROM " + rewardTable);

			while (rs.next()) {

				System.out.println(
						rs.getInt(rewardID) + ", " 
						+ rs.getString(rewardName) + ", " 
						+ rs.getInt(rewardPersonID) + ", "
						+ rs.getString(rewardPersonName) + ", " 
						+ rs.getInt(rewardPoint) + ", " 
						+ rs.getBoolean(isFullfield));

			}
			rs.close();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	
	// Create ArrayList from personal details of reward table
	public static ArrayList<Reward> readRewards(Familymembers f) throws SQLException {

		Connection conn = null;
		Statement stmt = null;

		try {
			conn = DriverManager.getConnection(connString);
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(
					"SELECT * FROM " + rewardTable + " WHERE " + rewardPersonID + " = " + f.getPersonID());

			ArrayList<Reward> rewardList = new ArrayList<>();

			while (rs.next()) {
				rewardList.add(new Reward(rs.getInt(rewardID), 
											rs.getString(rewardName), 
											rs.getInt(rewardPersonID),
											rs.getString(rewardPersonName),
											rs.getInt(rewardPoint), 
											rs.getBoolean(isFullfield)));
			}
			rs.close();
			return rewardList;
		} catch (SQLException e) {
			throw e;
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}

				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				throw e;
			}
		}
	}

	
	//Create ArrayList from all details of reward table
	public static ArrayList<Reward> readAllRewards() throws SQLException{
	
		Connection conn = null;
		Statement stmt = null;

		try {
			conn = DriverManager.getConnection(connString);
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(
					"SELECT * FROM " + rewardTable);

			ArrayList<Reward> rewardList = new ArrayList<>();

			while (rs.next()) {
				rewardList.add(new Reward(rs.getInt(rewardID), 
						rs.getString(rewardName), 
						rs.getInt(rewardPersonID),
						rs.getString(rewardPersonName),
						rs.getInt(rewardPoint), 
						rs.getBoolean(isFullfield)));
			}
			rs.close();
			
			return rewardList;
		} catch (SQLException e) {
			throw e;
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}

				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				throw e;
			}
		}
	}
	
	
	// Delete reward
	public static void deleteReward(Reward reward) throws SQLException {
		String delete = "DELETE FROM " + rewardTable + " WHERE " + rewardID + " = " + reward.getIDNummber();
		
		Connection conn = null;
		PreparedStatement pstmt = null;

		try {
			conn = DriverManager.getConnection(connString);
			pstmt = conn.prepareStatement(delete);

			pstmt.executeUpdate();
			System.out.println
			("Herausforderung (" + reward.getName() + ")" + " mit " + reward.getIDNummber() + " wurde gelöscht." );
		}

		catch (SQLException e) {
			throw e;
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				throw e;
			}
		}
	}

	
	//Update reward table
	public static void updateReward(Reward reward) throws SQLException {
		String update = " UPDATE " + rewardTable + " SET " 
				+ isFullfield + " = " + reward.isFullfield()
				+ " WHERE " + rewardID + " = " + reward.getIDNummber();
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			conn = DriverManager.getConnection(connString);
			pstmt = conn.prepareStatement(update);

			pstmt.executeUpdate();
		}

		catch (SQLException e) {
			throw e;
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}

				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				throw e;
			}
		}
	}
	
	
	// Create challenge table
	public static void createChallenge() throws SQLException {

		String create = "CREATE TABLE " + challengeTable + "(" + challengeID + " INTEGER GENERATED ALWAYS AS IDENTITY, "
				+ challengeName + " VARCHAR(200), " 
				+ challengePersonID + " INTEGER, " 
				+ challengePersonName + " VARCHAR(200), "
				+ challengePoint + " INTEGER, "
				+ ischallengeFullfield + " BOOLEAN, " 
				+ challengefrequency + " INTEGER, " 
				+ challengeActualPoint + " INTEGER, " 
				+ "PRIMARY KEY(" + challengeID + ")" + ")";
		
		Connection conn = null;
		Statement stmt = null;

		try {
			conn = DriverManager.getConnection(connString);
			ResultSet rs = conn.getMetaData().getTables(null, null, challengeTable.toUpperCase(),
					new String[] { "TABLE" });
			if (rs.next()) {
				return;
			}
			stmt = conn.createStatement();
			stmt.executeUpdate(create);
		} catch (SQLException e) {
			throw e;
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}

				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				throw e;
			}
		}

	}

	
	// Insert new challenge
	public static void insertChallenge(Challenge challenge) throws SQLException {

		String insert = "INSERT INTO " + challengeTable + "(" 
				+ challengeName + "," 
				+ challengePersonID + ","
				+ challengePersonName + "," 
				+ challengePoint + "," 
				+ ischallengeFullfield + "," 
				+ challengefrequency + "," 
				+ challengeActualPoint + ")"
				+ "VALUES(?, ?, ?, ?, ?, ?, ?)";
		Connection conn = null;
		PreparedStatement pstmt = null;

		try {
			conn = DriverManager.getConnection(connString);
			pstmt = conn.prepareStatement(insert);
			pstmt.setString(1, challenge.getName());
			pstmt.setInt(2, challenge.getOwnerID());
			pstmt.setString(3, challenge.getOwnerName());
			pstmt.setInt(4, challenge.getPoint());
			pstmt.setBoolean(5, challenge.isFullfield());
			pstmt.setInt(6, challenge.getFrequency());
			pstmt.setInt(7, challenge.getActualPoint());

			pstmt.executeUpdate();
			System.out.println("Es gibt eine neue Herausforderung in der Herausforderung Tabelle:" + challenge.getName());
		}

		catch (SQLException e) {
			throw e;
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}

				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				throw e;
			}
		}

		// Testing challenge table

		Statement stmt = null;
		ResultSet rs = null;
		try {
			conn = DriverManager.getConnection(connString);
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT * FROM " + challengeTable);

			while (rs.next()) {

				System.out.println(rs.getInt(challengeID) + ", " 
								+ rs.getString(challengeName) + ", "
								+ rs.getInt(challengePersonID) + ", " 
								+ rs.getString(challengePersonName) + ", "
								+ rs.getInt(challengePoint) + ", "
								+ rs.getBoolean(ischallengeFullfield) + ", " 
								+ rs.getInt(challengefrequency)+ ", " 
								+ rs.getInt(challengeActualPoint));
			}
			rs.close();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	
	// Create ArrayList from personal details of challenge table
	public static ArrayList<Challenge> readChallenges(Familymembers f) throws SQLException {

		Connection conn = null;
		Statement stmt = null;

		try {
			conn = DriverManager.getConnection(connString);
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(
					"SELECT * FROM " + challengeTable + " WHERE " + challengePersonID + " = " + f.getPersonID());

			ArrayList<Challenge> challangeList = new ArrayList<>();

			while (rs.next()) {
				challangeList.add(new Challenge(rs.getInt(challengeID), 
						rs.getString(challengeName),
						rs.getInt(challengePersonID), 
						rs.getString(challengePersonName),
						rs.getInt(challengePoint), 
						rs.getBoolean(ischallengeFullfield),
						rs.getInt(challengefrequency), 
						rs.getInt(challengeActualPoint)));
			}

			rs.close();
			return challangeList;
		} catch (SQLException e) {
			throw e;
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}

				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				throw e;
			}
		}

	}

	
	// Delete challenge
	public static void deleteChallenge(Challenge challenge) throws SQLException {

		String delete = "DELETE FROM " + challengeTable + " WHERE " + challengeID + " = " + challenge.getIDNummber();
		
		Connection conn = null;
		PreparedStatement pstmt = null;

		try {
			conn = DriverManager.getConnection(connString);
			pstmt = conn.prepareStatement(delete);

			pstmt.executeUpdate();
			System.out.println
			("Herausforderung (" + challenge.getName() + ")" + " mit " + challenge.getIDNummber() + " wurde gelöscht." );
		}

		catch (SQLException e) {
			throw e;
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}

				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				throw e;
			}
		}
	}

	
	//Update challenge table
	public static void updateChallenge(Familymembers f, Challenge c) throws SQLException {

		String update = " UPDATE " + challengeTable + " SET " + challengeActualPoint  + " = " + c.getActualPoint() + " , " 
				+ ischallengeFullfield + " = " + c.isFullfield()
				+ " WHERE " + challengeID + " = " + c.getIDNummber();
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			conn = DriverManager.getConnection(connString);
			pstmt = conn.prepareStatement(update);

			pstmt.executeUpdate();
			System.out.println("Challenge update ");
		}

		catch (SQLException e) {
			throw e;
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}

				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				throw e;
			}
		}
	}
	
}

