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
import org.bukkit.inventory.meta.SkullMeta;
import org.woukie.brawl.Main;
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

	// Button descriptions
	private String mainButtonLore = ChatColor.GREEN + "Go back to the main menu";
	private String eventButtonLore = ChatColor.GREEN + "Change how the game plays!";
	private String teamsViewerButtonLore = ChatColor.GREEN + "See and review teams";
	private String teamsManagerButtonLore = ChatColor.GREEN + "Manage teams";
	private String teamsSettingsButtonLore = ChatColor.GREEN + "Change teams settings";
	
	private int teamsPageNum; // Refresh teams viewer page (auto clamps page number)
	
	private Main pluginMain;
	
	// Handles menus/navigating menus in game
	public MenuManager(Main _pluginMain) {
		pluginMain = _pluginMain;
		Bukkit.getPluginManager().registerEvents(this, pluginMain);
		
		makeMainMenu();
		makeEventsMenu();
		makeTeamsViewer();
		makeTeamsManager();
		makeTeamsSettings();
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
		eventsMenu.setItem(4, Utility.setName(new ItemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE), mainButtonName, mainButtonLore)); // main menu button
	}

	public void makeTeamsViewer() { // shows all player heads (opens from manager)
		teamsViewer = Bukkit.createInventory(null, 54, teamsName);
		ItemStack[] items = new ItemStack[54];
		
		for (int i = 0; i < 9; i++) {
			items[i] = Utility.setName(new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1), " ");
			items[i + 45] = Utility.setName(new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1), " ");
		}
		
		ItemStack rightArrow = new ItemStack(Material.PLAYER_HEAD, 1);
		ItemStack leftArrow = new ItemStack(Material.PLAYER_HEAD, 1);
		
		SkullMeta rightArrowMeta = (SkullMeta) rightArrow.getItemMeta();
		SkullMeta leftArrowMeta = (SkullMeta) leftArrow.getItemMeta();
		
		rightArrowMeta.setOwningPlayer(Bukkit.getPlayer("natatos"));
		rightArrowMeta.setDisplayName(ChatColor.YELLOW + "Next Page");
		
		leftArrowMeta.setOwningPlayer(Bukkit.getPlayer("saidus2"));
		leftArrowMeta.setDisplayName(ChatColor.YELLOW + "Previous Page"); 
		
		rightArrow.setItemMeta(rightArrowMeta);
		leftArrow.setItemMeta(leftArrowMeta);
		
		teamsViewer.setContents(items);
		
		teamsViewer.setItem(51, rightArrow);
		teamsViewer.setItem(47, leftArrow);
		
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
		teamsPageNum = Math.max(0, Math.min(teams.size() / 36, teamsPageNum)); // Clamps page num
		
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
	
	public void openManager(CommandSender sender) {
		((Player) sender).openInventory(mainMenu);
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) { // Handles opening relevant menu
		Player player = (Player) event.getWhoClicked();
		ItemStack stackClicked = event.getCurrentItem();
		Inventory inventory = event.getInventory();
		
		if (stackClicked == null) return;
		
		String buttonName = stackClicked.getItemMeta().getDisplayName().toString();
		
		if (inventory == mainMenu | inventory == eventsMenu | inventory == teamsManager | inventory == teamsViewer | inventory == teamsSettings) {
			event.setCancelled(true);
			
			switch (buttonName) {
				case mainButtonName:
					player.openInventory(mainMenu);
					break;
					
				case eventButtonName:
					player.openInventory(eventsMenu);
					break;
					
				case teamsViewerButtonName: 
					updateTeamsViewer();
					player.openInventory(teamsViewer);
					break;
					
				case teamsManagerButtonName:
					player.openInventory(teamsManager);
					break;
					
				case teamsSettingsButtonName:
					player.openInventory(teamsSettings);
					break;
			}
			
			// TODO: could be for menu other than team viewer in future
			if (buttonName.equals("Next Page")) {
				teamsPageNum++;
				updateTeamsViewer();
				player.openInventory(teamsViewer);
			}
			
			if (buttonName.equals("Previous Page")) {
				teamsPageNum--;
				updateTeamsViewer();
				player.openInventory(teamsViewer);
			}
		}
	}
}
