package bl4ckscor3.plugin.animalessentials.cmd;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
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
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import com.darkblade12.particleeffect.ParticleEffect;

import bl4ckscor3.plugin.animalessentials.save.Spawning;
import bl4ckscor3.plugin.animalessentials.save.SpawningHorse;
import bl4ckscor3.plugin.animalessentials.save.SpawningOcelot;
import bl4ckscor3.plugin.animalessentials.save.SpawningPig;
import bl4ckscor3.plugin.animalessentials.save.SpawningRabbit;
import bl4ckscor3.plugin.animalessentials.save.SpawningSheep;
import bl4ckscor3.plugin.animalessentials.save.SpawningTameable;
import bl4ckscor3.plugin.animalessentials.save.SpawningWolf;
import bl4ckscor3.plugin.animalessentials.save.Spawning.EnumSpawningType;
import bl4ckscor3.plugin.animalessentials.util.Utilities;

public class Spawn implements IAECommand,Listener
{
	private static HashMap<Player, Spawning> currentlySpawning = new HashMap<Player, Spawning>();
	private static List<Player> currentlyNaming = new ArrayList<Player>();
	private static String[] colorNames = new String[]{
			"White",
			"Orange",
			"Magenta",
			"Light Blue",
			"Yellow",
			"Light Green",
			"Pink",
			"Dark Gray",
			"Light Gray",
			"Cyan",
			"Purple",
			"Blue",
			"Brown",
			"Green",
			"Red",
			"Black"
	};

