package com.github.commoble.potionofbees;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

public class SplashPotionOfBeesItem extends Item
{

	public SplashPotionOfBeesItem(Properties properties)
	{
		super(properties);
	}

	/**
	 * Called to trigger the item's "innate" right click behavior. To handle when
	 * this item is used on a Block, see {@link #onItemUse}.
	 */
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn)
	{
		ItemStack itemstack = playerIn.getHeldItem(handIn);
		worldIn.playSound((PlayerEntity) null, playerIn.func_226277_ct_(), playerIn.func_226278_cu_(), playerIn.func_226281_cx_(), SoundEvents.ENTITY_EXPERIENCE_BOTTLE_THROW,
			SoundCategory.NEUTRAL, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));
		if (!worldIn.isRemote)
		{
			SplashPotionOfBeesEntity potionEntity = SplashPotionOfBeesEntity.asThrownEntity(worldIn, playerIn);
			potionEntity.func_213884_b(itemstack);
			potionEntity.shoot(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, -20.0F, 0.7F, 1.0F);
			worldIn.addEntity(potionEntity);
		}

		playerIn.addStat(Stats.ITEM_USED.get(this));
		if (!playerIn.abilities.isCreativeMode)
		{
			itemstack.shrink(1);
		}

		return ActionResult.func_226248_a_(itemstack);
	}
}
