/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.network.chat.Component
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
import com.perigrine3.createcybernetics.item.ModItems;
import java.util.List;
import java.util.Set;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

public class CardiovascularCouplerItem
extends Item
implements ICyberwareItem {
    private final int humanityCost;
    private static final int ENERGY_PER_PULSE = 6;
    private static final int PULSE_TICKS_STILL = 20;
    private static final int PULSE_TICKS_WALK = 16;
    private static final int PULSE_TICKS_EXERTION = 12;
    private static final int PULSE_TICKS_FEAR = 8;
    private static final float FEAR_FALL_DISTANCE_THRESHOLD = 8.0f;
    private static final double WALKING_SPEED_SQR_EPS = 1.0E-4;

    public CardiovascularCouplerItem(Item.Properties props, int humanityCost) {
        super(props);
        this.humanityCost = humanityCost;
    }

    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        if (Screen.hasShiftDown()) {
            tooltip.add((Component)Component.translatable((String)"tooltip.createcybernetics.humanity", (Object[])new Object[]{this.humanityCost}).withStyle(ChatFormatting.GOLD));
            tooltip.add((Component)Component.translatable((String)"tooltip.createcybernetics.heartupgrades_coupler.energy").withStyle(ChatFormatting.DARK_GREEN));
        }
    }

    @Override
    public int getEnergyGeneratedPerTick(Player player, ItemStack installedStack, CyberwareSlot slot) {
        int pulseTicks = CardiovascularCouplerItem.computePulseTicks(player);
        return player.tickCount % pulseTicks == 0 ? 6 : 0;
    }

    private static int computePulseTicks(Player player) {
        boolean meaningfulFalling;
        boolean attacked = player.hurtTime > 0;
        boolean bl = meaningfulFalling = !player.onGround() && !player.isSwimming() && !player.isFallFlying() && !player.getAbilities().flying && player.getDeltaMovement().y < 0.0 && player.fallDistance >= 8.0f;
        if (attacked || meaningfulFalling) {
            return 8;
        }
        if (player.isSprinting() || player.isSwimming()) {
            return 12;
        }
        double horizontalSpeedSqr = player.getDeltaMovement().horizontalDistanceSqr();
        if (horizontalSpeedSqr > 1.0E-4) {
            return 16;
        }
        return 20;
    }

    @Override
    public int getEnergyCapacity(Player player, ItemStack installedStack, CyberwareSlot slot) {
        return 0;
    }

    @Override
    public boolean acceptsGeneratedEnergy(Player player, ItemStack installedStack, CyberwareSlot slot) {
        return false;
    }

    @Override
    public boolean acceptsChargerEnergy(Player player, ItemStack installedStack, CyberwareSlot slot) {
        return false;
    }

    @Override
    public int getChargerEnergyReceivePerTick(Player player, ItemStack installedStack, CyberwareSlot slot) {
        return 0;
    }

    @Override
    public int getHumanityCost() {
        return this.humanityCost;
    }

    @Override
    public Set<Item> requiresCyberware(ItemStack installedStack, CyberwareSlot slot) {
        return Set.of((Item)ModItems.BODYPART_HEART.get());
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
    public int maxStacksPerSlotType(ItemStack stack, CyberwareSlot slotType) {
        return 1;
    }

    @Override
    public void onInstalled(Player player) {
    }

    @Override
    public void onRemoved(Player player) {
    }

    @Override
    public void onTick(Player player) {
    }
}