	@Override
	public void exe(Plugin pl, Player p, Command cmd, String[] args) throws IOException
	{
		openMain(p);
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event)
	{
		if(event.getCurrentItem() == null) //check to prevent stupid stacktraces when clicking outside of inventory
			return;
		
		Player p = (Player)event.getWhoClicked();
		Inventory inv = event.getInventory();
		int slot = event.getSlot();

		if(inv.getName().equals("Animal Selection"))
		{
			switch(slot)
			{
				case 0:
					currentlySpawning.put(p, new Spawning(EnumSpawningType.CHICKEN));
					event.setCancelled(true);
					openGeneric(p, "Chicken");
					break;
				case 1:
					currentlySpawning.put(p, new Spawning(EnumSpawningType.COW));
					event.setCancelled(true);
					openGeneric(p, "Cow");
					break;
				case 2:
					currentlySpawning.put(p, new SpawningHorse(EnumSpawningType.HORSE));
					event.setCancelled(true);
					openHorse(p);
					break;
				case 3:
					currentlySpawning.put(p, new Spawning(EnumSpawningType.MOOSHROOM));
					event.setCancelled(true);
					openGeneric(p, "Mooshroom");
					break;
				case 4:
					currentlySpawning.put(p, new SpawningOcelot(EnumSpawningType.OCELOT));
					event.setCancelled(true);
					openOcelot(p);
					break;
				case 5:
					currentlySpawning.put(p, new SpawningPig(EnumSpawningType.PIG));
					event.setCancelled(true);
					openPig(p);
					break;
				case 6:
					currentlySpawning.put(p, new SpawningRabbit(EnumSpawningType.RABBIT));
					event.setCancelled(true);
					openRabbit(p);
					break;
				case 7:
					currentlySpawning.put(p, new SpawningSheep(EnumSpawningType.SHEEP));
					event.setCancelled(true);
					openSheep(p);
					break;
				case 8:
					currentlySpawning.put(p, new SpawningWolf(EnumSpawningType.WOLF));
					event.setCancelled(true);
					openWolf(p);
					break;
				case 13:
					p.closeInventory();
					removeFromLists(p);
					break;
			}
		}
		else if(inv.getName().equals("Spawn Chicken"))
		{
			event.setCancelled(true);
			guiSpawnGeneric(event, p, slot, EntityType.CHICKEN);
		}
		else if(inv.getName().equals("Spawn Cow"))
		{
			event.setCancelled(true);
			guiSpawnGeneric(event, p, slot, EntityType.COW);
		}
		else if(inv.getName().equals("Spawn Horse"))
		{
			SpawningHorse s = (SpawningHorse)currentlySpawning.get(p);

			event.setCancelled(true);

			if(slot == 0) //setting name
			{
				currentlyNaming.add(p);
				Utilities.sendChatMessage(p, "Please enter a name into the chat:");
				p.closeInventory();
			}
			else if(slot == 1) //setting baby
			{
				s.setBaby(!s.isBaby());
				addBabyButton(s, inv, 1);
			}
			else if(slot == 2) //setting saddle
			{
				s.setSaddle(!s.hasSaddle());
				inv.setItem(2, getItemWithName(Material.SADDLE, (short)0, (!s.hasSaddle() ? "Not Saddled" : "Saddled"), null));
			}
			else if(slot == 3) //taming
			{
				s.setTamed(!s.isTamed());
				addTamingButton(s, inv, 3, Material.HAY_BLOCK);
			}
			else if(slot == 4) //setting variant
				openHorseVariant(p);
			else if(slot == 5) //setting color
				openHorseColor(p);
			else if(slot == 6) //setting style
				openHorseStyle(p);
			else if(slot == 7) //setting armor
				openHorseArmor(p);
			else if(slot == 8) //spawning
			{
				Horse h = (Horse)p.getWorld().spawnEntity(p.getLocation(), EntityType.HORSE);

				if(s.hasCustomName())
					h.setCustomName(s.getName());

				if(s.isBaby())
					((Ageable)h).setBaby();
				else
					((Ageable)h).setAdult();

				if(s.isTamed())
				{
					h.setTamed(true);
					h.setOwner(p);
					//h.setOwnerUUID(p); shouldn't be needed
				}
				else //just in case
					h.setTamed(false);

				if(s.hasSaddle())
					h.getInventory().setSaddle(new ItemStack(Material.SADDLE));

				if(s.hasVariant())
					h.setVariant(s.getVariant());

				if(s.hasColor())
					h.setColor(s.getColor());

				if(s.hasStyle())
					h.setStyle(s.getStyle());

				if(s.hasArmor())
					h.getInventory().setArmor(new ItemStack(s.getArmor(), 1));

				removeFromLists(p);
				p.closeInventory();
				sendParticlesAndMsg(p, h);
			}
			else if(slot == 13) //back
				openMain(p);
		}
		else if(inv.getName().equals("Spawn Mooshroom"))
		{
			event.setCancelled(true);
			guiSpawnGeneric(event, p, slot, EntityType.MUSHROOM_COW);
		}
		else if(inv.getName().equals("Spawn Ocelot"))
		{
			SpawningOcelot s = (SpawningOcelot)currentlySpawning.get(p);

			event.setCancelled(true);

			if(slot == 0) //setting name
			{
				currentlyNaming.add(p);
				Utilities.sendChatMessage(p, "Please enter a name into the chat:");
				p.closeInventory();
			}
			else if(slot == 1) //setting baby
			{
				s.setBaby(!s.isBaby());
				addBabyButton(s, inv, 1);
			}
			else if(slot == 2) //taming
			{
				s.setTamed(!s.isTamed());
				addTamingButton(s, inv, 2, Material.RAW_FISH);
			}
			else if(slot == 3) //setting type
				openOcelotType(p);
			else if(slot == 7) //spawning
			{
				Ocelot o = (Ocelot)p.getWorld().spawnEntity(p.getLocation(), EntityType.OCELOT);

				if(s.hasCustomName())
					o.setCustomName(s.getName());

				if(s.isBaby())
					((Ageable)o).setBaby();
				else
					((Ageable)o).setAdult();

				if(s.hasType())
					o.setCatType(s.getOcelotType());

				if(s.isTamed())
				{
					o.setTamed(true);
					o.setOwner(p);
					//o.setOwnerUUID(p.getUniqueId()); shouldn't be needed
				}
				else //just in case
					o.setTamed(false);

				removeFromLists(p);
				p.closeInventory();
				sendParticlesAndMsg(p, o);
			}
			else if(slot == 8) //back
				openMain(p);
		}
		else if(inv.getName().equals("Spawn Pig"))
		{
			SpawningPig s = (SpawningPig)currentlySpawning.get(p);

			event.setCancelled(true);

			if(slot == 0) //setting name
			{
				currentlyNaming.add(p);
				Utilities.sendChatMessage(p, "Please enter a name into the chat:");
				p.closeInventory();
			}
			else if(slot == 1) //setting baby
			{
				s.setBaby(!s.isBaby());
				addBabyButton(s, inv, 1);
			}
			else if(slot == 2) //setting saddle
			{
				s.setSaddle(!s.hasSaddle());
				inv.setItem(2, getItemWithName(Material.SADDLE, (short)0, (!s.hasSaddle() ? "Not Saddled" : "Saddled"), null));
			}
			else if(slot == 7) //spawning
			{
				Pig pig = (Pig)p.getWorld().spawnEntity(p.getLocation(), EntityType.PIG);

				if(s.hasCustomName())
					pig.setCustomName(s.getName());

				if(s.isBaby())
					((Ageable)pig).setBaby();
				else
					((Ageable)pig).setAdult();;

				if(s.hasSaddle())
					pig.setSaddle(true);
				else //just in case
					pig.setSaddle(false);

				removeFromLists(p);
				p.closeInventory();
				sendParticlesAndMsg(p, pig);
			}
			else if(slot == 8) //back
				openMain(p);
		}
		else if(inv.getName().equals("Spawn Rabbit"))
		{
			SpawningRabbit s = (SpawningRabbit)currentlySpawning.get(p);

			event.setCancelled(true);

			if(slot == 0) //setting name
			{
				currentlyNaming.add(p);
				Utilities.sendChatMessage(p, "Please enter a name into the chat:");
				p.closeInventory();
			}
			else if(slot == 1) //setting baby
			{
				s.setBaby(!s.isBaby());
				addBabyButton(s, inv, 1);
			}
			else if(slot == 2) //setting type
				openRabbitType(p);
			else if(slot == 7) //spawning
			{
				Rabbit r = (Rabbit)p.getWorld().spawnEntity(p.getLocation(), EntityType.valueOf("RABBIT"));

				if(s.hasCustomName())
					r.setCustomName(s.getName());

				if(s.isBaby())
					((Ageable)r).setBaby();
				else
					((Ageable)r).setAdult();

				if(s.hasType())
					r.setRabbitType(s.getRabbitType());

				removeFromLists(p);
				p.closeInventory();
				sendParticlesAndMsg(p, r);
			}
			else if(slot == 8) //back
				openMain(p);
		}
		else if(inv.getName().equals("Spawn Sheep"))
		{
			SpawningSheep s = (SpawningSheep)currentlySpawning.get(p);

			event.setCancelled(true);

			if(slot == 0) //setting name
			{
				currentlyNaming.add(p);
				Utilities.sendChatMessage(p, "Please enter a name into the chat:");
				p.closeInventory();
			}
			else if(slot == 1) //setting baby
			{
				s.setBaby(!s.isBaby());
				addBabyButton(s, inv, 1);
			}
			else if(slot == 2) //setting color
				openSheepColors(p);
			else if(slot == 7) //spawning
			{
				Sheep sheep = (Sheep)p.getWorld().spawnEntity(p.getLocation(), EntityType.SHEEP);

				if(s.hasCustomName())
					sheep.setCustomName(s.getName());

				if(s.isBaby())
					((Ageable)sheep).setBaby();
				else
					((Ageable)sheep).setAdult();
				if(s.hasColor())
					sheep.setColor(s.getColor());

				removeFromLists(p);
				p.closeInventory();
				sendParticlesAndMsg(p, sheep);
			}
			else if(slot == 8) //back
			{
				openMain(p);
				return;
			}
		}
		else if(inv.getName().equals("Spawn Wolf"))
		{
			SpawningWolf s = (SpawningWolf)currentlySpawning.get(p);

			event.setCancelled(true);

			if(slot == 0) //setting name
			{
				currentlyNaming.add(p);
				Utilities.sendChatMessage(p, "Please enter a name into the chat:");
				p.closeInventory();
			}
			else if(slot == 1) //setting baby
			{
				s.setBaby(!s.isBaby());
				addBabyButton(s, inv, 1);
			}
			else if(slot == 2) //taming
			{
				s.setTamed(!s.isTamed());
				addTamingButton(s, inv, 2, Material.BONE);
			}
			else if(slot == 3) //setting color
				openCollarColors(p);
			else if(slot == 7) //spawning
			{
				Wolf w = (Wolf)p.getWorld().spawnEntity(p.getLocation(), EntityType.WOLF);

				event.setCancelled(true);
				
				if(s.hasCustomName())
					w.setCustomName(s.getName());

				if(s.isBaby())
					((Ageable)w).setBaby();
				else
					((Ageable)w).setAdult();

				if(s.isTamed())
				{
					w.setTamed(true);
					w.setOwner(p);
					//w.setOwnerUUID(p.getUniqueId()); shouldn't be needed
					w.setHealth(w.getMaxHealth());
				}
				else //just in case
					w.setTamed(false);
				
				if(s.hasColor())
					w.setCollarColor(s.getColor());

				removeFromLists(p);
				p.closeInventory();
				sendParticlesAndMsg(p, w);
			}
			else if(slot == 8) //back
				openMain(p);
		}
		else if(inv.getName().equals("Choose Ocelot Type"))
		{
			SpawningOcelot s = (SpawningOcelot)currentlySpawning.get(p);

			event.setCancelled(true);

			if(slot == 0)
				s.setOcelotType(Ocelot.Type.WILD_OCELOT);
			else if(slot == 1)
				s.setOcelotType(Ocelot.Type.BLACK_CAT);
			else if(slot == 2)
				s.setOcelotType(Ocelot.Type.RED_CAT);
			else if(slot == 3)
				s.setOcelotType(Ocelot.Type.SIAMESE_CAT);
			else if(slot == 8) //back
				openOcelot(p);

			inv.setItem(2, getItemWithName(Material.CLAY, (short)0, s.typeToString(), null));
			openOcelot(p);
		}
		else if(inv.getName().equals("Choose Rabbit Type"))
		{
			SpawningRabbit s = (SpawningRabbit)currentlySpawning.get(p);

			event.setCancelled(true);

			if(slot == 0)
				s.setRabbitType(Rabbit.Type.BROWN);
			else if(slot == 1)
				s.setRabbitType(Rabbit.Type.WHITE);
			else if(slot == 2)
				s.setRabbitType(Rabbit.Type.BLACK);
			else if(slot == 3)
				s.setRabbitType(Rabbit.Type.BLACK_AND_WHITE);
			else if(slot == 4)
				s.setRabbitType(Rabbit.Type.GOLD);
			else if(slot == 5)
				s.setRabbitType(Rabbit.Type.SALT_AND_PEPPER);
			else if(slot == 6)
				s.setRabbitType(Rabbit.Type.THE_KILLER_BUNNY);
			else if(slot == 8) //back
				openRabbit(p);

			inv.setItem(2, getItemWithName(Material.CLAY, (short)0, s.typeToString(), null));
			openRabbit(p);
		}
		else if(inv.getName().equals("Choose Horse Variant"))
		{
			SpawningHorse s = (SpawningHorse)currentlySpawning.get(p);

			event.setCancelled(true);

			if(slot == 0)
				s.setVariant(Horse.Variant.HORSE);
			else if(slot == 1)
				s.setVariant(Horse.Variant.DONKEY);
			else if(slot == 2)
				s.setVariant(Horse.Variant.MULE);
			else if(slot == 3)
				s.setVariant(Horse.Variant.UNDEAD_HORSE);
			else if(slot == 4)
				s.setVariant(Horse.Variant.SKELETON_HORSE);
			else if(slot == 8) //back
				openHorse(p);

			inv.setItem(4, getItemWithName(Material.COOKIE, (short)0, s.variantToString(), null));
			openHorse(p);
		}
		else if(inv.getName().equals("Choose Horse Color"))
		{
			SpawningHorse s = (SpawningHorse)currentlySpawning.get(p);

			event.setCancelled(true);

			if(slot == 0)
				s.setColor(Horse.Color.WHITE);
			else if(slot == 1)
				s.setColor(Horse.Color.CREAMY);
			else if(slot == 2)
				s.setColor(Horse.Color.CHESTNUT);
			else if(slot == 3)
				s.setColor(Horse.Color.BROWN);
			else if(slot == 4)
				s.setColor(Horse.Color.BLACK);
			else if(slot == 5)
				s.setColor(Horse.Color.GRAY);
			else if(slot == 6)
				s.setColor(Horse.Color.DARK_BROWN);
			else if(slot == 8) //back
				openHorse(p);

			inv.setItem(5, getItemWithName(Material.CLAY, (short)0, s.colorToString(), null));
			openHorse(p);
		}
		else if(inv.getName().equals("Choose Horse Style"))
		{
			SpawningHorse s = (SpawningHorse)currentlySpawning.get(p);

			event.setCancelled(true);

			if(slot == 0)
				s.setStyle(Horse.Style.NONE);
			else if(slot == 1)
				s.setStyle(Horse.Style.WHITE);
			else if(slot == 2)
				s.setStyle(Horse.Style.WHITEFIELD);
			else if(slot == 3)
				s.setStyle(Horse.Style.WHITE_DOTS);
			else if(slot == 4)
				s.setStyle(Horse.Style.BLACK_DOTS);
			else if(slot == 8) //back
				openHorse(p);

			inv.setItem(6, getItemWithName(Material.CLAY_BALL, (short)0, s.styleToString(), null));
			openHorse(p);
		}
		else if(inv.getName().equals("Choose Horse Armor"))
		{
			SpawningHorse s = (SpawningHorse)currentlySpawning.get(p);

			event.setCancelled(true);

			if(slot == 0)
				s.setArmor(Material.GLASS);
			else if(slot == 1)
				s.setArmor(Material.IRON_BARDING);
			else if(slot == 2)
				s.setArmor(Material.GOLD_BARDING);
			else if(slot == 3)
				s.setArmor(Material.DIAMOND_BARDING);
			else if(slot == 8) //back
				openHorse(p);

			inv.setItem(7, getItemWithName(Material.LEATHER_CHESTPLATE, (short)0, s.armorToString(), null));
			openHorse(p);
		}
		else if(inv.getName().equals("Choose Sheep Color"))
		{
			SpawningSheep s = (SpawningSheep)currentlySpawning.get(p);

			event.setCancelled(true);

			if(slot == 0)
				s.setColor(DyeColor.WHITE);
			else if(slot == 1)
				s.setColor(DyeColor.ORANGE);
			else if(slot == 2)
				s.setColor(DyeColor.MAGENTA);
			else if(slot == 3)
				s.setColor(DyeColor.LIGHT_BLUE);
			else if(slot == 4)
				s.setColor(DyeColor.YELLOW);
			else if(slot == 5)
				s.setColor(DyeColor.LIME);
			else if(slot == 6)
				s.setColor(DyeColor.PINK);
			else if(slot == 7)
				s.setColor(DyeColor.GRAY);
			else if(slot == 8)
				s.setColor(DyeColor.SILVER);
			else if(slot == 9)
				s.setColor(DyeColor.CYAN);
			else if(slot == 10)
				s.setColor(DyeColor.PURPLE);
			else if(slot == 11)
				s.setColor(DyeColor.BLUE);
			else if(slot == 12)
				s.setColor(DyeColor.BROWN);
			else if(slot == 13)
				s.setColor(DyeColor.GREEN);
			else if(slot == 14)
				s.setColor(DyeColor.RED);
			else if(slot == 15)
				s.setColor(DyeColor.BLACK);
			else if(slot == 17)
				openSheep(p);

			inv.setItem(2, getItemWithName(Material.CLAY, (short)0, (s.colorToInt() == -1 ? "Color" : colorNames[s.colorToInt()]), null));
			openSheep(p);
		}
		else if(inv.getName().equals("Choose Collar Color"))
		{
			SpawningWolf s = (SpawningWolf)currentlySpawning.get(p);

			event.setCancelled(true);

			if(slot == 0)
				s.setColor(DyeColor.WHITE);
			else if(slot == 1)
				s.setColor(DyeColor.ORANGE);
			else if(slot == 2)
				s.setColor(DyeColor.MAGENTA);
			else if(slot == 3)
				s.setColor(DyeColor.LIGHT_BLUE);
			else if(slot == 4)
				s.setColor(DyeColor.YELLOW);
			else if(slot == 5)
				s.setColor(DyeColor.LIME);
			else if(slot == 6)
				s.setColor(DyeColor.PINK);
			else if(slot == 7)
				s.setColor(DyeColor.GRAY);
			else if(slot == 8)
				s.setColor(DyeColor.SILVER);
			else if(slot == 9)
				s.setColor(DyeColor.CYAN);
			else if(slot == 10)
				s.setColor(DyeColor.PURPLE);
			else if(slot == 11)
				s.setColor(DyeColor.BLUE);
			else if(slot == 12)
				s.setColor(DyeColor.BROWN);
			else if(slot == 13)
				s.setColor(DyeColor.GREEN);
			else if(slot == 14)
				s.setColor(DyeColor.RED);
			else if(slot == 15)
				s.setColor(DyeColor.BLACK);
			else if(slot == 17)
				openWolf(p);

			inv.setItem(2, getItemWithName(Material.CLAY, (short)0, (s.colorToInt() == -1 ? "Collar Color" : colorNames[s.colorToInt()]), null));
			openWolf(p);
		}
		else
			return;
	}

