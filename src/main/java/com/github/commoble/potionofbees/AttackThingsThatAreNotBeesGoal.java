package com.github.commoble.potionofbees;

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
		return this.func_226465_h_() && super.shouldExecute();
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
		boolean flag = this.func_226465_h_();
		if (flag && this.goalOwner.getAttackTarget() != null)
		{
			return super.shouldContinueExecuting();
		}
		else
		{
			this.target = null;
			return false;
		}
	}

	private boolean func_226465_h_()
	{
		BeeEntity beeentity = (BeeEntity) this.goalOwner;
		return beeentity.func_226427_ez_() && !beeentity.func_226412_eE_();
	}
}
