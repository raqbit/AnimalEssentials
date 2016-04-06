package bl4ckscor3.plugin.animalessentials.save;

public class Killing
{
	private int amount;
	private boolean enabled = true;
	
	public Killing(int a)
	{
		amount = a;
	}
	
	public int getAmount()
	{
		return amount;
	}
	
	public boolean enabled()
	{
		return enabled;
	}
	
	public void enable()
	{
		enabled = true;
	}
	
	public void decreaseAmount()
	{
		amount--;
		enabled = false;
	}
}
