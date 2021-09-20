package org.woukie.brawl.game;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.woukie.brawl.Main;
import org.woukie.brawl.game.Events.Event;
import org.woukie.brawl.utility.Utility;


public class MenuManager implements Listener {
	private Inventory mainMenu, eventsMenu, teamsViewer, teamsManager, teamsSettings;
	
	// Menu names (refer to switch at onInventoryClick() for how to change without breaking)
	private String mainName = "Brawl Main Menu";
	private String eventsName = "Brawl Events Menu";
	private String teamsName = "Brawl Teams Menu";
	
	// Button names (uses colour codes due to being in a switch statement later)
	private final String mainButtonName = "§eMain Menu";
	private final String eventButtonName = "§eEvent Manager";
	private final String teamsViewerButtonName = "§eTeams Viewer";
	private final String teamsManagerButtonName = "§eTeams Manager";
	private final String teamsSettingsButtonName = "§eTeams Settings";
	private final String addEventButtonName = "§eAdd Event";

	// Button descriptions
	private String mainButtonLore = ChatColor.GREEN + "Go back to the main menu";
	private String eventButtonLore = ChatColor.GREEN + "Change how the game plays!";
	private String teamsViewerButtonLore = ChatColor.GREEN + "See and review teams";
	private String teamsManagerButtonLore = ChatColor.GREEN + "Manage teams";
	private String teamsSettingsButtonLore = ChatColor.GREEN + "Change teams settings";
	
	private int teamsPageNum, eventsPageNum; // Teams viewer and events viewer page (both auto clamped when menu updated)
	
	private Main pluginMain;
	
	// Handles menus/navigating menus in game
	public MenuManager(Main _pluginMain) {
		pluginMain = _pluginMain;
		Bukkit.getPluginManager().registerEvents(this, pluginMain);
		
		// TODO: store specific events menus on the event object for easier code management
		
		makeMainMenu(); // Root menu for events and teams
		makeEventsMenu(); // Menu shows all events
		makeTeamsViewer(); // Menu showing all current teams
		makeTeamsManager(); // Intermediate menu for teams settings and viewer
		makeTeamsSettings(); // Menu for changing global settings for teams
	}
	
