package commoble.potionofbees;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Position;
import net.minecraft.network.protocol.Packet;
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
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages.SpawnEntity;

public class SplashPotionOfBeesEntity extends ThrowableItemProjectile
{
	public SplashPotionOfBeesEntity(EntityType<? extends SplashPotionOfBeesEntity> entityType, Level world)
	{
		super(entityType, world);
	}
	
	private SplashPotionOfBeesEntity(Level worldIn, LivingEntity throwerIn)
	{
		super(PotionOfBeesMod.get().splashPotionOfBeesEntityType.get(), throwerIn, worldIn);
	}
	
	public static SplashPotionOfBeesEntity throwFromThrower(Level worldIn, LivingEntity throwerIn)
	{
		return new SplashPotionOfBeesEntity(worldIn, throwerIn);
	}
	
	private SplashPotionOfBeesEntity(Level worldIn, double x, double y, double z)
	{
		super(PotionOfBeesMod.get().splashPotionOfBeesEntityType.get(), x, y, z, worldIn);
	}
	
	public static SplashPotionOfBeesEntity dispenseFromDispenser(Level worldIn, Position position, ItemStack stack)
	{
		SplashPotionOfBeesEntity entity = new SplashPotionOfBeesEntity(worldIn, position.x(), position.y(), position.z());
		entity.setItem(stack);
		return entity;
	}
	
	public static SplashPotionOfBeesEntity spawnOnClient(SpawnEntity spawnPacket, Level world)
	{
		return new SplashPotionOfBeesEntity(PotionOfBeesMod.get().splashPotionOfBeesEntityType.get(), world);
	}

	@Override
	protected Item getDefaultItem()
	{
		return PotionOfBeesMod.get().splashPotionOfBeesItem.get();
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
		if (this.level instanceof ServerLevel serverLevel)
		{
			this.level.levelEvent(2002, new BlockPos(this.xOld, this.yOld, this.zOld), PotionUtils.getColor(Potions.FIRE_RESISTANCE));
			WorldUtil.spawnAngryBees(serverLevel, result.getLocation());
		}

		this.discard();

	}
	
	@Override
	public Packet<?> getAddEntityPacket()
	{
		return NetworkHooks.getEntitySpawningPacket(this);
	}
}
