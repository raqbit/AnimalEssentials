package bl4ckscor3.plugin.animalessentials.core;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import bl4ckscor3.plugin.animalessentials.cmd.Clone;
import bl4ckscor3.plugin.animalessentials.cmd.Find;
import bl4ckscor3.plugin.animalessentials.cmd.Heal;
import bl4ckscor3.plugin.animalessentials.cmd.Help;
import bl4ckscor3.plugin.animalessentials.cmd.IAECommand;
import bl4ckscor3.plugin.animalessentials.cmd.Kill;
import bl4ckscor3.plugin.animalessentials.cmd.Name;
import bl4ckscor3.plugin.animalessentials.cmd.Owner;
import bl4ckscor3.plugin.animalessentials.cmd.Reload;
import bl4ckscor3.plugin.animalessentials.cmd.Spawn;
import bl4ckscor3.plugin.animalessentials.cmd.Tame;
import bl4ckscor3.plugin.animalessentials.cmd.Teleport;
import bl4ckscor3.plugin.animalessentials.cmd.home.DeleteHome;
import bl4ckscor3.plugin.animalessentials.cmd.home.EditHome;
import bl4ckscor3.plugin.animalessentials.cmd.home.ListHomes;
import bl4ckscor3.plugin.animalessentials.cmd.home.SetHome;
import bl4ckscor3.plugin.animalessentials.util.CustomArrayList;
import bl4ckscor3.plugin.animalessentials.util.Utilities;

public class AECommands implements CommandExecutor
{
	private static final CustomArrayList<IAECommand> cmds = new CustomArrayList<IAECommand>();
	private static final AnimalEssentials pl = AnimalEssentials.instance;
	private static final List<Player> currentlyIssuing = new ArrayList<Player>();

	public AECommands()
	{
		if(cmds.size() == 0)
		{
			cmds.addEverything(new Reload(), //adding this command to the list so we can access it below and in help
				new SetHome(),
				new EditHome(),
				new DeleteHome(),
				new ListHomes(),
				new Teleport(),
				new Name(),
				new Find(),
				new Kill(),
				new Heal(),
				new Owner(),
				new Tame(),
				new Spawn(),
				new Clone(),
				new Help(cmds)); //make sure that this is always last
		}
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		try //huge try/catch statement to catch any errors and print their stacktrace to the console
		{
			Player p = null;

			if(sender instanceof BlockCommandSender)
			{
				Utilities.sendConsoleMessage("Commandblocks are not supported by this plugin.");
				return true;
			}

			if(sender instanceof Player)
				p  = (Player)sender;

			if(cmd.getName().equals("aetp"))
			{
				if(args.length <= 4 || !args[4].equals(AnimalEssentials.secret)) //if no fifth argument exists or the fifth argument is not comesFromLink then we don't let the player execute the command
					Utilities.sendChatMessage(p, "You are not allowed to use this command.");
				else
					Bukkit.getPlayer(p.getName()).teleport(new Location(Bukkit.getWorld(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3])));

				return true;
			}

			if(!pl.getConfig().getBoolean("allowMultipleCommands") && isIssuingCmd(p))
			{
				Utilities.sendChatMessage(p, "You cannot execute a second AnimalEssentials command while you still need to rightclick an animal. This is a safety precaution so you can't name and kill your animal at the same time, for instance.");

				if(p.isOp())
					Utilities.sendChatMessage(p, "This function can be turned off in the config file.");

				return true;
			}

			if(args.length != 0)
			{
				for(IAECommand c : cmds) //for each command in the commands list...
				{
					if(c.getAlias().equalsIgnoreCase(args[0])) //...we check if it's the command which got issued...
					{
						if(sender instanceof ConsoleCommandSender && c.isConsoleCommand()) //...we check if the sender is the console and if it's a console command, and if both is true...
						{
							if(c.allowedArgLengths().contains(args.length))
								c.exe(pl, sender, cmd, args); //...we execute it...
							else
								Utilities.sendConsoleMessage("Invalid arguments. Use /()/ae help " + c.getAlias() + "()/ to get more information on the command.");
							return true; //...and return true to stop the method since the command has been executed...
						}
						else if(p != null)//...if not, it has to come from a player but we check anyways so we can display a warning message to the console later on...
						{
							if(p.hasPermission(c.getPermission())) //...so we check if they have the permission needed to execute the command...
							{
								if(!(c instanceof Find || c instanceof Name)) //we need this because those two can have technically infinite arguments
								{
									if(c.allowedArgLengths().contains(args.length))
										c.exe(pl, p, cmd, args); //...and if so, we execute it...
									else
										Help.displayHelp(p, c.getHelp(), c.getAlias(), c.getPermission(), c.getSyntax());
								}
								else
								{
									if(c.allowedArgLengths().get(0) <= args.length) //...we check if the argument lengths are correct (including spaces)
										c.exe(pl, p, cmd, args);
									else
										Help.displayHelp(p, c.getHelp(), c.getAlias(), c.getPermission(), c.getSyntax());
								}
							}
							else
								Utilities.sendChatMessage(p, "You do not have the required permission to execute this command."); //...and if not we send this message to the player...

							return true; //...and finally return true since we know that everything went as expected...
						}
						else //...HOWEVER if the command gets issued from a command block or any other unhandled command sender (=anything else than console or player)...
						{
							Utilities.sendConsoleMessage(ChatColor.DARK_RED + "[SEVERE] ()/Command executed by unhandeled command sender: /()" + sender.getName()); //...we send this message to the console including the command sender's name (for a command block it would be something like "commandblock")...
							return true; //...and return true to stop the method since we know that the command was sent by something
						}
					}
				}
			}

			Utilities.sendChatMessage(p, "Unknown command executed. Use /()/ae help()/ to get help with the plugin.");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		return true;
	}

	/**
	 * Checks if a player is currently issuing a command
	 * @param p The player to check
	 * @return If they are issuing a command
	 */
	private static boolean isIssuingCmd(Player p)
	{
		return currentlyIssuing.contains(p);
	}

	/**
	 * Sets a player currently issuing a command or not
	 * @param p The player
	 * @param issuing Whether the player is issuing a command or not
	 */
	public static void setIssuingCmd(Player p, boolean issuing)
	{
		if(!issuing)
		{
			if(currentlyIssuing.contains(p))
				currentlyIssuing.remove(p);
		}
		else
		{
			if(!currentlyIssuing.contains(p))
				currentlyIssuing.add(p);
		}
	}
}
