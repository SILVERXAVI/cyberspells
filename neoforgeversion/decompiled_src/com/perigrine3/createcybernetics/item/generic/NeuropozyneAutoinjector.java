/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.ChatFormatting
 *  net.minecraft.network.chat.Component
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.stats.Stats
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.InteractionResultHolder
 *  net.minecraft.world.effect.MobEffectInstance
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.item.Item$TooltipContext
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.TooltipFlag
 *  net.minecraft.world.item.UseAnim
 *  net.minecraft.world.level.ItemLike
 *  net.minecraft.world.level.Level
 */
package com.perigrine3.createcybernetics.item.generic;

import com.perigrine3.createcybernetics.effect.ModEffects;
import com.perigrine3.createcybernetics.item.ModItems;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;

public class NeuropozyneAutoinjector
extends Item {
    private static final int CHARGE_TICKS = 16;
    private static final int EFFECT_AMPLIFIER = 0;

    public NeuropozyneAutoinjector(Item.Properties properties) {
        super(properties);
    }

    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add((Component)Component.translatable((String)"item.createcybernetics.neuropozyne_autoinjector.duration").withStyle(ChatFormatting.BLUE));
        tooltipComponents.add((Component)Component.translatable((String)"item.createcybernetics.neuropozyne_autoinjector.desc").withStyle(ChatFormatting.DARK_PURPLE));
    }

    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 72000;
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        player.startUsingItem(hand);
        return InteractionResultHolder.consume((Object)stack);
    }

    public void onUseTick(Level level, LivingEntity living, ItemStack stack, int timeLeft) {
        if (!(living instanceof Player)) {
            return;
        }
        Player player = (Player)living;
        int used = this.getUseDuration(stack, living) - timeLeft;
        if (used != 16) {
            return;
        }
        player.stopUsingItem();
        if (level.isClientSide) {
            return;
        }
        player.addEffect(new MobEffectInstance(ModEffects.NEUROPOZYNE, 24000, 0));
        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_HURT, SoundSource.PLAYERS, 0.25f, 1.2f);
        player.awardStat(Stats.ITEM_USED.get((Object)this));
        if (!player.getAbilities().instabuild) {
            stack.shrink(1);
            ItemStack empty = new ItemStack((ItemLike)ModItems.EMPTY_AUTOINJECTOR.get());
            if (stack.isEmpty()) {
                player.setItemInHand(player.getUsedItemHand(), empty);
            } else if (!player.getInventory().add(empty)) {
                player.drop(empty, false);
            }
        }
    }
}

