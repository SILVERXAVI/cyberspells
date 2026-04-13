/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.network.chat.Component
 *  net.minecraft.tags.TagKey
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.InteractionResult
 *  net.minecraft.world.entity.HumanoidArm
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
 *  net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent$Start
 *  net.neoforged.neoforge.event.entity.player.AttackEntityEvent
 *  net.neoforged.neoforge.event.entity.player.PlayerEvent$BreakSpeed
 *  net.neoforged.neoforge.event.entity.player.PlayerInteractEvent$RightClickBlock
 *  net.neoforged.neoforge.event.entity.player.PlayerInteractEvent$RightClickItem
 *  net.neoforged.neoforge.event.level.BlockEvent$BreakEvent
 *  net.neoforged.neoforge.event.tick.PlayerTickEvent$Post
 */
package com.perigrine3.createcybernetics.item.cyberware;

import com.perigrine3.createcybernetics.api.CyberwareSlot;
import com.perigrine3.createcybernetics.api.ICyberwareItem;
import com.perigrine3.createcybernetics.api.InstalledCyberware;
import com.perigrine3.createcybernetics.common.capabilities.ModAttachments;
import com.perigrine3.createcybernetics.common.capabilities.PlayerCyberwareData;
import com.perigrine3.createcybernetics.util.CyberwareAttributeHelper;
import com.perigrine3.createcybernetics.util.ModTags;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

