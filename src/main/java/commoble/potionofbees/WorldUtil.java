package commoble.potionofbees;

import org.jetbrains.annotations.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class WorldUtil
{
	public static void spawnAngryBees(ServerLevel world, Vec3 vec)
	{
		AABB targetBox = new AABB(vec,vec).inflate(PotionOfBeesMod.BEE_SEARCH_RADIUS);

		@Nullable LivingEntity target =
			world.getEntitiesOfClass(LivingEntity.class, targetBox, WorldUtil::isValidBeeTarget).stream()
				.reduce((entityA, entityB) -> entityB.distanceToSqr(vec) < entityA.distanceToSqr(vec) ? entityB : entityA)
				.orElse(null);
		
		
		int bees = 3 + world.random.nextInt(5) + world.random.nextInt(5);
		
		int maxTime = 3000;
		int ticksToExist = maxTime/bees;

		for (int i=0; i<bees; i++)
		{
			spawnAngryBee(world, vec, target, ticksToExist);
		}
	}
	
	public static void spawnAngryBee(ServerLevel world, Vec3 vec, @Nullable LivingEntity target, int ticksToExist)
	{
		BlockPos spawnPos = new BlockPos((int)vec.x, (int)vec.y, (int)vec.z);
		Entity ent = EntityType.BEE.spawn(world, spawnPos, MobSpawnType.EVENT);
		if (ent instanceof Bee bee)
		{
			bee.setPos(vec.x, vec.y, vec.z);
			bee.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, ticksToExist, 1, false, false));
			bee.addEffect(new MobEffectInstance(PotionOfBeesMod.get().evanescenceEffect, ticksToExist, 0, false, false));
			if (target != null)
			{
				bee.setTarget(target);
				bee.targetSelector.addGoal(0, new AttackThingsThatAreNotBeesGoal(bee));
			}
		}
	}
	
	public static boolean isValidBeeTarget(LivingEntity ent)
	{
		return (ent.getType() != EntityType.BEE) && (!ent.isInvulnerable());
	}
}
