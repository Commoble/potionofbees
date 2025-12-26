package commoble.potionofbees;

import java.util.List;
import java.util.function.Function;

import commoble.potionofbees.client.ClientProxy;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.dispenser.ProjectileDispenseBehavior;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.food.Foods;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.consume_effects.ConsumeEffect;
import net.minecraft.world.level.block.DispenserBlock;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

@Mod(PotionOfBeesMod.MODID)
public class PotionOfBeesMod
{
	public static final String MODID = "potionofbees";
	public static final double BEE_SEARCH_RADIUS = 10D;
	public static final TagKey<Item> POTION_INGREDIENT_TAG = TagKey.create(Registries.ITEM, id(Names.POTION_OF_BEES_INGREDIENTS));
	public static final TagKey<Item> DRAGON_BREATH_TAG = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("c","dragon_breath"));

	private static final DeferredRegister<ConsumeEffect.Type<?>> CONSUME_EFFECTS = defreg(Registries.CONSUME_EFFECT_TYPE);
	private static final DeferredRegister.Entities ENTITIES = defreg(DeferredRegister::createEntities);
	private static final DeferredRegister.Items ITEMS = defreg(DeferredRegister::createItems);
	private static final DeferredRegister<MobEffect> MOB_EFFECTS = defreg(Registries.MOB_EFFECT);
	
	public static final DeferredHolder<EntityType<?>, EntityType<SplashPotionOfBeesEntity>> SPLASH_POTION_OF_BEES_ENTITY = ENTITIES.registerEntityType(
		Names.SPLASH_POTION_OF_BEES,
		SplashPotionOfBeesEntity::new,
		MobCategory.MISC);
	public static final DeferredHolder<EntityType<?>, EntityType<LingeringPotionOfBeesEntity>> LINGERING_POTION_OF_BEES_ENTITY = ENTITIES.registerEntityType(
		Names.LINGERING_POTION_OF_BEES,
		LingeringPotionOfBeesEntity::create,
		MobCategory.MISC);
	public static final DeferredHolder<EntityType<?>, EntityType<LingeringPotionOfBeesCloud>> LINGERING_POTION_OF_BEES_CLOUD_ENTITY = ENTITIES.registerEntityType(
		Names.LINGERING_POTION_OF_BEES_CLOUD,
		LingeringPotionOfBeesCloud::create,
		MobCategory.MISC,
		builder -> builder
			.fireImmune()
			.sized(6.0F, 0.5F)
			.clientTrackingRange(10)
			.updateInterval(Integer.MAX_VALUE));
	public static final DeferredHolder<ConsumeEffect.Type<?>, ConsumeEffect.Type<BeesConsumeEffect>> BEES_CONSUME_EFFECT = CONSUME_EFFECTS.register("bees", () -> new ConsumeEffect.Type<>(BeesConsumeEffect.CODEC, BeesConsumeEffect.STREAM_CODEC));;
	public static final DeferredHolder<Item, Item> POTION_OF_BEES_ITEM = ITEMS.registerItem(
		Names.POTION_OF_BEES,
		Item::new,
		props -> props
			.usingConvertsTo(Items.GLASS_BOTTLE)
			.craftRemainder(Items.GLASS_BOTTLE)
			.food(Foods.HONEY_BOTTLE, new Consumable(
				1.6F,
				ItemUseAnimation.DRINK,
				SoundEvents.GENERIC_DRINK,
				false,
				List.of(BeesConsumeEffect.INSTANCE))));
	public static final DeferredHolder<Item, ThrowableItem> SPLASH_POTION_OF_BEES_ITEM = ITEMS.registerItem(
		Names.SPLASH_POTION_OF_BEES,
		props -> new ThrowableItem(
			props,
			() -> SoundEvents.SPLASH_POTION_THROW,
			SplashPotionOfBeesEntity::throwFromThrower,
			SplashPotionOfBeesEntity::throwFromPosition));
	public static final DeferredHolder<Item, ThrowableItem> LINGERING_POTION_OF_BEES_ITEM = ITEMS.registerItem(
		Names.LINGERING_POTION_OF_BEES,
		props -> new ThrowableItem(
			props,
			() -> SoundEvents.LINGERING_POTION_THROW,
			LingeringPotionOfBeesEntity::throwFromThrower,
			LingeringPotionOfBeesEntity::throwFromPosition));
	public static final DeferredHolder<MobEffect, EvanescenceEffect> EVANESCENCE_EFFECT = MOB_EFFECTS.register(Names.EVANESCENCE, () -> new EvanescenceEffect(MobEffectCategory.HARMFUL, 0));
	
	public PotionOfBeesMod(IEventBus modBus)
	{
		final IEventBus forgeBus = NeoForge.EVENT_BUS;
		
		modBus.addListener(this::onBuildCreativeTabs);
		modBus.addListener(this::onCommonSetup);
		
		forgeBus.addListener(this::onRegisterBrewingRecipes);
		
		ClientProxy.subscribeClientEvents(modBus, forgeBus);
	}
	
	private static <T> DeferredRegister<T> defreg(ResourceKey<Registry<T>> registryKey)
	{
		IEventBus modBus = ModList.get().getModContainerById(MODID).get().getEventBus();
		DeferredRegister<T> register = DeferredRegister.create(registryKey, MODID);
		register.register(modBus);
		return register;
	}
	
	private static <R extends DeferredRegister<?>> R defreg(Function<String,R> defregFactory)
	{
		IEventBus modBus = ModList.get().getModContainerById(MODID).get().getEventBus();
		R register = defregFactory.apply(MODID);
		register.register(modBus);
		return register;
	}
	
	private void onBuildCreativeTabs(BuildCreativeModeTabContentsEvent event)
	{
		if (event.getTabKey() == CreativeModeTabs.FOOD_AND_DRINKS)
		{
			event.accept(POTION_OF_BEES_ITEM.get());
			event.accept(SPLASH_POTION_OF_BEES_ITEM.get());
			event.accept(LINGERING_POTION_OF_BEES_ITEM.get());
		}
	}
	
	private void onCommonSetup(FMLCommonSetupEvent event)
	{
		event.enqueueWork(this::afterCommonSetup); // run unthreadsafe stuff on main thread
	}
	
	private void afterCommonSetup()
	{
		DispenserBlock.registerBehavior(SPLASH_POTION_OF_BEES_ITEM.get(), new ProjectileDispenseBehavior(SPLASH_POTION_OF_BEES_ITEM.get()));
		DispenserBlock.registerBehavior(LINGERING_POTION_OF_BEES_ITEM.get(), new ProjectileDispenseBehavior(LINGERING_POTION_OF_BEES_ITEM.get()));
	}
	
	private void onRegisterBrewingRecipes(RegisterBrewingRecipesEvent event)
	{
		var builder = event.getBuilder();
		builder.addRecipe(new PotionOfBeesRecipe(input -> {
				if (input.getItem() != Items.POTION)
				{
					return false;
				}
				PotionContents contents = input.get(DataComponents.POTION_CONTENTS);
				if (contents == null)
				{
					return false;
				}
				return contents.is(Potions.AWKWARD);
			},
			POTION_INGREDIENT_TAG,
			POTION_OF_BEES_ITEM));
		builder.addRecipe(new PotionOfBeesRecipe(input -> input.getItem() == POTION_OF_BEES_ITEM.get(),
			Tags.Items.GUNPOWDERS,
			SPLASH_POTION_OF_BEES_ITEM));
		builder.addRecipe(new PotionOfBeesRecipe(input -> input.getItem() == POTION_OF_BEES_ITEM.get(),
			DRAGON_BREATH_TAG,
			LINGERING_POTION_OF_BEES_ITEM));
		
	}
	
	public static Identifier id(String path)
	{
		return Identifier.fromNamespaceAndPath(MODID, path);
	}
}
