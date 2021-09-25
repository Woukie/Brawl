package org.woukie.brawl.game;

import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.woukie.brawl.game.Events.Event;
import org.woukie.brawl.game.Events.TimeEvent;

// Checks for and executes events
public class EventManager extends BukkitRunnable {
	private static EventManager instance = new EventManager();
	
	private EventManager() {
		events = new ArrayList<Event>();
	}
	
	public static EventManager getInstance() {
		return instance;
	}
	
	// Events in the ArrayList must be able to replace themselves to change their type (e.g. blank event to time event)
	// This has to be done through the event manager to preserve their order in relation to other events
	// The constructor for events cannot be specified therefore can't pass reference to EventManager
	// Which is why this is a singleton

	private ArrayList<Event> events;
	
	public void passClickToEvents(InventoryClickEvent event) {
		for (int i = 0; i < events.size(); i++) {
			events.get(i).onInventoryClick(event);
		}
	}
	
	public int getEventCount() {
		return events.size();
	}
	
	public ItemStack getEventItemStack(int eventID) {
		try {
			return events.get(eventID).getItemStack();
		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void openEventEditor(Player player, int eventID) {
		try {
			events.get(eventID).openInventory(player);
		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() { // Check every tick for timed events
		for (Event event : events) {
			if (event instanceof TimeEvent) {
				TimeEvent timeEvent = (TimeEvent) event;
				
				if (System.currentTimeMillis() >= timeEvent.time && timeEvent.lastTimeScanned <= timeEvent.time) {
					timeEvent.triggerEvent();
				}
				
				timeEvent.lastTimeScanned = System.currentTimeMillis();
			}
		}
	}
	
	public void replaceEvent(Event replaced, Event replacer) {
		if (!events.contains(replaced) && !(replacer instanceof Event)) return;
		
		int pos = events.indexOf(replaced);
		events.remove(pos);
		events.add(pos, replacer);
		
		replaced = null;
	}
	
	public void createEvent(Event event) {
		events.add(event);
	}
	
	public void saveEvents() {
		
	}
	
	public void loadEvents() {
		
	}
}
