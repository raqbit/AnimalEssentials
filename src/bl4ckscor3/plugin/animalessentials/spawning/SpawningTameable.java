package bl4ckscor3.plugin.animalessentials.spawning;

public class SpawningTameable extends Spawning
{
	private boolean tamed = false;
	
	public SpawningTameable(EnumSpawningType t)
	{
		super(t);
	}

	public void setTamed(boolean t)
	{
		tamed = t;
	}
	
	public boolean isTamed()
	{
		return tamed;
	}
}
