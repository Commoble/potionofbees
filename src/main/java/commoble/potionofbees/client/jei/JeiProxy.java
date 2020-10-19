package commoble.potionofbees.client.jei;

import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;

import commoble.potionofbees.PotionOfBeesMod;
import commoble.potionofbees.RegistryObjects;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaRecipeCategoryUid;
import mezz.jei.api.recipe.vanilla.IJeiBrewingRecipe;
import mezz.jei.api.recipe.vanilla.IVanillaRecipeFactory;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.tags.ITag;
import net.minecraft.util.ResourceLocation;

@JeiPlugin
public class JeiProxy implements IModPlugin
{
	public static final ResourceLocation ID = new ResourceLocation(PotionOfBeesMod.MODID, PotionOfBeesMod.MODID);
	
	@Override
	public ResourceLocation getPluginUid()
	{
		return ID;
	}

	@Override
	public void registerRecipes(IRecipeRegistration registration)
	{
		IVanillaRecipeFactory factory = registration.getVanillaRecipeFactory();
		registration.addRecipes(Lists.newArrayList(
			makeJeiBrewingRecipe(factory, Items.POTION, PotionOfBeesMod.POTION_INGREDIENT_TAG, new ItemStack(RegistryObjects.POTION_OF_BEES_ITEM)),
			makeJeiBrewingRecipe(factory, Items.SPLASH_POTION, PotionOfBeesMod.POTION_INGREDIENT_TAG, new ItemStack(RegistryObjects.SPLASH_POTION_OF_BEES_ITEM))),
			VanillaRecipeCategoryUid.BREWING);
	}

	public static IJeiBrewingRecipe makeJeiBrewingRecipe(IVanillaRecipeFactory factory, Item inputPotionItem, ITag<Item> catalystTag, ItemStack output)
	{
		List<ItemStack> ingredients = catalystTag.getAllElements()
			.stream()
			.map(ItemStack::new)
			.collect(Collectors.toList());
		return factory.createBrewingRecipe(ingredients, PotionUtils.addPotionToItemStack(new ItemStack(inputPotionItem), Potions.AWKWARD), output);
	}
}
