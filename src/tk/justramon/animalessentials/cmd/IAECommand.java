package tk.justramon.animalessentials.cmd;

import java.io.IOException;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public interface IAECommand
{
	/**
	 * Executes the command.
	 * @param pl The plugin the command is from
	 * @param p The player who issued the command
	 * @param cmd The command issued
	 * @param args The arguments of the command
	 * @throws IOException 
	 */
	public void exe(Plugin pl, Player p, Command cmd, String[] args) throws IOException;
	
	/**
	 * Determines what the command gets triggered from.
	 * e.g. setting the alias to "help" will trigger /ae help
	 * @return The name of the command which will trigger its functionality
	 */
	public String getAlias();
	
	/**
	 * Determines if the command can be used by the console or not
	 */
	public boolean isConsoleCommand();

	/**
	 * Determines what gets shown when /ae help is used
	 * @return Each array position is its own line
	 */
	public String[] getHelp();
	
	/**
	 * Determines the permission which is required to execute this command
	 * @return The permission required to execute this command
	 */
	public String getPermission();
}
