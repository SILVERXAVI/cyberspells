/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.network.chat.Component
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.damagesource.DamageSource
 *  net.minecraft.world.effect.MobEffectInstance
 *  net.minecraft.world.effect.MobEffects
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.item.Item$TooltipContext
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.TooltipFlag
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.fml.common.EventBusSubscriber$Bus
 *  net.neoforged.neoforge.event.entity.living.LivingDamageEvent$Post
 */
package com.perigrine3.createcybernetics.item.cyberware;

import com.perigrine3.createcybernetics.api.CyberwareSlot;
import com.perigrine3.createcybernetics.api.ICyberwareItem;
import com.perigrine3.createcybernetics.common.capabilities.ModAttachments;
import com.perigrine3.createcybernetics.common.capabilities.PlayerCyberwareData;
import java.util.List;
import java.util.Set;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

public class AdrenalPumpItem
extends Item
implements ICyberwareItem {
    private final int humanityCost;
    private static final String NBT_INSTALLED = "cc_adrenal_installed";
    private static final String NBT_ACTIVE_UNTIL = "cc_adrenal_active_until";
    private static final String NBT_NEXT_TRIGGER = "cc_adrenal_next_trigger";
    private static final String NBT_WAS_ACTIVE = "cc_adrenal_was_active";
    private static final int BUFF_TICKS = 4800;
    private static final int COOLDOWN_TICKS = 6000;
    private static final int WEAKNESS_TICKS = 2400;
    private static final int SPEED_AMP = 0;
    private static final int STRENGTH_AMP = 0;
    private static final int ENERGY_ACTIVATION_COST = 10;

    public AdrenalPumpItem(Item.Properties props, int humanityCost) {
        super(props);
        this.humanityCost = humanityCost;
    }

    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        if (Screen.hasShiftDown()) {
            tooltip.add((Component)Component.translatable((String)"tooltip.createcybernetics.humanity", (Object[])new Object[]{this.humanityCost}).withStyle(ChatFormatting.GOLD));
            tooltip.add((Component)Component.translatable((String)"tooltip.createcybernetics.organsupgrades_adrenaline.energy").withStyle(ChatFormatting.RED));
        }
    }

    @Override
    public int getEnergyActivationCost(Player player, ItemStack installedStack, CyberwareSlot slot) {
        return 10;
    }

    @Override
    public boolean requiresEnergyToFunction(Player player, ItemStack installedStack, CyberwareSlot slot) {
        return true;
    }

    @Override
    public int getHumanityCost() {
        return this.humanityCost;
    }

    @Override
    public Set<CyberwareSlot> getSupportedSlots() {
        return Set.of(CyberwareSlot.ORGANS);
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
    public void onInstalled(Player player) {
        player.getPersistentData().putBoolean(NBT_INSTALLED, true);
    }

    @Override
    public void onRemoved(Player player) {
        CompoundTag tag = player.getPersistentData();
        tag.remove(NBT_INSTALLED);
        tag.remove(NBT_ACTIVE_UNTIL);
        tag.remove(NBT_NEXT_TRIGGER);
        tag.remove(NBT_WAS_ACTIVE);
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
        CompoundTag tag = player.getPersistentData();
        if (!tag.getBoolean(NBT_INSTALLED)) {
            return;
        }
        long now = player.level().getGameTime();
        long activeUntil = tag.getLong(NBT_ACTIVE_UNTIL);
        boolean active = activeUntil > 0L && now < activeUntil;
        boolean wasActive = tag.getBoolean(NBT_WAS_ACTIVE);
        if (active) {
            tag.putBoolean(NBT_WAS_ACTIVE, true);
            if (now % 20L == 0L) {
                player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 40, 0, false, false, false));
                player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 40, 0, false, false, false));
            }
            return;
        }
        if (wasActive) {
            tag.putBoolean(NBT_WAS_ACTIVE, false);
            tag.remove(NBT_ACTIVE_UNTIL);
            player.removeEffect(MobEffects.MOVEMENT_SPEED);
            player.removeEffect(MobEffects.DAMAGE_BOOST);
            player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 2400, 0, false, false, true));
        }
    }

    @EventBusSubscriber(modid="createcybernetics", bus=EventBusSubscriber.Bus.GAME)
    public static final class Events {
        @SubscribeEvent
        public static void onLivingDamagePost(LivingDamageEvent.Post event) {
            LivingEntity livingEntity = event.getEntity();
            if (!(livingEntity instanceof ServerPlayer)) {
                return;
            }
            ServerPlayer player = (ServerPlayer)livingEntity;
            if (player.level().isClientSide) {
                return;
            }
            if (!player.isAlive()) {
                return;
            }
            if (player.isCreative() || player.isSpectator()) {
                return;
            }
            CompoundTag tag = player.getPersistentData();
            if (!tag.getBoolean(AdrenalPumpItem.NBT_INSTALLED)) {
                return;
            }
            DamageSource source = event.getSource();
            Entity attacker = source.getEntity();
            if (attacker == null) {
                return;
            }
            if (event.getNewDamage() <= 0.0f) {
                return;
            }
            long now = player.level().getGameTime();
            long next = tag.getLong(AdrenalPumpItem.NBT_NEXT_TRIGGER);
            if (next != 0L && now < next) {
                return;
            }
            if (!player.hasData(ModAttachments.CYBERWARE)) {
                return;
            }
            PlayerCyberwareData data = (PlayerCyberwareData)player.getData(ModAttachments.CYBERWARE);
            if (data == null) {
                return;
            }
            if (!data.tryConsumeEnergy(10)) {
                return;
            }
            tag.putLong(AdrenalPumpItem.NBT_ACTIVE_UNTIL, now + 4800L);
            tag.putLong(AdrenalPumpItem.NBT_NEXT_TRIGGER, now + 6000L);
            tag.putBoolean(AdrenalPumpItem.NBT_WAS_ACTIVE, true);
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 40, 0, false, true, false));
            player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 40, 0, false, true, false));
            player.removeEffect(MobEffects.WEAKNESS);
        }

        private Events() {
        }
    }
}

