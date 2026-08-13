/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.systems.RenderSystem
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.FormattedText
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.util.Mth
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.neoforge.client.event.CustomizeGuiOverlayEvent$BossEventProgress
 */
package io.redspace.ironsspellbooks.api.util;

import com.mojang.blaze3d.systems.RenderSystem;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.CustomizeGuiOverlayEvent;

@EventBusSubscriber(value={Dist.CLIENT})
public class BossbarManager {
    private static final Map<UUID, BossbarSprite> CUSTOM_BARS = new HashMap<UUID, BossbarSprite>();

    public static void startTracking(UUID uuid, BossbarSprite sprite) {
        CUSTOM_BARS.put(uuid, sprite);
    }

    public static void stopTracking(UUID uuid) {
        CUSTOM_BARS.remove(uuid);
    }

    @SubscribeEvent
    public static void renderCustomBossbar(CustomizeGuiOverlayEvent.BossEventProgress event) {
        BossbarSprite customSprite = CUSTOM_BARS.get(event.getBossEvent().getId());
        if (customSprite != null) {
            GuiGraphics guiGraphics = event.getGuiGraphics();
            int y = event.getY() + customSprite.yBarOffset;
            int x = (guiGraphics.guiWidth() - customSprite.width) / 2;
            RenderSystem.enableBlend();
            guiGraphics.blitSprite(customSprite.spriteLocation, customSprite.width, customSprite.height * 2, 0, 0, x, y, customSprite.width, customSprite.height);
            int progress = Mth.lerpDiscrete((float)event.getBossEvent().getProgress(), (int)0, (int)(customSprite.width - customSprite.buffer * 2)) + customSprite.buffer;
            if (progress > 0) {
                guiGraphics.blitSprite(customSprite.spriteLocation, customSprite.width, customSprite.height * 2, 0, customSprite.height, x, y, progress, customSprite.height);
            }
            RenderSystem.disableBlend();
            Component component = event.getBossEvent().getName();
            int l = Minecraft.getInstance().font.width((FormattedText)component);
            int i1 = guiGraphics.guiWidth() / 2 - l / 2;
            int j1 = y - 9 - customSprite.yBarOffset;
            event.setIncrement(event.getIncrement() - 5 + customSprite.height + customSprite.yBarOffset);
            guiGraphics.drawString(Minecraft.getInstance().font, component, i1, j1, 0xFFFFFF);
            event.setCanceled(true);
        }
    }

    public record BossbarSprite(ResourceLocation spriteLocation, int width, int height, int buffer, int yBarOffset) {
    }
}

