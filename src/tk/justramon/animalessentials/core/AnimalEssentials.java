package tk.justramon.animalessentials.core;

import org.bukkit.plugin.java.JavaPlugin;

import tk.justramon.animalessentials.util.Utilities;

public class AnimalEssentials extends JavaPlugin
{
	/**The instance of this plugin*/
	public static AnimalEssentials instance;
	
	@Override
	public void onEnable()
	{
		instance = this; //setting the instance so we can use it in any other class without needing to pass the variable through countless methods
		Utilities.sendConsoleMessage("AnimalEssentials successfully enabled."); //sending this message to the console
		Config.handle(this); //setting up the console
	}
	
	@Override
	public void onDisable()
	{
		Utilities.sendConsoleMessage("AnimalEssentials successfully disabled."); //sending this message to the console
	}
}
