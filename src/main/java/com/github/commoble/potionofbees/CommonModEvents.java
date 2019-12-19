package com.github.commoble.potionofbees;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

public class CommonModEvents
{
	public static void onRegisterItems(Registrator<Item> reg)
	{
		reg.register(ResourceLocations.BEE_POTION, new BeePotionItem(new Item.Properties().group(ItemGroup.BREWING)));
		reg.register(ResourceLocations.SPLASH_BEE_POTION, new SplashBeePotionItem(new Item.Properties().group(ItemGroup.BREWING)));
	}
	
	public static void onRegisterEntityTypes(Registrator<EntityType<?>> reg)
	{
		reg.register(
			ResourceLocations.SPLASH_BEE_POTION,
			EntityType.Builder.create(SplashBeePotionEntity::new, EntityClassification.MISC)
			.setCustomClientFactory(SplashBeePotionEntity::spawnOnClient)
			.build(ResourceLocations.SPLASH_BEE_POTION.toString()));
	}
}
