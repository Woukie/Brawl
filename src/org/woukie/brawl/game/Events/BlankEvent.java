package org.woukie.brawl.game.Events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.woukie.brawl.game.EventManager;
import org.woukie.brawl.game.Actions.Action;
import org.woukie.brawl.utility.Utility;

public class BlankEvent implements Event {
	private ItemStack icon; // Store the item stack as unique identification for handling opening the editor
	private Inventory menu;
	
	// Blank event is used as a placeholder for when the player hasn't set it up
	// Event needs an inventory to be used as a menu for the player
	public BlankEvent() {
		icon = new ItemStack(Material.STONE, 1);
		ItemMeta meta = icon.getItemMeta();
		meta.setDisplayName(ChatColor.YELLOW + "Blank Event");
		icon.setItemMeta(meta);
		
		makeMenu();
	}
	
	private void makeMenu() {
		menu = Bukkit.createInventory(null, 27, "Select an event");
		
		ItemStack[] contents = new ItemStack[27];
		
		for (int i = 0; i < 9; i++) {
			contents[i] = Utility.setName(new ItemStack(Material.GRAY_STAINED_GLASS_PANE), " ");
			contents[i + 18] = Utility.setName(new ItemStack(Material.GRAY_STAINED_GLASS_PANE), " ");
		}
		
		menu.setContents(contents);
		
		menu.setItem(13, Utility.setName(new ItemStack(Material.CLOCK, 1), "?eTime Event"));
//		menu.setItem(22, Utility.setName(new ItemStack(), " "));
	}
	
	@Override
	public void triggerEvent() {
		// Blank event does nothing
	}

	@Override
	public void setAction(Action action) {
		// Blank event does nothing
	}

	@Override
	public void onInventoryClick(InventoryClickEvent event) { 
		Player player = (Player) event.getWhoClicked();
		ItemStack stackClicked = event.getCurrentItem();
		Inventory inventory = event.getInventory();
		
		if (stackClicked == null) return;
		
		if (inventory == menu) {
			event.setCancelled(true);
			switch (stackClicked.getItemMeta().getDisplayName()) {
			case "?eTime Event":
				TimeEvent timeEvent = new TimeEvent();
				EventManager.getInstance().replaceEvent(this, timeEvent);
				
				break;

			default:
				break;
			}
		}
	}

	@Override
	public ItemStack getItemStack() {
		return icon;
	}

	@Override
	public void openInventory(Player player) {
		player.openInventory(menu);
	}

	@Override
	public void closeInventory() {
		for (int i = 0; i < menu.getViewers().size(); i++) {
			menu.getViewers().get(i).closeInventory();
		}
	}
}