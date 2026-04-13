/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.network.chat.Component
 *  net.minecraft.tags.TagKey
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
 *  net.neoforged.bus.api.EventPriority
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.fml.common.EventBusSubscriber$Bus
 *  net.neoforged.neoforge.event.entity.living.LivingDamageEvent$Post
 */
package com.perigrine3.createcybernetics.item.cyberware;

import com.perigrine3.createcybernetics.api.CyberwareSlot;
import com.perigrine3.createcybernetics.api.ICyberwareItem;
import com.perigrine3.createcybernetics.api.InstalledCyberware;
import com.perigrine3.createcybernetics.common.capabilities.ModAttachments;
import com.perigrine3.createcybernetics.common.capabilities.PlayerCyberwareData;
import com.perigrine3.createcybernetics.util.ModTags;
import java.util.List;
import java.util.Set;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

public class PlateletDispatcherItem
extends Item
implements ICyberwareItem {
    private final int humanityCost;
    private static final String NBT_LAST_COMBAT_TICK = "cc_platelet_lastCombatTick";
    private static final String NBT_ACTIVE = "cc_platelet_active";
    private static final int OUT_OF_COMBAT_TICKS = 2400;
    private static final int REGEN_TICKS = 600;
    private static final int ENERGY_PER_TICK_WHEN_ACTIVE = 5;

    public PlateletDispatcherItem(Item.Properties props, int humanityCost) {
        super(props);
        this.humanityCost = humanityCost;
    }

    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        if (Screen.hasShiftDown()) {
            tooltip.add((Component)Component.translatable((String)"tooltip.createcybernetics.humanity", (Object[])new Object[]{this.humanityCost}).withStyle(ChatFormatting.GOLD));
            tooltip.add((Component)Component.translatable((String)"tooltip.createcybernetics.heartupgrades_platelets.energy").withStyle(ChatFormatting.RED));
        }
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
    public boolean requiresEnergyToFunction(Player player, ItemStack installedStack, CyberwareSlot slot) {
        return true;
    }

    @Override
    public int getEnergyUsedPerTick(Player player, ItemStack installedStack, CyberwareSlot slot) {
        boolean inCombatWindow;
        if (player == null) {
            return 0;
        }
        if (player.level().isClientSide) {
            return 0;
        }
        if (!player.isAlive()) {
            return 0;
        }
        if (player.isCreative() || player.isSpectator()) {
            return 0;
        }
        long now = player.level().getGameTime();
        long lastCombat = player.getPersistentData().getLong(NBT_LAST_COMBAT_TICK);
        boolean bl = inCombatWindow = lastCombat != 0L && now - lastCombat < 2400L;
        if (inCombatWindow) {
            return 0;
        }
        boolean active = player.getPersistentData().getBoolean(NBT_ACTIVE);
        boolean needsHeal = player.getHealth() < player.getMaxHealth();
        return active || needsHeal ? 5 : 0;
    }

    @Override
    public void onInstalled(Player player) {
        if (!player.level().isClientSide) {
            player.getPersistentData().remove(NBT_ACTIVE);
        }
    }

    @Override
    public void onRemoved(Player player) {
        if (!player.level().isClientSide) {
            player.getPersistentData().remove(NBT_ACTIVE);
            player.removeEffect(MobEffects.REGENERATION);
        }
    }

    @Override
    public void onTick(Player player) {
    }

    @Override
    public void onTick(Player player, ItemStack installedStack, CyberwareSlot slot, int index) {
        boolean inCombatWindow;
        if (player.level().isClientSide) {
            return;
        }
        if (!player.isAlive()) {
            return;
        }
        if (player.isCreative() || player.isSpectator()) {
            return;
        }
        if (!player.hasData(ModAttachments.CYBERWARE)) {
            return;
        }
        PlayerCyberwareData data = (PlayerCyberwareData)player.getData(ModAttachments.CYBERWARE);
        if (data == null) {
            return;
        }
        InstalledCyberware cw = data.get(slot, index);
        if (cw == null) {
            return;
        }
        long now = player.level().getGameTime();
        long lastCombat = player.getPersistentData().getLong(NBT_LAST_COMBAT_TICK);
        boolean bl = inCombatWindow = lastCombat != 0L && now - lastCombat < 2400L;
        if (inCombatWindow) {
            if (player.hasEffect(MobEffects.REGENERATION)) {
                player.removeEffect(MobEffects.REGENERATION);
            }
            player.getPersistentData().putBoolean(NBT_ACTIVE, false);
            return;
        }
        boolean active = player.getPersistentData().getBoolean(NBT_ACTIVE);
        MobEffectInstance existing = player.getEffect(MobEffects.REGENERATION);
        if (active && existing == null) {
            player.getPersistentData().putBoolean(NBT_ACTIVE, false);
            active = false;
        }
        if (!cw.isPowered()) {
            if (player.hasEffect(MobEffects.REGENERATION)) {
                player.removeEffect(MobEffects.REGENERATION);
            }
            player.getPersistentData().putBoolean(NBT_ACTIVE, false);
            return;
        }
        if (player.getHealth() >= player.getMaxHealth()) {
            return;
        }
        if (existing == null || existing.getDuration() < 40) {
            player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 600, 0, false, true, true));
            player.getPersistentData().putBoolean(NBT_ACTIVE, true);
        }
    }

    @EventBusSubscriber(modid="createcybernetics", bus=EventBusSubscriber.Bus.GAME)
    public static final class Events {
        @SubscribeEvent(priority=EventPriority.HIGHEST)
        public static void onLivingDamagePost(LivingDamageEvent.Post event) {
            LivingEntity livingEntity = event.getEntity();
            if (!(livingEntity instanceof Player)) {
                return;
            }
            Player victim = (Player)livingEntity;
            if (victim.level().isClientSide) {
                return;
            }
            long now = victim.level().getGameTime();
            victim.getPersistentData().putLong(PlateletDispatcherItem.NBT_LAST_COMBAT_TICK, now);
            if (victim.hasEffect(MobEffects.REGENERATION)) {
                victim.removeEffect(MobEffects.REGENERATION);
            }
            victim.getPersistentData().putBoolean(PlateletDispatcherItem.NBT_ACTIVE, false);
            Entity src = event.getSource().getEntity();
            if (src instanceof Player) {
                Player attacker = (Player)src;
                if (!attacker.level().isClientSide) {
                    attacker.getPersistentData().putLong(PlateletDispatcherItem.NBT_LAST_COMBAT_TICK, attacker.level().getGameTime());
                    if (attacker.hasEffect(MobEffects.REGENERATION)) {
                        attacker.removeEffect(MobEffects.REGENERATION);
                    }
                    attacker.getPersistentData().putBoolean(PlateletDispatcherItem.NBT_ACTIVE, false);
                }
            }
        }

        private Events() {
        }
    }
}

