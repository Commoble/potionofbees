package commoble.potionofbees;

import java.util.List;
import java.util.Optional;

import org.jetbrains.annotations.Nullable;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class LingeringPotionOfBeesCloud extends AreaEffectCloud
{
	private static final PotionContents DUMMY_POTION = new PotionContents(Optional.empty(), Optional.of(16750848), List.of(), Optional.empty());

	public LingeringPotionOfBeesCloud(EntityType<? extends LingeringPotionOfBeesCloud> type, Level level)
	{
		super(type, level);
		this.setPotionContents(DUMMY_POTION);
		this.setInvulnerable(true);
		this.setRadius(3F);
		this.setRadiusOnUse(-0.5F);
		this.setWaitTime(10);
		this.setDuration(600);
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
		return new LingeringPotionOfBeesCloud(PotionOfBeesMod.LINGERING_POTION_OF_BEES_CLOUD_ENTITY.get(), level, x, y, z);
	}

	@Override
	public void tick()
	{
		super.tick();
		
		if (!this.isWaiting() && !this.isRemoved() && this.level() instanceof ServerLevel serverLevel)
		{
			Vec3 vec = this.position();
			AABB targetBox = new AABB(vec,vec).inflate(PotionOfBeesMod.BEE_SEARCH_RADIUS);

			Optional<LivingEntity> optionalTarget =
				serverLevel.getEntitiesOfClass(LivingEntity.class, targetBox, WorldUtil::isValidBeeTarget).stream()
					.reduce((entityA, entityB) -> entityB.distanceToSqr(vec) < entityA.distanceToSqr(vec) ? entityB : entityA);                                                                                   
			
			@Nullable LivingEntity target = optionalTarget.orElse(null);
			
			WorldUtil.spawnAngryBee(serverLevel, vec, target, 100 + this.random.nextInt(100));
		}
	}
}
