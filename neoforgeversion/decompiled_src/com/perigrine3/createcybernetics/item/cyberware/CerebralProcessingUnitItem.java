/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.systems.RenderSystem
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.client.player.Input
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.effect.MobEffects
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.item.Item$TooltipContext
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.TooltipFlag
 *  net.minecraft.world.phys.Vec3
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.bus.api.EventPriority
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.fml.common.EventBusSubscriber$Bus
 *  net.neoforged.neoforge.client.event.ClientTickEvent$Pre
 *  net.neoforged.neoforge.client.event.InputEvent$InteractionKeyMappingTriggered
 *  net.neoforged.neoforge.client.event.MovementInputUpdateEvent
 *  net.neoforged.neoforge.client.event.RenderGuiEvent$Pre
 *  net.neoforged.neoforge.event.entity.player.AttackEntityEvent
 *  net.neoforged.neoforge.event.entity.player.PlayerEvent$BreakSpeed
 *  net.neoforged.neoforge.event.entity.player.PlayerEvent$Clone
 *  net.neoforged.neoforge.event.entity.player.PlayerEvent$PlayerRespawnEvent
 *  net.neoforged.neoforge.event.entity.player.PlayerInteractEvent$EntityInteract
 *  net.neoforged.neoforge.event.entity.player.PlayerInteractEvent$EntityInteractSpecific
 *  net.neoforged.neoforge.event.entity.player.PlayerInteractEvent$LeftClickBlock
 *  net.neoforged.neoforge.event.entity.player.PlayerInteractEvent$RightClickBlock
 *  net.neoforged.neoforge.event.entity.player.PlayerInteractEvent$RightClickItem
 *  net.neoforged.neoforge.event.tick.PlayerTickEvent$Post
 *  net.neoforged.neoforge.event.tick.PlayerTickEvent$Pre
 *  net.neoforged.neoforge.network.PacketDistributor
 */
package com.perigrine3.createcybernetics.item.cyberware;

import com.mojang.blaze3d.systems.RenderSystem;
import com.perigrine3.createcybernetics.advancement.ModCriteria;
import com.perigrine3.createcybernetics.advancement.triggers.ThoughtsNotFoundTrigger;
import com.perigrine3.createcybernetics.api.CyberwareSlot;
import com.perigrine3.createcybernetics.api.ICyberwareItem;
import com.perigrine3.createcybernetics.api.InstalledCyberware;
import com.perigrine3.createcybernetics.common.capabilities.ModAttachments;
import com.perigrine3.createcybernetics.common.capabilities.PlayerCyberwareData;
import com.perigrine3.createcybernetics.item.cyberware.CybereyeItem;
import com.perigrine3.createcybernetics.network.payload.CerebralShutdownStatePayload;
import com.perigrine3.createcybernetics.util.CyberwareAttributeHelper;
import java.util.List;
import java.util.Set;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.Input;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.MovementInputUpdateEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

