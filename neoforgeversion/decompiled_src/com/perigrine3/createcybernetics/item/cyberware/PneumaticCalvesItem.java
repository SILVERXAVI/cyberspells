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
 *  net.neoforged.neoforge.event.entity.living.LivingEvent$LivingJumpEvent
 *  net.neoforged.neoforge.event.tick.PlayerTickEvent$Post
 */
package com.perigrine3.createcybernetics.item.cyberware;

import com.perigrine3.createcybernetics.api.CyberwareSlot;
import com.perigrine3.createcybernetics.api.ICyberwareItem;
import com.perigrine3.createcybernetics.api.InstalledCyberware;
import com.perigrine3.createcybernetics.common.capabilities.ModAttachments;
import com.perigrine3.createcybernetics.common.capabilities.PlayerCyberwareData;
import com.perigrine3.createcybernetics.effect.ModEffects;
import com.perigrine3.createcybernetics.util.CyberwareAttributeHelper;
import com.perigrine3.createcybernetics.util.ModTags;
import java.util.List;
import java.util.Set;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

public class PneumaticCalvesItem
extends Item
implements ICyberwareItem {
    private final int humanityCost;
    private static final int ENERGY_SPRINT_JUMP = 3;
    private static final int ENERGY_CROUCH_JUMP = 5;
    private static final String NBT_LAST_JUMP_CHARGE_TICK = "cc_calves_last_jump_charge_tick";

    public PneumaticCalvesItem(Item.Properties props, int humanityCost) {
        super(props);
        this.humanityCost = humanityCost;
    }

    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        if (Screen.hasShiftDown()) {
            tooltip.add((Component)Component.translatable((String)"tooltip.createcybernetics.humanity", (Object[])new Object[]{this.humanityCost}).withStyle(ChatFormatting.GOLD));
            tooltip.add((Component)Component.translatable((String)"tooltip.createcybernetics.legupgrades_jumpboost.energy").withStyle(ChatFormatting.RED));
        }
    }

    @Override
    public int getHumanityCost() {
        return this.humanityCost;
    }

    @Override
    public Set<TagKey<Item>> requiresCyberwareTags(ItemStack installedStack, CyberwareSlot slot) {
        return switch (slot) {
            case CyberwareSlot.RLEG -> Set.of(ModTags.Items.RIGHTLEG_REPLACEMENTS);
            case CyberwareSlot.LLEG -> Set.of(ModTags.Items.LEFTLEG_REPLACEMENTS);
            default -> Set.of();
        };
    }

    @Override
    public Set<CyberwareSlot> getSupportedSlots() {
        return Set.of(CyberwareSlot.RLEG, CyberwareSlot.LLEG);
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
    }

    @Override
    public void onRemoved(Player player) {
        if (player.level().isClientSide) {
            return;
        }
        PneumaticCalvesItem.cleanup(player);
    }

    @Override
    public void onTick(Player player) {
        if (player.level().isClientSide) {
            return;
        }
        if (!player.hasData(ModAttachments.CYBERWARE)) {
            return;
        }
        PlayerCyberwareData data = (PlayerCyberwareData)player.getData(ModAttachments.CYBERWARE);
        if (data == null) {
            return;
        }
        PairState pair = PneumaticCalvesItem.enforcePairRuleAndGetState(player, data);
        if (!pair.bothInstalled || !pair.bothEnabled) {
            PneumaticCalvesItem.cleanup(player);
            return;
        }
        player.addEffect(new MobEffectInstance(ModEffects.PNEUMATIC_CALVES_EFFECT, 100, 0, false, false, false));
        if (player.isSprinting()) {
            CyberwareAttributeHelper.applyModifier((LivingEntity)player, "calves_sprint");
        } else {
            CyberwareAttributeHelper.removeModifier((LivingEntity)player, "calves_sprint");
        }
    }

    private static void cleanup(Player player) {
        CyberwareAttributeHelper.removeModifier((LivingEntity)player, "calves_sprint");
        player.removeEffect(ModEffects.PNEUMATIC_CALVES_EFFECT);
        player.getPersistentData().remove(NBT_LAST_JUMP_CHARGE_TICK);
    }

    private static PairState enforcePairRuleAndGetState(Player player, PlayerCyberwareData data) {
        Found first = null;
        Found second = null;
        block0: for (CyberwareSlot slot : new CyberwareSlot[]{CyberwareSlot.RLEG, CyberwareSlot.LLEG}) {
            for (int i = 0; i < slot.size; ++i) {
                ItemStack st;
                InstalledCyberware cw = data.get(slot, i);
                if (cw == null || (st = cw.getItem()) == null || st.isEmpty() || !(st.getItem() instanceof PneumaticCalvesItem)) continue;
                if (first == null) {
                    first = new Found(slot, i);
                    continue;
                }
                if (second != null) continue block0;
                second = new Found(slot, i);
            }
        }
        if (first == null) {
            return new PairState(false, false);
        }
        if (second == null) {
            PneumaticCalvesItem.forceDisabled(player, data, first.slot(), first.index());
            PneumaticCalvesItem.cleanup(player);
            return new PairState(false, false);
        }
        boolean e1 = data.isEnabled(first.slot(), first.index());
        boolean e2 = data.isEnabled(second.slot(), second.index());
        return new PairState(true, e1 && e2);
    }

    private static void forceDisabled(Player player, PlayerCyberwareData data, CyberwareSlot slot, int index) {
        if (!data.isEnabled(slot, index)) {
            return;
        }
        data.setEnabled(slot, index, false);
        data.setDirty();
        player.syncData(ModAttachments.CYBERWARE);
    }

    private record PairState(boolean bothInstalled, boolean bothEnabled) {
    }

    private record Found(CyberwareSlot slot, int index) {
    }

    @EventBusSubscriber(modid="createcybernetics", bus=EventBusSubscriber.Bus.GAME)
    public static final class Events {
        private Events() {
        }

        @SubscribeEvent
        public static void onPlayerTick(PlayerTickEvent.Post event) {
            Player player = event.getEntity();
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
            PneumaticCalvesItem.enforcePairRuleAndGetState(player, data);
        }

        @SubscribeEvent(priority=EventPriority.HIGHEST)
        public static void onLivingJump(LivingEvent.LivingJumpEvent event) {
            int cost;
            boolean sprintJump;
            LivingEntity livingEntity = event.getEntity();
            if (!(livingEntity instanceof Player)) {
                return;
            }
            Player player = (Player)livingEntity;
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
            PairState pair = PneumaticCalvesItem.enforcePairRuleAndGetState(player, data);
            if (!pair.bothInstalled || !pair.bothEnabled) {
                return;
            }
            boolean crouchJump = player.isCrouching();
            boolean bl = sprintJump = !crouchJump && player.isSprinting();
            int n = crouchJump ? 5 : (cost = sprintJump ? 3 : 0);
            if (cost <= 0) {
                return;
            }
            long now = player.level().getGameTime();
            CompoundTag ptag = player.getPersistentData();
            if (ptag.getLong(PneumaticCalvesItem.NBT_LAST_JUMP_CHARGE_TICK) == now) {
                return;
            }
            ptag.putLong(PneumaticCalvesItem.NBT_LAST_JUMP_CHARGE_TICK, now);
            if (!data.tryConsumeEnergy(cost)) {
                PneumaticCalvesItem.cleanup(player);
                return;
            }
            data.setDirty();
            player.syncData(ModAttachments.CYBERWARE);
        }
    }
}

