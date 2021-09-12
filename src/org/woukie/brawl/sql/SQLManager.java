package org.woukie.brawl.sql;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

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
		
	}
	
	public void setPoints(Player player, Integer points) {
		
	}
	
	public String getTeam(Player player) {
		return "";
	}
	
	public void setPlayersTeam(Player player, String name) {
		
	}
	
	public void setLeader(Player player, boolean leader) {
		
	}
	
	public boolean checkTeamExists(String team) {
		return false;
	}
	
	public int countLeaders(String team) {
		return 0;
	}
	
	public void promoteFirstMemberInTeam(String team) {
		
	}
}