	private void openMain(Player p)
	{
		if(currentlySpawning.containsKey(p))
			removeFromLists(p);

		Inventory inv = p.getServer().createInventory(p, 18, "Animal Selection");

		inv.setItem(0, getItemWithName(Material.MONSTER_EGG, (short)93, "Chicken", null));
		inv.setItem(1, getItemWithName(Material.MONSTER_EGG, (short)92, "Cow", null));
		inv.setItem(2, getItemWithName(Material.MONSTER_EGG, (short)100, "Horse", null));
		inv.setItem(3, getItemWithName(Material.MONSTER_EGG, (short)96, "Mooshroom", null));
		inv.setItem(4, getItemWithName(Material.MONSTER_EGG, (short)98, "Ocelot", null));
		inv.setItem(5, getItemWithName(Material.MONSTER_EGG, (short)90, "Pig", null));
		inv.setItem(6, getItemWithName(Material.MONSTER_EGG, (short)101, "Rabbit", null));
		inv.setItem(7, getItemWithName(Material.MONSTER_EGG, (short)91, "Sheep", null));
		inv.setItem(8, getItemWithName(Material.MONSTER_EGG, (short)95, "Wolf", null));
		addBackButton(inv, 13);
		p.openInventory(inv);
	}

	/**
	 * Opens a spawn animal GUI for animals without any extra NBT data
	 * @param p The player who wants to spawn the animal
	 * @param mobName The name of the animal to display
	 */
	private void openGeneric(Player p, String mobName)
	{
		Inventory inv = p.getServer().createInventory(p, 9, "Spawn " + mobName);
		Spawning s = currentlySpawning.get(p);

		addNamingButton(s, inv, 0);
		addBabyButton(s, inv, 1);
		addSpawnButton(s, inv, 7);
		addBackButton(inv, 8);
		p.openInventory(inv);
	}

