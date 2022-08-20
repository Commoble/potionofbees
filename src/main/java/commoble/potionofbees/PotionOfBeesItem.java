package commoble.potionofbees;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

public class PotionOfBeesItem extends Item
{
	public PotionOfBeesItem(Properties properties)
	{
		super(properties);
	}

	/**
	 * How long it takes to use or consume an item
	 */
	@Override
	public int getUseDuration(ItemStack stack)
	{
		return 32;
	}

	/**
	 * returns the action that specifies what animation to play when the items is
	 * being used
	 */
	@Override
	public UseAnim getUseAnimation(ItemStack stack)
	{
		return UseAnim.DRINK;
	}

	/**
	 * Called to trigger the item's "innate" right click behavior. To handle when
	 * this item is used on a Block, see {@link #onItemUse}.
	 */
	@Override
	public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn)
	{
		playerIn.startUsingItem(handIn);
		return InteractionResultHolder.success(playerIn.getItemInHand(handIn));
	}

	/**
	 * Called when the player finishes using this Item (E.g. finishes eating.). Not
	 * called when the player stops using the Item before the action is complete.
	 */
	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity)
	{
		Player player = entity instanceof Player playerEntity ? playerEntity : null;
		if (player instanceof ServerPlayer serverPlayer)
		{
			CriteriaTriggers.CONSUME_ITEM.trigger(serverPlayer, stack);
		}

		if (level instanceof ServerLevel serverLevel)
		{
			entity.hurt(DamageSource.CRAMMING, 4F);
			WorldUtil.spawnAngryBees(serverLevel, entity.position());
		}

		if (player != null)
		{
			player.awardStat(Stats.ITEM_USED.get(this));
			if (!player.getAbilities().instabuild)
			{
				stack.shrink(1);
			}
		}

		if (player == null || !player.getAbilities().instabuild)
		{
			if (stack.isEmpty())
			{
				return new ItemStack(Items.GLASS_BOTTLE);
			}

			if (player != null)
			{
				player.getInventory().add(new ItemStack(Items.GLASS_BOTTLE));
			}
		}

		return stack;
	}
}
