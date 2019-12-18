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
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class WorldUtil
{
	public static void spawnAngryBees(World world, Vec3d vec)
	{
		AxisAlignedBB targetBox = new AxisAlignedBB(vec,vec).grow(PotionOfBeesMod.BEE_SEARCH_RADIUS);

		Optional<LivingEntity> foundTarget =
			world.getEntitiesWithinAABB(LivingEntity.class, targetBox, WorldUtil::isValidBeeTarget).stream()
			.reduce((entityA, entityB) -> entityB.getDistanceSq(vec) < entityA.getDistanceSq(vec) ? entityB : entityA);
		
		
		int bees = 3 + world.rand.nextInt(5) + world.rand.nextInt(5);

		for (int i=0; i<bees; i++)
		{
			BlockPos spawnPos = new BlockPos(vec.x, vec.y, vec.z);
			Entity ent = EntityType.field_226289_e_.spawn(world, null, null, spawnPos, SpawnReason.EVENT, false, false);
			if (ent instanceof BeeEntity)
			{
				BeeEntity bee = (BeeEntity)ent;
				bee.setPosition(vec.x, vec.y, vec.z);
				bee.addPotionEffect(new EffectInstance(Effects.SPEED));
				foundTarget.ifPresent(target -> { // make bee angry at target
						bee.func_226391_a_(target);
						bee.targetSelector.addGoal(0, new AttackThingsThatAreNotBeesGoal(bee));
					});
			}
			
		}
	}
	
	public static boolean isValidBeeTarget(LivingEntity ent)
	{
		return (ent.getType() != EntityType.field_226289_e_) && (!ent.isInvulnerable());
	}
}
