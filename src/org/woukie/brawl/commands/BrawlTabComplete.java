package org.woukie.brawl.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.woukie.brawl.Main;

public class BrawlTabComplete implements TabCompleter {

	public BrawlTabComplete(Main pluginMain) {
		pluginMain.getCommand("brawl").setTabCompleter(this);
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player && command.getName().equalsIgnoreCase("brawl")) {
			if (args.length == 1) {
				ArrayList<String> a = new ArrayList<String>();
				a.add("manage");
				a.add("pedestal");
				a.add("teams");
				
				return a;
			}
			
			if (args.length == 2 && args[0].equals("teams")) {
				ArrayList<String> a = new ArrayList<String>();
				a.add("create");
				a.add("join");
				a.add("leave");
				
				return a;
			}
			
			if (args.length == 2 && args[0].equals("pedestal")) {
				ArrayList<String> a = new ArrayList<String>();
				a.add("create");
				a.add("delete");
				
				return a;
			}
		}
	
		return null;
	}
	
}
