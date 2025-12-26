package commoble.potionofbees;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Position;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;

public class SplashPotionOfBeesEntity extends ThrowableItemProjectile
{
	public SplashPotionOfBeesEntity(EntityType<? extends SplashPotionOfBeesEntity> entityType, Level world)
	{
		super(entityType, world);
	}
	
	private SplashPotionOfBeesEntity(Level worldIn, LivingEntity throwerIn, ItemStack stack)
	{
		super(PotionOfBeesMod.SPLASH_POTION_OF_BEES_ENTITY.get(), throwerIn, worldIn, stack);
	}
	
	public static SplashPotionOfBeesEntity throwFromThrower(Level worldIn, LivingEntity throwerIn)
	{
		return new SplashPotionOfBeesEntity(worldIn, throwerIn, new ItemStack(PotionOfBeesMod.SPLASH_POTION_OF_BEES_ITEM.get()));
	}
	
	public static SplashPotionOfBeesEntity throwFromPosition(Level worldIn, Position pos)
	{
		return new SplashPotionOfBeesEntity(worldIn, pos.x(), pos.y(), pos.z(), new ItemStack(PotionOfBeesMod.SPLASH_POTION_OF_BEES_ITEM.get()));
	}
	
	private SplashPotionOfBeesEntity(Level worldIn, double x, double y, double z, ItemStack stack)
	{
		super(PotionOfBeesMod.SPLASH_POTION_OF_BEES_ENTITY.get(), x, y, z, worldIn, stack);
	}

	@Override
	protected Item getDefaultItem()
	{
		return PotionOfBeesMod.SPLASH_POTION_OF_BEES_ITEM.get();
	}

	/**
	 * Gets the amount of gravity to apply to the thrown entity with each tick.
	 */
	@Override
	public double getDefaultGravity()
	{
		return 0.05D; // same as potions
	}

	/**
	 * Called when this hits a block or entity.
	 */
	@Override
	protected void onHit(HitResult result)
	{
		if (this.level() instanceof ServerLevel serverLevel)
		{
			serverLevel.levelEvent(2002, new BlockPos((int)this.xOld, (int)this.yOld, (int)this.zOld), 16750848); // color of fire resistance
			WorldUtil.spawnAngryBees(serverLevel, result.getLocation());
		}

		this.discard();

	}
}
