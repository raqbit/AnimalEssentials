package tk.justramon.animalessentials.cmd;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import tk.justramon.animalessentials.util.Utilities;

public class Help implements IAECommand
{
	private final List<IAECommand> cmds;
	
	public Help(List<IAECommand> commands)
	{
		cmds = commands;
	}
	
	@Override
	public void exe(Plugin pl, Player p, Command cmd)
	{
		if(cmd.getLabel() == getAlias()) //while passing the cmds list into the constructor in line 22 of AECommands, we don't actually pass in the help class, since it's currently being added
			displayHelp(p, getHelp()); //tl;dr - the help command is not yet in the list so we need to handle it seperately
		else
		{
			for(IAECommand c : cmds) //for each command in the commands list...
			{
				if(c.getAlias().equals(cmd.getName())) //...we check if it's the command which got issued...
				{
					displayHelp(p, c.getHelp()); //...and if it is we send the help of this command...
					return; //...and stop the method since we don't want the message at the bottom of it to show
				}
			}
			
			Utilities.sendChatMessage(p, "This command does not exist."); //if we didn't find the command, send this message
		}
			
	}

	@Override
	public String getAlias()
	{
		return "help";
	}

	@Override
	public boolean isConsoleCommand()
	{
		return false;
	}

	@Override
	public String[] getHelp()
	{
		return new String[]{
				"Prints out this help menu.",
				"Append an AnimalEssentials command to show its help."
		};
	}

	@Override
	public String getPermission()
	{
		return "aess.help";
	}

	/**
	 * Sends command help to a player
	 * @param p The player to send the help to
	 * @param help The help to send
	 */
	private void displayHelp(Player p, String[] help)
	{
		for(String s : help) //for each string contained in the help array...
		{
			Utilities.sendChatMessage(p, s); //...we send it to the player
		}
	}
}
