/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.material.FogType
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.neoforge.attachment.AttachmentType
 *  net.neoforged.neoforge.client.event.ViewportEvent$ComputeFov
 *  net.neoforged.neoforge.client.event.ViewportEvent$RenderFog
 *  net.neoforged.neoforge.items.ItemStackHandler
 */
package com.maxwell.cyber_ware_port.client.upgrades.cybereye;

import com.maxwell.cyber_ware_port.api.json.CyberwareAPI;
import com.maxwell.cyber_ware_port.common.capability.CyberwareCapabilityProvider;
import com.maxwell.cyber_ware_port.common.capability.CyberwareUserData;
import com.maxwell.cyber_ware_port.common.item.base.ICyberware;
import com.maxwell.cyber_ware_port.init.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.FogType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.client.event.ViewportEvent;
import net.neoforged.neoforge.items.ItemStackHandler;

@EventBusSubscriber(modid="cyber_ware_port", value={Dist.CLIENT})
public class EyeWareEvents {
    private static boolean isFeatureActive(Player player, Item item) {
        CyberwareUserData data = (CyberwareUserData)player.getData((AttachmentType)CyberwareCapabilityProvider.CYBERWARE_DATA.get());
        if (data == null) {
            return false;
        }
        ItemStackHandler handler = data.getInstalledCyberware();
        for (int i = 0; i < handler.getSlots(); ++i) {
            ICyberware cw;
            ItemStack stack = handler.getStackInSlot(i);
            if (!stack.is(item) || (cw = CyberwareAPI.getCyberware(stack)) == null) continue;
            if (!cw.isActive(stack)) {
                return false;
            }
            if (cw.hasEnergyProperties(stack) && cw.getEnergyConsumption(stack) > 0) {
                return data.isPowered();
            }
            return true;
        }
        return false;
    }

    @SubscribeEvent
    public static void onRenderFog(ViewportEvent.RenderFog event) {
        LocalPlayer player;
        if (event.getCamera().getFluidInCamera() == FogType.WATER && (player = Minecraft.getInstance().player) != null && EyeWareEvents.isFeatureActive((Player)player, (Item)ModItems.LIQUID_REFRACTION.get())) {
            event.setNearPlaneDistance(-8.0f);
            event.setFarPlaneDistance(200.0f);
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onComputeFov(ViewportEvent.ComputeFov event) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null && EyeWareEvents.isFeatureActive((Player)player, (Item)ModItems.DISTANCE_ENHANCER.get()) && player.isCrouching()) {
            double originalFov = event.getFOV();
            event.setFOV(originalFov * (double)0.3f);
        }
    }
}

