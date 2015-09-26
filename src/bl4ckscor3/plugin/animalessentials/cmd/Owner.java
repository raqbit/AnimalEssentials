package bl4ckscor3.plugin.animalessentials.cmd;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import bl4ckscor3.plugin.animalessentials.core.AECommands;
import bl4ckscor3.plugin.animalessentials.util.Utilities;

public class Owner implements IAECommand,Listener
{
	private static List<Player> currentlyChecking = new ArrayList<Player>();
	private static HashMap<Player,Integer> taskIDs = new HashMap<Player,Integer>();
	public static Plugin plugin;

	@Override
	public void exe(Plugin pl, CommandSender sender, Command cmd, String[] args) throws IOException
	{
		final Player p = (Player)sender;
		
		if(currentlyChecking.contains(p))
		{
			Utilities.sendChatMessage(p, "You can't check the owner of multiple animals at a time. Please check the owner of one animal or wait, then issue the command again.");
			return;
		}

		plugin = pl;
		Utilities.sendChatMessage(p, "Please rightclick the animal you want to check the owner of.");
		currentlyChecking.add(p);
		AECommands.setIssuingCmd(p, true);
	
		AbortRunnable task = new AbortRunnable(p);

		task.runTaskLater(pl, 10 * 20L);
		taskIDs.put(p, task.getTaskId());
	}

	@EventHandler
	public void onPlayerInteractEntity(final PlayerInteractEntityEvent event)
	{
		if(currentlyChecking.contains(event.getPlayer()))
		{
			final Entity entity = event.getRightClicked();

			if(!Utilities.isAnimal(entity) || !(entity instanceof Tameable))
			{
				Utilities.sendChatMessage(event.getPlayer(), "You can't check the owner of this mob, it's " + Utilities.aN(entity.getType().name(), false) + " /()" + (entity.getType().name() == null ? "Player" : Utilities.capitalizeFirstLetter(entity.getType().name())) + "()/ and not a tameable animal.");
				event.setCancelled(true);
				return;
			}

			if(((Tameable)entity).isTamed())
			{
				if(((Tameable)entity).getOwner().getName().equals(event.getPlayer().getName()))
					Utilities.sendChatMessage(event.getPlayer(), "This /()" + Utilities.capitalizeFirstLetter(entity.getType().name()) + "()/ is owned by /()you()/.");
				else
					Utilities.sendChatMessage(event.getPlayer(), "This /()" + Utilities.capitalizeFirstLetter(entity.getType().name()) + "()/ is owned by /()" + ((Tameable)entity).getOwner().getName() + "()/.");
			}
			else
			{
				Utilities.sendChatMessage(event.getPlayer(), "This /()" + Utilities.capitalizeFirstLetter(entity.getType().name()) + "()/ is not tamed.");
				return;
			}

			currentlyChecking.remove(event.getPlayer());
			AECommands.setIssuingCmd(event.getPlayer(), false);
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
			if(currentlyChecking.contains(p))
			{
				currentlyChecking.remove(p);
				AECommands.setIssuingCmd(p, false);
				Utilities.sendChatMessage(p, "You ran out of time to select an animal to check the owner of. Use /()/ae owner()/ to start again.");
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
		return "owner";
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
				"Shows the owner of the right-clicked animal."
		};
	}

	@Override
	public String getPermission()
	{
		return "aess.owner";
	}

	@Override
	public List<Integer> allowedArgLengths()
	{
		return Arrays.asList(new Integer[]{1}); // /ae owner
	}

	@Override
	public String getSyntax()
	{
		return "";
	}
}
