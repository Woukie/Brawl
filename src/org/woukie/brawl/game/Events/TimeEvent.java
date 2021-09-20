package org.woukie.brawl.game.Events;

import org.bukkit.Material;
import org.woukie.brawl.game.Actions.Action;

public class TimeEvent extends Event{
	public Action action;
	public Long time; // Time in milliseconds, determines when the action is triggered
	// The difference, measured in milliseconds, between the current time and midnight, January 1, 1970 UTC.
	
	public TimeEvent(Long time, Action action, String name) {
		super(name, Material.CLOCK);
		this.time = time;
		this.action = action;
	}
}
