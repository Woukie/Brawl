package org.woukie.brawl.game.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.woukie.brawl.game.Actions.Action;

public class TimeEvent implements Event {
	private Action action;
	public Long time; // Time in milliseconds, determines when the action is triggered
	// The difference, measured in milliseconds, between the current time and midnight, January 1, 1970 UTC.

	public TimeEvent(Long time, Action action, String name) {
		this.time = time;
		this.action = action;
	}

	@Override
	public void triggerEvent() {
		action.trigger();
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void openInventory(Player player) {
		// TODO Auto-generated method stub
		
	}
}
