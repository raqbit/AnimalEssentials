package bl4ckscor3.plugin.animalessentials.save;

import org.bukkit.Material;
import org.bukkit.entity.Horse.Color;
import org.bukkit.entity.Horse.Style;
import org.bukkit.entity.Horse.Variant;

public class SpawningHorse extends SpawningTameable
{
	private Variant variant;
	private Color color;
	private Style style;
	private Material armor;
	private boolean hasVariant = false;
	private boolean hasColor = false;
	private boolean hasStyle = false;
	private boolean hasArmor = false;
	private boolean saddle = false;
	
	public SpawningHorse(EnumSpawningType t)
	{
		super(t);
	}
	
	public void setVariant(Variant v)
	{
		variant = v;
		hasVariant = true;
	}
	
	public void setColor(Color c)
	{
		color = c;
		hasColor = true;
	}
	
	public void setStyle(Style s)
	{
		style = s;
		hasStyle = true;
	}
	
	public void setArmor(Material a)
	{
		armor = a;
		
		if(a == Material.GLASS)
			hasArmor = false;
		else
			hasArmor = true;
	}
	
	public void setSaddle(boolean s)
	{
		saddle = s;
	}
	
	public Variant getVariant()
	{
		return variant;
	}
	
	public Color getColor()
	{
		return color;
	}
	
	public Style getStyle()
	{
		return style;
	}

	public Material getArmor()
	{
		return armor;
	}
	
	public boolean hasVariant()
	{
		return hasVariant;
	}
	
	public boolean hasColor()
	{
		return hasColor;
	}
	
	public boolean hasStyle()
	{
		return hasStyle;
	}
	
	public boolean hasArmor()
	{
		return hasArmor;
	}
	
	public boolean hasSaddle()
	{
		return saddle;
	}
	
	public String variantToString()
	{
		if(variant == Variant.HORSE)
			return "Horse";
		else if(variant == Variant.DONKEY)
			return "Donkey";
		else if(variant == Variant.MULE)
			return "Mule";
		else if(variant == Variant.UNDEAD_HORSE)
			return "Undead Horse";
		else if(variant == Variant.SKELETON_HORSE)
			return "Skeleton Horse";
		return "Variant";
	}
	
	public String colorToString()
	{
		if(color == Color.WHITE)
			return "White";
		else if(color == Color.CREAMY)
			return "Creamy";
		else if(color == Color.CHESTNUT)
			return "Chestnut";
		else if(color == Color.BROWN)
			return "Brown";
		else if(color == Color.BLACK)
			return "Black";
		else if(color == Color.GRAY)
			return "Gray";
		else if(color == Color.DARK_BROWN)
			return "Dark Brown";
		return "Color";
	}
	
	public String styleToString()
	{
		if(style == Style.NONE)
			return "None";
		else if(style == Style.WHITE)
			return "White";
		else if(style == Style.WHITEFIELD)
			return "Whitefield";
		else if(style == Style.WHITE_DOTS)
			return "White Dots";
		else if(style == Style.BLACK_DOTS)
			return "Black Dots";
		return "Style";
	}
	
	public String armorToString()
	{
		if(armor == Material.GLASS)
			return "None";
		else if(armor == Material.IRON_BARDING)
			return "Iron Armor";
		else if(armor == Material.GOLD_BARDING)
			return "Gold Armor";
		else if(armor == Material.DIAMOND_BARDING)
			return "Diamond Barding";
		return "Armor";
	}
}
