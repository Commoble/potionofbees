package commoble.potionofbees;

import org.jetbrains.annotations.Nullable;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class LingeringPotionOfBeesCloud extends AreaEffectCloud
{

	public LingeringPotionOfBeesCloud(EntityType<? extends LingeringPotionOfBeesCloud> type, Level level)
	{
		super(type, level);
		this.setFixedColor(PotionUtils.getColor(Potions.FIRE_RESISTANCE));
		this.setInvulnerable(true);
		this.setRadius(3F);
		this.setRadiusOnUse(-0.5F);
		this.setWaitTime(10);
		this.setRadiusPerTick(-this.getRadius() / (float)this.getDuration());
	}

	protected LingeringPotionOfBeesCloud(EntityType<? extends LingeringPotionOfBeesCloud> type, Level level, double x, double y, double z)
	{
		this(type, level);
		this.setPos(x, y, z);
	}
	
	public static LingeringPotionOfBeesCloud create(EntityType<? extends LingeringPotionOfBeesCloud> type, Level level)
	{
		return new LingeringPotionOfBeesCloud(type, level);
	}
	
	public static LingeringPotionOfBeesCloud atPosition(Level level, double x, double y, double z)
	{
		return new LingeringPotionOfBeesCloud(PotionOfBeesMod.get().lingeringPotionOfBeesCloudEntityType.get(), level, x, y, z);
	}

	@Override
	public void tick()
	{
		super.tick();
		
		if (!this.isWaiting() && !this.isRemoved() && this.level() instanceof ServerLevel serverLevel)
		{
			Vec3 vec = this.position();
			AABB targetBox = new AABB(vec,vec).inflate(PotionOfBeesMod.BEE_SEARCH_RADIUS);

			@Nullable LivingEntity target =
				serverLevel.getEntitiesOfClass(LivingEntity.class, targetBox, WorldUtil::isValidBeeTarget).stream()
					.reduce((entityA, entityB) -> entityB.distanceToSqr(vec) < entityA.distanceToSqr(vec) ? entityB : entityA)
					.orElse(null);
			
			WorldUtil.spawnAngryBee(serverLevel, vec, target, 100 + this.random.nextInt(100));
		}
	}
}
