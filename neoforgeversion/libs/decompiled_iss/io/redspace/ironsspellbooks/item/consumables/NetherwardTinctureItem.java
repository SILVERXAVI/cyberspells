/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.ChatFormatting
 *  net.minecraft.network.chat.Component
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.InteractionResult
 *  net.minecraft.world.effect.MobEffectInstance
 *  net.minecraft.world.effect.MobEffects
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.monster.hoglin.Hoglin
 *  net.minecraft.world.entity.monster.piglin.AbstractPiglin
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item$TooltipContext
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.TooltipFlag
 */
package io.redspace.ironsspellbooks.item.consumables;

import io.redspace.ironsspellbooks.item.consumables.DrinkableItem;
import io.redspace.ironsspellbooks.util.ItemPropertiesHelper;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.hoglin.Hoglin;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

public class NetherwardTinctureItem
extends DrinkableItem {
    private static final Component description = Component.translatable((String)"item.irons_spellbooks.netherward_tincture.desc").withStyle(ChatFormatting.GRAY);

    public NetherwardTinctureItem() {
        super(ItemPropertiesHelper.material(16), NetherwardTinctureItem::applyEffect, null, false);
    }

    @Override
    public void appendHoverText(ItemStack pStack, Item.TooltipContext context, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, context, pTooltipComponents, pIsAdvanced);
        pTooltipComponents.add(description);
    }

    private static void applyEffect(ItemStack itemStack, LivingEntity livingEntity) {
        if (livingEntity instanceof AbstractPiglin) {
            AbstractPiglin piglin = (AbstractPiglin)livingEntity;
            piglin.setImmuneToZombification(true);
            piglin.playSound(SoundEvents.PIGLIN_CONVERTED_TO_ZOMBIFIED);
        } else if (livingEntity instanceof Hoglin) {
            Hoglin hoglin = (Hoglin)livingEntity;
            hoglin.setImmuneToZombification(true);
            hoglin.playSound(SoundEvents.HOGLIN_CONVERTED_TO_ZOMBIFIED);
        }
        livingEntity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 200));
        livingEntity.playSound(SoundEvents.INK_SAC_USE);
    }

    public SoundEvent getDrinkingSound() {
        return SoundEvents.HONEY_DRINK;
    }

    public InteractionResult interactLivingEntity(ItemStack pStack, Player pPlayer, LivingEntity pInteractionTarget, InteractionHand pUsedHand) {
        if (pInteractionTarget instanceof AbstractPiglin || pInteractionTarget instanceof Hoglin) {
            NetherwardTinctureItem.applyEffect(pStack, pInteractionTarget);
            if (!pPlayer.getAbilities().instabuild) {
                pStack.shrink(1);
            }
            return InteractionResult.SUCCESS;
        }
        return super.interactLivingEntity(pStack, pPlayer, pInteractionTarget, pUsedHand);
    }
}

