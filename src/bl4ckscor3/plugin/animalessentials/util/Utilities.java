package bl4ckscor3.plugin.animalessentials.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.event.Listener;

import bl4ckscor3.plugin.animalessentials.core.AnimalEssentials;

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
		Bukkit.getConsoleSender().sendMessage(getPrefix() + msg);
	}

	/**
	 * Sends a message to a player
	 * @param p The player to send the message to
	 * @param msg The message to send
	 */
	public static void sendChatMessage(Player p, String msg)
	{
		msg = msg.replace("/()", ChatColor.BLUE.toString()).replace("()/", ChatColor.RESET.toString()); //"/()" is an in-string replacement for blue color and "()/" is an in-string replacement for resetting it
		p.sendMessage(getPrefix() + msg);
	}

	/**
	 * Sends a message to the person defined in the sender param
	 * @param sender The person to send the message to
	 * @param msg The message to send
	 */
	public static void sendMessage(CommandSender sender, String msg)
	{
		msg = msg.replace("/()", ChatColor.BLUE.toString()).replace("()/", ChatColor.RESET.toString()); //"/()" is an in-string replacement for blue color and "()/" is an in-string replacement for resetting it
		sender.sendMessage(getPrefix() + msg);
	}
	
	/**
	 * Checks if the player owns the entity
	 * @param player The player who could be the owner
	 * @param entity The entity who could be owned by the player
	 * @param untameableOwned Whether an untameable animal should be considered as owned by the player
	 * @return Wether the entity is owned by the player or not
	 */
	public static boolean isOwnedBy(Player player, Entity entity, boolean untameableOwned)
	{
		if(entity instanceof Tameable)
		{
			if(((Tameable)entity).isTamed())
			{
				if(!((Tameable)entity).getOwner().getName().equals(player.getName()))
					return false;
				else
					return true;
			}
		}

		return untameableOwned;
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
	public static String getFormattedCoordinates(int x, int y, int z)
	{
		return "/()X: ()/" + x + "/() Y: ()/" + y + "/() Z: ()/" + z + "";
	}

	/**
	 * Determines wether to use "a" or "an"
	 * @param word The word in question
	 * @param beginning Wether "a" or "an" is at the beginning of the sentence
	 * @return "a" or "an"
	 */
	public static String aN(String word, boolean beginning)
	{
		try
		{
			switch(word.charAt(0))
			{
				case 'a': case 'A':
				case 'e': case 'E':
				case 'i': case 'I':
				case 'o': case 'O':
				case 'u': case 'U':
					return beginning ? "An" : "an";
				default:
					return beginning ? "A" : "a";
			}
		}
		catch(NullPointerException e)
		{
			return beginning ? "A" : "a";
		}
	}
	
	/**
	 * Returns the default message prefix of the plugin
	 */
	public static String getPrefix()
	{
		return ChatColor.GOLD + "[" + ChatColor.DARK_GREEN + pl.getDescription().getName() + ChatColor.GOLD + "] " + ChatColor.RESET;
	}

	/**
	 * Gets the Minecraft version of the server the plugin is running on
	 * @param server The server
	 * @return The Minecraft version as a string
	 */
	public static String getMinecraftVersion(Server server)
	{
		return server.getVersion().split("MC: ")[1].replaceFirst("\\)", "");
	}
	
	/**
	 * Capitalizes the first letter of a string
	 * @param line The string to capitalize the first letter of
	 * @return The capitalized first letter including the rest of the string
	 */
	public static String capitalizeFirstLetter(String line)
	{
		return line.substring(0, 1).toUpperCase() + line.substring(1).toLowerCase();
	}
}
