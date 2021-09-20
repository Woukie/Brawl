package org.woukie.brawl.utility;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.craftbukkit.libs.org.apache.commons.codec.binary.Base64;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

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
	
    public static ItemStack getSkull(String url, String name) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD, 1);
        if(url.isEmpty())return head;
       
       
        SkullMeta headMeta = (SkullMeta) head.getItemMeta();
        headMeta.setDisplayName(name);        
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        byte[] encodedData = Base64.encodeBase64(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes());
        profile.getProperties().put("textures", new Property("textures", new String(encodedData)));
        Field profileField = null;
        try {
            profileField = headMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(headMeta, profile);
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e1) {
            e1.printStackTrace();
        }
        head.setItemMeta(headMeta);
        return head;
    }
}
