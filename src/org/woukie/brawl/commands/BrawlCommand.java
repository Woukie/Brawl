package org.woukie.brawl.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.woukie.brawl.Main;

public class BrawlCommand implements CommandExecutor {
	String prefix = ChatColor.GREEN + "[BR] " + ChatColor.RESET;
	Map<String, String> invitations; // UUID, team
	
	public enum UsageErrors {
		ROOT, POINTS, WRONGSENDER, NONEXISTENTPLAYER, ALREADYINTEAM, TEAM, TEAMCREATE, TEAMALREADYEXISTS, ALREADYNOTINTEAM, NOTLEADER, NOTINVITED, NOTINTEAM, INVITE, CANNOTPERFORMONSELF, PROMOTE, THEYALREADYINTEAM, NOTSAMETEAM
	}
	
	private Main pluginMain;
	
	public BrawlCommand(Main _pluginMain) {
		invitations = new HashMap<String, String>();
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
					if (args.length == 3) { // Must have typed command correctly
						if (pluginMain.SQLManager.getTeam((Player) sender).equals("")) { // Cannot be in a team
							if (!pluginMain.SQLManager.checkTeamExists(args[2]) || args[2].equals("")) { // Team cannot exist already
								
								pluginMain.SQLManager.setPlayersTeam((Player) sender, args[2]);
								pluginMain.SQLManager.setLeader((Player) sender, true);
								
								sender.sendMessage(prefix + "Created team " + args[2]);
								
							} else {
								sendError(sender, UsageErrors.TEAMALREADYEXISTS);
							}
						} else {
							sendError(sender, UsageErrors.ALREADYINTEAM);
						}
					} else {
						sendError(sender, UsageErrors.TEAMCREATE);
					}
					
					break;
				case "leave": // If player is only leader, make random member leader
					String oldTeam = pluginMain.SQLManager.getTeam((Player) sender);
					
					if (oldTeam.equals("")) {
						sendError(sender, UsageErrors.ALREADYNOTINTEAM);
						return false;
					}
					
					pluginMain.SQLManager.setLeader((Player) sender, false);
					pluginMain.SQLManager.setPlayersTeam((Player) sender, "");
					
					sender.sendMessage(prefix + "Left team " + oldTeam);
					
					if (pluginMain.SQLManager.countMembers(oldTeam) == 0) { // In the case of disbanding, remove invitations
						for (Entry<String, String> entry : invitations.entrySet()) {
							if (entry.getValue().equals(oldTeam)) invitations.remove(entry.getKey());
						}
						
						return false;
					}
					if (pluginMain.SQLManager.countLeaders(oldTeam) == 0) pluginMain.SQLManager.promoteFirstMemberInTeam(oldTeam);
					
					break;
				case "join":
					if (!invitations.containsKey(((Player) sender).getUniqueId().toString())) { // If they were invited
						sendError(sender, UsageErrors.NOTINVITED);
						return false;
					}
					
					if (!pluginMain.SQLManager.getTeam((Player) sender).equals("")) { // If they aren't in a team
						sendError(sender, UsageErrors.ALREADYINTEAM);
						return false;
					}
					
					String teamJoining = invitations.get(((Player) sender).getUniqueId().toString());
					
					sender.sendMessage(prefix + "Joined team " + teamJoining + "!"); // Confirmation message to sender
					pluginMain.SQLManager.sendToAll(prefix + sender.getName() + " joined the team!", teamJoining); // Notify team
					pluginMain.SQLManager.setPlayersTeam((Player) sender, teamJoining); // Set team
					invitations.remove(((Player) sender).getUniqueId().toString()); // Remove from list
					
					break;
				case "promote": 
					if (pluginMain.SQLManager.checkLeader((Player) sender)) { // Sender has to be a leader
						if (args.length == 3) { // Must have all arguments
							if (!args[2].equals(sender.getName())) { // Cannot promote themselves
								if (players.contains(args[2])) { // Player must exist
									Player promotePlayer = Bukkit.getPlayer(args[2]);
									if (pluginMain.SQLManager.getTeam(promotePlayer).equals(pluginMain.SQLManager.getTeam((Player) sender))) { // Players must share same team
										
										promotePlayer.sendMessage(prefix + "You have been promoted by " + sender.getName() + "!");
										sender.sendMessage(prefix + "You promoted " + args[2]);
										
									} else {
										sendError(sender, UsageErrors.NOTSAMETEAM);
									}
								} else {
									sendError(sender, UsageErrors.NONEXISTENTPLAYER);
								}
							} else {
								sendError(sender, UsageErrors.CANNOTPERFORMONSELF);
							}
						} else {
							sendError(sender, UsageErrors.PROMOTE);
						}
					} else {
						sendError(sender, UsageErrors.NOTLEADER);
					}
					
					break;
				case "invite": 
					if (args.length == 3) { // Must have all arguments
						if (players.contains(args[2])) { // Invited player must exist
							if (!args[2].equals(sender.getName())) { // Sender can't invite themselves
								if (pluginMain.SQLManager.checkLeader((Player) sender)) { // Sender must be a leader
									if (!pluginMain.SQLManager.getTeam((Player) sender).equals("")) { // Sender must be in a team
										Player invited = Bukkit.getPlayer(args[2]);
										if (pluginMain.SQLManager.getTeam(invited).equals("")) { // Other player must not be in a team
											
											String team = pluginMain.SQLManager.getTeam((Player) sender);
											invitations.put(invited.getUniqueId().toString(), team);
											sender.sendMessage(prefix + "Invited player " + args[2] + " to " + team);
											invited.sendMessage(prefix + sender.getName() + " inviated you to team " + team);
											
											new java.util.Timer().schedule( // Delete invite after a while
										        new java.util.TimerTask() {
										            @Override
										            public void run() {
										            	if (invitations.containsKey(invited.getUniqueId().toString())) {
										            		invitations.remove(invited.getUniqueId().toString());
											                sender.sendMessage(prefix + "Invite to " + args[2] + " timed out!");
										            	}
										            }
										        }, 
										        5000
											);
											
										} else {
											sendError(sender, UsageErrors.THEYALREADYINTEAM);
										}
									} else {
										sendError(sender, UsageErrors.NOTINTEAM);
									}
								} else {
									sendError(sender, UsageErrors.NOTLEADER);
								}
							} else {
								sendError(sender, UsageErrors.CANNOTPERFORMONSELF);
							}
						} else {
							sendError(sender, UsageErrors.NONEXISTENTPLAYER);
						}
					} else {
						sendError(sender, UsageErrors.INVITE);
					}
					
					
					return false;
				default:
					sendError(sender, UsageErrors.TEAM);
					return false;
				}	
				
				
				break;
				
