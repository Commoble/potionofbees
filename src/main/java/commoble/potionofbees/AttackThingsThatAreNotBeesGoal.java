package commoble.potionofbees;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Bee;

public class AttackThingsThatAreNotBeesGoal extends NearestAttackableTargetGoal<LivingEntity>
{
	public static boolean isThingNotBee(LivingEntity ent)
	{
		return (ent.getType() != EntityType.BEE);
	}
	
	AttackThingsThatAreNotBeesGoal(Bee bee)
	{
		super(bee, LivingEntity.class, 10, true, false, AttackThingsThatAreNotBeesGoal::isThingNotBee);
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	@Override
	public boolean canUse()
	{
		return this.canSting() && super.canUse();
	}

	@Override
	protected void findTarget()
	{
		this.target = this.mob.level().getNearestEntity(
			this.targetType,
			this.targetConditions,
			this.mob,
			this.mob.getX(),
			this.mob.getY(),
			this.mob.getZ(),
			this.getTargetSearchArea(this.getFollowDistance()));
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	@Override
	public boolean canContinueToUse()
	{
		boolean canSting = this.canSting();
		if (canSting && this.mob.getTarget() != null)
		{
			return super.canContinueToUse();
		}
		else
		{
			this.targetMob = null;
			return false;
		}
	}

	private boolean canSting()
	{
		Bee beeentity = (Bee) this.mob;
		return beeentity.isAggressive() && !beeentity.hasStung();
	}
}
