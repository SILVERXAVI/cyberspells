/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.components.events.GuiEventListener
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.client.gui.screens.inventory.InventoryScreen
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.world.item.Item
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.fml.common.EventBusSubscriber$Bus
 *  net.neoforged.neoforge.attachment.AttachmentType
 *  net.neoforged.neoforge.client.event.ScreenEvent$Init$Post
 *  net.neoforged.neoforge.client.event.ScreenEvent$Render$Pre
 *  net.neoforged.neoforge.network.PacketDistributor
 */
package com.maxwell.cyber_ware_port.client.upgrades.handmenu;

import com.maxwell.cyber_ware_port.client.upgrades.handmenu.PortableCraftingButton;
import com.maxwell.cyber_ware_port.common.capability.CyberwareCapabilityProvider;
import com.maxwell.cyber_ware_port.common.capability.CyberwareUserData;
import com.maxwell.cyber_ware_port.common.network.OpenPortableCraftingPacket;
import com.maxwell.cyber_ware_port.init.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid="cyber_ware_port", bus=EventBusSubscriber.Bus.GAME, value={Dist.CLIENT})
public class CrafterOpenEvent {
    private static PortableCraftingButton craftingBtn = null;

    @SubscribeEvent
    public static void onScreenInit(ScreenEvent.Init.Post event) {
        Screen screen = event.getScreen();
        if (screen instanceof InventoryScreen) {
            InventoryScreen screen2 = (InventoryScreen)screen;
            Minecraft mc = Minecraft.getInstance();
            if (mc.player == null) {
                return;
            }
            craftingBtn = null;
            CyberwareUserData data = (CyberwareUserData)mc.player.getData((AttachmentType)CyberwareCapabilityProvider.CYBERWARE_DATA.get());
            if (data.isCyberwareInstalled((Item)ModItems.FINE_MANIPULATORS.get())) {
                int guiLeft = screen2.getGuiLeft();
                int guiTop = screen2.getGuiTop();
                int btnX = guiLeft + 130;
                int btnY = guiTop + 60;
                craftingBtn = new PortableCraftingButton(btnX, btnY, btn -> PacketDistributor.sendToServer((CustomPacketPayload)new OpenPortableCraftingPacket(), (CustomPacketPayload[])new CustomPacketPayload[0]));
                event.addListener((GuiEventListener)craftingBtn);
            }
        }
    }

    @SubscribeEvent
    public static void onScreenRenderPre(ScreenEvent.Render.Pre event) {
        Screen screen = event.getScreen();
        if (screen instanceof InventoryScreen) {
            InventoryScreen screen2 = (InventoryScreen)screen;
            if (craftingBtn != null) {
                int guiLeft = screen2.getGuiLeft();
                int guiTop = screen2.getGuiTop();
                craftingBtn.setX(guiLeft + 130);
                craftingBtn.setY(guiTop + 60);
            }
        }
    }
}