public class CyberarmItem
extends Item
implements ICyberwareItem {
    private final int humanityCost;
    final CyberwareSlot side;

    public CyberarmItem(Item.Properties props, int humanityCost, CyberwareSlot side) {
        super(props);
        this.humanityCost = humanityCost;
        this.side = side;
    }

    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        if (Screen.hasShiftDown()) {
            tooltip.add((Component)Component.translatable((String)"tooltip.createcybernetics.humanity", (Object[])new Object[]{this.humanityCost}).withStyle(ChatFormatting.GOLD));
            tooltip.add((Component)Component.translatable((String)"tooltip.basecyberware_cyberarm.energy").withStyle(ChatFormatting.RED));
        }
    }

    @Override
    public boolean isDyeable(ItemStack stack, CyberwareSlot slot) {
        return slot == this.side;
    }

    @Override
    public boolean isDyeable(ItemStack stack) {
        return true;
    }

    @Override
    public int getEnergyUsedPerTick(Player player, ItemStack installedStack, CyberwareSlot slot) {
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
        return Set.of(this.side);
    }

    @Override
    public boolean replacesOrgan() {
        return true;
    }

    @Override
    public Set<CyberwareSlot> getReplacedOrgans() {
        return Set.of(this.side);
    }

    @Override
    public Set<TagKey<Item>> incompatibleCyberwareTags(ItemStack installedStack, CyberwareSlot slot) {
        if (slot == CyberwareSlot.LARM) {
            return Set.of(ModTags.Items.LEFTARM_REPLACEMENTS);
        }
        if (slot == CyberwareSlot.RARM) {
            return Set.of(ModTags.Items.RIGHTARM_REPLACEMENTS);
        }
        return Set.of();
    }

    @Override
    public void onInstalled(Player player) {
        PlayerCyberwareData data = (PlayerCyberwareData)player.getData(ModAttachments.CYBERWARE);
        if (data.hasAnyTagged(ModTags.Items.RIGHT_CYBERARM, CyberwareSlot.RARM) && data.hasAnyTagged(ModTags.Items.LEFT_CYBERARM, CyberwareSlot.LARM)) {
            CyberwareAttributeHelper.applyModifier((LivingEntity)player, "cyberarm_strength1");
            CyberwareAttributeHelper.applyModifier((LivingEntity)player, "cyberarm_blockbreak1");
            CyberwareAttributeHelper.applyModifier((LivingEntity)player, "cyberarm_strength2");
            CyberwareAttributeHelper.applyModifier((LivingEntity)player, "cyberarm_blockbreak2");
        } else if (data.hasAnyTagged(ModTags.Items.RIGHT_CYBERARM, CyberwareSlot.RARM) || data.hasAnyTagged(ModTags.Items.LEFT_CYBERARM, CyberwareSlot.LARM)) {
            CyberwareAttributeHelper.applyModifier((LivingEntity)player, "cyberarm_strength1");
            CyberwareAttributeHelper.applyModifier((LivingEntity)player, "cyberarm_blockbreak1");
            CyberwareAttributeHelper.removeModifier((LivingEntity)player, "cyberarm_strength2");
            CyberwareAttributeHelper.removeModifier((LivingEntity)player, "cyberarm_blockbreak2");
        }
    }

    @Override
    public void onRemoved(Player player) {
        CyberwareAttributeHelper.removeModifier((LivingEntity)player, "cyberarm_strength1");
        CyberwareAttributeHelper.removeModifier((LivingEntity)player, "cyberarm_blockbreak1");
        CyberwareAttributeHelper.removeModifier((LivingEntity)player, "cyberarm_strength2");
        CyberwareAttributeHelper.removeModifier((LivingEntity)player, "cyberarm_blockbreak2");
        this.onInstalled(player);
    }

    @Override
    public void onTick(Player player) {
        ICyberwareItem.super.onTick(player);
    }

    @EventBusSubscriber(modid="createcybernetics", bus=EventBusSubscriber.Bus.GAME)
    public static final class PowerFailHooks {
        private static final ConcurrentHashMap<Class<?>, Method> POWER_METHOD_CACHE = new ConcurrentHashMap();
        private static final ConcurrentHashMap<Class<?>, Field> POWER_FIELD_CACHE = new ConcurrentHashMap();

        private PowerFailHooks() {
        }

        @SubscribeEvent(priority=EventPriority.HIGHEST)
        public static void onPlayerTick(PlayerTickEvent.Post event) {
            InteractionHand usingHand;
            Player player = event.getEntity();
            if (player.level().isClientSide) {
                return;
            }
            PlayerCyberwareData data = (PlayerCyberwareData)player.getData(ModAttachments.CYBERWARE);
            if (data == null) {
                return;
            }
            boolean leftDead = PowerFailHooks.isCyberarmUnpowered(player, data, CyberwareSlot.LARM);
            boolean rightDead = PowerFailHooks.isCyberarmUnpowered(player, data, CyberwareSlot.RARM);
            if (!leftDead && !rightDead) {
                return;
            }
            PowerFailHooks.dropIfHandDisabled(player, leftDead, rightDead, InteractionHand.MAIN_HAND);
            PowerFailHooks.dropIfHandDisabled(player, leftDead, rightDead, InteractionHand.OFF_HAND);
            if (player.isUsingItem() && PowerFailHooks.isHandDisabled(player, leftDead, rightDead, usingHand = player.getUsedItemHand())) {
                player.stopUsingItem();
            }
        }

        private static void dropIfHandDisabled(Player player, boolean leftDead, boolean rightDead, InteractionHand hand) {
            if (!PowerFailHooks.isHandDisabled(player, leftDead, rightDead, hand)) {
                return;
            }
            ItemStack held = player.getItemInHand(hand);
            if (held.isEmpty()) {
                return;
            }
            ItemStack toDrop = held.copy();
            player.setItemInHand(hand, ItemStack.EMPTY);
            player.drop(toDrop, true);
            player.inventoryMenu.broadcastChanges();
        }

        @SubscribeEvent(priority=EventPriority.HIGHEST)
        public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
            boolean rightDead;
            Player player = event.getEntity();
            PlayerCyberwareData data = (PlayerCyberwareData)player.getData(ModAttachments.CYBERWARE);
            if (data == null) {
                return;
            }
            boolean leftDead = PowerFailHooks.isCyberarmUnpowered(player, data, CyberwareSlot.LARM);
            if (!PowerFailHooks.isHandDisabled(player, leftDead, rightDead = PowerFailHooks.isCyberarmUnpowered(player, data, CyberwareSlot.RARM), event.getHand())) {
                return;
            }
            event.setCanceled(true);
            event.setCancellationResult(InteractionResult.FAIL);
        }

        @SubscribeEvent(priority=EventPriority.HIGHEST)
        public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
            boolean rightDead;
            Player player = event.getEntity();
            PlayerCyberwareData data = (PlayerCyberwareData)player.getData(ModAttachments.CYBERWARE);
            if (data == null) {
                return;
            }
            boolean leftDead = PowerFailHooks.isCyberarmUnpowered(player, data, CyberwareSlot.LARM);
            if (!PowerFailHooks.isHandDisabled(player, leftDead, rightDead = PowerFailHooks.isCyberarmUnpowered(player, data, CyberwareSlot.RARM), event.getHand())) {
                return;
            }
            event.setCanceled(true);
            event.setCancellationResult(InteractionResult.FAIL);
        }

        @SubscribeEvent(priority=EventPriority.HIGHEST)
        public static void onUseItemStart(LivingEntityUseItemEvent.Start event) {
            LivingEntity livingEntity = event.getEntity();
            if (!(livingEntity instanceof Player)) {
                return;
            }
            Player player = (Player)livingEntity;
            PlayerCyberwareData data = (PlayerCyberwareData)player.getData(ModAttachments.CYBERWARE);
            if (data == null) {
                return;
            }
            boolean leftDead = PowerFailHooks.isCyberarmUnpowered(player, data, CyberwareSlot.LARM);
            boolean rightDead = PowerFailHooks.isCyberarmUnpowered(player, data, CyberwareSlot.RARM);
            if (!leftDead && !rightDead) {
                return;
            }
            InteractionHand hand = PowerFailHooks.handHolding(player, event.getItem());
            if (hand != null && PowerFailHooks.isHandDisabled(player, leftDead, rightDead, hand)) {
                event.setCanceled(true);
            }
        }

        private static InteractionHand handHolding(Player player, ItemStack stack) {
            if (ItemStack.isSameItemSameComponents((ItemStack)stack, (ItemStack)player.getMainHandItem())) {
                return InteractionHand.MAIN_HAND;
            }
            if (ItemStack.isSameItemSameComponents((ItemStack)stack, (ItemStack)player.getOffhandItem())) {
                return InteractionHand.OFF_HAND;
            }
            return null;
        }

        @SubscribeEvent(priority=EventPriority.HIGHEST)
        public static void onAttack(AttackEntityEvent event) {
            Player player = event.getEntity();
            PlayerCyberwareData data = (PlayerCyberwareData)player.getData(ModAttachments.CYBERWARE);
            if (data == null) {
                return;
            }
            boolean leftDead = PowerFailHooks.isCyberarmUnpowered(player, data, CyberwareSlot.LARM);
            boolean rightDead = PowerFailHooks.isCyberarmUnpowered(player, data, CyberwareSlot.RARM);
            if (!leftDead && !rightDead) {
                return;
            }
            if (PowerFailHooks.isHandDisabled(player, leftDead, rightDead, InteractionHand.MAIN_HAND)) {
                event.setCanceled(true);
            }
        }

        @SubscribeEvent(priority=EventPriority.HIGHEST)
        public static void onBreakSpeed(PlayerEvent.BreakSpeed event) {
            boolean rightDead;
            Player player = event.getEntity();
            PlayerCyberwareData data = (PlayerCyberwareData)player.getData(ModAttachments.CYBERWARE);
            if (data == null) {
                return;
            }
            boolean leftDead = PowerFailHooks.isCyberarmUnpowered(player, data, CyberwareSlot.LARM);
            if (!PowerFailHooks.isHandDisabled(player, leftDead, rightDead = PowerFailHooks.isCyberarmUnpowered(player, data, CyberwareSlot.RARM), InteractionHand.MAIN_HAND)) {
                return;
            }
            event.setNewSpeed(0.0f);
        }

        @SubscribeEvent(priority=EventPriority.HIGHEST)
        public static void onBreakBlock(BlockEvent.BreakEvent event) {
            boolean rightDead;
            Player player = event.getPlayer();
            if (player == null) {
                return;
            }
            PlayerCyberwareData data = (PlayerCyberwareData)player.getData(ModAttachments.CYBERWARE);
            if (data == null) {
                return;
            }
            boolean leftDead = PowerFailHooks.isCyberarmUnpowered(player, data, CyberwareSlot.LARM);
            if (!PowerFailHooks.isHandDisabled(player, leftDead, rightDead = PowerFailHooks.isCyberarmUnpowered(player, data, CyberwareSlot.RARM), InteractionHand.MAIN_HAND)) {
                return;
            }
            event.setCanceled(true);
        }

        private static boolean isHandDisabled(Player player, boolean leftDead, boolean rightDead, InteractionHand hand) {
            HumanoidArm arm = PowerFailHooks.armForHand(player, hand);
            return arm == HumanoidArm.LEFT && leftDead || arm == HumanoidArm.RIGHT && rightDead;
        }

        private static HumanoidArm armForHand(Player player, InteractionHand hand) {
            HumanoidArm main = player.getMainArm();
            if (hand == InteractionHand.MAIN_HAND) {
                return main;
            }
            return main == HumanoidArm.RIGHT ? HumanoidArm.LEFT : HumanoidArm.RIGHT;
        }

        private static boolean isCyberarmUnpowered(Player player, PlayerCyberwareData data, CyberwareSlot armSlot) {
            InstalledCyberware[] arr = data.getAll().get((Object)armSlot);
            if (arr == null) {
                return false;
            }
            for (int idx = 0; idx < arr.length; ++idx) {
                Item item;
                ItemStack st;
                InstalledCyberware installed = arr[idx];
                if (installed == null || (st = installed.getItem()) == null || st.isEmpty() || !((item = st.getItem()) instanceof CyberarmItem)) continue;
                CyberarmItem cyberarm = (CyberarmItem)item;
                if (cyberarm.side != armSlot || !data.isEnabled(armSlot, idx)) continue;
                if (!cyberarm.requiresEnergyToFunction(player, st, armSlot)) {
                    return false;
                }
                return !PowerFailHooks.readInstalledPowered(installed);
            }
            return false;
        }

        private static boolean readInstalledPowered(Object installedCyberware) {
            try {
                Object v;
                Class<?> cls = installedCyberware.getClass();
                Method m = POWER_METHOD_CACHE.computeIfAbsent(cls, c -> {
                    Method found = PowerFailHooks.findBoolMethod(c, "isPowered", "getPowered", "powered");
                    if (found != null) {
                        found.setAccessible(true);
                    }
                    return found;
                });
                if (m != null && (v = m.invoke(installedCyberware, new Object[0])) instanceof Boolean) {
                    Boolean b = (Boolean)v;
                    return b;
                }
                Field f = POWER_FIELD_CACHE.computeIfAbsent(cls, c -> {
                    Field found = PowerFailHooks.findBoolField(c, "powered", "isPowered", "poweredFlag");
                    if (found != null) {
                        found.setAccessible(true);
                    }
                    return found;
                });
                if (f != null) {
                    return f.getBoolean(installedCyberware);
                }
                return true;
            }
            catch (Throwable t) {
                return true;
            }
        }

        private static Method findBoolMethod(Class<?> cls, String ... names) {
            for (String n : names) {
                Method m2;
                try {
                    m2 = cls.getDeclaredMethod(n, new Class[0]);
                    if (m2.getReturnType() == Boolean.TYPE || m2.getReturnType() == Boolean.class) {
                        return m2;
                    }
                }
                catch (NoSuchMethodException m2) {
                    // empty catch block
                }
                try {
                    m2 = cls.getMethod(n, new Class[0]);
                    if (m2.getReturnType() != Boolean.TYPE && m2.getReturnType() != Boolean.class) continue;
                    return m2;
                }
                catch (NoSuchMethodException noSuchMethodException) {
                    // empty catch block
                }
            }
            return null;
        }

        private static Field findBoolField(Class<?> cls, String ... names) {
            for (String n : names) {
                try {
                    Field f = cls.getDeclaredField(n);
                    if (f.getType() != Boolean.TYPE && f.getType() != Boolean.class) continue;
                    return f;
                }
                catch (NoSuchFieldException noSuchFieldException) {
                    // empty catch block
                }
            }
            return null;
        }
    }
}

