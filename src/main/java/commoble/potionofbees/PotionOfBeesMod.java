package commoble.potionofbees;

import commoble.potionofbees.client.ClientProxy;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryObject;

@Mod(PotionOfBeesMod.MODID)
public class PotionOfBeesMod
{
	public static final String MODID = "potionofbees";
	public static final double BEE_SEARCH_RADIUS = 10D;
	public static final TagKey<Item> POTION_INGREDIENT_TAG = TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(MODID, Names.POTION_OF_BEES_INGREDIENTS));
	public static final TagKey<Item> DRAGON_BREATH_TAG = TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation("forge:dragon_breath"));
	
	private static PotionOfBeesMod instance;
	public static PotionOfBeesMod get() { return instance; }

	public final RegistryObject<EntityType<SplashPotionOfBeesEntity>> splashPotionOfBeesEntityType;
	public final RegistryObject<EntityType<LingeringPotionOfBeesEntity>> lingeringPotionOfBeesEntityType;
	public final RegistryObject<EntityType<LingeringPotionOfBeesCloud>> lingeringPotionOfBeesCloudEntityType;
	public final RegistryObject<PotionOfBeesItem> potionOfBeesItem;
	public final RegistryObject<ThrowableItem> splashPotionOfBeesItem;
	public final RegistryObject<ThrowableItem> lingeringPotionOfBeesItem;
	public final RegistryObject<EvanescenceEffect> evanescenceEffect;

	public PotionOfBeesMod()
	{
		instance = this;
		final IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
		final IEventBus forgeBus = MinecraftForge.EVENT_BUS;

		DeferredRegister<EntityType<?>> entityTypes = makeDeferredRegister(modBus, ForgeRegistries.ENTITY_TYPES);
		DeferredRegister<Item> items = makeDeferredRegister(modBus, ForgeRegistries.ITEMS);
		DeferredRegister<MobEffect> mobEffects = makeDeferredRegister(modBus, ForgeRegistries.MOB_EFFECTS);

		this.splashPotionOfBeesEntityType = entityTypes.register(Names.SPLASH_POTION_OF_BEES, () -> EntityType.Builder.of(SplashPotionOfBeesEntity::new, MobCategory.MISC)
			.setCustomClientFactory(SplashPotionOfBeesEntity::spawnOnClient)
			.build(Names.SPLASH_POTION_OF_BEES));
		this.lingeringPotionOfBeesEntityType = entityTypes.register(Names.LINGERING_POTION_OF_BEES, () -> EntityType.Builder.of(LingeringPotionOfBeesEntity::create, MobCategory.MISC)
			.setCustomClientFactory(LingeringPotionOfBeesEntity::spawnOnClient)
			.build(Names.LINGERING_POTION_OF_BEES));
		this.lingeringPotionOfBeesCloudEntityType = entityTypes.register(Names.LINGERING_POTION_OF_BEES_CLOUD, () -> EntityType.Builder.of(LingeringPotionOfBeesCloud::create, MobCategory.MISC)
			.fireImmune()
			.sized(6.0F, 0.5F)
			.clientTrackingRange(10)
			.updateInterval(Integer.MAX_VALUE)
			.build(Names.LINGERING_POTION_OF_BEES_CLOUD));

		this.potionOfBeesItem = items.register(Names.POTION_OF_BEES, () -> new PotionOfBeesItem(new Item.Properties().tab(CreativeModeTab.TAB_BREWING)));
		this.splashPotionOfBeesItem = items.register(Names.SPLASH_POTION_OF_BEES, () -> new ThrowableItem(
			new Item.Properties().tab(CreativeModeTab.TAB_BREWING),
			() -> SoundEvents.SPLASH_POTION_THROW,
			SplashPotionOfBeesEntity::throwFromThrower));
		this.lingeringPotionOfBeesItem = items.register(Names.LINGERING_POTION_OF_BEES, () -> new ThrowableItem(
			new Item.Properties().tab(CreativeModeTab.TAB_BREWING),
			() -> SoundEvents.LINGERING_POTION_THROW,
			LingeringPotionOfBeesEntity::throwFromThrower));

		this.evanescenceEffect = mobEffects.register(Names.EVANESCENCE, () -> new EvanescenceEffect(MobEffectCategory.HARMFUL, 0));
		
		modBus.addListener(this::onCommonSetup);
		
		ClientProxy.subscribeClientEvents(modBus, forgeBus);
	}
	
	private static <T> DeferredRegister<T> makeDeferredRegister(IEventBus modBus, IForgeRegistry<T> registry)
	{
		DeferredRegister<T> register = DeferredRegister.create(registry, MODID);
		register.register(modBus);
		return register;
	}
	
	private void onCommonSetup(FMLCommonSetupEvent event)
	{
		event.enqueueWork(this::afterCommonSetup); // run unthreadsafe stuff on main thread
	}
	
	private void afterCommonSetup()
	{
		BrewingRecipeRegistry.addRecipe(new PotionOfBeesRecipe(input -> input.getItem() == Items.POTION && PotionUtils.getPotion(input) == Potions.AWKWARD,
			POTION_INGREDIENT_TAG,
			this.potionOfBeesItem));
		BrewingRecipeRegistry.addRecipe(new PotionOfBeesRecipe(input -> input.getItem() == this.potionOfBeesItem.get(),
			Tags.Items.GUNPOWDER,
			this.splashPotionOfBeesItem));
		BrewingRecipeRegistry.addRecipe(new PotionOfBeesRecipe(input -> input.getItem() == this.potionOfBeesItem.get(),
			DRAGON_BREATH_TAG,
			this.lingeringPotionOfBeesItem));
		
		DispenserBlock.registerBehavior(this.splashPotionOfBeesItem.get(), new ThrowableItem.DispenseBehavior(SplashPotionOfBeesEntity::dispenseFromDispenser));
		DispenserBlock.registerBehavior(this.lingeringPotionOfBeesItem.get(), new ThrowableItem.DispenseBehavior(LingeringPotionOfBeesEntity::dispenseFromDispenser));
	}
}
