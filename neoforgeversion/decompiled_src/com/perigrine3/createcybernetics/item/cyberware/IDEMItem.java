/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.registries.Registries
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.nbt.Tag
 *  net.minecraft.network.chat.Component
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.tags.TagKey
 *  net.minecraft.util.Mth
 *  net.minecraft.world.entity.EntityDimensions
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.Pose
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.item.Item$TooltipContext
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.TooltipFlag
 *  net.minecraft.world.level.BlockGetter
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.AABB
 *  net.minecraft.world.phys.Vec3
 *  net.neoforged.bus.api.EventPriority
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.fml.common.EventBusSubscriber$Bus
 *  net.neoforged.neoforge.event.entity.living.LivingDeathEvent
 *  net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent
 */
package com.perigrine3.createcybernetics.item.cyberware;

import com.perigrine3.createcybernetics.api.CyberwareSlot;
import com.perigrine3.createcybernetics.api.ICyberwareItem;
import com.perigrine3.createcybernetics.api.InstalledCyberware;
import com.perigrine3.createcybernetics.common.capabilities.ModAttachments;
import com.perigrine3.createcybernetics.common.capabilities.PlayerCyberwareData;
import com.perigrine3.createcybernetics.item.ModItems;
import com.perigrine3.createcybernetics.util.ModTags;
import java.util.List;
import java.util.Set;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

