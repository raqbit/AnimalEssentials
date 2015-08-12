package tk.justramon.animalessentials.cmd;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.plugin.Plugin;

import net.md_5.bungee.api.ChatColor;
import tk.justramon.animalessentials.core.AnimalEssentials;
import tk.justramon.animalessentials.util.Utilities;

public class Kill implements IAECommand,Listener
{
	private static boolean waiting = false;
	private static String playerName;
	public static Plugin plugin;
	
	@Override
	public void exe(Plugin pl, final Player p, Command cmd, String[] args) throws IOException
	{
		if(waiting)
		{
			Utilities.sendChatMessage(p, "A player is currently killing an animal and the magic invisible killing device can't handle that much. Please try again later.");
			return;
		}

		plugin = pl;
		playerName = p.getName();
		Utilities.sendChatMessage(p, "Please rightclick the animal you want to kill. " + ChatColor.RED + " THIS IS IRREVERSIBLE!!");
		waiting = true;
		Bukkit.getScheduler().runTaskLater(AnimalEssentials.instance, new Runnable(){
			@Override
			public void run()
			{
				if(waiting)
				{
					waiting = false;
					Utilities.sendChatMessage(p, "You ran out of time to select an animal to kill. Use /()/ae kill()/ to start again.");
				}
			}
		}, 10L * 20); //10 seconds * 20 (server ticks/second)
	}

	@EventHandler
	public void onPlayerInteractEntity(PlayerInteractEntityEvent event)
	{
		if(waiting && event.getPlayer().getName().equals(playerName))
		{
			Entity entity = event.getRightClicked();

			if(!Utilities.isAnimal(entity))
			{
				Utilities.sendChatMessage(event.getPlayer(), "You can't kill this mob, it's a(n) /()" + entity.getType().getName() + "()/ and not an animal.");
				event.setCancelled(true);
				return;
			}

			if(!Utilities.isOwnedBy(event.getPlayer(), entity, true))
			{
				Utilities.sendChatMessage(event.getPlayer(), "This is not your animal, you can't kill it.");
				event.setCancelled(true);
				return;
			}

			for(Player player : Bukkit.getOnlinePlayers())
			{
				player.playSound(entity.getLocation(), Sound.FIZZ, 1.0F, 1.0F);
			}

			entity.remove();
			waiting = false;
			Bukkit.getScheduler().cancelTasks(plugin);
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
				"Kills the right-clicked animal."
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
		return Arrays.asList(new Integer[]{1}); // /ae kill
	}

	@Override
	public String getSyntax()
	{
		return "";
	}
}
