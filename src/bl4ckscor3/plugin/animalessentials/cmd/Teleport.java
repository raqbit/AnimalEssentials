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
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import com.darkblade12.particleeffect.ParticleEffect;

import bl4ckscor3.plugin.animalessentials.core.AECommands;
import bl4ckscor3.plugin.animalessentials.core.AnimalEssentials;
import bl4ckscor3.plugin.animalessentials.save.Teleporting;
import bl4ckscor3.plugin.animalessentials.util.Utilities;

public class Teleport implements IAECommand,Listener
{
	private static HashMap<Player, Teleporting> currentlyTeleporting = new HashMap<Player, Teleporting>();
	public static Plugin plugin;
	
	@Override
	public void exe(Plugin pl, CommandSender sender, Command cmd, String[] args) throws IOException
	{
		final Player p = (Player)sender;
		
		if(currentlyTeleporting.containsKey(p))
		{
			Utilities.sendChatMessage(p, "You can't teleport multiple animals at a time. Please teleport an animal or wait, then issue the command again.");
			return;
		}

		String destination = args[1];
		File folder = new File(pl.getDataFolder(), "playerStorage");
		File f = new File(pl.getDataFolder(), "playerStorage" + File.separator + p.getUniqueId() +".yml");
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
		AECommands.setIssuingCmd(p, true);
		Utilities.sendChatMessage(p, "Please rightclick the animal you want to teleport.");
		Bukkit.getScheduler().runTaskLater(AnimalEssentials.instance, new Runnable(){
			@Override
			public void run()
			{
				if(currentlyTeleporting.containsKey(p))
				{
					currentlyTeleporting.remove(p);
					AECommands.setIssuingCmd(p, false);
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
				Utilities.sendChatMessage(event.getPlayer(), "You can't teleport this mob, it's " + Utilities.aN(entity.getType().name(), false) + " /()" + (entity.getType().name() == null ? "Player" : entity.getType().name()) + "()/ and not an animal.");
				event.setCancelled(true);
				return;
			}
			
			if(!Utilities.isOwnedBy(event.getPlayer(), entity, true))
			{
				Utilities.sendChatMessage(event.getPlayer(), "This is not your animal, you can't teleport it.");
				event.setCancelled(true);
				return;
			}

			//x offset, y offset, z offset from the center, speed, amount, center, range
			ParticleEffect.PORTAL.display(0.0F, 0.0F, 0.0F, 10.0F, 3000, entity.getLocation(), 255);

			TeleportRunnable task = new TeleportRunnable(event, entity);
			
			task.runTaskLater(plugin, 50L);
			event.setCancelled(true);
		}
	}

	public class TeleportRunnable extends BukkitRunnable implements BukkitTask
	{
		private PlayerInteractEntityEvent event;
		private Entity entity;
		
		public TeleportRunnable(PlayerInteractEntityEvent e, Entity en)
		{
			event = e;
			entity = en;
		}
		
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
				((LivingEntity)entity).setNoDamageTicks(5*20); //no damage for 5 seconds
			}
			else
				entity.teleport(new Location(Bukkit.getWorld(yaml.getString(destination + ".world")), yaml.getDouble(destination + ".x"), yaml.getDouble(destination + ".y"), yaml.getDouble(destination + ".z")));
		
			currentlyTeleporting.remove(event.getPlayer());
			AECommands.setIssuingCmd(event.getPlayer(), false);
			Bukkit.getScheduler().cancelTask(getTaskId());
			Utilities.sendChatMessage(event.getPlayer(), "Animal teleported.");
		}

		@Override
		public Plugin getOwner()
		{
			return plugin;
		}

		@Override
		public boolean isSync()
		{
			return false;
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
