package org.woukie.brawl.game;

import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.woukie.brawl.game.Events.Event;
import org.woukie.brawl.game.Events.TimeEvent;

// Checks for and executes events
public class EventManager extends BukkitRunnable {
	private ArrayList<Event> events;
	
	public EventManager() {
		events = new ArrayList<Event>();
		loadEvents();
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
	
	public void openEvent(Player player, int event) {
		
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
	
	public void createEvent(Event event) {
		events.add(event);
	}
	
	public void saveEvents() {
		
	}
	
	public void loadEvents() {
		
	}
}
