package org.woukie.brawl;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.plugin.java.JavaPlugin;
import org.woukie.brawl.commands.BrawlCommand;
import org.woukie.brawl.commands.BrawlTabComplete;
import org.woukie.brawl.game.MenuManager;
import org.woukie.brawl.sql.SQLManager;

public final class Main extends JavaPlugin {
	public final Logger logger = java.util.logging.Logger.getLogger("Minecraft");
	public MenuManager menuManager;
	public SQLManager SQLManager;

	@Override
	public void onEnable() {
		new BrawlCommand(this);
		new BrawlTabComplete(this);
		menuManager = new MenuManager(this);
		SQLManager = new SQLManager(this, logger);
		
		logger.log(Level.INFO, "[Brawl] Brawl v0.1 by Woukie enabled!");
	}
	
	@Override
	public void onDisable() {
		SQLManager.quit();
		logger.log(Level.INFO, "[Brawl] Brawl v0.1 by Woukie saved and disabled.");
	}
}
