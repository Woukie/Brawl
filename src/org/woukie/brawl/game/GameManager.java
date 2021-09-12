package org.woukie.brawl.game;

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
import org.bukkit.plugin.Plugin;
import org.woukie.brawl.utility.Utility;


public class GameManager implements Listener{
	private Inventory mainMenu, eventsMenu, teamsMenu;
	private String mainName = "Brawl Main Menu";
	private String eventsName = "Brawl Events Menu";
	private String teamsName = "Brawl Teams Menu";
	private String mainButtonName = ChatColor.YELLOW + "Main Menu";
	private String eventButtonName = ChatColor.YELLOW + "Event Manager";
	private String teamsButtonName = ChatColor.YELLOW + "Teams Manager";
	private String mainButtonLore = ChatColor.GREEN + "Go back to the main menu";
	private String eventButtonLore = ChatColor.GREEN + "Change how the game plays!";
	private String teamsButtonLore = ChatColor.GREEN + "Review teams";

	//private int teamsPage;
	
	public GameManager(Plugin pluginMain) {
		Bukkit.getPluginManager().registerEvents(this, pluginMain);
		
		makeManageMenu();
		makeEventsMenu();
		makeTeamsMenu();
	}
	
	public void makeManageMenu() {
		mainMenu = Bukkit.createInventory(null, 27, mainName);
		ItemStack[] items = new ItemStack[27];
		
		for (int i = 0; i < 9; i++) {
			items[i] = Utility.setName(new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1), " ");
			items[i + 9] = Utility.setName(new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE, 1), " ");
			items[i + 18] = Utility.setName(new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1), " ");
		}
		
		mainMenu.setContents(items);
		mainMenu.setItem(12, Utility.setName(new ItemStack(Material.EMERALD, 1), eventButtonName, eventButtonLore));
		mainMenu.setItem(14, Utility.setName(new ItemStack(Material.PLAYER_HEAD, 1), teamsButtonName, teamsButtonLore));
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

	public void makeTeamsMenu() {
		teamsMenu = Bukkit.createInventory(null, 54, teamsName);
		ItemStack[] items = new ItemStack[54];
		
		for (int i = 0; i < 9; i++) {
			items[i] = Utility.setName(new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1), " ");
			items[i + 45] = Utility.setName(new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1), " ");
		}
		
		teamsMenu.setContents(items);
		teamsMenu.setItem(4, Utility.setName(new ItemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE), mainButtonName, mainButtonLore)); // main menu button
	}
	
	public void openManager(CommandSender sender) {
		((Player) sender).openInventory(mainMenu);
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		ItemStack stackClicked = event.getCurrentItem();
		Inventory inventory = event.getInventory();
		
		if (stackClicked == null) return;
		
		if (inventory == mainMenu) {
			event.setCancelled(true);
			
			String buttonName = stackClicked.getItemMeta().getDisplayName();
			
			if (buttonName.equals(eventButtonName)) {
				player.openInventory(eventsMenu);
			} else if (buttonName.equals(teamsButtonName)) {
				player.openInventory(teamsMenu);
			}
		}
		
		if (inventory == eventsMenu) {
			event.setCancelled(true);
			
			String buttonName = stackClicked.getItemMeta().getDisplayName();
			
			if (buttonName.equals(mainButtonName)) {
				player.openInventory(mainMenu);
			}
		}
		
		if (inventory == teamsMenu) {
			event.setCancelled(true);
			
			String buttonName = stackClicked.getItemMeta().getDisplayName();
			
			if (buttonName.equals(mainButtonName)) {
				player.openInventory(mainMenu);
			}
		}
	}
}
