/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.chat.Component
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.MenuProvider
 *  net.minecraft.world.SimpleMenuProvider
 *  net.minecraft.world.item.ItemStack
 */
package com.perigrine3.createcybernetics.network.handler;

import com.perigrine3.createcybernetics.api.CyberwareSlot;
import com.perigrine3.createcybernetics.api.InstalledCyberware;
import com.perigrine3.createcybernetics.common.capabilities.ModAttachments;
import com.perigrine3.createcybernetics.common.capabilities.PlayerCyberwareData;
import com.perigrine3.createcybernetics.item.cyberware.HeatEngineItem;
import com.perigrine3.createcybernetics.network.payload.OpenHeatEnginePayload;
import com.perigrine3.createcybernetics.screen.custom.HeatEngineMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.item.ItemStack;

public final class OpenHeatEnginePayloadHandler {
    private OpenHeatEnginePayloadHandler() {
    }

    public static void handle(OpenHeatEnginePayload payload, ServerPlayer player) {
        if (player == null) {
            return;
        }
        if (!player.hasData(ModAttachments.CYBERWARE)) {
            return;
        }
        PlayerCyberwareData data = (PlayerCyberwareData)player.getData(ModAttachments.CYBERWARE);
        if (data == null) {
            return;
        }
        if (!OpenHeatEnginePayloadHandler.hasHeatEngineInstalled(data)) {
            return;
        }
        player.openMenu((MenuProvider)new SimpleMenuProvider((containerId, inv, p) -> new HeatEngineMenu(containerId, inv), (Component)Component.translatable((String)"gui.heatengine.title")));
    }

    private static boolean hasHeatEngineInstalled(PlayerCyberwareData data) {
        InstalledCyberware[] arr = data.getAll().get((Object)CyberwareSlot.ORGANS);
        if (arr == null) {
            return false;
        }
        for (InstalledCyberware inst : arr) {
            ItemStack st;
            if (inst == null || (st = inst.getItem()) == null || st.isEmpty() || !(st.getItem() instanceof HeatEngineItem)) continue;
            return true;
        }
        return false;
    }
}

