/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.ChatFormatting
 *  net.minecraft.network.chat.Component
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.InteractionResultHolder
 *  net.minecraft.world.effect.MobEffects
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.item.Item$TooltipContext
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.ItemUtils
 *  net.minecraft.world.item.TooltipFlag
 *  net.minecraft.world.item.UseAnim
 *  net.minecraft.world.level.Level
 *  net.neoforged.neoforge.attachment.AttachmentType
 */
package com.maxwell.cyber_ware_port.common.item;

import com.maxwell.cyber_ware_port.common.capability.CyberwareCapabilityProvider;
import com.maxwell.cyber_ware_port.common.capability.CyberwareUserData;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.attachment.AttachmentType;

public class NeuropozyneItem
extends Item {
    private static final int DURATION = 24000;

    public NeuropozyneItem() {
        super(new Item.Properties().stacksTo(16));
    }

    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entityLiving) {
        if (!level.isClientSide && entityLiving instanceof Player) {
            Player player = (Player)entityLiving;
            CyberwareUserData data = (CyberwareUserData)player.getData((AttachmentType)CyberwareCapabilityProvider.CYBERWARE_DATA.get());
            data.applyImmunity(24000);
            player.sendSystemMessage((Component)Component.translatable((String)"cyberware.message.suppressant_applied").withStyle(ChatFormatting.GREEN));
            player.removeEffect(MobEffects.CONFUSION);
            player.removeEffect(MobEffects.DIG_SLOWDOWN);
            player.removeEffect(MobEffects.WEAKNESS);
        }
        return super.finishUsingItem(stack, level, entityLiving);
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        return ItemUtils.startUsingInstantly((Level)level, (Player)player, (InteractionHand)hand);
    }

    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.DRINK;
    }

    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 32;
    }

    public void appendHoverText(ItemStack pStack, Item.TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pContext, pTooltipComponents, pIsAdvanced);
        pTooltipComponents.add((Component)Component.translatable((String)"cyberware.item.neuropozyne.desc").withStyle(ChatFormatting.GRAY));
    }
}

