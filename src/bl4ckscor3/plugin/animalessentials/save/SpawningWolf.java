package bl4ckscor3.plugin.animalessentials.save;

import org.bukkit.DyeColor;

public class SpawningWolf extends SpawningTameable
{
	private DyeColor color;
	private boolean hasColor;
	
	public SpawningWolf(EnumSpawningType t)
	{
		super(t);
	}
	
	public void setColor(DyeColor c)
	{
		color = c;
		hasColor = true;
	}
	
	public DyeColor getColor()
	{
		return color;
	}
	
	public boolean hasColor()
	{
		return hasColor;
	}
	
	public int colorToInt()
	{
		if(color == DyeColor.WHITE)
			return 0;
		else if(color == DyeColor.ORANGE)
			return 1;
		else if(color == DyeColor.MAGENTA)
			return 2;
		else if(color == DyeColor.LIGHT_BLUE)
			return 3;
		else if(color == DyeColor.YELLOW)
			return 4;
		else if(color == DyeColor.LIME)
			return 5;
		else if(color == DyeColor.PINK)
			return 6;
		else if(color == DyeColor.GRAY)
			return 7;
		else if(color == DyeColor.SILVER)
			return 8;
		else if(color == DyeColor.CYAN)
			return 9;
		else if(color == DyeColor.PURPLE)
			return 10;
		else if(color == DyeColor.BLUE)
			return 11;
		else if(color == DyeColor.BROWN)
			return 12;
		else if(color == DyeColor.GREEN)
			return 13;
		else if(color == DyeColor.RED)
			return 14;
		else if(color == DyeColor.BLACK)
			return 15;
		return -1;
	}
}
