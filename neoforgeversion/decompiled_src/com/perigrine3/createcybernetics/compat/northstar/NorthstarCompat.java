/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.damagesource.DamageSource
 *  net.minecraft.world.damagesource.DamageTypes
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.Vec3
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.neoforge.common.NeoForge
 *  net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent
 *  net.neoforged.neoforge.event.tick.PlayerTickEvent$Post
 *  net.neoforged.neoforge.network.PacketDistributor
 */
package com.perigrine3.createcybernetics.compat.northstar;

import com.perigrine3.createcybernetics.common.capabilities.ModAttachments;
import com.perigrine3.createcybernetics.common.capabilities.PlayerCyberwareData;
import com.perigrine3.createcybernetics.compat.ModCompats;
import com.perigrine3.createcybernetics.compat.northstar.CopernicusSuitPredicate;
import com.perigrine3.createcybernetics.network.payload.CopernicusOxygenSyncPayload;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

public final class NorthstarCompat {
    public static final String NORTHSTAR_MODID = "northstar";
    public static final int COPERNICUS_OXYGEN_MAX_DISPLAY = 3000;
    public static final int COPERNICUS_DEPLETION_PER_SECOND = 1;
    public static final int COPERNICUS_RECHARGE_PER_SECOND = 10;
    private static final List<SuitPredicate> SUIT_PREDICATES = new ArrayList<SuitPredicate>();
    private static boolean bootstrapped = false;
    private static boolean enabled = false;

    private NorthstarCompat() {
    }

    public static void bootstrap() {
        if (bootstrapped) {
            return;
        }
        bootstrapped = true;
        enabled = ModCompats.isInstalled(NORTHSTAR_MODID);
        if (!enabled) {
            return;
        }
        SUIT_PREDICATES.add(CopernicusSuitPredicate::hasCopernicusSetInstalled);
        NeoForge.EVENT_BUS.register((Object)Events.INSTANCE);
    }

    public static boolean isEnabled() {
        return enabled;
    }

    public static void registerSuitPredicate(SuitPredicate predicate) {
        if (predicate != null) {
            SUIT_PREDICATES.add(predicate);
        }
    }

    public static boolean isNorthstarDimension(Level level) {
        ResourceLocation id = level.dimension().location();
        return NORTHSTAR_MODID.equals(id.getNamespace());
    }

    public static boolean hasSuitEquivalent(Player player) {
        if (!enabled || player == null) {
            return false;
        }
        for (SuitPredicate pred : SUIT_PREDICATES) {
            if (!pred.isSuitEquivalent(player)) continue;
            return true;
        }
        return false;
    }

    public static boolean isNorthstarOxygenDamage(DamageSource source) {
        if (source == null) {
            return false;
        }
        Optional keyOpt = source.typeHolder().unwrapKey();
        if (keyOpt.isEmpty()) {
            return false;
        }
        ResourceLocation id = ((ResourceKey)keyOpt.get()).location();
        if (!NORTHSTAR_MODID.equals(id.getNamespace())) {
            return false;
        }
        String path = id.getPath().toLowerCase(Locale.ROOT);
        return path.equals("suffocation") || path.contains("suffocat") || path.contains("vacuum") || path.contains("no_oxygen") || path.contains("asphyx") || path.contains("oxygen");
    }

    private static PlayerCyberwareData getData(Player player) {
        return (PlayerCyberwareData)player.getData(ModAttachments.CYBERWARE);
    }

    public static int getOxygen(Player player) {
        PlayerCyberwareData data = NorthstarCompat.getData(player);
        return data == null ? 0 : data.getCopernicusOxygen();
    }

    public static void setOxygen(Player player, int value) {
        PlayerCyberwareData data = NorthstarCompat.getData(player);
        if (data == null) {
            return;
        }
        data.setCopernicusOxygen(value, 3000);
    }

    @FunctionalInterface
    public static interface SuitPredicate {
        public boolean isSuitEquivalent(Player var1);
    }