	private void openHorse(Player p)
	{
		Inventory inv = p.getServer().createInventory(p, 18, "Spawn Horse");
		SpawningHorse s = (SpawningHorse)currentlySpawning.get(p);

		addNamingButton(s, inv, 0);
		addBabyButton(s, inv, 1);
		inv.setItem(2, getItemWithName(Material.SADDLE, (short)0, (!s.hasSaddle() ? "Not Saddled" : "Saddled"), null));
		addTamingButton(s, inv, 3, Material.HAY_BLOCK);
		inv.setItem(4, getItemWithName(Material.COOKIE, (short)0, s.variantToString(), null));
		inv.setItem(5, getItemWithName(Material.CLAY, (short)0, s.colorToString(), null));
		inv.setItem(6, getItemWithName(Material.CLAY_BALL, (short)0, s.styleToString(), null));
		inv.setItem(7, getItemWithName((s.getArmor() == null ? Material.LEATHER_CHESTPLATE : s.getArmor()), (short)0, s.armorToString(), null));
		addSpawnButton(s, inv, 8);
		addBackButton(inv, 13);
		p.openInventory(inv);
	}

	private void openHorseVariant(Player p)
	{
		Inventory inv = p.getServer().createInventory(p, 9, "Choose Horse Variant");

		inv.setItem(0, getItemWithName(Material.WHEAT, (short)0, "Horse", null));
		inv.setItem(1, getItemWithName(Material.APPLE, (short)0, "Donkey", null));
		inv.setItem(2, getItemWithName(Material.CHEST, (short)0, "Mule", null));
		inv.setItem(3, getItemWithName(Material.SKULL_ITEM, (short)2, "Undead Horse", null));
		inv.setItem(4, getItemWithName(Material.SKULL_ITEM, (short)0, "Skeleton Horse", null));
		addBackButton(inv, 8);
		p.openInventory(inv);
	}

