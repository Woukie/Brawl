package org.woukie.brawl.game;

import java.util.Date;
import java.util.logging.Logger;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;


public class Event implements Listener{
	public final Logger Logger = java.util.logging.Logger.getLogger("Minecraft");
	
	public String name; 
	public Date dateToTrigger;
	public Boolean death;
	public ItemStack itemStack;
	
	public String playerName = "{victim}";
	
}
