package com.github.commoble.potionofbees;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.tags.ItemTags;
import net.minecraftforge.common.brewing.IBrewingRecipe;

public class PotionOfBeesRecipe implements IBrewingRecipe
{

	@Override
	public boolean isInput(ItemStack input)
	{
		return input.getItem() == Items.POTION && PotionUtils.getPotionFromItem(input) == Potions.AWKWARD;
	}

	@Override
	public boolean isIngredient(ItemStack ingredient)
	{
		return ItemTags.getCollection().get(ResourceLocations.POTION_OF_BEES_INGREDIENTS).contains(ingredient.getItem());
	}

	@Override
	public ItemStack getOutput(ItemStack input, ItemStack ingredient)
	{
		return this.isInput(input) && this.isIngredient(ingredient)
			? new ItemStack(RegistryObjects.POTION_OF_BEES_ITEM)
			: ItemStack.EMPTY;
	}

}
