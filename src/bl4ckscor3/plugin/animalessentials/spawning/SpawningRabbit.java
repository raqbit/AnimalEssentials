package bl4ckscor3.plugin.animalessentials.spawning;

import org.bukkit.ChatColor;
import org.bukkit.entity.Rabbit.Type;

public class SpawningRabbit extends Spawning
{
	private Type rabbitType;
	private boolean hasType;
	
	public SpawningRabbit(EnumSpawningType t)
	{
		super(t);
	}
	
	public void setRabbitType(Type rt)
	{
		rabbitType = rt;
		hasType = true;
	}
	
	public Type getRabbitType()
	{
		return rabbitType;
	}
	
	public boolean hasType()
	{
		return hasType;
	}
	
	public String typeToString()
	{
		if(rabbitType == Type.BROWN)
			return "Brown";
		else if(rabbitType == Type.WHITE)
			return "White";
		else if(rabbitType == Type.BLACK)
			return "Black";
		else if(rabbitType == Type.BLACK_AND_WHITE)
			return "Black and White";
		else if(rabbitType == Type.GOLD)
			return "Gold";
		else if(rabbitType == Type.SALT_AND_PEPPER)
			return "Salt and Pepper";
		else if(rabbitType == Type.THE_KILLER_BUNNY)
			return ChatColor.DARK_RED + "" + ChatColor.BOLD + "THE KILLER BUNNY";
		return "Type";
	}
}
