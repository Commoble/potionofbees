package commoble.potionofbees;

import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IPosition;
import net.minecraft.dispenser.ProjectileDispenseBehavior;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Util;
import net.minecraft.world.World;

public class SplashPotionOfBeesDispenserBehavior extends ProjectileDispenseBehavior
{
	public static ItemStack dispenseSplashPotionOfBees(IBlockSource blockSource, ItemStack itemSource)
	{
		return (new SplashPotionOfBeesDispenserBehavior()).dispense(blockSource, itemSource);
	}
	
	/**
	 * Return the projectile entity spawned by this dispense behavior.
	 */
	@Override
	protected ProjectileEntity getProjectileEntity(World worldIn, IPosition position, ItemStack stackIn)
	{
		return Util.make(SplashPotionOfBeesEntity.asDispensedEntity(worldIn, position.getX(), position.getY(), position.getZ()), 
			entity -> entity.setItem(stackIn));
	}

	@Override
	protected float getProjectileInaccuracy()
	{
		return super.getProjectileInaccuracy() * 0.5F;
	}

	@Override
	protected float getProjectileVelocity()
	{
		return super.getProjectileVelocity() * 1.25F;
	}
}
