package commoble.potionofbees;

import java.util.function.BiFunction;
import java.util.function.Supplier;

import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileItem;
import net.minecraft.world.level.Level;

public class ThrowableItem extends Item implements ProjectileItem
{
	protected final Supplier<SoundEvent> soundEvent;
	protected final BiFunction<Level, LivingEntity, ThrowableItemProjectile> thrownProjectileFactory;
	protected final BiFunction<Level, Position, ThrowableItemProjectile> dispensedProjectileFactory;

	public ThrowableItem(Properties props, Supplier<SoundEvent> soundEvent, BiFunction<Level, LivingEntity, ThrowableItemProjectile> projectileFactory, BiFunction<Level, Position, ThrowableItemProjectile> dispensedProjectileFactory)
	{
		super(props);
		this.soundEvent = soundEvent;
		this.thrownProjectileFactory = projectileFactory;
		this.dispensedProjectileFactory = dispensedProjectileFactory;
	}

	@Override
	public InteractionResult use(Level level, Player player, InteractionHand hand)
	{
		level.playSound(null, player.getX(), player.getY(), player.getZ(), this.soundEvent.get(), SoundSource.NEUTRAL, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));

		ItemStack itemstack = player.getItemInHand(hand);
		if (!level.isClientSide())
		{
			ThrowableItemProjectile projectile = this.thrownProjectileFactory.apply(level, player);
			projectile.setItem(itemstack);
			projectile.shootFromRotation(player, player.getXRot(), player.getYRot(), -20.0F, 0.5F, 1.0F);
			level.addFreshEntity(projectile);
		}

		player.awardStat(Stats.ITEM_USED.get(this));
		if (!player.getAbilities().instabuild)
		{
			itemstack.shrink(1);
		}

		return InteractionResult.SUCCESS;
	}

	@Override
	public Projectile asProjectile(Level level, Position position, ItemStack stack, Direction direction)
	{
		return this.dispensedProjectileFactory.apply(level, position);
	}
	
	public static interface ProjectileFactory
	{
		public abstract ThrowableItemProjectile apply(Level level, Position position, ItemStack stack);
	}
}
