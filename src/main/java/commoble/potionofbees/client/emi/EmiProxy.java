package commoble.potionofbees.client.emi;

import commoble.potionofbees.PotionOfBeesMod;
import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.recipe.EmiBrewingRecipe;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.neoforged.neoforge.common.Tags;

@EmiEntrypoint
public class EmiProxy implements EmiPlugin
{
	@Override
	public void register(EmiRegistry registry)
	{
		registry.addRecipe(new EmiBrewingRecipe(
			EmiStack.of(PotionContents.createItemStack(Items.POTION, Potions.AWKWARD)),
			EmiIngredient.of(PotionOfBeesMod.POTION_INGREDIENT_TAG),
			EmiStack.of(new ItemStack(PotionOfBeesMod.get().potionOfBeesItem.get())),
			PotionOfBeesMod.get().potionOfBeesItem.getId()));
		registry.addRecipe(new EmiBrewingRecipe(
			EmiStack.of(new ItemStack(PotionOfBeesMod.get().potionOfBeesItem.get())),
			EmiIngredient.of(Tags.Items.GUNPOWDERS),
			EmiStack.of(new ItemStack(PotionOfBeesMod.get().splashPotionOfBeesItem.get())),
			PotionOfBeesMod.get().splashPotionOfBeesItem.getId()));
		registry.addRecipe(new EmiBrewingRecipe(
			EmiStack.of(new ItemStack(PotionOfBeesMod.get().potionOfBeesItem.get())),
			EmiIngredient.of(PotionOfBeesMod.DRAGON_BREATH_TAG),
			EmiStack.of(new ItemStack(PotionOfBeesMod.get().lingeringPotionOfBeesItem.get())),
			PotionOfBeesMod.get().lingeringPotionOfBeesItem.getId()));
	}
}
