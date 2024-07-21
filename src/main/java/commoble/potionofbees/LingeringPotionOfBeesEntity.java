package commoble.potionofbees;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Position;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;

public class LingeringPotionOfBeesEntity extends ThrowableItemProjectile
{
	protected LingeringPotionOfBeesEntity(EntityType<? extends LingeringPotionOfBeesEntity> type, Level level)
	{
		super(type, level);
	}
	
	protected LingeringPotionOfBeesEntity(EntityType<? extends LingeringPotionOfBeesEntity> type, LivingEntity thrower, Level level)
	{
		super(type, thrower, level);
	}
	
	protected LingeringPotionOfBeesEntity(EntityType<? extends LingeringPotionOfBeesEntity> type, double x, double y, double z, Level level)
	{
		super(type, x, y, z, level);
	}
	
	public static LingeringPotionOfBeesEntity create(EntityType<? extends LingeringPotionOfBeesEntity> type, Level level)
	{
		return new LingeringPotionOfBeesEntity(type, level);
	}
	
	public static LingeringPotionOfBeesEntity throwFromThrower(Level level, LivingEntity thrower)
	{
		return new LingeringPotionOfBeesEntity(PotionOfBeesMod.get().lingeringPotionOfBeesEntityType.get(), thrower, level);
	}
	
	public static LingeringPotionOfBeesEntity throwFromPosition(Level level, Position pos)
	{
		return new LingeringPotionOfBeesEntity(PotionOfBeesMod.get().lingeringPotionOfBeesEntityType.get(), pos.x(), pos.y(), pos.z(), level);
	}

	@Override
	protected Item getDefaultItem()
	{
		return PotionOfBeesMod.get().lingeringPotionOfBeesItem.get();
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
			serverLevel.levelEvent(2002, new BlockPos((int)this.xOld, (int)this.yOld, (int)this.zOld), PotionContents.getColor(Potions.FIRE_RESISTANCE));
			serverLevel.addFreshEntity(LingeringPotionOfBeesCloud.atPosition(serverLevel, this.getX(), this.getY(), this.getZ()));
		}

		this.discard();

	}
}