public class CerebralProcessingUnitItem
extends Item
implements ICyberwareItem {
    private final int humanityCost;
    private static final int ENERGY_PER_TICK = 5;
    private static volatile boolean CLIENT_SHUTDOWN_ACTIVE = false;
    private static final String NBT_SHUTDOWN_ACTIVE = "cc_cpu_shutdown_active";
    private static final String NBT_ANCHOR_SET = "cc_cpu_shutdown_anchor";
    private static final String NBT_AX = "cc_cpu_shutdown_ax";
    private static final String NBT_AY = "cc_cpu_shutdown_ay";
    private static final String NBT_AZ = "cc_cpu_shutdown_az";
    private static final String NBT_AYAW = "cc_cpu_shutdown_yaw";
    private static final String NBT_APITCH = "cc_cpu_shutdown_pitch";

    public static void setClientShutdownActive(boolean active) {
        CLIENT_SHUTDOWN_ACTIVE = active;
    }

    public static boolean clientShutdownActive() {
        return CLIENT_SHUTDOWN_ACTIVE;
    }

    public CerebralProcessingUnitItem(Item.Properties props, int humanityCost) {
        super(props);
        this.humanityCost = humanityCost;
    }

    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        if (Screen.hasShiftDown()) {
            tooltip.add((Component)Component.translatable((String)"tooltip.createcybernetics.humanity", (Object[])new Object[]{this.humanityCost}).withStyle(ChatFormatting.GOLD));
            tooltip.add((Component)Component.translatable((String)"tooltip.createcybernetics.brainupgrades_cyberbrain.energy").withStyle(ChatFormatting.RED));
        }
    }

    @Override
    public int getHumanityCost() {
        return this.humanityCost;
    }

    @Override
    public Set<CyberwareSlot> getSupportedSlots() {
        return Set.of(CyberwareSlot.BRAIN);
    }

    @Override
    public boolean replacesOrgan() {
        return true;
    }

    @Override
    public Set<CyberwareSlot> getReplacedOrgans() {
        return Set.of(CyberwareSlot.BRAIN);
    }

    @Override
    public int getEnergyUsedPerTick(Player player, ItemStack installedStack, CyberwareSlot slot) {
        return 5;
    }

    @Override
    public void onInstalled(Player player) {
        CyberwareAttributeHelper.applyModifier((LivingEntity)player, "cyberbrain_learn");
        CyberwareAttributeHelper.applyModifier((LivingEntity)player, "cyberbrain_insomnia");
    }

    @Override
    public void onRemoved(Player player) {
        CyberwareAttributeHelper.removeModifier((LivingEntity)player, "cyberbrain_learn");
        CyberwareAttributeHelper.removeModifier((LivingEntity)player, "cyberbrain_insomnia");
    }

    @Override
    public void onTick(Player player, ItemStack installedStack, CyberwareSlot slot, int index) {
    }

    @Override
    public void onTick(Player player) {
    }

    private static boolean cpuInstalledEnabledAndUnpowered(PlayerCyberwareData data) {
        InstalledCyberware[] arr = data.getAll().get((Object)CyberwareSlot.BRAIN);
        if (arr == null) {
            return false;
        }
        for (int idx = 0; idx < arr.length; ++idx) {
            ItemStack st;
            InstalledCyberware installed = arr[idx];
            if (installed == null || (st = installed.getItem()) == null || st.isEmpty() || !(st.getItem() instanceof CerebralProcessingUnitItem) || !data.isEnabled(CyberwareSlot.BRAIN, idx)) continue;
            return !installed.isPowered();
        }
        return false;
    }

    private static boolean clientOverlayActive(Player player) {
        PlayerCyberwareData data;
        if (player == null) {
            return false;
        }
        if (CLIENT_SHUTDOWN_ACTIVE) {
            return true;
        }
        PlayerCyberwareData playerCyberwareData = data = player.hasData(ModAttachments.CYBERWARE) ? (PlayerCyberwareData)player.getData(ModAttachments.CYBERWARE) : null;
        if (data == null) {
            return false;
        }
        boolean eyesEnabled = CerebralProcessingUnitItem.hasCybereyesInstalledAndEnabled(data);
        if (!eyesEnabled) {
            return false;
        }
        return player.hasEffect(MobEffects.BLINDNESS) || player.hasEffect(MobEffects.DARKNESS);
    }

    private static boolean hasCybereyesInstalledAndEnabled(PlayerCyberwareData data) {
        InstalledCyberware[] arr = data.getAll().get((Object)CyberwareSlot.EYES);
        if (arr == null) {
            return false;
        }
        for (int idx = 0; idx < arr.length; ++idx) {
            ItemStack st;
            InstalledCyberware installed = arr[idx];
            if (installed == null || (st = installed.getItem()) == null || st.isEmpty() || !(st.getItem() instanceof CybereyeItem) || !data.isEnabled(CyberwareSlot.EYES, idx)) continue;
            return true;
        }
        return false;
    }

    @EventBusSubscriber(modid="createcybernetics", bus=EventBusSubscriber.Bus.GAME)
    public static final class ShutdownResetHooks {
        private ShutdownResetHooks() {
        }

        @SubscribeEvent
        public static void onClone(PlayerEvent.Clone event) {
            if (!event.isWasDeath()) {
                return;
            }
            Player player = event.getEntity();
            if (!(player instanceof ServerPlayer)) {
                return;
            }
            ServerPlayer sp = (ServerPlayer)player;
            ShutdownResetHooks.clearShutdownState(sp);
            PacketDistributor.sendToPlayer((ServerPlayer)sp, (CustomPacketPayload)new CerebralShutdownStatePayload(false), (CustomPacketPayload[])new CustomPacketPayload[0]);
        }

        @SubscribeEvent
        public static void onRespawn(PlayerEvent.PlayerRespawnEvent event) {
            Player player = event.getEntity();
            if (!(player instanceof ServerPlayer)) {
                return;
            }
            ServerPlayer sp = (ServerPlayer)player;
            ShutdownResetHooks.clearShutdownState(sp);
            PacketDistributor.sendToPlayer((ServerPlayer)sp, (CustomPacketPayload)new CerebralShutdownStatePayload(false), (CustomPacketPayload[])new CustomPacketPayload[0]);
        }

        private static void clearShutdownState(ServerPlayer sp) {
            CompoundTag pt = sp.getPersistentData();
            pt.putBoolean(CerebralProcessingUnitItem.NBT_SHUTDOWN_ACTIVE, false);
            pt.remove(CerebralProcessingUnitItem.NBT_ANCHOR_SET);
            pt.remove(CerebralProcessingUnitItem.NBT_AX);
            pt.remove(CerebralProcessingUnitItem.NBT_AY);
            pt.remove(CerebralProcessingUnitItem.NBT_AZ);
            pt.remove(CerebralProcessingUnitItem.NBT_AYAW);
            pt.remove(CerebralProcessingUnitItem.NBT_APITCH);
        }
    }

    @EventBusSubscriber(modid="createcybernetics", value={Dist.CLIENT}, bus=EventBusSubscriber.Bus.GAME)
    public static final class ShutdownClientHooks {
        private static boolean anchorSet = false;
        private static float anchorYaw;
        private static float anchorPitch;

        private ShutdownClientHooks() {
        }

        @SubscribeEvent
        public static void onMove(MovementInputUpdateEvent event) {
            if (!CLIENT_SHUTDOWN_ACTIVE) {
                return;
            }
            Input in = event.getInput();
            in.leftImpulse = 0.0f;
            in.forwardImpulse = 0.0f;
            in.up = false;
            in.down = false;
            in.left = false;
            in.right = false;
            in.jumping = false;
            in.shiftKeyDown = false;
        }

        @SubscribeEvent
        public static void onInteract(InputEvent.InteractionKeyMappingTriggered event) {
            if (!CLIENT_SHUTDOWN_ACTIVE) {
                return;
            }
            event.setCanceled(true);
        }

        @SubscribeEvent
        public static void onClientTick(ClientTickEvent.Pre event) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player == null) {
                return;
            }
            if (!CLIENT_SHUTDOWN_ACTIVE) {
                anchorSet = false;
                return;
            }
            if (!anchorSet) {
                anchorSet = true;
                anchorYaw = mc.player.getYRot();
                anchorPitch = mc.player.getXRot();
            }
            mc.player.setYRot(anchorYaw);
            mc.player.setXRot(anchorPitch);
            mc.player.yRotO = anchorYaw;
            mc.player.xRotO = anchorPitch;
            mc.player.yHeadRot = anchorYaw;
            mc.player.yHeadRotO = anchorYaw;
            mc.player.yBodyRot = anchorYaw;
            mc.player.yBodyRotO = anchorYaw;
        }

        @SubscribeEvent
        public static void onRenderGuiPre(RenderGuiEvent.Pre event) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player == null) {
                return;
            }
            if (mc.screen != null) {
                return;
            }
            if (!CerebralProcessingUnitItem.clientOverlayActive((Player)mc.player)) {
                return;
            }
            GuiGraphics gg = event.getGuiGraphics();
            int w = mc.getWindow().getGuiScaledWidth();
            int h = mc.getWindow().getGuiScaledHeight();
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            gg.fill(0, 0, w, h, -16777216);
            RenderSystem.disableBlend();
        }
    }

    @EventBusSubscriber(modid="createcybernetics", bus=EventBusSubscriber.Bus.GAME)
    public static final class ShutdownServerEnforce {
        private ShutdownServerEnforce() {
        }

        @SubscribeEvent(priority=EventPriority.HIGHEST)
        public static void onTickPre(PlayerTickEvent.Pre event) {
            Player p = event.getEntity();
            if (p.level().isClientSide) {
                return;
            }
            if (!(p instanceof ServerPlayer)) {
                return;
            }
            ServerPlayer sp = (ServerPlayer)p;
            CompoundTag pt = sp.getPersistentData();
            if (!pt.getBoolean(CerebralProcessingUnitItem.NBT_SHUTDOWN_ACTIVE)) {
                return;
            }
            if (!pt.getBoolean(CerebralProcessingUnitItem.NBT_ANCHOR_SET)) {
                pt.putBoolean(CerebralProcessingUnitItem.NBT_ANCHOR_SET, true);
                pt.putDouble(CerebralProcessingUnitItem.NBT_AX, sp.getX());
                pt.putDouble(CerebralProcessingUnitItem.NBT_AY, sp.getY());
                pt.putDouble(CerebralProcessingUnitItem.NBT_AZ, sp.getZ());
                pt.putFloat(CerebralProcessingUnitItem.NBT_AYAW, sp.getYRot());
                pt.putFloat(CerebralProcessingUnitItem.NBT_APITCH, sp.getXRot());
            }
            double ax = pt.getDouble(CerebralProcessingUnitItem.NBT_AX);
            double ay = pt.getDouble(CerebralProcessingUnitItem.NBT_AY);
            double az = pt.getDouble(CerebralProcessingUnitItem.NBT_AZ);
            float yaw = pt.getFloat(CerebralProcessingUnitItem.NBT_AYAW);
            float pitch = pt.getFloat(CerebralProcessingUnitItem.NBT_APITCH);
            sp.connection.teleport(ax, ay, az, yaw, pitch);
            sp.setDeltaMovement(Vec3.ZERO);
            sp.fallDistance = 0.0f;
            sp.setSprinting(false);
            sp.stopUsingItem();
        }

        @SubscribeEvent
        public static void onAttackEntity(AttackEntityEvent event) {
            Player player = event.getEntity();
            if (!(player instanceof ServerPlayer)) {
                return;
            }
            ServerPlayer sp = (ServerPlayer)player;
            if (sp.getPersistentData().getBoolean(CerebralProcessingUnitItem.NBT_SHUTDOWN_ACTIVE)) {
                event.setCanceled(true);
            }
        }

        @SubscribeEvent
        public static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
            Player player = event.getEntity();
            if (!(player instanceof ServerPlayer)) {
                return;
            }
            ServerPlayer sp = (ServerPlayer)player;
            if (sp.getPersistentData().getBoolean(CerebralProcessingUnitItem.NBT_SHUTDOWN_ACTIVE)) {
                event.setCanceled(true);
            }
        }

        @SubscribeEvent
        public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
            Player player = event.getEntity();
            if (!(player instanceof ServerPlayer)) {
                return;
            }
            ServerPlayer sp = (ServerPlayer)player;
            if (sp.getPersistentData().getBoolean(CerebralProcessingUnitItem.NBT_SHUTDOWN_ACTIVE)) {
                event.setCanceled(true);
            }
        }

        @SubscribeEvent
        public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
            Player player = event.getEntity();
            if (!(player instanceof ServerPlayer)) {
                return;
            }
            ServerPlayer sp = (ServerPlayer)player;
            if (sp.getPersistentData().getBoolean(CerebralProcessingUnitItem.NBT_SHUTDOWN_ACTIVE)) {
                event.setCanceled(true);
            }
        }

        @SubscribeEvent
        public static void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
            Player player = event.getEntity();
            if (!(player instanceof ServerPlayer)) {
                return;
            }
            ServerPlayer sp = (ServerPlayer)player;
            if (sp.getPersistentData().getBoolean(CerebralProcessingUnitItem.NBT_SHUTDOWN_ACTIVE)) {
                event.setCanceled(true);
            }
        }

        @SubscribeEvent
        public static void onEntityInteractSpecific(PlayerInteractEvent.EntityInteractSpecific event) {
            Player player = event.getEntity();
            if (!(player instanceof ServerPlayer)) {
                return;
            }
            ServerPlayer sp = (ServerPlayer)player;
            if (sp.getPersistentData().getBoolean(CerebralProcessingUnitItem.NBT_SHUTDOWN_ACTIVE)) {
                event.setCanceled(true);
            }
        }

        @SubscribeEvent
        public static void onBreakSpeed(PlayerEvent.BreakSpeed event) {
            Player player = event.getEntity();
            if (!(player instanceof ServerPlayer)) {
                return;
            }
            ServerPlayer sp = (ServerPlayer)player;
            if (sp.getPersistentData().getBoolean(CerebralProcessingUnitItem.NBT_SHUTDOWN_ACTIVE)) {
                event.setNewSpeed(0.0f);
            }
        }
    }

    @EventBusSubscriber(modid="createcybernetics", bus=EventBusSubscriber.Bus.GAME)
    public static final class ShutdownServerDecision {
        private ShutdownServerDecision() {
        }

        @SubscribeEvent(priority=EventPriority.LOWEST)
        public static void onTickPost(PlayerTickEvent.Post event) {
            CompoundTag pt;
            boolean prev;
            Player p = event.getEntity();
            if (p.level().isClientSide) {
                return;
            }
            if (!(p instanceof ServerPlayer)) {
                return;
            }
            ServerPlayer sp = (ServerPlayer)p;
            if (!sp.hasData(ModAttachments.CYBERWARE)) {
                return;
            }
            PlayerCyberwareData data = (PlayerCyberwareData)sp.getData(ModAttachments.CYBERWARE);
            if (data == null) {
                return;
            }
            boolean shutdownNow = CerebralProcessingUnitItem.cpuInstalledEnabledAndUnpowered(data);
            if (shutdownNow != (prev = (pt = sp.getPersistentData()).getBoolean(CerebralProcessingUnitItem.NBT_SHUTDOWN_ACTIVE))) {
                pt.putBoolean(CerebralProcessingUnitItem.NBT_SHUTDOWN_ACTIVE, shutdownNow);
                if (shutdownNow) {
                    ((ThoughtsNotFoundTrigger)((Object)ModCriteria.THOUGHTS_NOT_FOUND.get())).trigger(sp);
                }
                if (!shutdownNow) {
                    pt.remove(CerebralProcessingUnitItem.NBT_ANCHOR_SET);
                } else {
                    pt.putBoolean(CerebralProcessingUnitItem.NBT_ANCHOR_SET, false);
                }
                PacketDistributor.sendToPlayer((ServerPlayer)sp, (CustomPacketPayload)new CerebralShutdownStatePayload(shutdownNow), (CustomPacketPayload[])new CustomPacketPayload[0]);
            }
        }
    }
}

