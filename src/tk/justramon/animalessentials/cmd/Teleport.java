package tk.justramon.animalessentials.cmd;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.plugin.Plugin;

import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;
import tk.justramon.animalessentials.core.AnimalEssentials;
import tk.justramon.animalessentials.util.Utilities;

public class Teleport implements IAECommand,Listener
{
	private static boolean waiting = false;
	private static File folder;
	private static File f;
	private static YamlConfiguration yaml;
	private static List<String> homes;
	private static String destination;
	private static String playerName;
	private static boolean tpToPlayer;
	public static Plugin plugin;

	@Override
	public void exe(Plugin pl, final Player p, Command cmd, String[] args) throws IOException
	{
		if(waiting)
		{
			Utilities.sendChatMessage(p, "A player is currently teleporting an animal and the magic invisible teleportation device can't handle that much. Please try again later.");
			return;
		}

		plugin = pl;
		destination = args[1];
		playerName = p.getName();
		folder = new File(pl.getDataFolder(), "playerStorage");
		f = new File(pl.getDataFolder(), "playerStorage/" + p.getUniqueId() +".yml");

		if(!folder.exists())
			folder.mkdirs();

		if(!f.exists())
			f.createNewFile();

		yaml = YamlConfiguration.loadConfiguration(f);
		homes = yaml.getStringList("homes");

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

		Utilities.sendChatMessage(p, "Please rightclick the animal you want to teleport.");
		waiting = true;
		Bukkit.getScheduler().runTaskLater(AnimalEssentials.instance, new Runnable(){
			@Override
			public void run()
			{
				if(waiting)
				{
					waiting = false;
					Utilities.sendChatMessage(p, "You ran out of time to select an animal to teleport. Use /()/ae teleport()/ to start again.");
				}
			}
		}, 10L * 20); //10 seconds * 20 (server ticks/second)
	}

	@EventHandler
	public void onPlayerInteractEntity(PlayerInteractEntityEvent event)
	{
		if(waiting && event.getPlayer().getName().equals(playerName))
		{
			final Entity entity = event.getRightClicked();

			if(!Utilities.isAnimal(entity))
			{
				Utilities.sendChatMessage(event.getPlayer(), "You can't teleport this mob, it's a /()" + entity.getName() + "()/ and not an animal.");
				return;
			}
			
			if(!Utilities.isOwnedBy(event.getPlayer(), entity))
			{
				Utilities.sendChatMessage(event.getPlayer(), "This is not your animal, you can't teleport it.");
				return;
			}

			//particle type | show particles 65k blocks away? (false = 255 block radius) | x coord of particle | y coord | z coord | x offset (area of effect) | y offset | z offset | speed of particles (some particles move, some don't) | amount of particles (the bigger the offset the bigger this has to be) | ?
			PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(EnumParticle.PORTAL, false, (float)entity.getLocation().getX(), (float)entity.getLocation().getY(), (float)entity.getLocation().getZ(), 0.0F, 0.0F, 0.0F, 10.0F, 3000, null);

			for(Player player : Bukkit.getOnlinePlayers())
			{
				((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet); //sending the packet (CraftPlayer is the craftbukkit equivalent of Playre)
			}

			Bukkit.getScheduler().runTaskLater(plugin, new Runnable(){
				
				@Override
				public void run()
				{
					for(Player player : Bukkit.getOnlinePlayers())
					{
						player.playSound(entity.getLocation(), Sound.ENDERMAN_TELEPORT, 1.0F, 1.0F);
					}

					if(tpToPlayer)
						entity.teleport(Bukkit.getPlayer(destination));
					else
						entity.teleport(new Location(Bukkit.getWorld(yaml.getString(destination + ".world")), yaml.getDouble(destination + ".x"), yaml.getDouble(destination + ".y"), yaml.getDouble(destination + ".z")));
				
					waiting = false;
					tpToPlayer = false;
					Bukkit.getScheduler().cancelAllTasks();
				}
			}, 50L); //2.5 seconds
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
				"Teleports an animal to the specified home or player. Poof!"
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
