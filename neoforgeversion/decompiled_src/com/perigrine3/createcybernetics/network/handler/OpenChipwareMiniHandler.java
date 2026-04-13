/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.chat.Component
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.MenuProvider
 *  net.minecraft.world.SimpleMenuProvider
 *  net.minecraft.world.entity.player.Player
 *  net.neoforged.neoforge.network.handling.IPayloadContext
 */
package com.perigrine3.createcybernetics.network.handler;

import com.perigrine3.createcybernetics.network.payload.OpenChipwareMiniPayload;
import com.perigrine3.createcybernetics.screen.container.ChipwareContainer;
import com.perigrine3.createcybernetics.screen.custom.ChipwareMiniMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public final class OpenChipwareMiniHandler {
    private OpenChipwareMiniHandler() {
    }

    public static void handle(OpenChipwareMiniPayload payload, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            Player patt0$temp = ctx.player();
            if (!(patt0$temp instanceof ServerPlayer)) {
                return;
            }
            ServerPlayer sp = (ServerPlayer)patt0$temp;
            if (!ChipwareMiniMenu.canOpen((Player)sp)) {
                return;
            }
            SimpleMenuProvider provider = new SimpleMenuProvider((id, inv, player) -> new ChipwareMiniMenu(id, inv, new ChipwareContainer(player)), (Component)Component.translatable((String)"gui.chipwarecreative.title"));
            sp.openMenu((MenuProvider)provider, buf -> {});
        });
    }
}

