package bl4ckscor3.plugin.animalessentials.cmd;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import com.darkblade12.particleeffect.ParticleEffect;

import bl4ckscor3.plugin.animalessentials.core.AECommands;
import bl4ckscor3.plugin.animalessentials.save.Killing;
import bl4ckscor3.plugin.animalessentials.util.Utilities;

public class Kill implements IAECommand,Listener
{
	private static HashMap<Player,Killing> currentlyKilling = new HashMap<Player,Killing>();
	private static HashMap<Player,Integer> taskIDs = new HashMap<Player,Integer>();
	public static Plugin plugin;

	@Override
	public void exe(Plugin pl, CommandSender sender, Command cmd, String[] args) throws IOException
	{
		final Player p = (Player)sender;
		
		if(currentlyKilling.containsKey(p))
		{
			Utilities.sendChatMessage(p, "You can't kill multiple animals by issuing the command multiple times. Please use /()/ae kill <amount>()/ after rightclicking an animal or waiting.");
			return;
		}

		int kills = args.length == 1 || Integer.parseInt(args[1]) <= 0 ? 1 : Integer.parseInt(args[1]);
		
		plugin = pl;
		Utilities.sendChatMessage(p, "Please rightclick the animal you want to kill. " + ChatColor.RED + " THIS IS IRREVERSIBLE!!");
		Utilities.sendChatMessage(p, "Kills available: " + kills);
		currentlyKilling.put(p, new Killing(kills));
		AECommands.setIssuingCmd(p, true);
		
		AbortRunnable task = new AbortRunnable(p);

		task.runTaskLater(pl, 10 * 20L);
		taskIDs.put(p, task.getTaskId());
	}

	@EventHandler
	public void onPlayerInteractEntity(PlayerInteractEntityEvent event)
	{
		if(currentlyKilling.containsKey(event.getPlayer()))
		{
			Entity entity = event.getRightClicked();

			if(!Utilities.isAnimal(entity))
			{
				Utilities.sendChatMessage(event.getPlayer(), "You can't kill this mob, it's " + Utilities.aN(entity.getType().name(), false) + " /()" + (entity.getType().name() == null? "Player" : Utilities.capitalizeFirstLetter(entity.getType().name())) + "()/ and not an animal.");
				event.setCancelled(true);
				return;
			}

			if(!Utilities.isOwnedBy(event.getPlayer(), entity, true))
			{
				Utilities.sendChatMessage(event.getPlayer(), "This is not your animal, you can't kill it.");
				event.setCancelled(true);
				return;
			}
			//x offset, y offset, z offset from the center, speed, amount, center, radius
			ParticleEffect.SMOKE_NORMAL.display(0.0F, 0.0F, 0.0F, 0.5F, 100, entity.getLocation(), 255);
			//Play the sound at the location
			entity.getLocation().getWorld().playSound(entity.getLocation(), Sound.FIZZ, 1.0F, 1.0F);

			entity.remove();
			Utilities.sendChatMessage(event.getPlayer(), "Animal killed.");
			
			if(currentlyKilling.get(event.getPlayer()).getAmount() == 1)
			{
				Utilities.sendChatMessage(event.getPlayer(), "Kills left: 0");
				currentlyKilling.remove(event.getPlayer());
				AECommands.setIssuingCmd(event.getPlayer(), false);
			}
			else
			{
				currentlyKilling.get(event.getPlayer()).decreaseAmount();
				Utilities.sendChatMessage(event.getPlayer(), "Kills left: " + currentlyKilling.get(event.getPlayer()).getAmount());
			}

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
			if(currentlyKilling.containsKey(p))
			{
				currentlyKilling.remove(p);
				AECommands.setIssuingCmd(p, false);
				Utilities.sendChatMessage(p, "You ran out of time to select an animal to kill. Use /()/ae kill()/ to start again.");
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
