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

public class LingeringPotionOfBeesEntity extends ThrowableItemProjectile
{
	protected LingeringPotionOfBeesEntity(EntityType<? extends LingeringPotionOfBeesEntity> type, Level level)
	{
		super(type, level);
	}
	
	protected LingeringPotionOfBeesEntity(EntityType<? extends LingeringPotionOfBeesEntity> type, LivingEntity thrower, Level level, ItemStack stack)
	{
		super(type, thrower, level, stack);
	}
	
	protected LingeringPotionOfBeesEntity(EntityType<? extends LingeringPotionOfBeesEntity> type, double x, double y, double z, Level level, ItemStack stack)
	{
		super(type, x, y, z, level, stack);
	}
	
	public static LingeringPotionOfBeesEntity create(EntityType<? extends LingeringPotionOfBeesEntity> type, Level level)
	{
		return new LingeringPotionOfBeesEntity(type, level);
	}
	
	public static LingeringPotionOfBeesEntity throwFromThrower(Level level, LivingEntity thrower)
	{
		return new LingeringPotionOfBeesEntity(PotionOfBeesMod.LINGERING_POTION_OF_BEES_ENTITY.get(), thrower, level, new ItemStack(PotionOfBeesMod.LINGERING_POTION_OF_BEES_ITEM.get()));
	}
	
	public static LingeringPotionOfBeesEntity throwFromPosition(Level level, Position pos)
	{
		return new LingeringPotionOfBeesEntity(PotionOfBeesMod.LINGERING_POTION_OF_BEES_ENTITY.get(), pos.x(), pos.y(), pos.z(), level, new ItemStack(PotionOfBeesMod.LINGERING_POTION_OF_BEES_ITEM.get()));
	}

	@Override
	protected Item getDefaultItem()
	{
		return PotionOfBeesMod.LINGERING_POTION_OF_BEES_ITEM.get();
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
			serverLevel.addFreshEntity(LingeringPotionOfBeesCloud.atPosition(serverLevel, this.getX(), this.getY(), this.getZ()));
		}

		this.discard();

	}
}
