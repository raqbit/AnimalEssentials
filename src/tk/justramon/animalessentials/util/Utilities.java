package tk.justramon.animalessentials.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import tk.justramon.animalessentials.core.AnimalEssentials;

public class Utilities
{
	/**Gets the instance of this plugin so we don't need to pass that argument into every method*/
	private static final AnimalEssentials pl = AnimalEssentials.instance;
	
	/**
	 * Sends a message to the console
	 * @param msg The message to send
	 */
	public static void sendConsoleMessage(String msg)
	{
		Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD + "[" + ChatColor.DARK_GREEN + pl.getDescription().getName() + ChatColor.GOLD + "] " + ChatColor.RESET + msg);
	}
	
	/**
	 * Sends a message to a player
	 * @param p The player to send the message to
	 * @param msg The message to send
	 */
	public static void sendChatMessage(Player p, String msg)
	{
		p.sendMessage(ChatColor.GOLD + "[" + ChatColor.DARK_GREEN + pl.getDescription().getName() + ChatColor.GOLD + "] " + ChatColor.RESET + msg);
	}
}
