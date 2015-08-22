package bl4ckscor3.plugin.animalessentials.cmd;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.plugin.Plugin;

import bl4ckscor3.plugin.animalessentials.core.AECommands;
import bl4ckscor3.plugin.animalessentials.core.AnimalEssentials;
import bl4ckscor3.plugin.animalessentials.save.Killing;
import bl4ckscor3.plugin.animalessentials.util.Utilities;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;

public class Kill implements IAECommand,Listener
{
	private static HashMap<Player,Killing> currentlyKilling = new HashMap<Player,Killing>();
	public static Plugin plugin;

	@Override
	public void exe(Plugin pl, final Player p, Command cmd, String[] args) throws IOException
	{
		if(currentlyKilling.containsKey(p))
		{
			Utilities.sendChatMessage(p, "You can't kill multiple animals at a time. Please kill an animal or wait, then issue the command again.");
			return;
		}

		plugin = pl;
		Utilities.sendChatMessage(p, "Please rightclick the animal you want to kill. " + ChatColor.RED + " THIS IS IRREVERSIBLE!!");
		currentlyKilling.put(p, new Killing(args.length == 1 ? 1 : Integer.parseInt(args[1])));
		AECommands.setIssuingCmd(p, true);
		Bukkit.getScheduler().runTaskLater(AnimalEssentials.instance, new Runnable(){
			@Override
			public void run()
			{
				if(currentlyKilling.containsKey(p))
				{
					currentlyKilling.remove(p);
					AECommands.setIssuingCmd(p, false);
					Utilities.sendChatMessage(p, "You ran out of time to select an animal to kill. Use /()/ae kill()/ to start again.");
				}
			}
		}, 10L * 20); //10 seconds * 20 (server ticks/second)
	}

	@EventHandler
	public void onPlayerInteractEntity(PlayerInteractEntityEvent event)
	{
		if(currentlyKilling.containsKey(event.getPlayer()))
		{
			Entity entity = event.getRightClicked();

			if(!Utilities.isAnimal(entity))
			{
				Utilities.sendChatMessage(event.getPlayer(), "You can't kill this mob, it's " + Utilities.aN(entity.getType().getName(), false) + " /()" + (entity.getType().getName() == null? "Player" : entity.getType().getName()) + "()/ and not an animal.");
				event.setCancelled(true);
				return;
			}

			if(!Utilities.isOwnedBy(event.getPlayer(), entity, true))
			{
				Utilities.sendChatMessage(event.getPlayer(), "This is not your animal, you can't kill it.");
				event.setCancelled(true);
				return;
			}

			//particle type | show particles 65k blocks away? (false = 255 block radius) | x coord of particle | y coord | z coord | x offset (area of effect) | y offset | z offset | speed of particles (some particles move, some don't) | amount of particles (the bigger the offset the bigger this has to be) | ?
			PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(EnumParticle.SMOKE_NORMAL, false, (float)entity.getLocation().getX(), (float)entity.getLocation().getY(), (float)entity.getLocation().getZ(), 0.0F, 0.0F, 0.0F, 0.5F, 100, null);

			for(Player player : Bukkit.getOnlinePlayers())
			{
				((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet); //sending the packet (CraftPlayer is the craftbukkit equivalent of Player)
				player.playSound(entity.getLocation(), Sound.FIZZ, 1.0F, 1.0F);
			}

			entity.remove();

			if(currentlyKilling.get(event.getPlayer()).getAmount() == 1)
			{
				currentlyKilling.remove(event.getPlayer());
				AECommands.setIssuingCmd(event.getPlayer(), false);
			}
			else
				currentlyKilling.get(event.getPlayer()).decreaseAmount();

			Utilities.sendChatMessage(event.getPlayer(), "Animal killed.");
			event.setCancelled(true);
		}
	}

	@Override
	public String getAlias()
	{
		return "kill";
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
				"Kills the right-clicked animal.",
				"By specifying a number at the end of the command, you can kill multiple animals."
		};
	}

	@Override
	public String getPermission()
	{
		return "aess.kill";
	}

	@Override
	public List<Integer> allowedArgLengths()
	{
		return Arrays.asList(new Integer[]{1,2}); // /ae kill [number]
	}

	@Override
	public String getSyntax()
	{
		return "[number]";
	}
}
