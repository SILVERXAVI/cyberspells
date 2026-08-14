/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.multiplayer.ClientLevel
 *  net.minecraft.core.HolderLookup$Provider
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.player.Player
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.api.distmarker.OnlyIn
 *  net.neoforged.neoforge.attachment.AttachmentType
 *  net.neoforged.neoforge.network.handling.IPayloadContext
 */
package com.maxwell.cyber_ware_port.common.network;

import com.maxwell.cyber_ware_port.common.capability.CyberwareCapabilityProvider;
import com.maxwell.cyber_ware_port.common.capability.CyberwareUserData;
import com.maxwell.cyber_ware_port.common.network.SyncCyberwareDataPacket;
import com.maxwell.cyber_ware_port.common.network.SyncSurgeryProgressPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ClientPacketHandler {
    public static int currentProgress = 0;
    public static int maxProgress = 100;

    public static void update(int progress, int max) {
        currentProgress = progress;
        maxProgress = max;
    }

    public static void reset() {
        currentProgress = 0;
        maxProgress = 100;
    }

    @OnlyIn(value=Dist.CLIENT)
    public static void handleSyncPacket(SyncCyberwareDataPacket msg, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            ClientLevel level = Minecraft.getInstance().level;
            if (level == null) {
                return;
            }
            Entity patt0$temp = level.getEntity(msg.entityId());
            if (patt0$temp instanceof Player) {
                Player player = (Player)patt0$temp;
                CyberwareUserData data = (CyberwareUserData)player.getData((AttachmentType)CyberwareCapabilityProvider.CYBERWARE_DATA.get());
                data.deserializeNBT((HolderLookup.Provider)player.registryAccess(), msg.data());
            }
        });
    }

    @OnlyIn(value=Dist.CLIENT)
    public static void handleProgressPacket(SyncSurgeryProgressPacket msg, IPayloadContext ctx) {
        ClientPacketHandler.update(msg.progress(), msg.maxProgress());
    }
}

