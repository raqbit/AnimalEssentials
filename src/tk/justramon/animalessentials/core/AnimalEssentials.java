package tk.justramon.animalessentials.core;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import tk.justramon.animalessentials.cmd.Heal;
import tk.justramon.animalessentials.cmd.Kill;
import tk.justramon.animalessentials.cmd.Name;
import tk.justramon.animalessentials.cmd.Owner;
import tk.justramon.animalessentials.cmd.Spawn;
import tk.justramon.animalessentials.cmd.Tame;
import tk.justramon.animalessentials.cmd.Teleport;
import tk.justramon.animalessentials.util.Utilities;

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
		Utilities.registerEvents(this, new Teleport(), new Name(), new Kill(), new Heal(), new Owner(), new Tame(), new Spawn());
		Config.createConfig(this); //setting up the config
		checkForUpdate();
	}
	
	@Override
	public void onDisable()
	{
		Utilities.sendConsoleMessage("AnimalEssentials successfully disabled."); //sending this message to the console
	}
	
	public void checkForUpdate()
	{
		try
		{
			BufferedReader reader = new BufferedReader(new InputStreamReader(new URL("https://www.dropbox.com/s/rl6f8cif7yf92bw/AnimalEssentials.txt?dl=1").openStream()));
			String version = reader.readLine();
			int major = Integer.parseInt(version.split("\\.")[0]);
			int minor = Integer.parseInt(version.split("\\.")[1]);
			int bugfix = Integer.parseInt(version.split("\\.")[2]);
			int curMajor = Integer.parseInt(getDescription().getVersion().split("\\.")[0]);
			int curMinor = Integer.parseInt(getDescription().getVersion().split("\\.")[1]);
			int curBugfix = Integer.parseInt(getDescription().getVersion().split("\\.")[2]);
			
			if(bugfix > curBugfix || minor > curMinor || major > curMajor)
			{
				if(getConfig().getBoolean("forceUpdate"))
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
			e.printStackTrace();
		}

		Utilities.sendConsoleMessage("AnimalEssentials successfully enabled."); //sending this message to the console
	}
}
