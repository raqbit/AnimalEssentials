package bl4ckscor3.plugin.animalessentials.save;

public class Spawning
{
	private EnumSpawningType type;
	private String name;
	private boolean baby = false;
	private boolean hasCustomName = false;
	
	public Spawning(EnumSpawningType t)
	{
		type = t;
	}
	
	public EnumSpawningType getType()
	{
		return type;
	}
	
	public void setName(String n)
	{
		name = n;
		hasCustomName = true;
	}
	
	public void setBaby(boolean b)
	{
		baby = b;
	}
	
	public String getName()
	{
		return name;
	}
	
	public boolean isBaby()
	{
		return baby;
	}
	
	public boolean hasCustomName()
	{
		return hasCustomName;
	}
	
	public enum EnumSpawningType
	{
		CHICKEN,
		COW,
		HORSE,
		MOOSHROOM,
		OCELOT,
		PIG,
		RABBIT,
		SHEEP,
		WOLF;
	}
}
