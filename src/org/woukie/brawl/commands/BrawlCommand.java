package org.woukie.brawl.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.woukie.brawl.Main;

public class BrawlCommand implements CommandExecutor {

	private Main pluginMain;
	
	public BrawlCommand(Main _pluginMain) {
		pluginMain = _pluginMain;
		pluginMain.getCommand("brawl").setExecutor(this);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		//Logger.log(Level.INFO, sender + " | " + cmd + " | " + label + " | " + args);
		
		if (args.length < 1) {
			sendUsage(sender);
			return false;
		}
		
		if (!(sender instanceof Player)) {
			sender.sendMessage("This command can only be ran by a player!");
			return false;
		}
		
		switch (args[0].toLowerCase()) {
			case "manage":
				
				pluginMain.gameManager.openManager(sender);
				break;
				
			case "team":
				
				break;
				
			case "pedestal":
				
				break;
				
			default:
				sendUsage(sender);
				
				break;
		}
		return false;
	}
	
	public void sendUsage(CommandSender sender) {
		sender.sendMessage("Usage: /brawl [manage | team | pedestal]");
	}
}