    private static final class Events {
        private static final Events INSTANCE = new Events();

        private Events() {
        }

        @SubscribeEvent
        public void onIncomingDamage(LivingIncomingDamageEvent event) {
            if (!NorthstarCompat.isEnabled()) {
                return;
            }
            LivingEntity entity = event.getEntity();
            if (!(entity instanceof Player)) {
                return;
            }
            Player player = (Player)entity;
            Level level = player.level();
            if (level.isClientSide()) {
                return;
            }
            if (!NorthstarCompat.isNorthstarDimension(level)) {
                return;
            }
            if (!CopernicusSuitPredicate.hasCopernicusSetInstalled(player)) {
                return;
            }
            DamageSource source = event.getSource();
            if (NorthstarCompat.isNorthstarOxygenDamage(source)) {
                PlayerCyberwareData data = NorthstarCompat.getData(player);
                if (data == null) {
                    return;
                }
                if (data.getCopernicusOxygen() > 0) {
                    event.setCanceled(true);
                }
                return;
            }
            Optional keyOpt = source.typeHolder().unwrapKey();
            if (keyOpt.isEmpty()) {
                return;
            }
            ResourceKey key = (ResourceKey)keyOpt.get();
            if (key.equals(DamageTypes.FREEZE) || key.equals(DamageTypes.IN_FIRE) || key.equals(DamageTypes.ON_FIRE) || key.equals(DamageTypes.HOT_FLOOR) || key.equals(DamageTypes.LAVA)) {
                event.setCanceled(true);
            }
        }

        @SubscribeEvent
        public void onPlayerTickPost(PlayerTickEvent.Post event) {
            if (!NorthstarCompat.isEnabled()) {
                return;
            }
            Player player = event.getEntity();
            Level level = player.level();
            if (level.isClientSide()) {
                return;
            }
            if (player.isCreative() || player.isSpectator()) {
                return;
            }
            if (!CopernicusSuitPredicate.hasCopernicusSetInstalled(player)) {
                return;
            }
            PlayerCyberwareData data = NorthstarCompat.getData(player);
            if (data == null) {
                return;
            }
            boolean oxygenatedEnvironment = NorthstarCompat.isNorthstarDimension(level) ? NorthstarOxygenAccess.hasOxygen(level, player.getEyePosition()) : !player.isUnderWater();
            data.setCopernicusOxygenatedEnvironment(oxygenatedEnvironment);
            data.tickCopernicusOxygen(oxygenatedEnvironment, 1, 10, 3000);
            if (player instanceof ServerPlayer) {
                ServerPlayer sp = (ServerPlayer)player;
                PacketDistributor.sendToPlayer((ServerPlayer)sp, (CustomPacketPayload)new CopernicusOxygenSyncPayload(data.getCopernicusOxygen()), (CustomPacketPayload[])new CustomPacketPayload[0]);
            }
            if (player.getTicksFrozen() > 0) {
                player.setTicksFrozen(0);
            }
            if (player.isOnFire()) {
                player.clearFire();
            }
        }
    }

    private static final class NorthstarOxygenAccess {
        private static final boolean AVAILABLE;
        private static final Method HAS_OXYGEN;

        private NorthstarOxygenAccess() {
        }

        static boolean hasOxygen(Level level, Vec3 pos) {
            if (!AVAILABLE || level == null || pos == null) {
                return false;
            }
            try {
                Boolean b;
                Object r = HAS_OXYGEN.invoke(null, level, pos);
                return r instanceof Boolean && (b = (Boolean)r) != false;
            }
            catch (Throwable ignored) {
                return false;
            }
        }

        static {
            Method m = null;
            boolean ok = false;
            try {
                Class<?> cls = Class.forName("com.lightning.northstar.world.oxygen.NorthstarOxygen");
                m = cls.getMethod("hasOxygen", Level.class, Vec3.class);
                ok = true;
            }
            catch (Throwable ignored) {
                ok = false;
            }
            HAS_OXYGEN = m;
            AVAILABLE = ok;
        }
    }
}

