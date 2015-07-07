package tk.justramon.animalessentials.cmd;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import tk.justramon.animalessentials.util.Utilities;

public class Reload implements IAECommand
{
	@Override
	public void exe(Plugin pl, Player p, Command cmd, String[] args)
	{
		pl.reloadConfig();
		pl.saveConfig();
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
}
