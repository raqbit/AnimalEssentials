package bl4ckscor3.plugin.animalessentials.cmd;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.entity.Rabbit;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Tameable;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import com.darkblade12.particleeffect.ParticleEffect;

import bl4ckscor3.plugin.animalessentials.core.AECommands;
import bl4ckscor3.plugin.animalessentials.util.Utilities;

public class Clone implements IAECommand,Listener
{
	private static HashMap<Player,Boolean> currentlyCloning = new HashMap<Player,Boolean>();
	private static HashMap<Player,Integer> taskIDs = new HashMap<Player,Integer>();
	public static Plugin plugin;

	@Override
	public void exe(Plugin pl, CommandSender sender, Command cmd, String[] args) throws IOException
	{
		final Player p = (Player)sender;

		if(currentlyCloning.containsKey(p))
		{
			Utilities.sendChatMessage(p, "You can't clone multiple animals at a time. Please clone one or wait, then issue the command again.");
			return;
		}

		plugin = pl;
		Utilities.sendChatMessage(p, "Please rightclick the animal you want to clone.");
		currentlyCloning.put(p, true);
		AECommands.setIssuingCmd(p, true);

		AbortRunnable task = new AbortRunnable(p);

		task.runTaskLater(pl, 10 * 20L);
		taskIDs.put(p, task.getTaskId());
	}

	@EventHandler
	public void onPlayerInteractEntity(final PlayerInteractEntityEvent event)
	{
		if(currentlyCloning.containsKey(event.getPlayer()) && currentlyCloning.get(event.getPlayer()))
		{
			currentlyCloning.put(event.getPlayer(), false);
			final Entity e = event.getRightClicked();

			if(!Utilities.isAnimal(e))
			{
				Utilities.sendChatMessage(event.getPlayer(), "You can't clone this mob, it's " + Utilities.aN(e.getType().name(), false) + " /()" + (e.getType().name() == null ? "Player" : Utilities.capitalizeFirstLetter(e.getType().name())) + "()/ and not an animal.");
				event.setCancelled(true);
				return;
			}

			EntityType type = e.getType();
			LivingEntity entity = (LivingEntity)e;
			LivingEntity le = (LivingEntity)e.getWorld().spawnEntity(e.getLocation(), type);

			if(!((Ageable)entity).isAdult())
				((Ageable)le).setBaby();
			else
				((Ageable)le).setAdult();

			if(entity.getCustomName() != null)
				le.setCustomName(entity.getCustomName());

			if(entity instanceof Tameable)
			{
				if(((Tameable)entity).isTamed())
				{
					((Tameable)le).setTamed(true);
					((Tameable)le).setOwner(event.getPlayer());
				}
			}

			if(type == EntityType.HORSE)
			{
				Horse h = (Horse)entity;

				if(h.getInventory().getSaddle() != null)
					((Horse)le).getInventory().setSaddle(new ItemStack(Material.SADDLE));

				((Horse)le).setVariant(h.getVariant());
				((Horse)le).setColor(h.getColor());
				((Horse)le).setStyle(h.getStyle());

				if(h.getInventory().getArmor() != null)
					((Horse)le).getInventory().setArmor(new ItemStack(h.getInventory().getArmor()));
			}
			else if(type == EntityType.OCELOT)
			{
				Ocelot o = (Ocelot)entity;

				((Ocelot)le).setCatType(o.getCatType());
			}
			else if(type == EntityType.PIG)
			{
				Pig p = (Pig)entity;

				((Pig)le).setSaddle(p.hasSaddle());
			}
			else if(type == EntityType.SHEEP)
			{
				Sheep s = (Sheep)entity;

				if(entity.getCustomName() == null)
				{
					if(s.getColor() == DyeColor.WHITE && new Random().nextInt(100) == 50)
						entity.setCustomName("Dolly the Sheep");
				}
					
				((Sheep)le).setColor(s.getColor());
			}
			else if(type == EntityType.WOLF)
			{
				Wolf w = (Wolf)entity;

				((Wolf)le).setCollarColor(w.getCollarColor());
			}
			else
			{
				if(!Utilities.getMinecraftVersion(e.getServer()).equals("1.7.10"))
				{
					if(type == EntityType.RABBIT)
					{
						Rabbit r = (Rabbit)entity;

						((Rabbit)le).setRabbitType(r.getRabbitType());
					}
				}
			}

			//x offset, y offset, z offset from the center, speed, amount, center, radius
			ParticleEffect.VILLAGER_HAPPY.display(0.5F, 1.0F, 0.5F, 10.0F, 1000, entity.getLocation(), 255);
			//play the sound at the location
			entity.getWorld().playSound(entity.getLocation(), Sound.ENTITY_FIREWORK_LARGE_BLAST, 2.0F, 1.0F);
			currentlyCloning.remove(event.getPlayer());
			AECommands.setIssuingCmd(event.getPlayer(), false);
			Utilities.sendChatMessage(event.getPlayer(), "Animal cloned.");
			event.setCancelled(true);
			Bukkit.getScheduler().cancelTask(taskIDs.get(event.getPlayer()));
			taskIDs.remove(event.getPlayer());
			event.setCancelled(true);
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
			if(currentlyCloning.containsKey(p))
			{
				currentlyCloning.remove(p);
				AECommands.setIssuingCmd(p, false);
				Utilities.sendChatMessage(p, "You ran out of time to select an animal to clone. Use /()/ae clone()/ to start again.");
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
		return "clone";
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
				"Clones the right-clicked animal."
		};
	}

	@Override
	public String getPermission()
	{
		return "aess.clone";
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
