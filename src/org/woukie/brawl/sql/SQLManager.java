package org.woukie.brawl.sql;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.woukie.brawl.Main;

public class SQLManager implements Listener {
	MySQL SQL;
	SQLGetter data;
	
	public SQLManager(Main pluginMain, Logger logger) {
		pluginMain.getServer().getPluginManager().registerEvents(this, pluginMain);
		
		SQL = new MySQL();
		data = new SQLGetter(this);
		
		try {
			SQL.connect();
		} catch (ClassNotFoundException | SQLException e) {
			logger.log(Level.INFO, "[Brawl] Database not connected!");
		}
		
		if (SQL.isConnected()) {
			logger.log(Level.INFO, "[Brawl] Database connected!");
			data.createTable();
		}
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		data.createPlayer(event.getPlayer());
	}
	
	public void quit() {
		SQL.disconnect();
	}
	
	public void addPoints(Player player, Integer points) {
		data.addPoints(player.getUniqueId(), points);
	}
	
	public void setPoints(Player player, Integer points) {
		data.addPoints(player.getUniqueId(), -data.getPoints(player.getUniqueId()) + points);
	}
	
	public String getTeam(Player player) {
		return data.getTeam(player.getUniqueId());
	}
	
	public void setPlayersTeam(Player player, String name) {
		data.setTeam(player.getUniqueId(), name);
	}
	
	public void setLeader(Player player, Boolean leader) {
		data.setLeader(player.getUniqueId(), leader);
	}
	
	public boolean checkTeamExists(String team) {
		return false;
	}
	
	public int countLeaders(String team) {
		return data.countLeaders(team);
	}
	
	public int countMembers(String team) { // Includes leaders
		return data.countMembers(team);
	}
	
	public void promoteFirstMemberInTeam(String team) {
		Player player = Bukkit.getPlayer(data.getFirstPlayer(team));
		setLeader(player, true);
	}
	
	public boolean checkLeader(Player player) {
		return data.checkIfLeader(player.getUniqueId());
	}
}
