package commoble.potionofbees.client.jei;

import java.util.ArrayList;
import java.util.List;

import commoble.potionofbees.PotionOfBeesMod;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.recipe.vanilla.IJeiBrewingRecipe;
import mezz.jei.api.recipe.vanilla.IVanillaRecipeFactory;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.neoforged.neoforge.common.Tags;

@JeiPlugin
public class JeiProxy implements IModPlugin
{
	public static final Identifier ID = PotionOfBeesMod.id(PotionOfBeesMod.MODID);
	
	@Override
	public Identifier getPluginUid()
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
				PotionContents.createItemStack(Items.POTION, Potions.AWKWARD),
				new ItemStack(PotionOfBeesMod.POTION_OF_BEES_ITEM.get())),
			tagCatalyzedBrewingRecipe(factory,
				Tags.Items.GUNPOWDERS,
				new ItemStack(PotionOfBeesMod.POTION_OF_BEES_ITEM.get()),
				new ItemStack(PotionOfBeesMod.SPLASH_POTION_OF_BEES_ITEM.get())),
			tagCatalyzedBrewingRecipe(factory,
				PotionOfBeesMod.DRAGON_BREATH_TAG,
				new ItemStack(PotionOfBeesMod.POTION_OF_BEES_ITEM.get()),
				new ItemStack(PotionOfBeesMod.LINGERING_POTION_OF_BEES_ITEM.get()))));
	}
	
	private static IJeiBrewingRecipe tagCatalyzedBrewingRecipe(IVanillaRecipeFactory factory, TagKey<Item> catalystTag, ItemStack inputPotion, ItemStack outputPotion)
	{
		List<ItemStack> catalysts = new ArrayList<>();
		for (var item : BuiltInRegistries.ITEM.getTagOrEmpty(catalystTag))
		{
			catalysts.add(new ItemStack(item));
		}
		return factory.createBrewingRecipe(
			catalysts,
			inputPotion,
			outputPotion,
			outputPotion.typeHolder().unwrapKey().get().identifier());
	}
}
