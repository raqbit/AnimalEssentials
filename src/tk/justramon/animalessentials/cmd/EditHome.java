package tk.justramon.animalessentials.cmd;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import tk.justramon.animalessentials.util.Utilities;

public class EditHome implements IAECommand
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

		yaml.set(args[1] + ".world", p.getWorld().getName());
		yaml.set(args[1] + ".x", p.getLocation().getX());
		yaml.set(args[1] + ".y", p.getLocation().getY());
		yaml.set(args[1] + ".z", p.getLocation().getZ());
		yaml.save(f); //saving the file after editing it
		Utilities.sendChatMessage(p, "Home /()" + args[1] + "()/ has been edited in world /()" + yaml.getString(args[1] + ".world") + "()/ at these coordinates: /()X:" + yaml.getInt(args[1] + ".x") + " Y:" + yaml.getInt(args[1] + ".y") + " Z:" + yaml.getInt(args[1] + ".z"));
	}

	@Override
	public String getAlias()
	{
		return "edithome";
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
				"Edits an existing home."
		};
	}

	@Override
	public String getPermission()
	{
		return "aess.home.edit";
	}
	
	@Override
	public List<Integer> allowedArgLengths()
	{
		return Arrays.asList(new Integer[]{2}); // /ae edithome test 
	}
}
