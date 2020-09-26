package commoble.potionofbees;

import net.minecraft.util.ResourceLocation;

public class ResourceLocations
{
	public static final ResourceLocation POTION_OF_BEES = getModRL("potion_of_bees");
	public static final ResourceLocation SPLASH_POTION_OF_BEES = getModRL("splash_potion_of_bees");
	public static final ResourceLocation EVANESCENCE = getModRL("evanescence");
	public static final ResourceLocation POTION_OF_BEES_INGREDIENTS = getModRL("potion_of_bees_ingredients");
	
	public static ResourceLocation getModRL(String name)
	{
		return new ResourceLocation(PotionOfBeesMod.MODID, name);
	}
}
