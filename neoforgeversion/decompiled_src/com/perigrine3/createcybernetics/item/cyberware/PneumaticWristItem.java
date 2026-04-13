/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.network.chat.Component
 *  net.minecraft.tags.TagKey
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
 *  net.neoforged.neoforge.event.entity.player.AttackEntityEvent
 *  net.neoforged.neoforge.event.entity.player.PlayerInteractEvent$LeftClickBlock
 *  net.neoforged.neoforge.event.entity.player.PlayerInteractEvent$RightClickBlock
 *  net.neoforged.neoforge.event.entity.player.PlayerInteractEvent$RightClickItem
 *  net.neoforged.neoforge.event.level.BlockEvent$BreakEvent
 */
package com.perigrine3.createcybernetics.item.cyberware;

import com.perigrine3.createcybernetics.api.CyberwareSlot;
import com.perigrine3.createcybernetics.api.ICyberwareItem;
import com.perigrine3.createcybernetics.api.InstalledCyberware;
import com.perigrine3.createcybernetics.common.capabilities.ModAttachments;
import com.perigrine3.createcybernetics.common.capabilities.PlayerCyberwareData;
import com.perigrine3.createcybernetics.item.ModItems;
import com.perigrine3.createcybernetics.util.CyberwareAttributeHelper;
import com.perigrine3.createcybernetics.util.ModTags;
import java.util.List;
import java.util.Set;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.BlockEvent;