	private void openHorseColor(Player p)
	{
		Inventory inv = p.getServer().createInventory(p, 9, "Choose Horse Color");

		inv.setItem(0, getItemWithName(Material.WOOL, (short)0, "White", null));
		inv.setItem(1, getItemWithName(Material.WOOL, (short)8, "Creamy", null));
		inv.setItem(2, getItemWithName(Material.WOOL, (short)1, "Chestnut", null));
		inv.setItem(3, getItemWithName(Material.WOOL, (short)4, "Brown", null));
		inv.setItem(4, getItemWithName(Material.WOOL, (short)15, "Black", null));
		inv.setItem(5, getItemWithName(Material.WOOL, (short)7, "Gray", null));
		inv.setItem(6, getItemWithName(Material.WOOL, (short)12, "Dark Brown", null));
		addBackButton(inv, 8);
		p.openInventory(inv);
	}

	private void openHorseStyle(Player p)
	{
		Inventory inv = p.getServer().createInventory(p, 9, "Choose Horse Style");

		inv.setItem(0, getItemWithName(Material.GLASS, (short)0, "None", null));
		inv.setItem(1, getItemWithName(Material.INK_SACK, (short)15, "White", null));
		inv.setItem(2, getItemWithName(Material.WOOL, (short)0, "Whitefields", null));
		inv.setItem(3, getItemWithName(Material.PUMPKIN_SEEDS, (short)0, "White Dots", null));
		inv.setItem(4, getItemWithName(Material.MELON_SEEDS, (short)0, "Black Dots", null));
		addBackButton(inv, 8);
		p.openInventory(inv);
	}

