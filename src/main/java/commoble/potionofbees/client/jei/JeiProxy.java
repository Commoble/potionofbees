package commoble.potionofbees.client.jei;

import java.util.List;

import commoble.potionofbees.PotionOfBeesMod;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.recipe.vanilla.IJeiBrewingRecipe;
import mezz.jei.api.recipe.vanilla.IVanillaRecipeFactory;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ForgeRegistries;

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
		registration.addRecipes(RecipeTypes.BREWING, List.of(
			tagCatalyzedBrewingRecipe(factory,
				PotionOfBeesMod.POTION_INGREDIENT_TAG,
				PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.AWKWARD),
				new ItemStack(PotionOfBeesMod.get().potionOfBeesItem.get())),
			tagCatalyzedBrewingRecipe(factory,
				Tags.Items.GUNPOWDER,
				new ItemStack(PotionOfBeesMod.get().potionOfBeesItem.get()),
				new ItemStack(PotionOfBeesMod.get().splashPotionOfBeesItem.get())),
			tagCatalyzedBrewingRecipe(factory,
				PotionOfBeesMod.DRAGON_BREATH_TAG,
				new ItemStack(PotionOfBeesMod.get().potionOfBeesItem.get()),
				new ItemStack(PotionOfBeesMod.get().lingeringPotionOfBeesItem.get()))));
	}
	
	private static IJeiBrewingRecipe tagCatalyzedBrewingRecipe(IVanillaRecipeFactory factory, TagKey<Item> catalystTag, ItemStack inputPotion, ItemStack outputPotion)
	{
		return factory.createBrewingRecipe(
			ForgeRegistries.ITEMS.tags()
				.getTag(catalystTag)
				.stream()
				.map(ItemStack::new)
				.toList(),
			inputPotion,
			outputPotion);
	}
}
