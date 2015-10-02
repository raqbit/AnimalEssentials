package bl4ckscor3.plugin.animalessentials.listener;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.Plugin;

import bl4ckscor3.plugin.animalessentials.util.Utilities;

public class EntityDamageByEntityListener implements Listener
{
	private static final List<String> cooldown = new ArrayList<String>();
	private Plugin pl;

	public EntityDamageByEntityListener(Plugin plugin)
	{
		pl = plugin;
	}

	@EventHandler
	public void onEntityDamageByEntity(final EntityDamageByEntityEvent event)
	{
		if(!pl.getConfig().getBoolean("playerDamageWhenTamed"))
		{
			if(Utilities.isAnimal(event.getEntity()) && event.getEntity() instanceof Tameable && event.getDamager() instanceof Player)
			{
				if(((Tameable)event.getEntity()).isTamed())
				{
					if(!((Tameable)event.getEntity()).getOwner().getUniqueId().equals(event.getDamager().getUniqueId()))
					{
						event.setCancelled(true);
						
						if(!cooldown.contains(event.getDamager().getUniqueId().toString()))
						{
							Utilities.sendChatMessage((Player)event.getDamager(), "You cannot damage this animal. It is owned by /()" + ((Tameable)event.getEntity()).getOwner().getName() + "/().");
							cooldown.add(event.getDamager().getUniqueId().toString());
							pl.getServer().getScheduler().runTaskLater(pl, new Runnable(){
								@Override
								public void run()
								{
									cooldown.remove(event.getDamager().getUniqueId().toString());
								}
							}, 5L * 20L); //5 seconds * 20 server ticks
						}
					}
				}
			}
		}
	}
}