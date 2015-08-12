package tk.justramon.animalessentials.cmd;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import tk.justramon.animalessentials.util.Utilities;

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
				Utilities.sendChatMessage(p, "A /()" + e.getType().getName() + "()/ with the name /()" + args[1] + "()/ was spotted in world /()" + e.getWorld().getName() + "()/ at the following coordinates: " + Utilities.printCoords((int)e.getLocation().getX(), (int)e.getLocation().getY(), (int)e.getLocation().getZ()));
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
