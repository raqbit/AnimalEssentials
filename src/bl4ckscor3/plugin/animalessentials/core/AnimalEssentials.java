package bl4ckscor3.plugin.animalessentials.core;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import bl4ckscor3.plugin.animalessentials.cmd.Heal;
import bl4ckscor3.plugin.animalessentials.cmd.Kill;
import bl4ckscor3.plugin.animalessentials.cmd.Name;
import bl4ckscor3.plugin.animalessentials.cmd.Owner;
import bl4ckscor3.plugin.animalessentials.cmd.Spawn;
import bl4ckscor3.plugin.animalessentials.cmd.Tame;
import bl4ckscor3.plugin.animalessentials.cmd.Teleport;
import bl4ckscor3.plugin.animalessentials.util.Utilities;

public class AnimalEssentials extends JavaPlugin
{
	/**
	 * The instance of this plugin
	 */
	public static AnimalEssentials instance;
	
	@Override
	public void onEnable()
	{
		instance = this; //setting the instance so we can use it in any other class without needing to pass the variable through countless methods
		getCommand("animalessentials").setExecutor(new AECommands()); //registers the command executor to the command "animalessentials"
		getCommand("aetp").setExecutor(new AECommands()); //registers the command executor to the command "aetp"
		Utilities.registerEvents(this, new Teleport(), new Name(), new Kill(), new Heal(), new Owner(), new Tame(), new Spawn());
		Config.createConfig(this); //setting up the config
		
		if(getConfig().getBoolean("update.check"))
			checkForUpdate();
		else
			Utilities.sendConsoleMessage("AnimalEssentials successfully enabled."); //sending this message to the console
	}
	
	@Override
	public void onDisable()
	{
		Utilities.sendConsoleMessage("AnimalEssentials successfully disabled."); //sending this message to the console
	}
	
	/**
	 * Checks for a plugin update, if enabled in config
	 */
	public void checkForUpdate()
	{
		try
		{
			BufferedReader reader = new BufferedReader(new InputStreamReader(new URL("https://raw.githubusercontent.com/JustRamon/AnimalEssentials/d368a233fb747a97ab101155ce6291e60017df2f/version.txt").openStream()));
			String version = reader.readLine();
			int major = Integer.parseInt(version.split("\\.")[0]);
			int minor = Integer.parseInt(version.split("\\.")[1]);
			int bugfix = Integer.parseInt(version.split("\\.")[2]);
			int curMajor = Integer.parseInt(getDescription().getVersion().split("\\.")[0]);
			int curMinor = Integer.parseInt(getDescription().getVersion().split("\\.")[1]);
			int curBugfix = Integer.parseInt(getDescription().getVersion().split("\\.")[2]);
			
			if(bugfix > curBugfix || minor > curMinor || major > curMajor)
			{
				if(getConfig().getBoolean("update.force"))
				{
					Utilities.sendConsoleMessage(ChatColor.RED + "There is a new version (" + major + "." + minor + "." + bugfix + ") available here: "); //TODO: Insert thread link!
					Utilities.sendConsoleMessage(ChatColor.RED + "It is highly suggested to update to this version!");
					Utilities.sendConsoleMessage(ChatColor.RED + "You can't use the plugin until you update it.");
					getServer().getPluginManager().disablePlugin(this);
					return;
				}
				else
				{
					Utilities.sendConsoleMessage(ChatColor.RED + "There is a new version (" + major + "." + minor + "." + bugfix + ") available here: "); //TODO: Insert thread link!
					Utilities.sendConsoleMessage(ChatColor.RED + "It is highly suggested to update to this version!");
				}
			}
			else
				Utilities.sendConsoleMessage(ChatColor.RED + "No update has been found.");
			
			reader.close();
		}
		catch(Exception e)
		{
			Utilities.sendConsoleMessage(ChatColor.RED + "Could not find update file. Are you connected to the internet?");
		}

		Utilities.sendConsoleMessage("AnimalEssentials successfully enabled."); //sending this message to the console
	}
}
