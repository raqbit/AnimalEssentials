package tk.justramon.animalessentials.cmd;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;

import tk.justramon.animalessentials.core.AnimalEssentials;
import tk.justramon.animalessentials.util.Utilities;

public class Name implements IAECommand,Listener
{
	private static boolean waiting = false;
	private static String playerName;
	public static Plugin plugin;
	private static String animalName;

	@Override
	public void exe(Plugin pl, final Player p, Command cmd, String[] args) throws IOException
	{
		if(waiting)
		{
			Utilities.sendChatMessage(p, "A player is currently naming an animal and the magic invisible naming device can't handle that much. Please try again later.");
			return;
		}

		plugin = pl;
		playerName = p.getName();
		animalName = args[1];
		Utilities.sendChatMessage(p, "Please rightclick the animal you want to name.");
		waiting = true;
		Bukkit.getScheduler().runTaskLater(AnimalEssentials.instance, new Runnable(){
			@Override
			public void run()
			{
				if(waiting)
				{
					waiting = false;
					Utilities.sendChatMessage(p, "You ran out of time to select an animal to name. Use /()/ae name()/ to start again.");
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
				Utilities.sendChatMessage(event.getPlayer(), "You can't name this mob, it's a(n) /()" + entity.getType().getName() + "()/ and not an animal.");
				Bukkit.getScheduler().cancelTasks(plugin);
				waiting = false;
				return;
			}

			if(!Utilities.isOwnedBy(event.getPlayer(), entity, true))
			{
				Utilities.sendChatMessage(event.getPlayer(), "This is not your animal, you can't name it.");
				event.setCancelled(true);
				return;
			}

			if(entity.getCustomName() != null && entity.getCustomName().equals(animalName))
			{
				Utilities.sendChatMessage(event.getPlayer(), "The animal is already named /()" + animalName + "()/.");
				event.setCancelled(true);
				return;
			}
			
			if(plugin.getConfig().getBoolean("shouldNamingUseNametag"))
			{
				if(event.getPlayer().getGameMode() != GameMode.CREATIVE) //checking if the player is in creative, if so, we won't use a name tag
				{
					if(!event.getPlayer().getInventory().contains(Material.NAME_TAG))
					{
						Utilities.sendChatMessage(event.getPlayer(), "You need a name tag in your inventory to be able to name this animal.");
						return;
					}
					else
					{
						PlayerInventory inv = event.getPlayer().getInventory();
						ItemStack stack = inv.getItem(inv.first(Material.NAME_TAG));

						stack.setAmount(stack.getAmount() - 1);
						inv.setItem(inv.first(Material.NAME_TAG), stack);
					}
				}
			}

			for(Player player : Bukkit.getOnlinePlayers())
			{
				player.playSound(entity.getLocation(), Sound.CHICKEN_EGG_POP, 1.0F, 1.0F);
			}

			entity.setCustomName(animalName);
			waiting = false;
			Bukkit.getScheduler().cancelTasks(plugin);
			Utilities.sendChatMessage(event.getPlayer(), "Animal named.");
			event.setCancelled(true);
		}
	}

	@Override
	public String getAlias()
	{
		return "name";
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
				"Names the right-clicked animal."
		};
	}

	@Override
	public String getPermission()
	{
		return "aess.name";
	}

	@Override
	public List<Integer> allowedArgLengths()
	{
		return Arrays.asList(new Integer[]{2}); // /ae name <name>
	}
	
	@Override
	public String getSyntax()
	{
		return "<newName>";
	}
}
