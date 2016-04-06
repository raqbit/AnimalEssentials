package bl4ckscor3.plugin.animalessentials.save;

public class Naming
{
	private String name;
	private boolean enabled = true;
	
	public Naming(String n)
	{
		name = n;
	}
	
	public String getName()
	{
		return name;
	}
	
	public boolean enabled()
	{
		return enabled;
	}
	
	public void disable()
	{
		enabled = false;
	}
}
