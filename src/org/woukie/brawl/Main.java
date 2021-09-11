package org.woukie.brawl;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.plugin.java.JavaPlugin;
import org.woukie.brawl.commands.BrawlCommand;
import org.woukie.brawl.commands.BrawlTabComplete;
import org.woukie.brawl.game.GameManager;
import org.woukie.brawl.game.teams.TeamManager;

public final class Main extends JavaPlugin {
	public final Logger Logger = java.util.logging.Logger.getLogger("Minecraft");
	public GameManager gameManager = null;
	public TeamManager teamManager = null;
	
	@Override
	public void onEnable() {
		new BrawlCommand(this);
		new BrawlTabComplete(this);
		teamManager = new TeamManager(this);
		gameManager = new GameManager(this);
		
		Logger.log(Level.INFO, "[Brawl] Brawl v0.1 by Woukie enabled!");
	}
	
	@Override
	public void onDisable() {
		teamManager.SaveTeams();
		Logger.log(Level.INFO, "[Brawl] Brawl v0.1 by Woukie saved and disabled.");
	}
}
