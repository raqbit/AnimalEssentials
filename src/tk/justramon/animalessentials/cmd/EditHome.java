package tk.justramon.animalessentials.cmd;

import java.io.File;
import java.io.IOException;
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
		YamlConfiguration yaml = YamlConfiguration.loadConfiguration(f);

		if(!folder.exists())
			folder.mkdirs();

		if(!f.exists())
			f.createNewFile();

		List<String> homes = yaml.getStringList("homes");

		if(!homes.contains(args[2]))
		{
			Utilities.sendChatMessage(p, "/()" + args[2] + "()/ does not exist.");
			return;        
		}

		yaml.set(args[2] + ".world", p.getWorld().getName());
		yaml.set(args[2] + ".x", p.getLocation().getX());
		yaml.set(args[2] + ".y", p.getLocation().getY());
		yaml.set(args[2] + ".z", p.getLocation().getZ());
		yaml.save(f); //saving the file after editing it
		Utilities.sendChatMessage(p, "Home /()" + args[2] + "()/ has been edited in world /()" + yaml.getString(args[2] + ".world") + "()/ at these coordinates: /()X:" + yaml.getInt(args[2] + ".x") + " Y:" + yaml.getInt(args[2] + ".y" + " Z:" + yaml.getInt(args[2] + ".z")));
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
}