	private void openHorseArmor(Player p)
	{
		Inventory inv = p.getServer().createInventory(p, 9, "Choose Horse Armor");

		inv.setItem(0, getItemWithName(Material.GLASS, (short)0, "None", null));
		inv.setItem(1, getItemWithName(Material.IRON_BARDING, (short)0, "Iron Armor", null));
		inv.setItem(2, getItemWithName(Material.GOLD_BARDING, (short)0, "Gold Armor", null));
		inv.setItem(3, getItemWithName(Material.DIAMOND_BARDING, (short)0, "Diamond Armor", null));
		addBackButton(inv, 8);
		p.openInventory(inv);
	}

	private void openOcelot(Player p)
	{
		Inventory inv = p.getServer().createInventory(p, 9, "Spawn Ocelot");
		SpawningOcelot s = (SpawningOcelot)currentlySpawning.get(p);

		addNamingButton(s, inv, 0);
		addBabyButton(s, inv, 1);
		addTamingButton(s, inv, 2, Material.RAW_FISH);
		inv.setItem(3, getItemWithName(Material.CLAY, (short)0, s.typeToString(), null));
		addSpawnButton(s, inv, 7);
		addBackButton(inv, 8);
		p.openInventory(inv);
	}

	private void openOcelotType(Player p)
	{
		Inventory inv = p.getServer().createInventory(p, 9, "Choose Ocelot Type");

		inv.setItem(0, getItemWithName(Material.WOOL, (short)4, "Wild", null));
		inv.setItem(1, getItemWithName(Material.WOOL, (short)15, "Black", null));
		inv.setItem(2, getItemWithName(Material.WOOL, (short)14, "Red", null));
		inv.setItem(3, getItemWithName(Material.WOOL, (short)0, "Siamese", null));
		addBackButton(inv, 8);
		p.openInventory(inv);
	}

	private void openPig(Player p)
	{
		Inventory inv = p.getServer().createInventory(p, 9, "Spawn Pig");
		SpawningPig s = (SpawningPig)currentlySpawning.get(p);

		addNamingButton(s, inv, 0);
		addBabyButton(s, inv, 1);
		inv.setItem(2, getItemWithName(Material.SADDLE, (short)0, (!s.hasSaddle() ? "Not Saddled" : "Saddled"), null));
		addSpawnButton(s, inv, 7);
		addBackButton(inv, 8);
		p.openInventory(inv);
	}

