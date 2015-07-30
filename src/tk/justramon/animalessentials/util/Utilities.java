package tk.justramon.animalessentials.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.Listener;

import tk.justramon.animalessentials.core.AnimalEssentials;

public class Utilities
{
	/**
	 * Gets the instance of this plugin so we don't need to pass that argument into every method
	 */
	private static final AnimalEssentials pl = AnimalEssentials.instance;
	
	/**
	 * Sends a message to the console
	 * @param msg The message to send
	 */
	public static void sendConsoleMessage(String msg)
	{
		msg = msg.replace("/()", ChatColor.BLUE.toString()).replace("()/", ChatColor.RESET.toString()); //"/()" is an in-string replacement for blue color and "()/" is an in-string replacement for resetting it
		Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD + "[" + ChatColor.DARK_GREEN + pl.getDescription().getName() + ChatColor.GOLD + "] " + ChatColor.RESET + msg);
	}
	
	/**
	 * Sends a message to a player
	 * @param p The player to send the message to
	 * @param msg The message to send
	 */
	public static void sendChatMessage(Player p, String msg)
	{
		msg = msg.replace("/()", ChatColor.BLUE.toString()).replace("()/", ChatColor.RESET.toString()); //"/()" is an in-string replacement for blue color and "()/" is an in-string replacement for resetting it
		p.sendMessage(ChatColor.GOLD + "[" + ChatColor.DARK_GREEN + pl.getDescription().getName() + ChatColor.GOLD + "] " + ChatColor.RESET + msg);
	}

	/**
	 * Checks if the player owns the entity
	 * @param player The player who could be the owner
	 * @param entity The entity who could be owned by the player
	 * @return Wether the entity is owned by the player or not
	 */
	public static boolean isOwnedBy(Player player, Entity entity)
	{
		if(entity instanceof Horse)
		{
			if(((Horse)entity).isTamed() && !((Horse)entity).getOwner().getName().equals(player.getName()))
				return false;
		}
		else if(entity instanceof Wolf)
		{
			if(((Wolf)entity).isTamed() && !((Wolf)entity).getOwner().getName().equals(player.getName()))
				return false;
		}
		else if(entity instanceof Ocelot)
		{
			if(((Ocelot)entity).isTamed() && !((Ocelot)entity).getOwner().getName().equals(player.getName()))
				return false;
		}
		
		return true;
	}
	
	/**
	 * Checks if the given entity is an animal (as in: not a zombie/creeper etc)
	 * @param entity The entity to check
	 * @return Wether the entity is an animal or not
	 */
	public static boolean isAnimal(Entity entity)
	{
		return entity instanceof Animals;
	}

	/**
	 * Registers all given events
	 * @param pl The plugin the events get registered from
	 * @param listener The events to register
	 */
	public static void registerEvents(AnimalEssentials pl, Listener... listener)
	{
		for(Listener l : listener)
		{
			pl.getServer().getPluginManager().registerEvents(l, pl);
		}
	}
	
	/**
	 * Checks if a player is online.
	 * 
	 * @param name - The player to search for
	 * @return - Returns if the player is online or not
	 */
	public static boolean isPlayerOnline(String name)
	{
		for(Player p : Bukkit.getOnlinePlayers())
		{
			if(p.getName().equalsIgnoreCase(name))
				return true;
		}
		return false;
	}
	
	/**
	 * Formats coordinates into a nice String
	 * @param x The x coord
	 * @param y The y coord
	 * @param z The z coord
	 * @return The finalized coordinates String to print out in a message
	 */
	public static String printCoords(int x, int y, int z)
	{
		return "/()X: ()/" + x + "/() Y: ()/" + y + "/() Z: ()/" + z + "";
	}
}
