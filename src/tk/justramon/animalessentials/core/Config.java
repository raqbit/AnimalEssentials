package tk.justramon.animalessentials.core;

import org.bukkit.plugin.Plugin;

import tk.justramon.animalessentials.util.Utilities;

public class Config
{
	/**
	 * Sets up the config when the server is started
	 */
	public static void createConfig(Plugin pl)
	{
		pl.reloadConfig();
		/* add
		 * config options
		 * here
		 */
		pl.getConfig().options().copyDefaults(true);
		pl.saveConfig();
		Utilities.sendConsoleMessage("Config successfully created/enabled!");
	}
}
