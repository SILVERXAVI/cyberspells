/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.world.item.Item
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.fml.common.EventBusSubscriber$Bus
 *  net.neoforged.neoforge.client.event.RenderGuiLayerEvent$Pre
 *  net.neoforged.neoforge.client.gui.VanillaGuiLayers
 */
package com.perigrine3.createcybernetics.client;

import com.perigrine3.createcybernetics.api.CyberwareSlot;
import com.perigrine3.createcybernetics.common.capabilities.ModAttachments;
import com.perigrine3.createcybernetics.common.capabilities.PlayerCyberwareData;
import com.perigrine3.createcybernetics.item.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

@EventBusSubscriber(modid="createcybernetics", bus=EventBusSubscriber.Bus.GAME, value={Dist.CLIENT})
public final class OxygenOverlayHider {
    private OxygenOverlayHider() {
    }

    @SubscribeEvent
    public static void onRenderGuiLayerPre(RenderGuiLayerEvent.Pre event) {
        if (!VanillaGuiLayers.AIR_LEVEL.equals((Object)event.getName())) {
            return;
        }
        LocalPlayer player = Minecraft.getInstance().player;
        boolean underwater = player.isUnderWater();
        PlayerCyberwareData data = (PlayerCyberwareData)player.getData(ModAttachments.CYBERWARE);
        boolean hasGills = data.hasSpecificItem((Item)ModItems.WETWARE_WATERBREATHINGLUNGS.get(), CyberwareSlot.LUNGS);
        if (player == null) {
            return;
        }
        if (!underwater) {
            return;
        }
        if (data == null) {
            return;
        }
        if (hasGills) {
            event.setCanceled(true);
        }
    }
}

