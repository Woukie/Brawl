package org.woukie.brawl;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.plugin.java.JavaPlugin;
import org.woukie.brawl.commands.BrawlCommand;
import org.woukie.brawl.commands.BrawlTabComplete;
import org.woukie.brawl.game.GameManager;

public final class Main extends JavaPlugin {
	public final Logger Logger = java.util.logging.Logger.getLogger("Minecraft");
	public GameManager manager = null;
	
	@Override
	public void onEnable() {
		new BrawlCommand(this);
		new BrawlTabComplete(this);
		manager = new GameManager(this);
		
		Logger.log(Level.INFO, "[Brawl] Brawl v0.1 by Woukie enabled!");
	}
}
