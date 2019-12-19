package com.github.commoble.potionofbees;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraftforge.common.brewing.IBrewingRecipe;

public class SplashPotionOfBeesRecipe implements IBrewingRecipe
{

	@Override
	public boolean isInput(ItemStack input)
	{
		return input.getItem() == RegistryObjects.POTION_OF_BEES_ITEM.get();
	}

	@Override
	public boolean isIngredient(ItemStack ingredient)
	{
		return ingredient.getItem() == Items.GUNPOWDER;
	}

	@Override
	public ItemStack getOutput(ItemStack input, ItemStack ingredient)
	{
		return new ItemStack(RegistryObjects.SPLASH_POTION_OF_BEES_ITEM.get());
	}

}
