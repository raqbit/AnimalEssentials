package bl4ckscor3.plugin.animalessentials.cmd;

import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import bl4ckscor3.plugin.animalessentials.util.Utilities;

public class Help implements IAECommand
{
	private final List<IAECommand> cmds;
	
	public Help(List<IAECommand> commands)
	{
		cmds = commands;
	}
	
	@Override
	public void exe(Plugin pl, CommandSender sender, Command cmd, String[] args)
	{
		if((args[0].equals(getAlias()) && args.length == 1) || (args.length == 2 && args[1].equals(getAlias()))) //used if the user only executes /ae help
		{
			boolean blue = false;
			int displayedCommands = 0;
			String s = "";
			
			displayHelp(sender, getHelp(), "help", getPermission(), getSyntax());
			Utilities.sendMessage(sender, "Available commands:");
			
			for(IAECommand c : cmds)
			{
				if(displayedCommands == 5)
				{
					Utilities.sendMessage(sender, s.trim());
					displayedCommands = 0;
					s = "";
				}
				
				s += (blue ? "/()" : "") + "/ae " + c.getAlias() + (blue ? "()/" : "") + " ";
				blue = !blue;
				displayedCommands++;
			}
			
			Utilities.sendMessage(sender, s);
			Utilities.sendMessage(sender, ChatColor.RED + "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		}
		else
		{
			for(IAECommand c : cmds) //for each command in the commands list...
			{
				if(c.getAlias().equals(args[1])) //...we check if it's the command which got issued...
				{
					displayHelp(sender, c.getHelp(), args[1], c.getPermission(), c.getSyntax()); //...and if it is we send the help of this command...
					return; //...and stop the method since we don't want the message at the bottom of it to show
				}
			}
			
			Utilities.sendMessage(sender, "This command does not exist."); //if we didn't find the command, send this message
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
		return true;
	}

	@Override
	public String[] getHelp()
	{
		return new String[]{
				"Prints out this help menu.",
				"Append an AnimalEssentials command to show its help.",
		};
	}

	@Override
	public String getPermission()
	{
		return "aess.help";
	}

	@Override
	public List<Integer> allowedArgLengths()
	{
		return Arrays.asList(new Integer[]{1, 2}); // /ae help [command]
	}
	
	@Override
	public String getSyntax()
	{
		return "[command]";
	}
	
	/**
	 * Sends command help to a player
	 * @param sender The person to send the help to
	 * @param help The help to send
	 * @param label The command name to display in the help menu header
	 * @param permission The permission needed to execute this command
	 */
	public static void displayHelp(CommandSender sender, String[] help, String label, String permission, String syntax)
	{
		Utilities.sendMessage(sender, ChatColor.RED + "~~~~~~~~~~" + ChatColor.GRAY + "Help Menu: " + label + ChatColor.RED + "~~~~~~~~~~");
		
		for(String s : help) //for each string contained in the help array...
		{
			Utilities.sendMessage(sender, s); //...we send it to the player
		}
		
		Utilities.sendMessage(sender, "Syntax: /ae " + label + " " + syntax);
		Utilities.sendMessage(sender, "Required permission: " + permission);
		Utilities.sendMessage(sender, ChatColor.RED + "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
	}
}