public class PneumaticWristItem
extends Item
implements ICyberwareItem {
    private final int humanityCost;
    private static final int ENERGY_PER_TICK_ACTIVE = 3;
    private static final int ACTIVE_WINDOW_TICKS = 10;
    private static final String NBT_ACTIVE_UNTIL = "cc_pwrist_active_until";
    private static final String NBT_LAST_APPLY_TICK = "cc_pwrist_last_apply_tick";

    public PneumaticWristItem(Item.Properties props, int humanityCost) {
        super(props);
        this.humanityCost = humanityCost;
    }

    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        if (Screen.hasShiftDown()) {
            tooltip.add((Component)Component.translatable((String)"tooltip.createcybernetics.humanity", (Object[])new Object[]{this.humanityCost}).withStyle(ChatFormatting.GOLD));
            tooltip.add((Component)Component.translatable((String)"tooltip.createcybernetics.armupgrades_pneumaticwrist.energy").withStyle(ChatFormatting.RED));
        }
    }

    @Override
    public int getHumanityCost() {
        return this.humanityCost;
    }

    @Override
    public Set<TagKey<Item>> requiresCyberwareTags(ItemStack installedStack, CyberwareSlot slot) {
        return switch (slot) {
            case CyberwareSlot.RARM -> Set.of(ModTags.Items.RIGHTARM_REPLACEMENTS);
            case CyberwareSlot.LARM -> Set.of(ModTags.Items.LEFTARM_REPLACEMENTS);
            default -> Set.of();
        };
    }

    @Override
    public Set<CyberwareSlot> getSupportedSlots() {
        return Set.of(CyberwareSlot.RARM, CyberwareSlot.LARM);
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
        if (player == null) {
            return 0;
        }
        if (player.level().isClientSide) {
            return 0;
        }
        long now = player.level().getGameTime();
        long until = player.getPersistentData().getLong(NBT_ACTIVE_UNTIL);
        if (until <= now) {
            return 0;
        }
        if (!this.shouldChargeOnThisSlot(player, slot)) {
            return 0;
        }
        return 3;
    }

    @Override
    public void onInstalled(Player player) {
        CyberwareAttributeHelper.applyModifier((LivingEntity)player, "pneumatic_wrist_block");
        CyberwareAttributeHelper.applyModifier((LivingEntity)player, "pneumatic_wrist_entity");
        CyberwareAttributeHelper.applyModifier((LivingEntity)player, "pneumatic_wrist_knockback");
    }

    @Override
    public void onRemoved(Player player) {
        CyberwareAttributeHelper.removeModifier((LivingEntity)player, "pneumatic_wrist_block");
        CyberwareAttributeHelper.removeModifier((LivingEntity)player, "pneumatic_wrist_entity");
        CyberwareAttributeHelper.removeModifier((LivingEntity)player, "pneumatic_wrist_knockback");
        if (!player.level().isClientSide) {
            player.getPersistentData().remove(NBT_ACTIVE_UNTIL);
            player.getPersistentData().remove(NBT_LAST_APPLY_TICK);
        }
    }

    @Override
    public void onTick(Player player) {
    }

    @Override
    public void onTick(Player player, ItemStack installedStack, CyberwareSlot slot, int index) {
        if (player.level().isClientSide) {
            return;
        }
        if (!player.isAlive()) {
            return;
        }
        long now = player.level().getGameTime();
        CompoundTag ptag = player.getPersistentData();
        if (ptag.getLong(NBT_LAST_APPLY_TICK) == now) {
            return;
        }
        ptag.putLong(NBT_LAST_APPLY_TICK, now);
        if (!player.hasData(ModAttachments.CYBERWARE)) {
            return;
        }
        PlayerCyberwareData data = (PlayerCyberwareData)player.getData(ModAttachments.CYBERWARE);
        if (data == null) {
            return;
        }
        boolean active = ptag.getLong(NBT_ACTIVE_UNTIL) > now;
        boolean powered = false;
        for (CyberwareSlot s : new CyberwareSlot[]{CyberwareSlot.RARM, CyberwareSlot.LARM}) {
            for (int i = 0; i < s.size; ++i) {
                ItemStack st;
                InstalledCyberware cw = data.get(s, i);
                if (cw == null || (st = cw.getItem()) == null || st.isEmpty() || st.getItem() != this || !cw.isPowered()) continue;
                powered = true;
            }
        }
        if (active && powered) {
            CyberwareAttributeHelper.applyModifier((LivingEntity)player, "pneumatic_wrist_block");
            CyberwareAttributeHelper.applyModifier((LivingEntity)player, "pneumatic_wrist_entity");
            CyberwareAttributeHelper.applyModifier((LivingEntity)player, "pneumatic_wrist_knockback");
        } else {
            CyberwareAttributeHelper.removeModifier((LivingEntity)player, "pneumatic_wrist_block");
            CyberwareAttributeHelper.removeModifier((LivingEntity)player, "pneumatic_wrist_entity");
            CyberwareAttributeHelper.removeModifier((LivingEntity)player, "pneumatic_wrist_knockback");
        }
    }

    private boolean shouldChargeOnThisSlot(Player player, CyberwareSlot slot) {
        if (!player.hasData(ModAttachments.CYBERWARE)) {
            return false;
        }
        PlayerCyberwareData data = (PlayerCyberwareData)player.getData(ModAttachments.CYBERWARE);
        if (data == null) {
            return false;
        }
        boolean hasRight = false;
        for (int i = 0; i < CyberwareSlot.RARM.size; ++i) {
            ItemStack st;
            InstalledCyberware cw = data.get(CyberwareSlot.RARM, i);
            if (cw == null || (st = cw.getItem()) == null || st.isEmpty() || st.getItem() != this) continue;
            hasRight = true;
            break;
        }
        if (hasRight) {
            return slot == CyberwareSlot.RARM;
        }
        return slot == CyberwareSlot.LARM;
    }

    @EventBusSubscriber(modid="createcybernetics", bus=EventBusSubscriber.Bus.GAME)
    public static final class Events {
        @SubscribeEvent(priority=EventPriority.HIGHEST)
        public static void onAttackEntity(AttackEntityEvent event) {
            Player player = event.getEntity();
            if (!(player instanceof Player)) {
                return;
            }
            Player player2 = player;
            if (player2.level().isClientSide) {
                return;
            }
            Events.markActive(player2);
        }

        @SubscribeEvent(priority=EventPriority.HIGHEST)
        public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
            Player player = event.getEntity();
            if (!(player instanceof Player)) {
                return;
            }
            Player player2 = player;
            if (player2.level().isClientSide) {
                return;
            }
            Events.markActive(player2);
        }

        @SubscribeEvent(priority=EventPriority.HIGHEST)
        public static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
            Player player = event.getEntity();
            if (!(player instanceof Player)) {
                return;
            }
            Player player2 = player;
            if (player2.level().isClientSide) {
                return;
            }
            Events.markActive(player2);
        }

        @SubscribeEvent(priority=EventPriority.HIGHEST)
        public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
            Player player = event.getEntity();
            if (!(player instanceof Player)) {
                return;
            }
            Player player2 = player;
            if (player2.level().isClientSide) {
                return;
            }
            Events.markActive(player2);
        }

        @SubscribeEvent(priority=EventPriority.HIGHEST)
        public static void onBlockBreak(BlockEvent.BreakEvent event) {
            Player player = event.getPlayer();
            if (!(player instanceof Player)) {
                return;
            }
            Player player2 = player;
            if (player2.level().isClientSide) {
                return;
            }
            Events.markActive(player2);
        }

        private static void markActive(Player player) {
            if (!player.hasData(ModAttachments.CYBERWARE)) {
                return;
            }
            PlayerCyberwareData data = (PlayerCyberwareData)player.getData(ModAttachments.CYBERWARE);
            if (data == null) {
                return;
            }
            boolean installed = data.hasSpecificItem((Item)ModItems.ARMUPGRADES_PNEUMATICWRIST.get(), CyberwareSlot.RARM, CyberwareSlot.LARM);
            if (!installed) {
                return;
            }
            long now = player.level().getGameTime();
            player.getPersistentData().putLong(PneumaticWristItem.NBT_ACTIVE_UNTIL, now + 10L);
        }

        private Events() {
        }
    }
}

