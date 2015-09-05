package bl4ckscor3.plugin.animalessentials.core;

import org.bukkit.plugin.Plugin;

import bl4ckscor3.plugin.animalessentials.util.Utilities;

public class Config
{
	/**
	 * Sets up the config when the server is started
	 */
	public static void createConfig(Plugin pl)
	{
		pl.reloadConfig();
		pl.getConfig().options().header(getHeader());
		pl.getConfig().addDefault("shouldNamingUseNametag", true);
		pl.getConfig().addDefault("allowMultipleCommands", false);
		pl.getConfig().addDefault("update.force", true);
		pl.getConfig().addDefault("update.check", true);
		pl.getConfig().addDefault("find.onlyOwnAnimals", false);
		pl.getConfig().addDefault("find.keyword", "comesFromLink");
		pl.getConfig().options().copyDefaults(true);
		pl.saveConfig();
		Utilities.sendConsoleMessage("Config successfully created/enabled!");
	}
	
	private static String getHeader()
	{
		String[] lines = {
				"############################################################################################################################################### #",
				" _______  _       _________ _______  _______  _        _______  _______  _______  _______  _       __________________ _______  _       _______  #",
				"(  ___  )( (    /|\\__   __/(       )(  ___  )( \\      (  ____ \\(  ____ \\(  ____ \\(  ____ \\( (    /|\\__   __/\\__   __/(  ___  )( \\     (  ____ \\ #",
				"| (   ) ||  \\  ( |   ) (   | () () || (   ) || (      | (    \\/| (    \\/| (    \\/| (    \\/|  \\  ( |   ) (      ) (   | (   ) || (     | (    \\/ #",
				"| (___) ||   \\ | |   | |   | || || || (___) || |      | (__    | (_____ | (_____ | (__    |   \\ | |   | |      | |   | (___) || |     | (_____  #",
				"|  ___  || (\\ \\) |   | |   | |(_)| ||  ___  || |      |  __)   (_____  )(_____  )|  __)   | (\\ \\) |   | |      | |   |  ___  || |     (_____  ) #",
				"| (   ) || | \\   |   | |   | |   | || (   ) || |      | (            ) |      ) || (      | | \\   |   | |      | |   | (   ) || |           ) | #",
				"| )   ( || )  \\  |___) (___| )   ( || )   ( || (____/\\| (____/\\/\\____) |/\\____) || (____/\\| )  \\  |   | |   ___) (___| )   ( || (____/Y\\____) | #",
				"|/     \\||/    )_)\\_______/|/     \\||/     \\|(_______/(_______/\\_______)\\_______)(_______/|/    )_)   )_(   \\_______/|/     \\|(_______|_______) #",
				"----------------------------------------------------------------------------------------------------------------------------------------------- #",
				"                                          Made by bl4ckscor3 (Developer) & JustRamon (Creative Mind)                                            #",
				"############################################################################################################################################### #",
				"",
				"shouldNamingUseNametag: This value defines wether the user needs to have a nametag in their inventory when using /ae name on an animal. (Not effective in creative mode)",
				"",
				"allowMultipleCommands: When this is set to true, users will be able to issue multiple AnimalEssentials commands at a time. ",
				"                       When set to false, it prevents users from, for instance, killing and naming their animal at the same time.",
				"",
				"update: Options for the update checker",
				"",
				"	force: When set to true, this option disables the plugin if an update has been found. ",
				"         When set to false, only an update notifier will show up if \"check\" is enabled.",
				"",
				"	check: When set to false, the plugin will not check for updates.",
				"",
				"find: Options for /ae find",
				"",
				"	onlyOwnAnimals: When this option is enabled, only animals owned by the player are displayed.",
				"                  When disabled, untamed animals will also show.",
				"",
				"	keyword: This is the keyword used to determine wether the command is coming from clicking the chat, or from actually inputting the command into the chat.",
				"            It basically disabled users from using /aetp as a normal teleport command.",
				"            You can change this to anything, and it will still work. Think of it as a password needed to execute the command.",
				"############################################################################################################################################### #",
		};
		String all = "";
		
		for(String s : lines)
		{
			all += s + "\n";
		}
		
		return all;
	}
}