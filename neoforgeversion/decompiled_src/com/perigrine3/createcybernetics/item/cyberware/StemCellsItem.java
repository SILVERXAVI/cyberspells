/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.network.chat.Component
 *  net.minecraft.tags.TagKey
 *  net.minecraft.world.effect.MobEffectInstance
 *  net.minecraft.world.effect.MobEffects
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.item.Item$TooltipContext
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.TooltipFlag
 */
package com.perigrine3.createcybernetics.item.cyberware;

import com.perigrine3.createcybernetics.api.CyberwareSlot;
import com.perigrine3.createcybernetics.api.ICyberwareItem;
import com.perigrine3.createcybernetics.util.ModTags;
import java.util.List;
import java.util.Set;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

public class StemCellsItem
extends Item
implements ICyberwareItem {
    private final int humanityCost;
    private static final String NBT_REGEN_NEXT_TICK = "cc_regen_nextTick";
    private static final String NBT_REGEN_PAID_TICK = "cc_regen_paidTick";
    private static final int REGEN_TICKS = 600;
    private static final int REGEN_COOLDOWN_TICKS = 3600;
    private static final int ENERGY_ON_ACTIVATION = 5;

    public StemCellsItem(Item.Properties props, int humanityCost) {
        super(props);
        this.humanityCost = humanityCost;
    }

    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        if (Screen.hasShiftDown()) {
            tooltip.add((Component)Component.translatable((String)"tooltip.createcybernetics.humanity", (Object[])new Object[]{this.humanityCost}).withStyle(ChatFormatting.GOLD));
            tooltip.add((Component)Component.translatable((String)"tooltip.createcybernetics.heartupgrades_stemcell.energy").withStyle(ChatFormatting.RED));
        }
    }

    @Override
    public boolean requiresEnergyToFunction(Player player, ItemStack installedStack, CyberwareSlot slot) {
        return true;
    }

    @Override
    public String getActivationPaidNbtKey(Player player, ItemStack installedStack, CyberwareSlot slot) {
        return NBT_REGEN_PAID_TICK;
    }

    @Override
    public int getEnergyActivationCost(Player player, ItemStack installedStack, CyberwareSlot slot) {
        return 5;
    }

    @Override
    public boolean shouldConsumeActivationEnergyThisTick(Player player, ItemStack installedStack, CyberwareSlot slot) {
        if (player.level().isClientSide) {
            return false;
        }
        if (!player.isAlive()) {
            return false;
        }
        if (player.isCreative() || player.isSpectator()) {
            return false;
        }
        if (player.getHealth() > 5.0f) {
            return false;
        }
        long now = player.level().getGameTime();
        CompoundTag tag = player.getPersistentData();
        long next = tag.getLong(NBT_REGEN_NEXT_TICK);
        if (next != 0L && now < next) {
            return false;
        }
        return tag.getLong(NBT_REGEN_PAID_TICK) != now;
    }

    @Override
    public int getHumanityCost() {
        return this.humanityCost;
    }

    @Override
    public Set<TagKey<Item>> requiresCyberwareTags(ItemStack installedStack, CyberwareSlot slot) {
        return Set.of(ModTags.Items.HEART_ITEMS);
    }

    @Override
    public Set<CyberwareSlot> getSupportedSlots() {
        return Set.of(CyberwareSlot.HEART);
    }

    @Override
    public boolean replacesOrgan() {
        return false;
    }

    @Override
    public Set<CyberwareSlot> getReplacedOrgans() {
        return Set.of();
    }

    @Override
    public void onTick(Player player) {
        if (player.level().isClientSide) {
            return;
        }
        if (!player.isAlive()) {
            return;
        }
        if (player.isCreative() || player.isSpectator()) {
            return;
        }
        if (player.getHealth() > 5.0f) {
            return;
        }
        long now = player.level().getGameTime();
        CompoundTag tag = player.getPersistentData();
        long next = tag.getLong(NBT_REGEN_NEXT_TICK);
        if (next != 0L && now < next) {
            return;
        }
        if (tag.getLong(NBT_REGEN_PAID_TICK) != now) {
            return;
        }
        player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 600, 2, false, true, true));
        tag.putLong(NBT_REGEN_NEXT_TICK, now + 3600L);
        tag.remove(NBT_REGEN_PAID_TICK);
    }
}

