package com.github.commoble.potionofbees.client;

import com.github.commoble.potionofbees.PotionOfBeesMod;
import com.github.commoble.potionofbees.RegistryObjects;
import com.github.commoble.potionofbees.SplashPotionOfBeesEntity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.SpriteRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(modid=PotionOfBeesMod.MODID, value=Dist.CLIENT, bus=Bus.MOD)
public class ClientModEventHandler
{
	@SubscribeEvent
	public static void onClientSetup(FMLClientSetupEvent event)
	{
		RenderingRegistry.registerEntityRenderingHandler(RegistryObjects.getSplashPotionOfBeesEntityType(), ClientModEventHandler::getSplashPotionOfBeesRenderer);
	}
	
	public static SpriteRenderer<SplashPotionOfBeesEntity> getSplashPotionOfBeesRenderer(EntityRendererManager manager)
	{
		return new SpriteRenderer<>(manager, Minecraft.getInstance().getItemRenderer());
	}
}
