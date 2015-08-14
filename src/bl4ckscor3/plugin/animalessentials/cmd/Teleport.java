package bl4ckscor3.plugin.animalessentials.cmd;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftAnimals;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.plugin.Plugin;

import bl4ckscor3.plugin.animalessentials.core.AnimalEssentials;
import bl4ckscor3.plugin.animalessentials.teleporting.Teleporting;
import bl4ckscor3.plugin.animalessentials.util.Utilities;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;

public class Teleport implements IAECommand,Listener
{
	private static HashMap<Player, Teleporting> currentlyTeleporting = new HashMap<Player, Teleporting>();
	public static Plugin plugin;

	@Override
	public void exe(Plugin pl, final Player p, Command cmd, String[] args) throws IOException
	{
		if(currentlyTeleporting.containsKey(p))
		{
			Utilities.sendChatMessage(p, "You can't teleport multiple animals at a time. Please teleport an animal or wait, then issue the command again.");
			return;
		}

		String destination = args[1];
		File folder = new File(pl.getDataFolder(), "playerStorage");
		File f = new File(pl.getDataFolder(), "playerStorage/" + p.getUniqueId() +".yml");
		boolean tpToPlayer = false;
		
		if(!folder.exists())
			folder.mkdirs();

		if(!f.exists())
			f.createNewFile();

		YamlConfiguration yaml = YamlConfiguration.loadConfiguration(f);
		List<String> homes = yaml.getStringList("homes");

		if(!homes.contains(destination))
		{
			if(Utilities.isPlayerOnline(args[1]))
				tpToPlayer = true;
			else
			{
				Utilities.sendChatMessage(p, "/()" + destination + "()/ does not exist or is not online.");
				return;
			}
		}
		else
			tpToPlayer = false;

		plugin = pl;
		currentlyTeleporting.put(p, new Teleporting(yaml, destination, tpToPlayer));
		Utilities.sendChatMessage(p, "Please rightclick the animal you want to teleport.");
		Bukkit.getScheduler().runTaskLater(AnimalEssentials.instance, new Runnable(){
			@Override
			public void run()
			{
				if(currentlyTeleporting.containsKey(p))
				{
					currentlyTeleporting.remove(p);
					Utilities.sendChatMessage(p, "You ran out of time to select an animal to teleport. Use /()/ae tp()/ to start again.");
				}
			}
		}, 10L * 20); //10 seconds * 20 (server ticks/second)
	}

	@EventHandler
	public void onPlayerInteractEntity(final PlayerInteractEntityEvent event)
	{
		if(currentlyTeleporting.containsKey(event.getPlayer()))
		{
			final Entity entity = event.getRightClicked();

			if(!Utilities.isAnimal(entity))
			{
				Utilities.sendChatMessage(event.getPlayer(), "You can't teleport this mob, it's " + Utilities.aN(entity.getType().getName(), false) + " /()" + (entity.getType().getName() == "" ? "Player" : entity.getType().getName()) + "()/ and not an animal.");
				event.setCancelled(true);
				return;
			}
			
			if(!Utilities.isOwnedBy(event.getPlayer(), entity, true))
			{
				Utilities.sendChatMessage(event.getPlayer(), "This is not your animal, you can't teleport it.");
				event.setCancelled(true);
				return;
			}

			//particle type | show particles 65k blocks away? (false = 255 block radius) | x coord of particle | y coord | z coord | x offset (area of effect) | y offset | z offset | speed of particles (some particles move, some don't) | amount of particles (the bigger the offset the bigger this has to be) | ?
			PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(EnumParticle.PORTAL, false, (float)entity.getLocation().getX(), (float)entity.getLocation().getY(), (float)entity.getLocation().getZ(), 0.0F, 0.0F, 0.0F, 10.0F, 3000, null);

			for(Player player : Bukkit.getOnlinePlayers())
			{
				((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet); //sending the packet (CraftPlayer is the craftbukkit equivalent of Player)
			}

			Bukkit.getScheduler().runTaskLater(plugin, new Runnable(){
				@Override
				public void run()
				{
					Teleporting t = currentlyTeleporting.get(event.getPlayer());
					YamlConfiguration yaml = t.getYamlConfiguration();
					String destination = t.getDestination();
					
					for(Player player : Bukkit.getOnlinePlayers())
					{
						player.playSound(entity.getLocation(), Sound.ENDERMAN_TELEPORT, 1.0F, 1.0F);
					}

					if(t.shouldTpToPlayer())
					{
						entity.teleport(Bukkit.getPlayer(destination));
						((CraftAnimals)entity).setNoDamageTicks(5*20); //no damage for 5 seconds
					}
					else
						entity.teleport(new Location(Bukkit.getWorld(yaml.getString(destination + ".world")), yaml.getDouble(destination + ".x"), yaml.getDouble(destination + ".y"), yaml.getDouble(destination + ".z")));
				
					currentlyTeleporting.remove(event.getPlayer());
					Bukkit.getScheduler().cancelTasks(plugin);
					Utilities.sendChatMessage(event.getPlayer(), "Animal teleported.");
				}
			}, 50L); //2.5 seconds
			
			event.setCancelled(true);
		}
	}

	@Override
	public String getAlias()
	{
		return "tp";
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
				"Teleports the right-clicked animal to the specified home or player. Poof!"
		};
	}

	@Override
	public String getPermission()
	{
		return "aess.teleport";
	}

	@Override
	public List<Integer> allowedArgLengths()
	{
		return Arrays.asList(new Integer[]{2}); // /ae teleport <home|player>
	}
	
	@Override
	public String getSyntax()
	{
		return "<homeName|playerName>";
	}
}
