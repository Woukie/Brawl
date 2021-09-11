package org.woukie.brawl.game.teams;

import java.util.LinkedHashMap;

public class Team {
	String name;
	LinkedHashMap<String, Integer> teamMates; // UUID
	
	Team (String leader) {
		teamMates = new LinkedHashMap<String, Integer>();
		teamMates.put(leader, 0);
	}
	
}