	private void openRabbit(Player p)
	{
		Inventory inv = p.getServer().createInventory(p, 9, "Spawn Rabbit");
		SpawningRabbit s = (SpawningRabbit)currentlySpawning.get(p);

		addNamingButton(s, inv, 0);
		addBabyButton(s, inv, 1);
		inv.setItem(2, getItemWithName(Material.CLAY, (short)0, s.typeToString(), null));
		addSpawnButton(s, inv, 7);
		addBackButton(inv, 8);
		p.openInventory(inv);
	}

	private void openRabbitType(Player p)
	{
		Inventory inv = p.getServer().createInventory(p, 9, "Choose Rabbit Type");

		inv.setItem(0, getItemWithName(Material.WOOL, (short)12, "Brown", null));
		inv.setItem(1, getItemWithName(Material.WOOL, (short)0, "White", null));
		inv.setItem(2, getItemWithName(Material.WOOL, (short)15, "Black", null));
		inv.setItem(3, getItemWithName(Material.WOOL, (short)8, "Black and White", null));
		inv.setItem(4, getItemWithName(Material.WOOL, (short)4, "Gold", null));
		inv.setItem(5, getItemWithName(Material.INK_SACK, (short)15, "Salt and Pepper", null));
		inv.setItem(6, getItemWithName(Material.WOOL, (short)14, ChatColor.BOLD + "THE KILLER BUNNY", ChatColor.DARK_RED));
		addBackButton(inv, 8);
		p.openInventory(inv);
	}

	private void openSheep(Player p)
	{
		Inventory inv = p.getServer().createInventory(p, 9, "Spawn Sheep");
		SpawningSheep s = (SpawningSheep)currentlySpawning.get(p);

		addNamingButton(s, inv, 0);
		addBabyButton(s, inv, 1);
		inv.setItem(2, getItemWithName(Material.CLAY, (short)0, (s.colorToInt() == -1 ? "Color" : colorNames[s.colorToInt()]), null));
		addSpawnButton(s, inv, 7);
		addBackButton(inv, 8);
		p.openInventory(inv);
	}

	private void openSheepColors(Player p)
	{
		Inventory inv = p.getServer().createInventory(p, 18, "Choose Sheep Color");

		for(int i = 0; i < colorNames.length; i++)
		{
			inv.setItem(i, getItemWithName(Material.WOOL, (short)i, colorNames[i], null));
		}

		addBackButton(inv, 17);
		p.openInventory(inv);
	}

	private void openWolf(Player p)
	{
		Inventory inv = p.getServer().createInventory(p, 9, "Spawn Wolf");
		SpawningWolf s = (SpawningWolf)currentlySpawning.get(p);

		addNamingButton(s, inv, 0);
		addBabyButton(s, inv, 1);
		addTamingButton(s, inv, 2, Material.BONE);
		inv.setItem(3, getItemWithName(Material.CLAY, (short)0, (s.colorToInt() == -1 ? "Collar Color" : colorNames[s.colorToInt()]), null));
		addSpawnButton(s, inv, 7);
		addBackButton(inv, 8);
		p.openInventory(inv);
	}

	private void openCollarColors(Player p)
	{
		Inventory inv = p.getServer().createInventory(p, 18, "Choose Collar Color");
		int meta = 0;
		
		for(int i = 15; i >= 0; i--)
		{
			inv.setItem(i, getItemWithName(Material.INK_SACK, (short)meta++, colorNames[i], null));
		}

		addBackButton(inv, 17);
		p.openInventory(inv);
	}
	
	private void guiSpawnGeneric(InventoryClickEvent event, Player p, int slot, EntityType type)
	{
		Spawning g = currentlySpawning.get(p);

		if(slot == 0) //setting name
		{
			currentlyNaming.add(p);
			Utilities.sendChatMessage(p, "Please enter a name into the chat:");
			p.closeInventory();
		}
		else if(slot == 1) //setting baby
		{
			g.setBaby(!g.isBaby());
			event.getInventory().setItem(1, getItemWithName(Material.MILK_BUCKET, (short)0, (!g.isBaby()? "Not A Baby" : "Baby"), null));
		}
		else if(slot == 7) //spawning
		{
			LivingEntity e = (LivingEntity)p.getWorld().spawnEntity(p.getLocation(), type);

			if(g.hasCustomName())
				e.setCustomName(g.getName());

			if(g.isBaby())
				((Ageable)e).setBaby();

			removeFromLists(p);
			p.closeInventory();
			sendParticlesAndMsg(p, e);
		}
		else if(slot == 8) //back
			openMain(p);
	}

