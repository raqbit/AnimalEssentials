package bl4ckscor3.plugin.animalessentials.cmd;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import bl4ckscor3.plugin.animalessentials.util.Utilities;
import mkremins.fanciful.FancyMessage;

public class Find implements IAECommand
{
	@Override
	public void exe(Plugin pl, Player p, Command cmd, String[] args) throws IOException
	{
		HashMap<World,List<Entity>> entityWorlds = new HashMap<World,List<Entity>>();
		List<Entity> foundAnimals = new ArrayList<Entity>();
		
		for(World w : pl.getServer().getWorlds()) //looping through all the worlds on the server
		{
			entityWorlds.put(w, w.getEntities()); //putting the worlds in the hashmap above with their entities
		}
		
		for(World w : entityWorlds.keySet()) //looping through the worlds
		{
			for(Entity e : entityWorlds.get(w)) //looping through all the entities of the given world
			{
				if(!Utilities.isAnimal(e)) //just continuing with the next entity if it's not an animal
					continue;
				
				if(e.getCustomName() == null) //just continuing with the next entity if it doesn't have a custom name
					continue;
				
				if(!e.getCustomName().equalsIgnoreCase(args[1])) //just continuing with the next entity if it's not having the name we're searching for
					continue;
				
				if(pl.getConfig().getBoolean("onlyFindOwnAnimals"))
				{
					if(Utilities.isOwnedBy(p, e, false))
						foundAnimals.add(e);
					else
						continue;
				}
				else
					foundAnimals.add(e); //adding the animal which was found to the list
			}
		}
		
		if(foundAnimals.isEmpty())
			Utilities.sendChatMessage(p, "No animal was found with the name /()" + args[1] + "()/.");
		else
		{
			for(Entity e : foundAnimals)
			{
				int x = (int)e.getLocation().getX();
				int y = (int)e.getLocation().getY();
				int z = (int)e.getLocation().getZ();
				FancyMessage msg = new FancyMessage(Utilities.getPrefix())
						.then(Utilities.aN(e.getType().getName(), true) + " ")
						.then(e.getType().getName()).color(ChatColor.BLUE)
						.then(" with the name ")
						.then(e.getCustomName()).color(ChatColor.BLUE)
						.then(" was spotted in world ")
						.then(e.getWorld().getName()).color(ChatColor.BLUE)
						.then(" at the following coordinates: ")
						.then(ChatColor.BLUE + "X: " + ChatColor.RESET + x + ChatColor.BLUE + " Y: " + ChatColor.RESET + y + ChatColor.BLUE + " Z: " + ChatColor.RESET + z).tooltip("Teleport to the animal.").command("/tp " + p.getName() + " " + x + " " + y + " " + z);
				
				msg.send(p);
			}
		}
	}

	@Override
	public String getAlias()
	{
		return "find";
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
				"Finds an animal with the given name.",
		};
	}

	@Override
	public String getPermission()
	{
		return "aess.find";
	}

	@Override
	public List<Integer> allowedArgLengths()
	{
		return Arrays.asList(new Integer[]{2}); // /ae find <name>
	}

	@Override
	public String getSyntax()
	{
		return "<name>";
	}
}