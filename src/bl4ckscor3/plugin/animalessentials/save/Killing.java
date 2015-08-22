package bl4ckscor3.plugin.animalessentials.save;

public class Killing
{
	private int amount;
	
	public Killing(int a)
	{
		amount = a;
	}
	
	public int getAmount()
	{
		return amount;
	}
	
	public void decreaseAmount()
	{
		amount--;
	}
}
