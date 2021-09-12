package org.woukie.brawl.commands;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.woukie.brawl.Main;

public class BrawlCommand implements CommandExecutor {
	String prefix = ChatColor.GREEN + "[BR] ";
	
	public enum UsageErrors {
		ROOT, POINTS, WRONGSENDER, NONEXISTENTPLAYER, ALREADYINTEAM, TEAM, TEAMCREATE, TEAMALREADYEXISTS, ALREADYNOTINTEAM
	}
	
	private Main pluginMain;
	
	public BrawlCommand(Main _pluginMain) {
		pluginMain = _pluginMain;
		pluginMain.getCommand("brawl").setExecutor(this);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		//Logger.log(Level.INFO, sender + " | " + cmd + " | " + label + " | " + args);
		
		ArrayList<String> players = new ArrayList<String>();
		
		for (Player player : Bukkit.getOnlinePlayers()) {
			players.add(player.getName());
		}
		
		if (args.length < 1) {
			sendError(sender, UsageErrors.ROOT);
			return false;
		}
		
		if (!(sender instanceof Player)) {
			sendError(sender, UsageErrors.WRONGSENDER);
			return false;
		}
		
		switch (args[0].toLowerCase()) {
			case "manage":
				
				pluginMain.gameManager.openManager(sender);
				break;
				
			case "team":
				if (args.length <= 1) {
					sendError(sender, UsageErrors.TEAM);
					return false;
				}
				
				switch (args[1]) {
				case "create":
					if (args.length != 3) {
						sendError(sender, UsageErrors.TEAMCREATE);
						return false;
					}
					
					if(!pluginMain.SQLManager.getTeam((Player) sender).equals("")) {
						sendError(sender, UsageErrors.ALREADYINTEAM);
						return false;
					}
					
					if (pluginMain.SQLManager.checkTeamExists(args[2]) || args[2].equals("")) {
						sendError(sender, UsageErrors.TEAMALREADYEXISTS);
						return false;
					}
					
					pluginMain.SQLManager.setPlayersTeam((Player) sender, args[2]);
					pluginMain.SQLManager.setLeader((Player) sender, true);
					
					break;
				case "leave": // Leave team, if player is only leader, make random member leader
					String oldTeam = pluginMain.SQLManager.getTeam((Player) sender);
					if (oldTeam.equals("")) {
						sendError(sender, UsageErrors.ALREADYNOTINTEAM);
						return false;
					}
					
					pluginMain.SQLManager.setLeader((Player) sender, false);
					pluginMain.SQLManager.setPlayersTeam((Player) sender, "");
					
					if (pluginMain.SQLManager.countLeaders(oldTeam) == 0) pluginMain.SQLManager.promoteFirstMemberInTeam(oldTeam);
					
					break;
				case "join":
					
					
					break;
				case "promote":
					break;
				case "invite":
					break;
				default:
					sendError(sender, UsageErrors.TEAM);
					return false;
				}	
				
				
				break;
				
			case "pedestal":
				
				break;
				
			case "points":
				int points = 0;
				
				if (args.length != 4) {
					sendError(sender, UsageErrors.POINTS);
					return false;
				}
				
				try {
					points = Integer.parseInt(args[3]);
				} catch (NumberFormatException e) {
					sendError(sender, UsageErrors.POINTS);
					return false;
				}
				
				if (players.contains(args[1])) {
					switch (args[2]) {
					case "set":
						pluginMain.SQLManager.setPoints((Player)sender, points);
						sender.sendMessage(prefix + "Set player " + args[1] + "'s points to " + points);
						break;
					case "add":
						pluginMain.SQLManager.addPoints((Player)sender, points);
						sender.sendMessage(prefix + "Added " + points + " points to " + args[1]);
						break;
					case "reduce":
						pluginMain.SQLManager.setPoints((Player)sender, points);
						sender.sendMessage(prefix + "Removed " + points + " points from " + args[1]);
						break;
					default:
						sendError(sender, UsageErrors.POINTS);
						return false;
					}
				} else {
					sendError(sender, UsageErrors.NONEXISTENTPLAYER);
					return false;
				}
				
				break;
				
			default:
				sendError(sender, UsageErrors.ROOT);
				
				break;
		}
		return false;
	}
	
	public void sendError(CommandSender sender, UsageErrors error) {
		switch (error) {
		case ROOT:
			sender.sendMessage(prefix + "Usage: /brawl [manage | team | pedestal | points]");
			break;
		case POINTS:
			sender.sendMessage(prefix + "Usage: /brawl points <player> [set | add | reduce] <points>");
			break;	
		case TEAM:
			sender.sendMessage(prefix + "Usage: /brawl team [create | join | leave | promote | invite]");
			break;
		case TEAMCREATE:
			sender.sendMessage(prefix + "Usage: /brawl team create <name>");
			break;
		case WRONGSENDER:
			sender.sendMessage(prefix + "This command can only be ran by a player!");
			break;
		case NONEXISTENTPLAYER:
			sender.sendMessage(prefix + "This player does not exist!");
			break;
		case ALREADYINTEAM:
			sender.sendMessage(prefix + "You are allready in a team!");
			break;
		case TEAMALREADYEXISTS:
			sender.sendMessage(prefix + "A team by this name already exists!");
			break;
		case ALREADYNOTINTEAM:
			sender.sendMessage(prefix + "You are not in a team!");
			break;
		}
	}
}
