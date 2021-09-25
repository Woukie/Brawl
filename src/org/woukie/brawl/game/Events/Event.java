package org.woukie.brawl.game.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.woukie.brawl.game.Actions.Action;


public interface Event extends Listener { // Must handle menu interactions itself, other than opening itself which the menuManager will handle
	
	public void triggerEvent();
	public void setAction(Action action);
	public ItemStack getItemStack(); // < TODO: Could be static
	public void openInventory(Player player);
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event); // Inheriting classes must register themselves as an event handler
	// ^ Doing this to move code off menu manager ^
}
