package commoble.potionofbees;

import java.util.function.Consumer;

import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.potion.Effect;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.IForgeRegistryEntry;

@Mod(PotionOfBeesMod.MODID)
public class PotionOfBeesMod
{
	public static final String MODID = "potionofbees";
	public static final double BEE_SEARCH_RADIUS = 10D;
	public static final ITag<Item> POTION_INGREDIENT_TAG = ItemTags.makeWrapperTag("potionofbees:potion_of_bees_ingredients"); 

	public static <T extends IForgeRegistryEntry<T>> Consumer<Register<T>> getRegistrator(Consumer<Registrator<T>> consumer)
	{
		return event -> consumer.accept(new Registrator<>(event.getRegistry()));
	}

	public PotionOfBeesMod()
	{
		final IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

		modBus.addGenericListener(Item.class, getRegistrator(CommonModEvents::onRegisterItems));
		modBus.addGenericListener(Effect.class, getRegistrator(CommonModEvents::onRegisterEffects));
		modBus.addGenericListener(EntityType.class, getRegistrator(CommonModEvents::onRegisterEntityTypes));
		
		modBus.addListener(CommonModEvents::onCommonSetup);
	}
}
