package org.woukie.brawl.game.Events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.woukie.brawl.game.Actions.Action;

public class BlankEvent implements Event {
	private ItemStack itemStack; // Store the item stack as unique identification for handling opening the editor
	private Inventory menu;
	
	// Blank event is used as a placeholder for when the player hasn't set it up
	// Event needs an inventory to be used as a menu for the player
	public BlankEvent() {
		itemStack = new ItemStack(Material.STONE, 1);
		ItemMeta meta = itemStack.getItemMeta();
		meta.setDisplayName(ChatColor.YELLOW + "Blank Event");
		itemStack.setItemMeta(meta);
		
		makeMenu();
	}
	
	private void makeMenu() {
		menu = Bukkit.createInventory(null, 27, "Select an event");
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
	public void onInventoryClick(InventoryClickEvent event) { // Identify event by
		Player player = (Player) event.getWhoClicked();
		ItemStack stackClicked = event.getCurrentItem();
		// Inventory inventory = event.getInventory();
		
		if (stackClicked == itemStack) { // Need to compare memory location as itemStack may have no unique identification
			player.openInventory(menu);
		}
	}

	@Override
	public ItemStack getItemStack() {
		return itemStack;
	}
}