package com.github.commoble.potionofbees;

import net.minecraft.block.DispenserBlock;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public class CommonModEvents
{
	public static void onRegisterItems(Registrator<Item> reg)
	{
		reg.register(ResourceLocations.POTION_OF_BEES, new PotionOfBeesItem(new Item.Properties().group(ItemGroup.BREWING)));
		reg.register(ResourceLocations.SPLASH_POTION_OF_BEES, new SplashPotionOfBeesItem(new Item.Properties().group(ItemGroup.BREWING)));
	}

	public static void onRegisterEffects(Registrator<Effect> reg)
	{
		reg.register(ResourceLocations.EVANESCENCE, new EvanescenceEffect(EffectType.HARMFUL, 0));
	}

	public static void onRegisterEntityTypes(Registrator<EntityType<?>> reg)
	{
		reg.register(ResourceLocations.SPLASH_POTION_OF_BEES, EntityType.Builder.create(SplashPotionOfBeesEntity::new, EntityClassification.MISC)
			.setCustomClientFactory(SplashPotionOfBeesEntity::spawnOnClient).build(ResourceLocations.SPLASH_POTION_OF_BEES.toString()));
	}

	public static void onCommonSetup(FMLCommonSetupEvent event)
	{
		BrewingRecipeRegistry.addRecipe(new PotionOfBeesRecipe());
		BrewingRecipeRegistry.addRecipe(new SplashPotionOfBeesRecipe());

		// add dispenser behavior for throwing splash potions of bees
		DeferredWorkQueue.runLater(
			() -> DispenserBlock.registerDispenseBehavior(
				RegistryObjects.SPLASH_POTION_OF_BEES_ITEM,
				SplashPotionOfBeesDispenserBehavior::dispenseSplashPotionOfBees));
		
	}
}