			case "pedestal":
				sender.sendMessage(prefix + "Not implemented yet!");
				pluginMain.SQLManager.countLeaders("a");
				
				break;
				
			case "points":
				int points = 0;
				
				if (args.length == 4) {
					try {
						points = Integer.parseInt(args[3]);
					} catch (NumberFormatException e) {
						sendError(sender, UsageErrors.POINTS);
						return false;
					}
					
				} else {
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
						pluginMain.SQLManager.addPoints((Player)sender, -points);
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
		case INVITE:
			sender.sendMessage(prefix + "Usage: /brawl invite <player>");
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
		case NOTLEADER:
			sender.sendMessage(prefix + "You are not the leader of this team!");
			break;
		case NOTINVITED:
			sender.sendMessage(prefix + "You have not been invited to a team!");
			break;
		case CANNOTPERFORMONSELF:
			sender.sendMessage(prefix + "You cannot perform this action on yourself!");
			break;
		case NOTINTEAM:
			sender.sendMessage(prefix + "You are not in a team!");
			break;
		case PROMOTE:
			sender.sendMessage(prefix + "Usage: /brawl promote <player>");
			break;
		case THEYALREADYINTEAM:
			sender.sendMessage(prefix + "This player is already in a team!");
			break;
		case NOTSAMETEAM:
			sender.sendMessage(prefix + "This player is not in your team!");
		}
	}
}
