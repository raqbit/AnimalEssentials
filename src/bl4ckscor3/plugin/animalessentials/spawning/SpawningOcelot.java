package bl4ckscor3.plugin.animalessentials.spawning;

import org.bukkit.entity.Ocelot.Type;

public class SpawningOcelot extends SpawningTameable
{
	private Type ocelotType;
	private boolean hasType;
	
	public SpawningOcelot(EnumSpawningType t)
	{
		super(t);
	}
	
	public void setOcelotType(Type rt)
	{
		ocelotType = rt;
		hasType = true;
	}
	
	public Type getOcelotType()
	{
		return ocelotType;
	}
	
	public boolean hasType()
	{
		return hasType;
	}
	
	public String typeToString()
	{
		if(ocelotType == Type.WILD_OCELOT)
			return "Wild";
		else if(ocelotType == Type.BLACK_CAT)
			return "Black";
		else if(ocelotType == Type.RED_CAT)
			return "Red";
		else if(ocelotType == Type.SIAMESE_CAT)
			return "Siamese";
		return "Type";
	}
}
