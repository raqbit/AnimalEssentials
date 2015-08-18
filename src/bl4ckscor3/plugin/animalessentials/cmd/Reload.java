package bl4ckscor3.plugin.animalessentials.cmd;

import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import bl4ckscor3.plugin.animalessentials.util.Utilities;

public class Reload implements IAECommand
{
	@Override
	public void exe(Plugin pl, Player p, Command cmd, String[] args)
	{
		pl.reloadConfig();
		pl.saveDefaultConfig();
		
		if(p == null)
			Utilities.sendConsoleMessage("Config successfully reloaded!");
		else
			Utilities.sendChatMessage(p, "Config successfully reloaded!");
	}

	@Override
	public String getAlias()
	{
		return "reload";
	}
	
	@Override
	public boolean isConsoleCommand()
	{
		return true;
	}

	@Override
	public String[] getHelp()
	{
		return new String[]{
				"Reloads the config.",
		};
	}

	@Override
	public String getPermission()
	{
		return "aess.reload";
	}
	
	@Override
	public List<Integer> allowedArgLengths()
	{
		return Arrays.asList(new Integer[]{1}); // /ae reload
	}
	
	@Override
	public String getSyntax()
	{
		return "";
	}
}
