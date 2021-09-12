package org.woukie.brawl.sql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.entity.Player;

public class SQLGetter {
	
	private SQLManager SQLManager;
	
	public SQLGetter(SQLManager _SQLManager) {
		SQLManager = _SQLManager;
	}
	
	public void createTable() {
		PreparedStatement ps;
		try {
			ps = SQLManager.SQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS teams "
					+ "(UUID VARCHAR(100),POINTS INT(100),TEAM VARCHAR(100),LEADER TINYINT(1),PRIMARY KEY (UUID))");
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void createPlayer(Player player) {
		try {
			UUID uuid = player.getUniqueId();
			
			if (!exists(uuid)) { // if does not exist create new entry
				PreparedStatement ps2 = SQLManager.SQL.getConnection().prepareStatement("INSERT IGNORE INTO teams "
						+ "(UUID,POINTS,TEAM,LEADER) VALUES (?,?,?,?)");
				ps2.setString(1, uuid.toString());
				ps2.setInt(2, 0);
				ps2.setString(3, "");
				ps2.setInt(4, 0);
				ps2.executeUpdate();
				
				return;
			}
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public boolean exists(UUID uuid) {
		try {
			PreparedStatement ps = SQLManager.SQL.getConnection().prepareStatement("SELECT * FROM teams WHERE UUID=?");
			ps.setString(1, uuid.toString());
			
			ResultSet results = ps.executeQuery();
			return results.next();
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public void addPoints(UUID uuid, int points) {
		try {
			PreparedStatement ps = SQLManager.SQL.getConnection().prepareStatement("UPDATE teams SET POINTS=? WHERE UUID=?");
			ps.setInt(1, getPoints(uuid) + points);
			ps.setString(2, uuid.toString());
			ps.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public int getPoints(UUID uuid) {
		try {
			PreparedStatement ps = SQLManager.SQL.getConnection().prepareStatement("SELECT POINTS FROM teams WHERE UUID=?");
			ps.setString(1, uuid.toString());
			ResultSet rs = ps.executeQuery();
			
			int points = 0;
			if (rs.next()) {
				points = rs.getInt("POINTS");
				return points;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return 0;
	}
	
	public String getTeam(UUID uuid) {
		try {
			PreparedStatement ps = SQLManager.SQL.getConnection().prepareStatement("SELECT TEAM FROM teams WHERE UUID=?");
			ps.setString(1, uuid.toString());
			ResultSet rs = ps.executeQuery();
			
			String team = "";
			if (rs.next()) {
				team = rs.getString("TEAM");
				return team;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return "";
	}
	
	public void setTeam(UUID uuid, String team) {
		try {
			PreparedStatement ps = SQLManager.SQL.getConnection().prepareStatement("UPDATE teams SET TEAM=? WHERE UUID=?");
			ps.setString(1, team);
			ps.setString(2, uuid.toString());
			ps.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void setLeader(UUID uuid, Boolean leader) {
		try {
			PreparedStatement ps = SQLManager.SQL.getConnection().prepareStatement("UPDATE teams SET LEADER=? WHERE UUID=?");
			ps.setInt(1, leader ? 1 : 0);
			ps.setString(2, uuid.toString());
			ps.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public int countMembers(String team) {
		try {
			PreparedStatement ps = SQLManager.SQL.getConnection().prepareStatement("SELECT COUNT(*) FROM teams WHERE TEAM=?");
			ps.setString(1, team);
			ResultSet rs = ps.executeQuery();
			
			rs.next();
			
			return rs.getInt(1);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return 0;
	}
	
	public int countLeaders(String team) {
		try {
			PreparedStatement ps = SQLManager.SQL.getConnection().prepareStatement("SELECT COUNT(*) FROM teams WHERE TEAM=? AND LEADER=?");
			ps.setString(1, team);
			ps.setInt(2, 1);
			ResultSet rs = ps.executeQuery();
			
			rs.next();
			
			return rs.getInt(1);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return 0;
	}
	
	public String getFirstPlayer(String team) { // Returns UUID of player in string form
		try {
			PreparedStatement ps = SQLManager.SQL.getConnection().prepareStatement("SELECT UUID FROM teams WHERE TEAM=?");
			ps.setString(1, team);
			ResultSet rs = ps.executeQuery();
			
			rs.next();
			
			return rs.getString(1);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return "";
	}
	
	public Boolean checkIfLeader(UUID uuid) {
		try {
			PreparedStatement ps = SQLManager.SQL.getConnection().prepareStatement("SELECT LEADER FROM teams WHERE UUID=?");
			ps.setString(1, uuid.toString());
			ResultSet rs = ps.executeQuery();
			
			rs.next();
			
			return rs.getInt(1) == 1 ? true : false;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}
}
