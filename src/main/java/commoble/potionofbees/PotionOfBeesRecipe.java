package commoble.potionofbees;

import java.util.function.Predicate;
import java.util.function.Supplier;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.brewing.IBrewingRecipe;

public class PotionOfBeesRecipe implements IBrewingRecipe
{
	private final Predicate<ItemStack> inputPredicate;
	private final TagKey<Item> catalystTag;
	private final Supplier<? extends Item> outputItem;

	public PotionOfBeesRecipe(Predicate<ItemStack> inputPredicate, TagKey<Item> catalystTag, Supplier<? extends Item> outputItem)
	{
		this.inputPredicate = inputPredicate;
		this.catalystTag = catalystTag;
		this.outputItem = outputItem;
	}

	@Override
	public boolean isInput(ItemStack input)
	{
		return this.inputPredicate.test(input);
//		return input.getItem() == Items.POTION && PotionUtils.getPotion(input) == Potions.AWKWARD;
	}

	@Override
	public boolean isIngredient(ItemStack ingredient)
	{
		return ingredient.is(this.catalystTag);
//		return ingredient.is(PotionOfBeesMod.POTION_INGREDIENT_TAG);
	}

	@Override
	public ItemStack getOutput(ItemStack input, ItemStack ingredient)
	{
		return this.isInput(input) && this.isIngredient(ingredient)
//			? new ItemStack(PotionOfBeesMod.get().potionOfBeesItem.get())
			? new ItemStack(this.outputItem.get())
			: ItemStack.EMPTY;
	}

}
