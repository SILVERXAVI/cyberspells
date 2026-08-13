/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.world.entity.Entity
 *  net.neoforged.neoforge.network.PacketDistributor
 */
package io.redspace.ironsspellbooks.api.network;

import io.redspace.ironsspellbooks.network.EntityEventPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.network.PacketDistributor;

public interface IClientEventEntity {
    public void handleClientEvent(byte var1);

    default public <T extends Entity> void serverTriggerEvent(byte eventId) {
        PacketDistributor.sendToPlayersTrackingEntity((Entity)((Entity)this), new EntityEventPacket((Entity)this, eventId), (CustomPacketPayload[])new CustomPacketPayload[0]);
    }
}

