package org.woukie.brawl.game.teams;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.woukie.brawl.Main;

import com.google.gson.JsonObject;

public class TeamManager implements Listener {
	// There is a team for every player (offline and online)
	
	private ArrayList<Team> teams = new ArrayList<Team>();
	private File path;
	
	public TeamManager(Main pluginMain) {
		path = new File(pluginMain.getDataFolder() + "/teams_data");
		Bukkit.getPluginManager().registerEvents(this, pluginMain);
		LoadTeams();
	}
	
	public void addPoints(int points, Player player) {
		
	}
	
	public void LoadTeams() {
		
	}
	
	public void SaveTeams() {
		JsonObject teamData = new JsonObject();
	}
	
	// Called when a player joins the game without a team
	@EventHandler
	private void Join(PlayerJoinEvent event) {
		String playerUUID = event.getPlayer().getUniqueId().toString();
		
		if (CheckTeam(playerUUID) == false) {
			teams.add(new Team(playerUUID));
		}
	}
	
	private boolean CheckTeam(String UUID) {
		for(Team team : teams) {
			if (team.teamMates.containsKey(UUID)) return true;
		}
		
		return false;
	}
}
