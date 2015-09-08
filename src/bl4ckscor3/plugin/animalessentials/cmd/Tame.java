package bl4ckscor3.plugin.animalessentials.cmd;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Ocelot.Type;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.plugin.Plugin;

import com.darkblade12.particleeffect.ParticleEffect;

import bl4ckscor3.plugin.animalessentials.core.AECommands;
import bl4ckscor3.plugin.animalessentials.core.AnimalEssentials;
import bl4ckscor3.plugin.animalessentials.util.Utilities;

public class Tame implements IAECommand,Listener
{
	private static List<Player> currentlyTaming = new ArrayList<Player>();
	public static Plugin plugin;

	@Override
	public void exe(Plugin pl, final Player p, Command cmd, String[] args) throws IOException
	{
		if(currentlyTaming.contains(p))
		{
			Utilities.sendChatMessage(p, "You can't tame multiple animals at a time. Please tame an animal or wait, then issue the command again.");
			return;
		}

		plugin = pl;
		Utilities.sendChatMessage(p, "Please rightclick the animal you want to tame.");
		currentlyTaming.add(p);
		AECommands.setIssuingCmd(p, true);
		Bukkit.getScheduler().runTaskLater(AnimalEssentials.instance, new Runnable(){
			@Override
			public void run()
			{
				if(currentlyTaming.contains(p))
				{
					currentlyTaming.remove(p);
					AECommands.setIssuingCmd(p, false);
					Utilities.sendChatMessage(p, "You ran out of time to select an animal to tame. Use /()/ae tame()/ to start again.");
				}
			}
		}, 10L * 20); //10 seconds * 20 (server ticks/second)
	}

	@EventHandler
	public void onPlayerInteractEntity(PlayerInteractEntityEvent event)
	{
		if(currentlyTaming.contains(event.getPlayer()))
		{
			Entity entity = event.getRightClicked();

			if(!(entity instanceof Tameable))
			{
				Utilities.sendChatMessage(event.getPlayer(), "You can't tame this mob, it's " + Utilities.aN(entity.getType().name(), false) + " /()" + (entity.getType().name() == null ? "Player" : Utilities.capitalizeFirstLetter(entity.getType().name())) + "()/ and not an tameable animal.");
				event.setCancelled(true);
				return;
			}
			else
			{
				if(((Tameable)entity).isTamed())
				{
					Utilities.sendChatMessage(event.getPlayer(), "This animal is already tamed.");
					event.setCancelled(true);
					return;
				}
			}

			((Tameable)entity).setOwner(event.getPlayer());
			((Tameable)entity).setTamed(true);
			(((LivingEntity) entity)).setHealth(((LivingEntity) entity).getMaxHealth());
			
			if(entity instanceof Ocelot)
			{
				Random r = new Random();
				Type t = Type.WILD_OCELOT;
				
				switch(r.nextInt(3))
				{
					case 0:
						t = Type.BLACK_CAT;
						break;
					case 1:
						t = Type.RED_CAT;
						break;
					case 2:
						t = Type.SIAMESE_CAT;
				}
				
				((Ocelot)entity).setCatType(t);
			}
			
			Location particleLoc = entity.getLocation();
			particleLoc.setY(entity.getLocation().getY() + 1);
			//x offset, y offset, z offset from the center, speed, amount, center, range
			ParticleEffect.FIREWORKS_SPARK.display(1.0F, 1.0F, 1.0F, 0.0F, 100, particleLoc, 255);
			//Play the sound at the location
			entity.getWorld().playSound(entity.getLocation(), Sound.CLICK, 1.0F, 1.0F);
			currentlyTaming.remove(event.getPlayer());
			AECommands.setIssuingCmd(event.getPlayer(), false);
			Utilities.sendChatMessage(event.getPlayer(), "Animal tamed.");
			event.setCancelled(true);
		}
	}

	@Override
	public String getAlias()
	{
		return "tame";
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
				"Tames the right-clicked animal."
		};
	}

	@Override
	public String getPermission()
	{
		return "aess.tame";
	}

	@Override
	public List<Integer> allowedArgLengths()
	{
		return Arrays.asList(new Integer[]{1});
	}

	@Override
	public String getSyntax()
	{
		return "";
	}
}
