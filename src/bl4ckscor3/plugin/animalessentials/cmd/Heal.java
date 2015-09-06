package bl4ckscor3.plugin.animalessentials.cmd;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.plugin.Plugin;

import com.darkblade12.particleeffect.ParticleEffect;

import bl4ckscor3.plugin.animalessentials.core.AECommands;
import bl4ckscor3.plugin.animalessentials.core.AnimalEssentials;
import bl4ckscor3.plugin.animalessentials.util.Utilities;

public class Heal implements IAECommand,Listener
{
	private static List<Player> currentlyHealing = new ArrayList<Player>();
	public static Plugin plugin;

	@Override
	public void exe(Plugin pl, final Player p, Command cmd, String[] args) throws IOException
	{
		if(currentlyHealing.contains(p))
		{
			Utilities.sendChatMessage(p, "You can't heal multiple animals at a time. Please heal an animal or wait, then issue the command again.");
			return;
		}
		
		plugin = pl;
		Utilities.sendChatMessage(p, "Please rightclick the animal you want to heal.");
		currentlyHealing.add(p);
		AECommands.setIssuingCmd(p, true);
		Bukkit.getScheduler().runTaskLater(AnimalEssentials.instance, new Runnable(){
			@Override
			public void run()
			{
				if(currentlyHealing.contains(p))
				{
					currentlyHealing.remove(p);
					AECommands.setIssuingCmd(p, false);
					Utilities.sendChatMessage(p, "You ran out of time to select an animal to heal. Use /()/ae heal()/ to start again.");
				}
			}
		}, 10L * 20); //10 seconds * 20 (server ticks/second)
	}

	@EventHandler
	public void onPlayerInteractEntity(final PlayerInteractEntityEvent event)
	{
		if(currentlyHealing.contains(event.getPlayer()))
		{
			final Entity entity = event.getRightClicked();

			if(!Utilities.isAnimal(entity))
			{
				Utilities.sendChatMessage(event.getPlayer(), "You can't heal this mob, it's " + Utilities.aN(entity.getType().name(), false) + " /()" + (entity.getType().name() == null ? "Player" : entity.getType().name()) + "()/ and not an animal.");
				event.setCancelled(true);
				return;
			}

			if(!Utilities.isOwnedBy(event.getPlayer(), entity, true))
			{
				Utilities.sendChatMessage(event.getPlayer(), "This is not your animal, you can't heal it.");
				event.setCancelled(true);
				return;
			}

			if(((LivingEntity) entity).getHealth() == ((LivingEntity) entity).getMaxHealth())
			{
																		  //getName is deprecated
				Utilities.sendChatMessage(event.getPlayer(), "This /()" + entity.getType().name() + "/() is already healed.");
				event.setCancelled(true);
				return;
			}
			
			//x offset, y offset, z offset from the center, speed, amount, center, radius
			ParticleEffect.HEART.display(0.5F, 0.5F, 0.5F, 0.0F, 20, entity.getLocation(), 255);
			//Play the sound at the location
			entity.getLocation().getWorld().playSound(entity.getLocation(), Sound.LEVEL_UP, 1.0F, 1.0F);

			((LivingEntity) entity).setHealth(((LivingEntity) entity).getMaxHealth());
			currentlyHealing.remove(event.getPlayer());
			AECommands.setIssuingCmd(event.getPlayer(), false);
			Utilities.sendChatMessage(event.getPlayer(), "Animal healed.");
			event.setCancelled(true);
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
		return "ae.heal";
	}

	@Override
	public List<Integer> allowedArgLengths()
	{
		return Arrays.asList(new Integer[]{1}); // /ae heal
	}

	@Override
	public String getSyntax()
	{
		return "<heal>";
	}
}
