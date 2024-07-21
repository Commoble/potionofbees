package commoble.potionofbees;

import commoble.potionofbees.client.ClientProxy;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.dispenser.ProjectileDispenseBehavior;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.block.DispenserBlock;
import net.neoforged.bus.api.IEventBus;
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
	public static final TagKey<Item> DRAGON_BREATH_TAG = TagKey.create(Registries.ITEM, ResourceLocation.parse("c:dragon_breath"));
	
	private static PotionOfBeesMod instance;
	public static PotionOfBeesMod get() { return instance; }

	public final DeferredHolder<EntityType<?>, EntityType<SplashPotionOfBeesEntity>> splashPotionOfBeesEntityType;
	public final DeferredHolder<EntityType<?>, EntityType<LingeringPotionOfBeesEntity>> lingeringPotionOfBeesEntityType;
	public final DeferredHolder<EntityType<?>, EntityType<LingeringPotionOfBeesCloud>> lingeringPotionOfBeesCloudEntityType;
	public final DeferredHolder<Item, PotionOfBeesItem> potionOfBeesItem;
	public final DeferredHolder<Item, ThrowableItem> splashPotionOfBeesItem;
	public final DeferredHolder<Item, ThrowableItem> lingeringPotionOfBeesItem;
	public final DeferredHolder<MobEffect, EvanescenceEffect> evanescenceEffect;

	public PotionOfBeesMod(IEventBus modBus)
	{
		instance = this;
		final IEventBus forgeBus = NeoForge.EVENT_BUS;

		DeferredRegister<EntityType<?>> entityTypes = makeDeferredRegister(modBus, Registries.ENTITY_TYPE);
		DeferredRegister<Item> items = makeDeferredRegister(modBus, Registries.ITEM);
		DeferredRegister<MobEffect> mobEffects = makeDeferredRegister(modBus, Registries.MOB_EFFECT);

		this.splashPotionOfBeesEntityType = entityTypes.register(Names.SPLASH_POTION_OF_BEES, () -> EntityType.Builder.of(SplashPotionOfBeesEntity::new, MobCategory.MISC)
			.build(Names.SPLASH_POTION_OF_BEES));
		this.lingeringPotionOfBeesEntityType = entityTypes.register(Names.LINGERING_POTION_OF_BEES, () -> EntityType.Builder.of(LingeringPotionOfBeesEntity::create, MobCategory.MISC)
			.build(Names.LINGERING_POTION_OF_BEES));
		this.lingeringPotionOfBeesCloudEntityType = entityTypes.register(Names.LINGERING_POTION_OF_BEES_CLOUD, () -> EntityType.Builder.of(LingeringPotionOfBeesCloud::create, MobCategory.MISC)
			.fireImmune()
			.sized(6.0F, 0.5F)
			.clientTrackingRange(10)
			.updateInterval(Integer.MAX_VALUE)
			.build(Names.LINGERING_POTION_OF_BEES_CLOUD));

		this.potionOfBeesItem = items.register(Names.POTION_OF_BEES, () -> new PotionOfBeesItem(new Item.Properties()));
		this.splashPotionOfBeesItem = items.register(Names.SPLASH_POTION_OF_BEES, () -> new ThrowableItem(
			new Item.Properties(),
			() -> SoundEvents.SPLASH_POTION_THROW,
			SplashPotionOfBeesEntity::throwFromThrower,
			SplashPotionOfBeesEntity::throwFromPosition));
		this.lingeringPotionOfBeesItem = items.register(Names.LINGERING_POTION_OF_BEES, () -> new ThrowableItem(
			new Item.Properties(),
			() -> SoundEvents.LINGERING_POTION_THROW,
			LingeringPotionOfBeesEntity::throwFromThrower,
			LingeringPotionOfBeesEntity::throwFromPosition));

		this.evanescenceEffect = mobEffects.register(Names.EVANESCENCE, () -> new EvanescenceEffect(MobEffectCategory.HARMFUL, 0));
		
		modBus.addListener(this::onBuildCreativeTabs);
		modBus.addListener(this::onCommonSetup);
		
		forgeBus.addListener(this::onRegisterBrewingRecipes);
		
		ClientProxy.subscribeClientEvents(modBus, forgeBus);
	}
	
	private static <T> DeferredRegister<T> makeDeferredRegister(IEventBus modBus, ResourceKey<Registry<T>> registry)
	{
		DeferredRegister<T> register = DeferredRegister.create(registry, MODID);
		register.register(modBus);
		return register;
	}
	
	private void onBuildCreativeTabs(BuildCreativeModeTabContentsEvent event)
	{
		if (event.getTabKey() == CreativeModeTabs.FOOD_AND_DRINKS)
		{
			event.accept(this.potionOfBeesItem.get());
			event.accept(this.splashPotionOfBeesItem.get());
			event.accept(this.lingeringPotionOfBeesItem.get());
		}
	}
	
	private void onCommonSetup(FMLCommonSetupEvent event)
	{
		event.enqueueWork(this::afterCommonSetup); // run unthreadsafe stuff on main thread
	}
	
	private void afterCommonSetup()
	{
		DispenserBlock.registerBehavior(this.splashPotionOfBeesItem.get(), new ProjectileDispenseBehavior(this.splashPotionOfBeesItem.get()));
		DispenserBlock.registerBehavior(this.lingeringPotionOfBeesItem.get(), new ProjectileDispenseBehavior(this.lingeringPotionOfBeesItem.get()));
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
			this.potionOfBeesItem));
		builder.addRecipe(new PotionOfBeesRecipe(input -> input.getItem() == this.potionOfBeesItem.get(),
			Tags.Items.GUNPOWDERS,
			this.splashPotionOfBeesItem));
		builder.addRecipe(new PotionOfBeesRecipe(input -> input.getItem() == this.potionOfBeesItem.get(),
			DRAGON_BREATH_TAG,
			this.lingeringPotionOfBeesItem));
		
	}
	
	public static ResourceLocation id(String path)
	{
		return ResourceLocation.fromNamespaceAndPath(MODID, path);
	}
}
