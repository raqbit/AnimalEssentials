package bl4ckscor3.plugin.animalessentials.teleporting;

import org.bukkit.configuration.file.YamlConfiguration;

public class Teleporting
{
	private YamlConfiguration yaml;
	private String destination;
	private boolean tpToPlayer;
	
	/**
	 * Saves data for an animal to be teleported.
	 * @param y The File of the player's AnimalEssentials data as a Yaml file
	 * @param d The destination to teleport the animal to (homename/playername)
	 * @param ttip Whether the animal should be teleported to a player
	 */
	public Teleporting(YamlConfiguration y, String d, boolean ttip) //ttip, get the joke? no? ... :( <- That means bl4ck is sad :(
	{
		yaml = y;
		destination = d;
		tpToPlayer = ttip;
	}
	
	public YamlConfiguration getYamlConfiguration()
	{
		return yaml;
	}
	
	public String getDestination()
	{
		return destination;
	}
	
	public boolean shouldTpToPlayer()
	{
		return tpToPlayer;
	}
}
