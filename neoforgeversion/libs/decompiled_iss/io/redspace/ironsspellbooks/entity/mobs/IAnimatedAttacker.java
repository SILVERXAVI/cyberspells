/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.world.entity.Entity
 *  net.neoforged.neoforge.network.PacketDistributor
 */
package io.redspace.ironsspellbooks.entity.mobs;

import io.redspace.ironsspellbooks.network.SyncAnimationPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.network.PacketDistributor;

public interface IAnimatedAttacker {
    public void playAnimation(String var1);

    default public <T extends Entity> void serverTriggerAnimation(String animationId) {
        PacketDistributor.sendToPlayersTrackingEntity((Entity)((Entity)this), new SyncAnimationPacket<Entity>(animationId, (Entity)this), (CustomPacketPayload[])new CustomPacketPayload[0]);
    }
}

