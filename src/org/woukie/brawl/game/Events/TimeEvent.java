package org.woukie.brawl.game.Events;

import java.sql.Date;

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
	private Inventory menu, setMonth, setDay;
	
	public Long lastTimeScanned; // Ensures event is only triggered once
	public Long time; // Time in milliseconds, (used by EventManager) determines when the action is triggered
	// The difference, measured in milliseconds, between the current time and midnight, January 1, 1970 UTC.

	public TimeEvent() {
		lastTimeScanned = 0L;
		time = 10L;
		
		makeMenu();
		makeDayMenu();
		makeMonthMenu();
		updateDayMenu();
		
		icon = Utility.setName(new ItemStack(Material.CLOCK), ChatColor.YELLOW + "Time Event");
	}
	
	private void makeMenu() {
		menu = Bukkit.createInventory(null, 27);
		
		ItemStack[] contents = new ItemStack[27];
		
		for (int i = 0; i < 9; i++) {
			contents[i] = Utility.setName(new ItemStack(Material.GRAY_STAINED_GLASS_PANE), " ");
			contents[i + 18] = Utility.setName(new ItemStack(Material.GRAY_STAINED_GLASS_PANE), " ");
		}
		
		menu.setContents(contents);
		
		String month = new Date(time).toLocalDate().getMonth().toString();
		menu.setItem(12, Utility.setName(new ItemStack(Material.CLOCK, 1), "§eTriggers every " + month.substring(0, 1).toUpperCase() + month.substring(1).toLowerCase(), "Click to change month"));
		menu.setItem(14, Utility.setName(new ItemStack(Material.CLOCK, 1), "§eTriggers on day " + new Date(time).toLocalDate().getDayOfMonth(), "Click to change day"));
	}
	
	private void makeMonthMenu() {
		setMonth = Bukkit.createInventory(null, 36);
		
		ItemStack[] contents = new ItemStack[36];
		
		for (int i = 0; i < 9; i++) {
			contents[i] = Utility.setName(new ItemStack(Material.GRAY_STAINED_GLASS_PANE), " ");
			contents[i + 27] = Utility.setName(new ItemStack(Material.GRAY_STAINED_GLASS_PANE), " ");
		}
		
		setMonth.setContents(contents);
	}
	
	private void makeDayMenu() {
		setDay = Bukkit.createInventory(null, 54);
		
		ItemStack[] contents = new ItemStack[54];
		
		for (int i = 0; i < 9; i++) {
			contents[i] = Utility.setName(new ItemStack(Material.GRAY_STAINED_GLASS_PANE), " ");
			contents[i + 45] = Utility.setName(new ItemStack(Material.GRAY_STAINED_GLASS_PANE), " ");
		}
		
		setDay.setContents(contents);
	}
	
	private void updateDayMenu() { // Shows specific number of days so user can't set day outside month
		
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
		Player player = (Player) event.getWhoClicked();
		ItemStack stackClicked = event.getCurrentItem();
		Inventory inventory = event.getInventory();
		
		if (stackClicked == null) return;
		
		event.setCancelled(true);
		
		if (inventory == menu) {
			String lore = stackClicked.getItemMeta().getLore().get(0);
			if (lore == null) return;
			if (lore.equals("Click to change month")) player.openInventory(setMonth);
			if (lore.equals("Click to change day")) player.openInventory(setDay);
			return;
		}
		
		if (inventory == setMonth) {
			return;
		}
		
		if (inventory == setDay) {
			return;
		}
		
		event.setCancelled(false);
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
