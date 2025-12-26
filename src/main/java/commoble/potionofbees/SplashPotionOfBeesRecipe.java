package commoble.potionofbees;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.brewing.IBrewingRecipe;

public class SplashPotionOfBeesRecipe implements IBrewingRecipe
{

	@Override
	public boolean isInput(ItemStack input)
	{
		return input.getItem() == PotionOfBeesMod.POTION_OF_BEES_ITEM.get();
	}

	@Override
	public boolean isIngredient(ItemStack ingredient)
	{
		return ingredient.is(Tags.Items.GUNPOWDERS);
	}

	@Override
	public ItemStack getOutput(ItemStack input, ItemStack ingredient)
	{
		return this.isInput(input) && this.isIngredient(ingredient)
			? new ItemStack(PotionOfBeesMod.SPLASH_POTION_OF_BEES_ITEM.get())
			: ItemStack.EMPTY;
	}

}
