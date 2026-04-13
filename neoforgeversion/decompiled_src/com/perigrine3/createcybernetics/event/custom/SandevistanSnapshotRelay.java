/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.entity.Entity
 *  net.neoforged.neoforge.network.PacketDistributor
 */
package com.perigrine3.createcybernetics.event.custom;

import com.perigrine3.createcybernetics.effect.ModEffects;
import com.perigrine3.createcybernetics.network.payload.SandevistanSnapshotC2SPayload;
import com.perigrine3.createcybernetics.network.payload.SandevistanSnapshotPayload;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.network.PacketDistributor;

public final class SandevistanSnapshotRelay {
    private static final String NBT_LAST_TICK = "cc_sandevistan_snap_last_tick";

    private SandevistanSnapshotRelay() {
    }

    public static void handle(ServerPlayer sp, SandevistanSnapshotC2SPayload p) {
        if (!sp.isAlive()) {
            return;
        }
        if (!sp.hasEffect(ModEffects.SANDEVISTAN_EFFECT)) {
            return;
        }
        long now = sp.level().getGameTime();
        long last = sp.getPersistentData().getLong(NBT_LAST_TICK);
        if (last == now) {
            return;
        }
        sp.getPersistentData().putLong(NBT_LAST_TICK, now);
        SandevistanSnapshotPayload out = new SandevistanSnapshotPayload(sp.getUUID(), p.x(), p.y(), p.z(), p.bodyYaw(), p.crouching(), p.headX(), p.headY(), p.headZ(), p.bodyX(), p.bodyY(), p.bodyZ(), p.rArmX(), p.rArmY(), p.rArmZ(), p.lArmX(), p.lArmY(), p.lArmZ(), p.rLegX(), p.rLegY(), p.rLegZ(), p.lLegX(), p.lLegY(), p.lLegZ(), p.ageInTicks());
        PacketDistributor.sendToPlayersTrackingEntityAndSelf((Entity)sp, (CustomPacketPayload)out, (CustomPacketPayload[])new CustomPacketPayload[0]);
    }
}

