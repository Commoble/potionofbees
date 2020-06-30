package com.github.commoble.potionofbees;

import java.util.Optional;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public class WorldUtil
{
	public static void spawnAngryBees(World world, Vector3d vec)
	{
		AxisAlignedBB targetBox = new AxisAlignedBB(vec,vec).grow(PotionOfBeesMod.BEE_SEARCH_RADIUS);

		Optional<LivingEntity> foundTarget =
			world.getEntitiesWithinAABB(LivingEntity.class, targetBox, WorldUtil::isValidBeeTarget).stream()
			.reduce((entityA, entityB) -> entityB.getDistanceSq(vec) < entityA.getDistanceSq(vec) ? entityB : entityA);
		
		
		int bees = 3 + world.rand.nextInt(5) + world.rand.nextInt(5);
		
		int maxTime = 3000;
		int ticksToExist = maxTime/bees;

		for (int i=0; i<bees; i++)
		{
			BlockPos spawnPos = new BlockPos(vec.x, vec.y, vec.z);
			Entity ent = EntityType.BEE.spawn(world, null, null, spawnPos, SpawnReason.EVENT, false, false);
			if (ent instanceof BeeEntity)
			{
				BeeEntity bee = (BeeEntity)ent;
				bee.setPosition(vec.x, vec.y, vec.z);
				bee.addPotionEffect(new EffectInstance(Effects.SPEED, maxTime, 1, false, false));
				bee.addPotionEffect(new EffectInstance(RegistryObjects.EVANESCENCE_EFFECT, ticksToExist, 0, false, false));
				foundTarget.ifPresent(target -> { // make bee angry at target
						bee.setAttackTarget(target);
						bee.targetSelector.addGoal(0, new AttackThingsThatAreNotBeesGoal(bee));
					});
			}
			
		}
	}
	
	public static boolean isValidBeeTarget(LivingEntity ent)
	{
		return (ent.getType() != EntityType.BEE) && (!ent.isInvulnerable());
	}
}