public class IDEMItem
extends Item
implements ICyberwareItem {
    private final int humanityCost;
    private static final int ACTIVATION_COST = 50;
    private static final int RETURN_DELAY_TICKS = 300;
    private static final int COOLDOWN_TICKS = 600;
    private static final String NBT_ROOT = "cc_idem";
    private static final String NBT_ACTIVE = "active";
    private static final String NBT_RETURN_TICKS = "return_ticks";
    private static final String NBT_ORIGIN_DIM = "origin_dim";
    private static final String NBT_COOLDOWN = "cooldown_ticks";

    public IDEMItem(Item.Properties props, int humanityCost) {
        super(props);
        this.humanityCost = humanityCost;
    }

    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        if (Screen.hasShiftDown()) {
            tooltip.add((Component)Component.translatable((String)"tooltip.createcybernetics.brainupgrades_idem.tooltip3").withStyle(ChatFormatting.DARK_GRAY));
            tooltip.add((Component)Component.translatable((String)"tooltip.createcybernetics.humanity", (Object[])new Object[]{this.humanityCost}).withStyle(ChatFormatting.GOLD));
            tooltip.add((Component)Component.translatable((String)"tooltip.createcybernetics.brainupgrades_idem.energy").withStyle(ChatFormatting.RED));
        }
    }

    @Override
    public int getHumanityCost() {
        return this.humanityCost;
    }

    @Override
    public Set<TagKey<Item>> requiresCyberwareTags(ItemStack installedStack, CyberwareSlot slot) {
        return Set.of(ModTags.Items.BRAIN_ITEMS);
    }

    @Override
    public Set<CyberwareSlot> getSupportedSlots() {
        return Set.of(CyberwareSlot.BRAIN);
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
    public void onRemoved(Player player) {
        if (player instanceof ServerPlayer) {
            ServerPlayer sp = (ServerPlayer)player;
            IDEMItem.clearPending(sp);
        }
    }

    @Override
    public void onTick(Player player) {
        if (!(player instanceof ServerPlayer)) {
            return;
        }
        ServerPlayer sp = (ServerPlayer)player;
        IDEMItem.tickCooldown(sp);
        IDEMItem.tickReturnCountdown(sp);
    }

    private static void tickCooldown(ServerPlayer player) {
        CompoundTag root = IDEMItem.getOrCreateRoot(player, false);
        if (root == null) {
            return;
        }
        int cd = root.getInt(NBT_COOLDOWN);
        if (cd > 0) {
            root.putInt(NBT_COOLDOWN, cd - 1);
        }
    }

    private static int getCooldownTicks(ServerPlayer player) {
        CompoundTag root = IDEMItem.getOrCreateRoot(player, false);
        return root == null ? 0 : Math.max(0, root.getInt(NBT_COOLDOWN));
    }

    private static void setCooldownTicks(ServerPlayer player, int ticks) {
        CompoundTag root = IDEMItem.getOrCreateRoot(player, true);
        root.putInt(NBT_COOLDOWN, Math.max(0, ticks));
    }

    private static void tickReturnCountdown(ServerPlayer player) {
        if (!player.isAlive()) {
            IDEMItem.clearPending(player);
            return;
        }
        CompoundTag root = IDEMItem.getOrCreateRoot(player, false);
        if (root == null) {
            return;
        }
        if (!root.getBoolean(NBT_ACTIVE)) {
            return;
        }
        if (!IDEMItem.stillHasIdemInstalled(player)) {
            IDEMItem.clearPending(player);
            return;
        }
        int ticksLeft = root.getInt(NBT_RETURN_TICKS);
        if (ticksLeft > 0) {
            root.putInt(NBT_RETURN_TICKS, ticksLeft - 1);
            return;
        }
        if (!player.hasData(ModAttachments.CYBERWARE)) {
            return;
        }
        PlayerCyberwareData data = (PlayerCyberwareData)player.getData(ModAttachments.CYBERWARE);
        if (data == null) {
            return;
        }
        if (!data.tryConsumeEnergy(50)) {
            root.putInt(NBT_RETURN_TICKS, 0);
            return;
        }
        ResourceKey<Level> origin = IDEMItem.readOriginDim(root);
        if (origin == null) {
            IDEMItem.clearPending(player);
            return;
        }
        ServerLevel target = player.server.getLevel(origin);
        if (target == null) {
            IDEMItem.clearPending(player);
            return;
        }
        ServerLevel current = player.serverLevel();
        if (!current.dimension().equals(origin)) {
            IDEMItem.teleportMapped(player, current, target);
        }
        IDEMItem.clearPending(player);
    }

    private static boolean stillHasIdemInstalled(ServerPlayer player) {
        if (!player.hasData(ModAttachments.CYBERWARE)) {
            return false;
        }
        PlayerCyberwareData data = (PlayerCyberwareData)player.getData(ModAttachments.CYBERWARE);
        if (data == null) {
            return false;
        }
        InstalledCyberware[] arr = data.getAll().get((Object)CyberwareSlot.BRAIN);
        if (arr == null) {
            return false;
        }
        Item idemItem = (Item)ModItems.BRAINUPGRADES_IDEM.get();
        for (InstalledCyberware cw : arr) {
            ItemStack st;
            if (cw == null || (st = cw.getItem()) == null || st.isEmpty() || st.getItem() != idemItem) continue;
            return true;
        }
        return false;
    }

    private static void armReturn(ServerPlayer player, ResourceKey<Level> origin, int ticksFromNow) {
        CompoundTag root = IDEMItem.getOrCreateRoot(player, true);
        root.putBoolean(NBT_ACTIVE, true);
        root.putInt(NBT_RETURN_TICKS, Math.max(0, ticksFromNow));
        root.putString(NBT_ORIGIN_DIM, origin.location().toString());
    }

    private static void clearPending(ServerPlayer player) {
        CompoundTag root = IDEMItem.getOrCreateRoot(player, false);
        if (root == null) {
            return;
        }
        root.putBoolean(NBT_ACTIVE, false);
        root.remove(NBT_RETURN_TICKS);
        root.remove(NBT_ORIGIN_DIM);
        int cd = root.getInt(NBT_COOLDOWN);
        if (cd <= 0) {
            // empty if block
        }
    }

    private static boolean isActive(ServerPlayer player) {
        CompoundTag root = IDEMItem.getOrCreateRoot(player, false);
        return root != null && root.getBoolean(NBT_ACTIVE);
    }

    private static CompoundTag getOrCreateRoot(ServerPlayer player, boolean create) {
        CompoundTag pdata = player.getPersistentData();
        if (!pdata.contains(NBT_ROOT)) {
            if (!create) {
                return null;
            }
            pdata.put(NBT_ROOT, (Tag)new CompoundTag());
        }
        return pdata.getCompound(NBT_ROOT);
    }

    private static ResourceKey<Level> readOriginDim(CompoundTag root) {
        if (!root.contains(NBT_ORIGIN_DIM)) {
            return null;
        }
        ResourceLocation rl = ResourceLocation.tryParse((String)root.getString(NBT_ORIGIN_DIM));
        if (rl == null) {
            return null;
        }
        return ResourceKey.create((ResourceKey)Registries.DIMENSION, (ResourceLocation)rl);
    }

    private static ResourceKey<Level> chooseDestination(ResourceKey<Level> origin, double roll01) {
        if (origin.equals((Object)Level.NETHER) || origin.equals((Object)Level.END)) {
            return Level.OVERWORLD;
        }
        return roll01 < 0.98 ? Level.NETHER : Level.END;
    }

    private static void teleportMapped(ServerPlayer player, ServerLevel from, ServerLevel to) {
        VecXZ mapped = IDEMItem.mapXZ((ResourceKey<Level>)from.dimension(), (ResourceKey<Level>)to.dimension(), player.getX(), player.getZ());
        double y = IDEMItem.findSafeYByCollision(player, to, mapped.x, mapped.z);
        boolean ok = player.teleportTo(to, mapped.x, y, mapped.z, Set.of(), player.getYRot(), player.getXRot());
        if (ok) {
            player.fallDistance = 0.0f;
            player.setDeltaMovement(Vec3.ZERO);
        }
    }

    private static VecXZ mapXZ(ResourceKey<Level> fromDim, ResourceKey<Level> toDim, double x, double z) {
        boolean fromNether = fromDim.equals((Object)Level.NETHER);
        boolean toNether = toDim.equals((Object)Level.NETHER);
        if (fromNether && !toNether) {
            return new VecXZ(x * 8.0, z * 8.0);
        }
        if (!fromNether && toNether) {
            return new VecXZ(x / 8.0, z / 8.0);
        }
        return new VecXZ(x, z);
    }

    private static double findSafeYByCollision(ServerPlayer player, ServerLevel level, double x, double z) {
        int minY = level.getMinBuildHeight() + 1;
        int maxY = level.getMaxBuildHeight() - 2;
        for (int y = minY; y <= maxY; ++y) {
            double yy = (double)y + 0.01;
            if (!IDEMItem.isStandingSpotSafeAt(player, level, x, yy, z)) continue;
            return yy;
        }
        return Mth.clamp((double)player.getY(), (double)minY, (double)maxY);
    }

    private static boolean isStandingSpotSafeAt(ServerPlayer player, ServerLevel level, double x, double y, double z) {
        EntityDimensions dims = player.getDimensions(Pose.STANDING);
        double w = dims.width();
        double h = dims.height();
        BlockPos feet = BlockPos.containing((double)x, (double)y, (double)z);
        BlockPos below = feet.below();
        if (level.getBlockState(below).getCollisionShape((BlockGetter)level, below).isEmpty()) {
            return false;
        }
        double jitter = Math.max(0.05, w * 0.5 * 0.25);
        double[] dx = new double[]{0.0, jitter, -jitter, 0.0, 0.0};
        double[] dz = new double[]{0.0, 0.0, 0.0, jitter, -jitter};
        for (int i = 0; i < dx.length; ++i) {
            double xx = x + dx[i];
            double zz = z + dz[i];
            AABB aabb = new AABB(xx - w / 2.0, y, zz - w / 2.0, xx + w / 2.0, y + h, zz + w / 2.0);
            if (!level.noCollision(aabb)) {
                return false;
            }
            if (level.containsAnyLiquid(aabb)) {
                return false;
            }
            if (level.getWorldBorder().isWithinBounds(BlockPos.containing((double)xx, (double)y, (double)zz))) continue;
            return false;
        }
        return true;
    }

    private static void nudgeOutOfBlocks(ServerPlayer player) {
        ServerLevel level = player.serverLevel();
        if (level.noCollision(player.getBoundingBox()) && !level.containsAnyLiquid(player.getBoundingBox())) {
            return;
        }
        for (int dy = 1; dy <= 12; ++dy) {
            double ny = player.getY() + (double)dy;
            AABB moved = player.getBoundingBox().move(0.0, (double)dy, 0.0);
            if (!level.noCollision(moved) || level.containsAnyLiquid(moved)) continue;
            player.teleportTo(level, player.getX(), ny, player.getZ(), Set.of(), player.getYRot(), player.getXRot());
            player.fallDistance = 0.0f;
            player.setDeltaMovement(Vec3.ZERO);
            return;
        }
    }

    private record VecXZ(double x, double z) {
    }

    @EventBusSubscriber(modid="createcybernetics", bus=EventBusSubscriber.Bus.GAME)
    public static final class Events {
        @SubscribeEvent(priority=EventPriority.HIGHEST)
        public static void onIncomingDamage(LivingIncomingDamageEvent event) {
            LivingEntity livingEntity = event.getEntity();
            if (!(livingEntity instanceof ServerPlayer)) {
                return;
            }
            ServerPlayer player = (ServerPlayer)livingEntity;
            if (event.isCanceled()) {
                return;
            }
            if (!player.hasData(ModAttachments.CYBERWARE)) {
                return;
            }
            PlayerCyberwareData data = (PlayerCyberwareData)player.getData(ModAttachments.CYBERWARE);
            if (data == null) {
                return;
            }
            if (IDEMItem.isActive(player)) {
                return;
            }
            if (IDEMItem.getCooldownTicks(player) > 0) {
                return;
            }
            InstalledLoc loc = Events.findInstalledIdem(data);
            if (loc == null) {
                return;
            }
            if (!loc.stack.is(ModTags.Items.TOGGLEABLE_CYBERWARE)) {
                return;
            }
            if (!data.isEnabled(loc.slot, loc.index)) {
                return;
            }
            if (!data.tryConsumeEnergy(50)) {
                return;
            }
            event.setCanceled(true);
            IDEMItem.setCooldownTicks(player, 600);
            ResourceKey originDim = player.level().dimension();
            IDEMItem.armReturn(player, (ResourceKey<Level>)originDim, 300);
            ResourceKey<Level> destDim = IDEMItem.chooseDestination((ResourceKey<Level>)originDim, player.getRandom().nextDouble());
            ServerLevel destLevel = player.server.getLevel(destDim);
            if (destLevel == null) {
                IDEMItem.clearPending(player);
                return;
            }
            IDEMItem.teleportMapped(player, player.serverLevel(), destLevel);
        }

        @SubscribeEvent
        public static void onDeath(LivingDeathEvent event) {
            LivingEntity livingEntity = event.getEntity();
            if (livingEntity instanceof ServerPlayer) {
                ServerPlayer sp = (ServerPlayer)livingEntity;
                IDEMItem.clearPending(sp);
            }
        }

        private static InstalledLoc findInstalledIdem(PlayerCyberwareData data) {
            InstalledCyberware[] arr = data.getAll().get((Object)CyberwareSlot.BRAIN);
            if (arr == null) {
                return null;
            }
            Item idemItem = (Item)ModItems.BRAINUPGRADES_IDEM.get();
            for (int i = 0; i < arr.length; ++i) {
                ItemStack st;
                InstalledCyberware cw = arr[i];
                if (cw == null || (st = cw.getItem()) == null || st.isEmpty() || st.getItem() != idemItem) continue;
                return new InstalledLoc(CyberwareSlot.BRAIN, i, st);
            }
            return null;
        }

        private record InstalledLoc(CyberwareSlot slot, int index, ItemStack stack) {
        }
    }
}