	@EventHandler
	public void onAsyncPlayerChat(AsyncPlayerChatEvent event)
	{
		if(currentlyNaming.contains(event.getPlayer()))
		{
			Spawning s = currentlySpawning.get(event.getPlayer());

			s.setName(event.getMessage());
			event.setCancelled(true);
			currentlyNaming.remove(event.getPlayer());

			if(s.getType() == EnumSpawningType.CHICKEN)
				openGeneric(event.getPlayer(), "Chicken");
			else if(s.getType() == EnumSpawningType.COW)
				openGeneric(event.getPlayer(), "Cow");
			else if(s.getType() == EnumSpawningType.HORSE)
				openHorse(event.getPlayer());
			else if(s.getType() == EnumSpawningType.MOOSHROOM)
				openGeneric(event.getPlayer(), "Mooshroom");
			else if(s.getType() == EnumSpawningType.OCELOT)
				openOcelot(event.getPlayer());
			else if(s.getType() == EnumSpawningType.PIG)
				openPig(event.getPlayer());
			else if(s.getType() == EnumSpawningType.RABBIT)
				openRabbit(event.getPlayer());
			else if(s.getType() == EnumSpawningType.SHEEP)
				openSheep(event.getPlayer());
			else if(s.getType() == EnumSpawningType.WOLF)
				openWolf(event.getPlayer());
		}
	}

	/**
	 * Creates an ItemStack with the given values
	 * @param mat The Material of the ItemStack
	 * @param damageValue The damage value of the ItemStack
	 * @param name The name of the ItemStack
	 * @param color The color of the name (null if white)
	 * @return The finished ItemStack
	 */
	private static ItemStack getItemWithName(Material mat, short damageValue, String name, ChatColor color)
	{
		ItemStack stack = new ItemStack(mat, 1, damageValue);
		ItemMeta meta = stack.getItemMeta();

		meta.setDisplayName((color == null ? ChatColor.WHITE : color) + name);
		stack.setItemMeta(meta);
		return stack;
	}

	/**
	 * Adds a back button to the inventory
	 * @param inv The inventory to add the button to
	 * @param slot The slot to add the button to
	 */
	private void addBackButton(Inventory inv, int slot)
	{
		ItemStack stack = new ItemStack(Material.REDSTONE_BLOCK, 1);
		ItemMeta meta = stack.getItemMeta();

		meta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "Back");
		stack.setItemMeta(meta);
		inv.setItem(slot, stack);
	}

	/**
	 * Adds a naming button to the inventory
	 * @param s The Spawning class which is currently holding the animaldata
	 * @param inv The inventory to add the button to
	 * @param slot The slot to add the button to
	 */
	private void addNamingButton(Spawning s, Inventory inv, int slot)
	{
		inv.setItem(slot, getItemWithName(Material.NAME_TAG, (short)0, (!s.hasCustomName() ? "Name" : s.getName()), null));
	}

	/**
	 * Adds a baby button to the inventory
	 * @param s The Spawning class which is currently holding the animaldata
	 * @param inv The inventory to add the button to
	 * @param slot The slot to add the button to
	 */
	private void addBabyButton(Spawning s, Inventory inv, int slot)
	{
		inv.setItem(slot, getItemWithName(Material.MILK_BUCKET, (short)0, (!s.isBaby()? "Not A Baby" : "Baby"), null));
	}

	/**
	 * Adds a taming button to the inventory
	 * @param s The SpawningTameable class which is currently holding the animaldata
	 * @param inv The inventory to add the button to
	 * @param slot The slot to add the button to
	 * @param m The item to show
	 */
	private void addTamingButton(SpawningTameable s, Inventory inv, int slot, Material m)
	{
		inv.setItem(slot, getItemWithName(m, (short)0, (!s.isTamed()? "Not Tamed" : "Tamed"), null));
	}

	/**
	 * Adds a taming button to the inventory
	 * @param s The Spawning class which is currently holding the animaldata
	 * @param inv The inventory to add the button to
	 * @param slot The slot to add the button to
	 */
	private void addSpawnButton(Spawning s, Inventory inv, int slot)
	{
		inv.setItem(slot, getItemWithName(Material.MONSTER_EGG, (short)0, "Spawn", null));
	}

	/**
	 * Removing the player from all lists
	 * @param p The player to remove from the lists
	 */
	private void removeFromLists(Player p)
	{
		if(currentlySpawning.containsKey(p))
			currentlySpawning.remove(p);

		if(currentlyNaming.contains(p))
			currentlyNaming.remove(p);
	}

	/**
	 * Spawns particles, plays a sound and sends a message after the animal has spawned
	 * @param p The player who spawned the animal
	 * @param entity The animal which got spawned
	 */
	private void sendParticlesAndMsg(Player p, Entity entity)
	{
		
		//x offset, y offset, z offset from the center, speed, amount, center, radius
		ParticleEffect.VILLAGER_HAPPY.display(0.5F, 1.0F, 0.5F, 10.0F, 1000, entity.getLocation(), 1);
		//Play the sound at the location
		entity.getLocation().getWorld().playSound(entity.getLocation(), Sound.FIREWORK_LARGE_BLAST, 2.0F, 1.0F);

		((LivingEntity)entity).setNoDamageTicks(5*20); //no damage for 5 seconds
		Utilities.sendChatMessage(p, "Animal spawned.");
	}
	
	@Override
	public String getAlias()
	{
		return "spawn";
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
				"Displays a menu to the player, in which he can select an animal to spawn and customize it."
		};
	}

	@Override
	public String getPermission()
	{
		return "aess.spawn";
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
