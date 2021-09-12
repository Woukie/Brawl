package org.woukie.brawl.sql;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

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
			logger.log(Level.INFO, "Database not connected!");
		}
		
		if (SQL.isConnected()) {
			logger.log(Level.INFO, "Database connected!");
			data.createTable();
		}
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		data.createPlayer(event.getPlayer());
	}
	
	public void Quit() {
		SQL.disconnect();
	}
}
