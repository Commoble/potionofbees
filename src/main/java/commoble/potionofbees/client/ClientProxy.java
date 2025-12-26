package commoble.potionofbees.client;

import commoble.potionofbees.PotionOfBeesMod;
import net.minecraft.client.renderer.entity.NoopRenderer;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

public class ClientProxy
{
	public static void subscribeClientEvents(IEventBus modBus, IEventBus forgeBus)
	{
		modBus.addListener(ClientProxy::registerRenderers);
	}
	
	private static void registerRenderers(EntityRenderersEvent.RegisterRenderers event)
	{
		event.registerEntityRenderer(PotionOfBeesMod.SPLASH_POTION_OF_BEES_ENTITY.get(), ThrownItemRenderer::new);
		event.registerEntityRenderer(PotionOfBeesMod.LINGERING_POTION_OF_BEES_ENTITY.get(), ThrownItemRenderer::new);
		event.registerEntityRenderer(PotionOfBeesMod.LINGERING_POTION_OF_BEES_CLOUD_ENTITY.get(), NoopRenderer::new);
	}
}
