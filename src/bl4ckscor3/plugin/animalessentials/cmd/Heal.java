package bl4ckscor3.plugin.animalessentials.cmd;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
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
import bl4ckscor3.plugin.animalessentials.util.Utilities;

public class Heal implements IAECommand,Listener
{
	private static HashMap<Player,Boolean> currentlyHealing = new HashMap<Player,Boolean>();
	private static HashMap<Player,Integer> taskIDs = new HashMap<Player,Integer>();
	public static Plugin plugin;

	@Override
	public void exe(Plugin pl, CommandSender sender, Command cmd, String[] args) throws IOException
	{
		final Player p = (Player)sender;
		
		if(currentlyHealing.containsKey(p))
		{
			Utilities.sendChatMessage(p, "You can't heal multiple animals at a time. Please heal an animal or wait, then issue the command again.");
			return;
		}
		
		plugin = pl;
		Utilities.sendChatMessage(p, "Please rightclick the animal you want to heal.");
		currentlyHealing.put(p, true);
		AECommands.setIssuingCmd(p, true);
		
		AbortRunnable task = new AbortRunnable(p);

		task.runTaskLater(pl, 10 * 20L);
		taskIDs.put(p, task.getTaskId());
	}

	@EventHandler
	public void onPlayerInteractEntity(final PlayerInteractEntityEvent event)
	{
		if(currentlyHealing.containsKey(event.getPlayer()) && currentlyHealing.get(event.getPlayer()))
		{
			currentlyHealing.put(event.getPlayer(), false);
			
			Entity entity = event.getRightClicked();

			if(!Utilities.isAnimal(entity))
			{
				Utilities.sendChatMessage(event.getPlayer(), "You can't heal this mob, it's " + Utilities.aN(entity.getType().name(), false) + " /()" + (entity.getType().name() == null ? "Player" : Utilities.capitalizeFirstLetter(entity.getType().name())) + "()/ and not an animal.");
				event.setCancelled(true);
				return;
			}

			if(!event.getPlayer().hasPermission("aess.heal.bypass") && !Utilities.isOwnedBy(event.getPlayer(), entity, true))
			{
				Utilities.sendChatMessage(event.getPlayer(), "This is not your animal, you can't heal it.");
				event.setCancelled(true);
				return;
			}

			if(((LivingEntity) entity).getHealth() == ((LivingEntity) entity).getMaxHealth())
			{
				Utilities.sendChatMessage(event.getPlayer(), "This /()" + Utilities.capitalizeFirstLetter(entity.getType().name()) + "()/ is already healed.");
				event.setCancelled(true);
				return;
			}
			
			//x offset, y offset, z offset from the center, speed, amount, center, radius
			ParticleEffect.HEART.display(0.5F, 0.5F, 0.5F, 10.0F, 10, entity.getLocation(), 255);
			//play the sound at the location
			entity.getWorld().playSound(entity.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F);
			((LivingEntity) entity).setHealth(((LivingEntity) entity).getMaxHealth());
			currentlyHealing.remove(event.getPlayer());
			AECommands.setIssuingCmd(event.getPlayer(), false);
			Utilities.sendChatMessage(event.getPlayer(), "Animal healed.");
			event.setCancelled(true);
			Bukkit.getScheduler().cancelTask(taskIDs.get(event.getPlayer()));
			taskIDs.remove(event.getPlayer());
		}
	}

	public class AbortRunnable extends BukkitRunnable implements BukkitTask
	{
		private Player p;
		
		public AbortRunnable(Player player)
		{
			p = player;
		}
		
		@Override
		public void run()
		{
			if(currentlyHealing.containsKey(p))
			{
				currentlyHealing.remove(p);
				AECommands.setIssuingCmd(p, false);
				Utilities.sendChatMessage(p, "You ran out of time to select an animal to heal. Use /()/ae heal()/ to start again.");
				taskIDs.remove(p);
			}
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
		return "heal";
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
				"Heals the right-clicked animal."
		};
	}

	@Override
	public String getPermission()
	{
		return "aess.heal";
	}

	@Override
	public List<Integer> allowedArgLengths()
	{
		return Arrays.asList(new Integer[]{1}); // /ae heal
	}

	@Override
	public String getSyntax()
	{
		return "";
	}
}
