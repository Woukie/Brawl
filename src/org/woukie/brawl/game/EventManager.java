package org.woukie.brawl.game;

import java.util.ArrayList;

import org.bukkit.Material;
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

	@Override
	public void run() { // Check every tick for timed events
		for (Event event : events) {
			if (event instanceof TimeEvent) {				
				if (((TimeEvent) event).time < System.currentTimeMillis()) {
					((TimeEvent) event).action.trigger();
				}
			}
		}
	}
	
	public ArrayList<String> getEventNames() {
		ArrayList<String> names = new ArrayList<String>();
		
		for (Event event : events) {
			names.add(event.name);
		}
		
		return names;
	}
	
	public ArrayList<Material> getEventIcons() {
		ArrayList<Material> materials = new ArrayList<Material>();
		
		for (Event event : events) {
			materials.add(event.icon);
		}
		
		return materials;
	}
	
	public void createEvent(Event event) {
		events.add(event);
	}
	
	public void saveEvents() {
		
	}
	
	public void loadEvents() {
		
	}
}
