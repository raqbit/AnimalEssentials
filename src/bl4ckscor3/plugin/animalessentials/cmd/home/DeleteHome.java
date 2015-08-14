package bl4ckscor3.plugin.animalessentials.cmd.home;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import bl4ckscor3.plugin.animalessentials.cmd.IAECommand;
import bl4ckscor3.plugin.animalessentials.util.Utilities;

public class DeleteHome implements IAECommand
{
	@Override
	public void exe(Plugin pl, Player p, Command cmd, String[] args) throws IOException
	{
		File folder = new File(pl.getDataFolder(), "playerStorage");
		File f = new File(pl.getDataFolder(), "playerStorage/" + p.getUniqueId() +".yml");
		
		if(!folder.exists())
			folder.mkdirs();

		if(!f.exists())
			f.createNewFile();
		
		YamlConfiguration yaml = YamlConfiguration.loadConfiguration(f);
		List<String> homes = yaml.getStringList("homes");

		if(!homes.contains(args[1]))
		{
			Utilities.sendChatMessage(p, "/()" + args[1] + "()/ does not exist.");
			return;
		}

		homes.remove(args[1]);
		yaml.set("homes" , homes); //deleting the destination from the destination list
		yaml.set(args[1], null); //deleting the destination from the file
		yaml.save(f); //saving the file after editing it
		Utilities.sendChatMessage(p, "Home /()" + args[1] + "()/ has been deleted.");
	}

	@Override
	public String getAlias()
	{
		return "delhome";
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
				"Gets rid of an existing home."
		};
	}

	@Override
	public String getPermission()
	{
		return "aess.home.delete";
	}
	
	@Override
	public List<Integer> allowedArgLengths()
	{
		return Arrays.asList(new Integer[]{2}); // /ae delhome test
	}
	
	@Override
	public String getSyntax()
	{
		return "<homeName>";
	}
}
