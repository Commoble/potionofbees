package commoble.potionofbees;

import com.mojang.serialization.MapCodec;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.consume_effects.ConsumeEffect;
import net.minecraft.world.level.Level;

public enum BeesConsumeEffect implements ConsumeEffect
{
	INSTANCE;
	
	public static final MapCodec<BeesConsumeEffect> CODEC = MapCodec.unit(INSTANCE);
	public static final StreamCodec<RegistryFriendlyByteBuf, BeesConsumeEffect> STREAM_CODEC = StreamCodec.unit(INSTANCE);
	
	@Override
	public Type<? extends ConsumeEffect> getType()
	{
		return PotionOfBeesMod.BEES_CONSUME_EFFECT.get();
	}

	@Override
	public boolean apply(Level level, ItemStack stack, LivingEntity entity)
	{
		if (level instanceof ServerLevel serverLevel)
		{
			entity.hurtServer(serverLevel, serverLevel.damageSources().cramming(), 4F);
			WorldUtil.spawnAngryBees(serverLevel, entity.position());
		}
		return true;
	}

}
