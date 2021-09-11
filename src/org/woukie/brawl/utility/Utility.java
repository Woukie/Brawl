package org.woukie.brawl.utility;

import java.util.ArrayList;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Utility {
	public static ItemStack setName(ItemStack is, String name){
        ItemMeta m = is.getItemMeta();
        m.setDisplayName(name);
        is.setItemMeta(m);
        return is;
    }
	
	public static ItemStack setName(ItemStack is, String name, String lore){
        ItemMeta m = is.getItemMeta();
        m.setDisplayName(name);
        ArrayList<String> Lore = new ArrayList<String>();
        Lore.add(lore);
        m.setLore(Lore);
        is.setItemMeta(m);
        return is;
    }
}
