package org.woukie.brawl.game;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.woukie.brawl.game.Events.Event;
import org.woukie.brawl.game.Events.TimeEvent;

// Checks for and executes events
public class EventManager extends BukkitRunnable {
	private static EventManager instance = new EventManager(); 
	// Events in the ArrayList must be able to replace themselves to change their type (e.g. blank event to time event)
	// This has to be done through the event manager to preserve their order in relation to other events
	// The constructor for events cannot be specified therefore can't pass reference to EventManager
	// Which is why this is a singleton
	
	private EventManager() {
		events = new ArrayList<Event>();
		loadEvents();
	}
	
	public enum EventNames {
		TIMEEVENT, BLANKEVENT
	}
	
	public String getEventName(EventNames type) {
		String returnName = "No Event Name";
		
		switch (type) {
			case BLANKEVENT:
				returnName = ChatColor.YELLOW + "Blank Event";
				
				break;
			case TIMEEVENT:
				returnName = ChatColor.YELLOW + "Time Event";
				
				break;
		}
		
		return returnName;
	}
	
	private ArrayList<Event> events;
	
	public static EventManager getInstance() {
		return instance;
	}
	
	public int getEventCount() {
		return events.size();
	}
	
	public ItemStack getEventItemStack(int eventID) {
		try {
			return events.get(eventID).getItemStack();
		} catch (ArrayIndexOutOfBoundsException e) {
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
				if (((TimeEvent) event).time < System.currentTimeMillis()) {
					((TimeEvent) event).triggerEvent();
				}
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
