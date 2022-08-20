package commoble.potionofbees;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class EvanescenceEffect extends MobEffect
{
	public EvanescenceEffect(MobEffectCategory typeIn, int liquidColorIn)
	{
		super(typeIn, liquidColorIn);
	}

	@Override
	public boolean isDurationEffectTick(int duration, int amplifier)
	{
		return duration <= 1;
	}

	@Override
	public void applyEffectTick(LivingEntity entityLivingBaseIn, int amplifier)
	{
		entityLivingBaseIn.kill();
	}
}
