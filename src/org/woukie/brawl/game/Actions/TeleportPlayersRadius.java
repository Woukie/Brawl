package org.woukie.brawl.game.Actions;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class TeleportPlayersRadius implements Action {
	ArrayList<UUID> players;
	float radius, centerX, centerZ;
	String worldName;
	
	@Override
	public void trigger() {
		for (UUID uuid : players) {
			Player player = Bukkit.getPlayer(uuid);
			
			double x = (Math.random() * 2 - 1) * radius;
			double z = Math.sqrt(radius * radius - x * x) * (Math.random() * 2 - 1);
			
			player.teleport(Bukkit.getWorld(worldName).getHighestBlockAt((int) (x + centerX), (int) (z + centerZ)).getLocation().add(0, 1, 0));
		}
	}
}