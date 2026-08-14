/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Multimap
 *  java.lang.MatchException
 *  net.minecraft.core.Holder
 *  net.minecraft.core.component.DataComponentType
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.ai.attributes.Attribute
 *  net.minecraft.world.entity.ai.attributes.AttributeModifier
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.neoforged.neoforge.event.entity.EntityTeleportEvent
 *  net.neoforged.neoforge.event.entity.living.LivingDamageEvent$Pre
 *  net.neoforged.neoforge.event.entity.living.LivingDeathEvent
 *  net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent$Tick
 *  net.neoforged.neoforge.event.entity.living.LivingEvent$LivingJumpEvent
 *  net.neoforged.neoforge.event.entity.living.LivingFallEvent
 *  net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent
 *  net.neoforged.neoforge.event.entity.living.MobEffectEvent$Applicable
 *  net.neoforged.neoforge.event.entity.player.PlayerEvent$BreakSpeed
 *  net.neoforged.neoforge.event.entity.player.PlayerEvent$HarvestCheck
 *  net.neoforged.neoforge.event.entity.player.PlayerInteractEvent$LeftClickBlock
 *  net.neoforged.neoforge.event.tick.PlayerTickEvent$Post
 */
package com.maxwell.cyber_ware_port.common.item.base;

import com.google.common.collect.Multimap;
import com.maxwell.cyber_ware_port.common.item.base.BodyPartType;
import com.maxwell.cyber_ware_port.init.ModDataComponents;
import java.util.Collections;
import java.util.Set;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.entity.EntityTeleportEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
import net.neoforged.neoforge.event.entity.living.LivingEvent;
import net.neoforged.neoforge.event.entity.living.LivingFallEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

public interface ICyberware {
    public int getEssenceCost(ItemStack var1);

    public int getSlot(ItemStack var1);

    public boolean isPristine(ItemStack var1);

    public void setPristine(ItemStack var1, boolean var2);

    public int getMaxInstallAmount(ItemStack var1);

    public Set<Item> getPrerequisites(ItemStack var1);

    default public Set<Item> getIncompatibleItems(ItemStack stack) {
        return Collections.emptySet();
    }

    public boolean hasEnergyProperties(ItemStack var1);

    public int getEnergyConsumption(ItemStack var1);

    default public int getEventConsumption(ItemStack stack) {
        return 0;
    }

    public int getEnergyGeneration(ItemStack var1);

    public int getEnergyStorage(ItemStack var1);

    public StackingRule getStackingEnergyRule(ItemStack var1);

    default public boolean isIncompatible(ItemStack self, ItemStack other) {
        if (self.getItem() == other.getItem()) {
            return this.getMaxInstallAmount(self) <= 1;
        }
        return this.getIncompatibleItems(self).contains(other.getItem());
    }

    default public BodyPartType getBodyPartType(ItemStack stack) {
        return BodyPartType.NONE;
    }

    default public boolean canToggle(ItemStack stack) {
        return false;
    }

    default public boolean isActive(ItemStack stack) {
        return (Boolean)stack.getOrDefault((DataComponentType)ModDataComponents.ACTIVE.get(), (Object)true);
    }

    default public int getQuality(ItemStack stack) {
        return 1;
    }

    default public void toggle(ItemStack stack) {
        stack.set((DataComponentType)ModDataComponents.ACTIVE.get(), (Object)(!this.isActive(stack) ? 1 : 0));
    }

    public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(ItemStack var1);

    default public void onPlayerTick(PlayerTickEvent.Post event, ItemStack stack, LivingEntity wearer) {
    }

    default public void onItemUseTick(LivingEntityUseItemEvent.Tick event, ItemStack stack, LivingEntity wearer) {
    }

    default public void onLivingIncomingDamage(LivingIncomingDamageEvent event, ItemStack stack, LivingEntity wearer) {
    }

    default public void onLivingDamagePre(LivingDamageEvent.Pre event, ItemStack stack, LivingEntity wearer) {
    }

    default public void onEntityTeleport(EntityTeleportEvent event, ItemStack stack, LivingEntity wearer) {
    }

    default public void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event, ItemStack stack, LivingEntity wearer) {
    }

    default public void onPotionApplicable(MobEffectEvent.Applicable event, ItemStack stack, LivingEntity wearer) {
    }

    default public void onLivingDeath(LivingDeathEvent event, ItemStack stack, LivingEntity wearer) {
    }

    default public void onHarvestCheck(PlayerEvent.HarvestCheck event, ItemStack stack, LivingEntity wearer) {
    }

    default public void onBreakSpeed(PlayerEvent.BreakSpeed event, ItemStack stack, LivingEntity wearer) {
    }

    default public void onLivingFall(LivingFallEvent event, ItemStack stack, LivingEntity wearer) {
    }

    default public void onLivingJump(LivingEvent.LivingJumpEvent event, ItemStack stack, LivingEntity wearer) {
    }

    default public void onSystemTick(LivingEntity entity, ItemStack stack) {
    }

    default public void onInstalled(LivingEntity entity, ItemStack stack) {
    }

    default public void onRemoved(LivingEntity entity, ItemStack stack) {
    }

    public static enum StackingRule {
        LINEAR,
        DIMINISHING,
        STATIC;


        public int calculate(int baseCost, int count) {
            return switch (this.ordinal()) {
                default -> throw new MatchException(null, null);
                case 0 -> baseCost * count;
                case 1 -> (int)((double)baseCost * (1.0 + Math.log(count)));
                case 2 -> count > 0 ? baseCost : 0;
            };
        }
    }
}

