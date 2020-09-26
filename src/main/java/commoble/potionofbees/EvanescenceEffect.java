package commoble.potionofbees;

import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;

public class EvanescenceEffect extends Effect
{
	public EvanescenceEffect(EffectType typeIn, int liquidColorIn)
	{
		super(typeIn, liquidColorIn);
	}

	@Override
	public boolean isReady(int duration, int amplifier)
	{
		return duration <= 1;
	}

	@Override
	public void performEffect(LivingEntity entityLivingBaseIn, int amplifier)
	{
		entityLivingBaseIn.onKillCommand();
	}
}
