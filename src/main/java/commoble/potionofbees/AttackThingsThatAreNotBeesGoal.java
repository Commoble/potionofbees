package commoble.potionofbees;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.passive.BeeEntity;

public class AttackThingsThatAreNotBeesGoal extends NearestAttackableTargetGoal<LivingEntity>
{
	public static boolean isThingNotBee(LivingEntity ent)
	{
		return (ent.getType() != EntityType.BEE);
	}
	
	AttackThingsThatAreNotBeesGoal(BeeEntity p_i225719_1_)
	{
		super(p_i225719_1_, LivingEntity.class, 10, true, false, AttackThingsThatAreNotBeesGoal::isThingNotBee);
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	@Override
	public boolean shouldExecute()
	{
		return this.canSting() && super.shouldExecute();
	}

	@Override
	protected void findNearestTarget()
	{
		this.nearestTarget = this.goalOwner.world.func_225318_b(
			this.targetClass,
			this.targetEntitySelector,
			this.goalOwner,
			this.goalOwner.getPosX(),
			this.goalOwner.getPosY(),
			this.goalOwner.getPosZ(),
			this.getTargetableArea(this.getTargetDistance()));
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	@Override
	public boolean shouldContinueExecuting()
	{
		boolean canSting = this.canSting();
		if (canSting && this.goalOwner.getAttackTarget() != null)
		{
			return super.shouldContinueExecuting();
		}
		else
		{
			this.target = null;
			return false;
		}
	}

	private boolean canSting()
	{
		BeeEntity beeentity = (BeeEntity) this.goalOwner;
		return beeentity.isAggressive() && !beeentity.hasStung();
	}
}
