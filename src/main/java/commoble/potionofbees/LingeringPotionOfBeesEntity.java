package commoble.potionofbees;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Position;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.network.PlayMessages.SpawnEntity;

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
	
	public static LingeringPotionOfBeesEntity spawnOnClient(SpawnEntity packet, Level level)
	{
		return new LingeringPotionOfBeesEntity(PotionOfBeesMod.get().lingeringPotionOfBeesEntityType.get(), level);
	}
	
	public static LingeringPotionOfBeesEntity dispenseFromDispenser(Level worldIn, Position position, ItemStack stack)
	{
		LingeringPotionOfBeesEntity entity = new LingeringPotionOfBeesEntity(PotionOfBeesMod.get().lingeringPotionOfBeesEntityType.get(), position.x(), position.y(), position.z(), worldIn);
		entity.setItem(stack);
		return entity;
	}
	
	public static LingeringPotionOfBeesEntity throwFromThrower(Level level, LivingEntity thrower)
	{
		return new LingeringPotionOfBeesEntity(PotionOfBeesMod.get().lingeringPotionOfBeesEntityType.get(), thrower, level);
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
	protected float getGravity()
	{
		return 0.05F; // same as potions
	}

	/**
	 * Called when this hits a block or entity.
	 */
	@Override
	protected void onHit(HitResult result)
	{
		if (this.level() instanceof ServerLevel serverLevel)
		{
			serverLevel.levelEvent(2002, new BlockPos((int)this.xOld, (int)this.yOld, (int)this.zOld), PotionUtils.getColor(Potions.FIRE_RESISTANCE));
			serverLevel.addFreshEntity(LingeringPotionOfBeesCloud.atPosition(serverLevel, this.getX(), this.getY(), this.getZ()));
		}

		this.discard();

	}
}
