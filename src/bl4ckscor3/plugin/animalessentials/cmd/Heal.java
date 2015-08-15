package bl4ckscor3.plugin.animalessentials.cmd;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftAnimals;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.plugin.Plugin;

import bl4ckscor3.plugin.animalessentials.core.AECommands;
import bl4ckscor3.plugin.animalessentials.core.AnimalEssentials;
import bl4ckscor3.plugin.animalessentials.util.Utilities;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;

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
				Utilities.sendChatMessage(event.getPlayer(), "You can't heal this mob, it's " + Utilities.aN(entity.getType().getName(), false) + " /()" + (entity.getType().getName() == null ? "Player" : entity.getType().getName()) + "()/ and not an animal.");
				event.setCancelled(true);
				return;
			}

			if(!Utilities.isOwnedBy(event.getPlayer(), entity, true))
			{
				Utilities.sendChatMessage(event.getPlayer(), "This is not your animal, you can't heal it.");
				event.setCancelled(true);
				return;
			}

			if(((CraftAnimals)entity).getHealth() == ((CraftAnimals)entity).getMaxHealth())
			{
				Utilities.sendChatMessage(event.getPlayer(), "This /()" + entity.getType().getName() + "/() is already healed.");
				event.setCancelled(true);
				return;
			}
			
			//particle type | show particles 65k blocks away? (false = 255 block radius) | x coord of particle | y coord | z coord | x offset (area of effect) | y offset | z offset | speed of particles (some particles move, some don't) | amount of particles (the bigger the offset the bigger this has to be) | ?
			PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(EnumParticle.HEART, false, (float)entity.getLocation().getX(), (float)entity.getLocation().getY(), (float)entity.getLocation().getZ(), 0.5F, 0.5F, 0.5F, 0.0F, 20, null);

			for(Player player : Bukkit.getOnlinePlayers())
			{
				((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet); //sending the packet (CraftPlayer is the craftbukkit equivalent of Player)
				player.playSound(entity.getLocation(), Sound.LEVEL_UP, 1.0F, 1.0F);

			}

			((CraftAnimals)entity).setHealth(((CraftAnimals)entity).getMaxHealth());
			currentlyHealing.remove(event.getPlayer());
			Bukkit.getScheduler().cancelTasks(plugin);
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