	public void makeMainMenu() {
		mainMenu = Bukkit.createInventory(null, 27, mainName);
		ItemStack[] items = new ItemStack[27];
		
		for (int i = 0; i < 9; i++) {
			items[i] = Utility.setName(new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1), " ");
			items[i + 9] = Utility.setName(new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE, 1), " ");
			items[i + 18] = Utility.setName(new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1), " ");
		}
		
		mainMenu.setContents(items);
		mainMenu.setItem(11, Utility.setName(new ItemStack(Material.EMERALD, 1), eventButtonName, eventButtonLore));
		mainMenu.setItem(15, Utility.setName(new ItemStack(Material.PLAYER_HEAD, 1), teamsManagerButtonName, teamsManagerButtonLore));
	}
	
	
	public void makeEventsMenu() {
		eventsMenu = Bukkit.createInventory(null, 54, eventsName);
		ItemStack[] items = new ItemStack[54];
		
		for (int i = 0; i < 9; i++) {
			items[i] = Utility.setName(new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1), " ");
			items[i + 45] = Utility.setName(new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1), " ");
		}
		
				
		eventsMenu.setContents(items);

		eventsMenu.setItem(51, Utility.getSkull("https://textures.minecraft.net/texture/2a3b8f681daad8bf436cae8da3fe8131f62a162ab81af639c3e0644aa6abac2f", ChatColor.YELLOW + "Next Page"));
		eventsMenu.setItem(49, Utility.getSkull("https://textures.minecraft.net/texture/3edd20be93520949e6ce789dc4f43efaeb28c717ee6bfcbbe02780142f716", addEventButtonName));
		eventsMenu.setItem(47, Utility.getSkull("https://textures.minecraft.net/texture/8652e2b936ca8026bd28651d7c9f2819d2e923697734d18dfdb13550f8fdad5f", ChatColor.YELLOW + "Previous Page"));

		eventsMenu.setItem(4, Utility.setName(new ItemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE), mainButtonName, mainButtonLore)); // main menu button
	}

	public void makeTeamsViewer() { // shows all player heads (opens from manager)
		teamsViewer = Bukkit.createInventory(null, 54, teamsName);
		ItemStack[] items = new ItemStack[54];
		
		for (int i = 0; i < 9; i++) {
			items[i] = Utility.setName(new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1), " ");
			items[i + 45] = Utility.setName(new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1), " ");
		}
		
		teamsViewer.setContents(items);
		
		teamsViewer.setItem(51, Utility.getSkull("https://textures.minecraft.net/texture/2a3b8f681daad8bf436cae8da3fe8131f62a162ab81af639c3e0644aa6abac2f", ChatColor.YELLOW + "Next Page"));
		teamsViewer.setItem(47, Utility.getSkull("https://textures.minecraft.net/texture/8652e2b936ca8026bd28651d7c9f2819d2e923697734d18dfdb13550f8fdad5f", ChatColor.YELLOW + "Previous Page"));
		
		teamsViewer.setItem(4, Utility.setName(new ItemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE), teamsManagerButtonName, teamsManagerButtonLore)); // Teams menu button
	}
	
	
	public void makeTeamsManager() { // Menu for setting up permissions/default settings/max players etc. (opens from main menu)
		teamsManager = Bukkit.createInventory(null, 27, teamsName);
		
		ItemStack[] items = new ItemStack[27];
		
		for (int i = 0; i < 9; i++) {
			items[i] = Utility.setName(new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1), " ");
			items[i + 18] = Utility.setName(new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1), " ");
		}
		
		teamsManager.setContents(items);
		
		teamsManager.setItem(4, Utility.setName(new ItemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE), mainButtonName, mainButtonLore)); // main menu button
		teamsManager.setItem(11, Utility.setName(new ItemStack(Material.PLAYER_HEAD), teamsViewerButtonName, teamsViewerButtonLore)); // view teams
		teamsManager.setItem(15, Utility.setName(new ItemStack(Material.REPEATER), teamsSettingsButtonName, teamsSettingsButtonLore)); // edit settings
	}
	
	public void makeTeamsSettings() {
		teamsSettings = Bukkit.createInventory(null, 54, teamsName);
		
		ItemStack[] items = new ItemStack[54];
		
		for (int i = 0; i < 9; i++) {
			items[i] = Utility.setName(new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1), " ");
			items[i + 45] = Utility.setName(new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1), " ");
		}
		
		teamsSettings.setContents(items);
		
		teamsSettings.setItem(4, Utility.setName(new ItemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE), teamsManagerButtonName, teamsManagerButtonLore)); // Previous page button
		teamsSettings.setItem(20, Utility.setName(new ItemStack(Material.BARRIER), ChatColor.YELLOW + "Team commands", ChatColor.GREEN + "Can players do team commands like /br team leave"));
		teamsSettings.setItem(24, Utility.setName(new ItemStack(Material.DIAMOND_SWORD), ChatColor.YELLOW + "In team PVP", ChatColor.GREEN + "This doesn't do anything"));
	}
	
	public void updateTeamsViewer() { // Teams menu shows player heads for teams and has navigatable pages
		ArrayList<String> teams = pluginMain.SQLManager.getTeams();
		teamsPageNum = Math.max(0, Math.min(teams.size() / 36, teamsPageNum)); // Clamps page number
		
		for (int i = 0; i < 36; i++) {
			int headID = i + teamsPageNum * 36;
			
			if (headID < teams.size()) {
				String team = teams.get(headID);
				
				ItemStack teamIcon =  new ItemStack(Material.PLAYER_HEAD, pluginMain.SQLManager.countMembers(team)); 
				SkullMeta meta = (SkullMeta) teamIcon.getItemMeta();
				
				meta.setDisplayName(team);
				meta.setOwningPlayer(pluginMain.SQLManager.getLeader(team));
				
				teamIcon.setItemMeta(meta);
				teamsViewer.setItem(i + 9, teamIcon);
			} else {
				teamsViewer.setItem(i + 9, new ItemStack(Material.AIR));
			}
		}
	}
	
	
	public void updateEventsViewer() { // Updates all icons in the users event viewer for modification/overview of events
		ArrayList<String> eventIconNames = pluginMain.eventManager.getEventNames();
		ArrayList<Material> eventIconMaterials = pluginMain.eventManager.getEventIcons();
		int eventCount = eventIconNames.size();
		
		Bukkit.getLogger().info(eventsPageNum + "");
		eventsPageNum = Math.max(0, Math.min(eventCount / 36, eventsPageNum)); // Clamps page number
		Bukkit.getLogger().info(eventsPageNum + "");
		
		for (int i = 0; i < 36; i++) {
			int iconID = i + eventsPageNum * 36;
			
			if (iconID < eventCount) {
				ItemStack eventIcon =  new ItemStack(eventIconMaterials.get(iconID), 1); 
				ItemMeta meta = eventIcon.getItemMeta();
				
				meta.setDisplayName(ChatColor.YELLOW + eventIconNames.get(iconID));
				eventIcon.setItemMeta(meta);
				
				eventsMenu.setItem(i + 9, eventIcon);
			} else {
				eventsMenu.setItem(i + 9, new ItemStack(Material.AIR));
			}
		}
	}
	
	
	public void openManager(CommandSender sender) {
		((Player) sender).openInventory(mainMenu);
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) { // Handles opening relevant menu
		Player player = (Player) event.getWhoClicked();
		ItemStack stackClicked = event.getCurrentItem();
		Inventory inventory = event.getInventory();
		boolean cancelEvent;
		
		if (stackClicked == null) return;
		
		String buttonName = stackClicked.getItemMeta().getDisplayName().toString();
		
		if (inventory == mainMenu | inventory == eventsMenu | inventory == teamsManager | inventory == teamsViewer | inventory == teamsSettings) {
			cancelEvent = true;
			
			switch (buttonName) {
				case mainButtonName:
					player.openInventory(mainMenu);
					break;
					
				case eventButtonName:
					eventsPageNum = 0;
					updateEventsViewer();
					player.openInventory(eventsMenu);
					break;
					
				case teamsViewerButtonName: 
					teamsPageNum = 0;
					updateTeamsViewer();
					player.openInventory(teamsViewer);
					break;
					
				case teamsManagerButtonName:
					player.openInventory(teamsManager);
					break;
					
				case teamsSettingsButtonName:
					player.openInventory(teamsSettings);
					break;	
					
				case addEventButtonName:
					pluginMain.eventManager.createEvent(new Event("Blank Event", Material.STONE));
					updateEventsViewer();
					player.openInventory(eventsMenu);
					break;
			}
			
			if (buttonName.equals(ChatColor.YELLOW + "Next Page")) {
				if (inventory == teamsViewer) {
					teamsPageNum++;
					updateTeamsViewer();
					player.openInventory(teamsViewer);
				} else {
					eventsPageNum++;
					updateEventsViewer();
					player.openInventory(eventsMenu);
				}
			}
			
			if (buttonName.equals(ChatColor.YELLOW + "Previous Page")) {
				if (inventory == teamsViewer) {
					teamsPageNum--;
					updateTeamsViewer();
					player.openInventory(teamsViewer);
				} else {
					eventsPageNum--;
					updateEventsViewer();
					player.openInventory(eventsMenu);
				}
			}
			
			event.setCancelled(cancelEvent);
		}		
	}
}
