package org.woukie.brawl.game;

import java.util.ArrayList;

import org.bukkit.entity.Player;

public class Team {
	Team (Player leader) {
		teamMates = new ArrayList<Player>();
		teamMates.add(leader);
	}
	
	int points;
	ArrayList<Player> teamMates;
}
