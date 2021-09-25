package org.woukie.brawl.game.Events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.woukie.brawl.game.Actions.Action;
import org.woukie.brawl.utility.Utility;

public class TimeEvent implements Event {
	private ItemStack icon;
	private Action action; // May be null
	private Inventory menu;
	
	public Long lastTimeScanned; // Ensures event is only triggered once
	public Long time; // Time in milliseconds, (used by EventManager) determines when the action is triggered
	// The difference, measured in milliseconds, between the current time and midnight, January 1, 1970 UTC.

	public TimeEvent() {
		lastTimeScanned = 0L;
		time = 10L;
		
		makeMenu();
		icon = Utility.setName(new ItemStack(Material.CLOCK), ChatColor.YELLOW + "Time Event");
	}
	
	private void makeMenu() {
		menu = Bukkit.createInventory(null, 27);
	}

	@Override
	public void triggerEvent() {
		if (action != null) action.trigger();
	}

	@Override
	public void setAction(Action action) {
		this.action = action;
	}

	@Override
	public void onInventoryClick(InventoryClickEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ItemStack getItemStack() {
		return icon;
	}

	@Override
	public void openInventory(Player player) {
		player.openInventory(menu);
	}
}
