/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.entity.player.Player
 *  net.neoforged.neoforge.network.handling.IPayloadContext
 */
package com.perigrine3.createcybernetics.network.handler;

import com.perigrine3.createcybernetics.item.cyberware.ArmCannonItem;
import com.perigrine3.createcybernetics.network.payload.ArmCannonFirePayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public final class ArmCannonFireHandler {
    private ArmCannonFireHandler() {
    }

    public static void handle(ArmCannonFirePayload payload, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            Player patt0$temp = ctx.player();
            if (!(patt0$temp instanceof ServerPlayer)) {
                return;
            }
            ServerPlayer sp = (ServerPlayer)patt0$temp;
            ArmCannonItem.fireLoaded(sp);
        });
    }
}

