/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.systems.RenderSystem
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.util.RandomSource
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.ItemLike
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.neoforge.attachment.AttachmentType
 *  net.neoforged.neoforge.client.event.ClientTickEvent$Post
 *  net.neoforged.neoforge.client.event.RenderGuiLayerEvent$Post
 *  net.neoforged.neoforge.client.gui.VanillaGuiLayers
 *  net.neoforged.neoforge.items.ItemStackHandler
 */
package com.maxwell.cyber_ware_port.client.upgrades.cybereye;

import com.maxwell.cyber_ware_port.api.json.CyberwareAPI;
import com.maxwell.cyber_ware_port.client.ClientCyberwareSettings;
import com.maxwell.cyber_ware_port.client.upgrades.cybereye.CyberwareMenuScreen;
import com.maxwell.cyber_ware_port.common.capability.CyberwareCapabilityProvider;
import com.maxwell.cyber_ware_port.common.capability.CyberwareUserData;
import com.maxwell.cyber_ware_port.common.item.base.ICyberware;
import com.maxwell.cyber_ware_port.init.ModItems;
import com.mojang.blaze3d.systems.RenderSystem;
import java.lang.invoke.LambdaMetafactory;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.function.Predicate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.neoforged.neoforge.items.ItemStackHandler;

@EventBusSubscriber(modid="cyber_ware_port", value={Dist.CLIENT})
public class CyberwareHudOverlay {
    private static final ResourceLocation BATTERY_TEXTURE = ResourceLocation.fromNamespaceAndPath((String)"cyber_ware_port", (String)"textures/gui/battery_hud.png");
    private static final Map<Integer, Float> itemPrevProgress = new HashMap<Integer, Float>();
    private static final Map<Integer, Float> itemProgress = new HashMap<Integer, Float>();
    private static final Map<Integer, Integer> itemYIndices = new HashMap<Integer, Integer>();
    private static float prevSlideProgress = 0.0f;
    private static float slideProgress = 0.0f;
    private static boolean lastHudActive = false;
    private static int bootTicks = 0;
    private static int shutdownTicks = 0;

    public static Set<Integer> getInactiveSlotIds(CyberwareUserData userData) {
        HashSet<Integer> inactive = new HashSet<Integer>();
        ItemStackHandler handler = userData.getInstalledCyberware();
        for (int i = 0; i < handler.getSlots(); ++i) {
            boolean isToggledOff;
            ICyberware cw;
            ItemStack stack = handler.getStackInSlot(i);
            if (stack.isEmpty() || (cw = CyberwareAPI.getCyberware(stack)) == null || stack.is((Item)ModItems.HUDJACK.get())) continue;
            boolean isToggledOn = cw.isActive(stack);
            boolean isEmpOffline = isToggledOn && userData.getEmpTicks() > 0;
            boolean isNoPower = isToggledOn && cw.getEnergyConsumption(stack) > 0 && userData.getEnergyStored() < cw.getEnergyConsumption(stack) && userData.getEmpTicks() <= 0;
            boolean bl = isToggledOff = cw.canToggle(stack) && !isToggledOn;
            if (!isEmpOffline && !isNoPower && !isToggledOff) continue;
            inactive.add(i);
        }
        return inactive;
    }

    public static boolean hasActiveErrorsOrOff(Player player, CyberwareUserData userData) {
        return !CyberwareHudOverlay.getInactiveSlotIds(userData).isEmpty();
    }

    /*
     * Unable to fully structure code
     */
    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        mc = Minecraft.getInstance();
        if (mc.player == null) {
            return;
        }
        userData = (CyberwareUserData)mc.player.getData((AttachmentType)CyberwareCapabilityProvider.CYBERWARE_DATA.get());
        currentHudActive = CyberwareHudOverlay.isHudActive(userData);
        if (currentHudActive && !CyberwareHudOverlay.lastHudActive) {
            CyberwareHudOverlay.bootTicks = 20;
            CyberwareHudOverlay.shutdownTicks = 0;
            if (mc.level != null) {
                mc.level.playLocalSound(mc.player.getX(), mc.player.getY(), mc.player.getZ(), SoundEvents.CONDUIT_ACTIVATE, SoundSource.PLAYERS, 0.7f, 1.4f, false);
            }
        } else if (!currentHudActive && CyberwareHudOverlay.lastHudActive) {
            CyberwareHudOverlay.shutdownTicks = 15;
            CyberwareHudOverlay.bootTicks = 0;
            if (mc.level != null) {
                mc.level.playLocalSound(mc.player.getX(), mc.player.getY(), mc.player.getZ(), SoundEvents.CONDUIT_DEACTIVATE, SoundSource.PLAYERS, 0.7f, 0.8f, false);
            }
        }
        CyberwareHudOverlay.lastHudActive = currentHudActive;
        if (CyberwareHudOverlay.bootTicks > 0) {
            --CyberwareHudOverlay.bootTicks;
        }
        if (CyberwareHudOverlay.shutdownTicks > 0) {
            --CyberwareHudOverlay.shutdownTicks;
        }
        if (!((var6_4 = mc.screen) instanceof CyberwareMenuScreen)) ** GOTO lbl-1000
        menu = (CyberwareMenuScreen)var6_4;
        if (menu.isHudMoveMode) {
            v0 = true;
        } else lbl-1000:
        // 2 sources

        {
            v0 = false;
        }
        isMoveMode = v0;
        currentInactive = CyberwareHudOverlay.getInactiveSlotIds(userData);
        var6_4 = currentInactive.iterator();
        while (var6_4.hasNext()) {
            slotId = (Integer)var6_4.next();
            if (CyberwareHudOverlay.itemYIndices.containsKey(slotId)) continue;
            occupied = new boolean[10];
            for (int index : CyberwareHudOverlay.itemYIndices.values()) {
                if (index < 0 || index >= 10) continue;
                occupied[index] = true;
            }
            freeIndex = 0;
            for (j = 0; j < 10; ++j) {
                if (occupied[j]) continue;
                freeIndex = j;
                break;
            }
            CyberwareHudOverlay.itemYIndices.put(slotId, freeIndex);
        }
        trackedSlots = new HashSet<Integer>(CyberwareHudOverlay.itemProgress.keySet());
        trackedSlots.addAll(currentInactive);
        var7_8 = trackedSlots.iterator();
        while (var7_8.hasNext()) {
            slotId = (Integer)var7_8.next();
            prev = CyberwareHudOverlay.itemProgress.getOrDefault(slotId, Float.valueOf(0.0f)).floatValue();
            CyberwareHudOverlay.itemPrevProgress.put(slotId, Float.valueOf(prev));
            target = currentInactive.contains(slotId) != false ? 1.0f : 0.0f;
            tickSpeed = 0.05f;
            next = prev < target ? Math.min(prev + tickSpeed, target) : Math.max(prev - tickSpeed, target);
            CyberwareHudOverlay.itemProgress.put(slotId, Float.valueOf(next));
        }
        CyberwareHudOverlay.itemProgress.entrySet().removeIf((Predicate<Map.Entry>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Z, lambda$onClientTick$0(java.util.Set java.util.Map$Entry ), (Ljava/util/Map$Entry;)Z)(currentInactive));
        CyberwareHudOverlay.itemPrevProgress.keySet().removeIf((Predicate<Integer>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Z, lambda$onClientTick$1(java.lang.Integer ), (Ljava/lang/Integer;)Z)());
        CyberwareHudOverlay.itemYIndices.keySet().removeIf((Predicate<Integer>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Z, lambda$onClientTick$2(java.lang.Integer ), (Ljava/lang/Integer;)Z)());
    }

    /*
     * Unable to fully structure code
     */
    @SubscribeEvent
    public static void onRenderGuiLayer(RenderGuiLayerEvent.Post event) {
        block11: {
            if (!VanillaGuiLayers.HOTBAR.equals((Object)event.getName())) break block11;
            mc = Minecraft.getInstance();
            player = mc.player;
            if (player == null) {
                return;
            }
            userData = (CyberwareUserData)player.getData((AttachmentType)CyberwareCapabilityProvider.CYBERWARE_DATA.get());
            v0 = isRenderable = CyberwareHudOverlay.isHudActive(userData) != false || CyberwareHudOverlay.shutdownTicks > 0;
            if (!isRenderable) {
                return;
            }
            var7_5 = mc.screen;
            if (!(var7_5 instanceof CyberwareMenuScreen)) ** GOTO lbl-1000
            menu = (CyberwareMenuScreen)var7_5;
            if (menu.isHudMoveMode) {
                v1 = true;
            } else lbl-1000:
            // 2 sources

            {
                v1 = false;
            }
            isMoveMode = v1;
            rand = mc.level != null ? mc.level.random : RandomSource.create();
            jitterX = 0;
            jitterY = 0;
            if (userData.getEmpTicks() > 0 || CyberwareHudOverlay.bootTicks > 0 || CyberwareHudOverlay.shutdownTicks > 0 && rand.nextFloat() < 0.3f) {
                jitterX = rand.nextInt(5) - 2;
                jitterY = rand.nextInt(5) - 2;
            }
            hudAlpha = 1.0f;
            if (CyberwareHudOverlay.bootTicks > 0) {
                hudAlpha = rand.nextFloat() < 0.15f ? 0.1f : rand.nextFloat() * 0.4f + 0.5f;
            } else if (CyberwareHudOverlay.shutdownTicks > 0) {
                progress = (float)CyberwareHudOverlay.shutdownTicks / 15.0f;
                hudAlpha = rand.nextFloat() < 0.25f ? 0.0f : progress;
            }
            x = ClientCyberwareSettings.hudX;
            y = ClientCyberwareSettings.hudY;
            CyberwareHudOverlay.renderBatteryHud(event.getGuiGraphics(), mc, userData, x, y, jitterX, jitterY, hudAlpha);
            currentY = y + 30;
            if (userData.getImmunityTime() > 0) {
                CyberwareHudOverlay.renderImmunityHud(event.getGuiGraphics(), mc, userData, x, currentY, hudAlpha);
            }
            v2 = hasActiveAnim = CyberwareHudOverlay.itemProgress.isEmpty() == false;
            if (hasActiveAnim || isMoveMode) {
                barX = ClientCyberwareSettings.barX;
                barY = ClientCyberwareSettings.barY;
                partialTick = event.getPartialTick().getGameTimeDeltaTicks();
                for (Map.Entry<Integer, Float> entry : CyberwareHudOverlay.itemProgress.entrySet()) {
                    slotId = entry.getKey();
                    stack = userData.getInstalledCyberware().getStackInSlot(slotId);
                    if (stack.isEmpty() || (cw = CyberwareAPI.getCyberware(stack)) == null || (yIndex = CyberwareHudOverlay.itemYIndices.get(slotId)) == null) continue;
                    slotY = barY + 5 + yIndex * 20 + jitterY;
                    prevProgress = CyberwareHudOverlay.itemPrevProgress.getOrDefault(slotId, Float.valueOf(0.0f)).floatValue();
                    currentProgress = prevProgress + (entry.getValue().floatValue() - prevProgress) * partialTick;
                    if (!(currentProgress > 0.0f)) continue;
                    screenWidth = mc.getWindow().getGuiScaledWidth();
                    itemStartX = ClientCyberwareSettings.slideDirection == 0 ? -36 : screenWidth + 10;
                    itemCurrentX = (int)((float)itemStartX + (float)(barX - itemStartX) * currentProgress) + jitterX;
                    CyberwareHudOverlay.renderBarSlot(event.getGuiGraphics(), mc, userData, itemCurrentX, slotY, isMoveMode, rand, stack, cw, slotId, hudAlpha);
                }
            }
        }
    }

    private static boolean isHudActive(CyberwareUserData data) {
        ItemStackHandler handler = data.getInstalledCyberware();
        for (int i = 0; i < handler.getSlots(); ++i) {
            ICyberware cw;
            ItemStack stack = handler.getStackInSlot(i);
            if (stack.isEmpty() || (cw = CyberwareAPI.getCyberware(stack)) == null || !stack.is((Item)ModItems.HUDJACK.get()) || !cw.isActive(stack)) continue;
            return true;
        }
        return false;
    }

    public static void renderBatteryHud(GuiGraphics g, Minecraft mc, CyberwareUserData data, int x, int y, int jitterX, int jitterY, float hudAlpha) {
        int textColor;
        float a;
        float b;
        float gVal;
        float r;
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderTexture((int)0, (ResourceLocation)BATTERY_TEXTURE);
        int current = data.getEnergyStored();
        int max = data.getMaxEnergyStored();
        int prod = data.getLastProduction();
        int cons = data.getLastConsumption();
        if (current <= 0) {
            boolean flash;
            long time = System.currentTimeMillis();
            boolean bl = flash = time % 500L < 250L;
            if (flash) {
                r = 1.0f;
                gVal = 0.0f;
                b = 0.0f;
                a = hudAlpha;
                textColor = (int)(hudAlpha * 255.0f) << 24 | 0xFF0000;
            } else {
                r = 0.5f;
                gVal = 0.0f;
                b = 0.0f;
                a = hudAlpha;
                textColor = (int)(hudAlpha * 255.0f) << 24 | 0x880000;
            }
        } else {
            float[] userColor = ClientCyberwareSettings.getColorFloats();
            r = userColor[0];
            gVal = userColor[1];
            b = userColor[2];
            a = userColor[3] * hudAlpha;
            textColor = (int)(a * 255.0f) << 24 | ClientCyberwareSettings.hudColor & 0xFFFFFF;
        }
        g.setColor(r, gVal, b, a);
        int startX = x + jitterX;
        int startY = y + jitterY;
        int texTotalWidth = 37;
        int texTotalHeight = 25;
        int frameWidth = 13;
        int frameHeight = 25;
        g.blit(BATTERY_TEXTURE, startX, startY, 0.0f, 0.0f, frameWidth, frameHeight, texTotalWidth, texTotalHeight);
        if (max > 0 && current > 0) {
            int barTextureU = 27;
            int barTextureV = 2;
            int barWidth = 10;
            int barFullHeight = 22;
            int offsetX = 2;
            int offsetY = 2;
            float pct = (float)current / (float)max;
            int renderHeight = (int)((float)barFullHeight * pct);
            if (renderHeight > 0) {
                int screenY = startY + offsetY + (barFullHeight - renderHeight);
                int textureV = barTextureV + (barFullHeight - renderHeight);
                g.blit(BATTERY_TEXTURE, startX + offsetX, screenY, (float)barTextureU, (float)textureV, barWidth, renderHeight, texTotalWidth, texTotalHeight);
            }
        }
        g.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        int textX = startX + frameWidth + 4;
        int textY = startY + 4;
        Object powerText = current + " / " + max;
        Object diffText = "-" + cons + " / +" + prod;
        if (data.getEmpTicks() > 0 || bootTicks > 10 || shutdownTicks > 0 && mc.level != null && mc.level.random.nextFloat() < 0.5f) {
            powerText = CyberwareHudOverlay.scrambleText((String)powerText, 0.45f);
            diffText = CyberwareHudOverlay.scrambleText((String)diffText, 0.45f);
        }
        g.drawString(mc.font, (String)powerText, textX, textY, textColor, true);
        g.drawString(mc.font, (String)diffText, textX, textY + 10, textColor, true);
        RenderSystem.disableBlend();
    }

    private static void renderBarSlot(GuiGraphics g, Minecraft mc, CyberwareUserData data, int startX, int slotY, boolean isMoveMode, RandomSource rand, ItemStack stack, ICyberware cw, int slotId, float hudAlpha) {
        int hudColor = ClientCyberwareSettings.hudColor;
        int width = 26;
        int height = 20;
        float[] floats = ClientCyberwareSettings.getColorFloats();
        g.setColor(floats[0], floats[1], floats[2], floats[3] * hudAlpha);
        int outlineColor = (int)(floats[3] * hudAlpha * 255.0f) << 24 | hudColor & 0xFFFFFF;
        int bLen = 5;
        int bThk = 1;
        g.fill(startX, slotY, startX + bLen, slotY + bThk, outlineColor);
        g.fill(startX, slotY, startX + bThk, slotY + bLen, outlineColor);
        g.fill(startX + width - bLen, slotY, startX + width, slotY + bThk, outlineColor);
        g.fill(startX + width - bThk, slotY, startX + width, slotY + bLen, outlineColor);
        g.fill(startX, slotY + height - bThk, startX + bLen, slotY + height, outlineColor);
        g.fill(startX, slotY + height - bLen, startX + bThk, slotY + height, outlineColor);
        g.fill(startX + width - bLen, slotY + height - bThk, startX + width, slotY + height, outlineColor);
        g.fill(startX + width - bThk, slotY + height - bLen, startX + width, slotY + height, outlineColor);
        g.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        if (isMoveMode) {
            ItemStack eyePlaceholder = new ItemStack((ItemLike)ModItems.CYBER_EYE.get());
            g.setColor(0.35f, 0.35f, 0.35f, hudAlpha);
            RenderSystem.setShaderColor((float)0.35f, (float)0.35f, (float)0.35f, (float)hudAlpha);
            g.renderItem(eyePlaceholder, startX + 5, slotY + 2);
            g.setColor(1.0f, 1.0f, 1.0f, 1.0f);
            RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            return;
        }
        boolean isToggledOn = cw.isActive(stack);
        boolean isEmpOffline = isToggledOn && data.getEmpTicks() > 0;
        boolean isNoPower = isToggledOn && cw.getEnergyConsumption(stack) > 0 && data.getEnergyStored() < cw.getEnergyConsumption(stack) && data.getEmpTicks() <= 0;
        float r = 1.0f;
        float gVal = 1.0f;
        float b = 1.0f;
        float a = hudAlpha;
        if (isEmpOffline) {
            r = 0.5f;
            gVal = 0.15f;
            b = 0.15f;
        } else if (isNoPower) {
            r = 0.45f;
            gVal = 0.4f;
            b = 0.15f;
        } else {
            r = 0.25f;
            gVal = 0.25f;
            b = 0.25f;
        }
        g.setColor(r, gVal, b, a);
        RenderSystem.setShaderColor((float)r, (float)gVal, (float)b, (float)a);
        g.renderItem(stack, startX + 5, slotY + 2);
        g.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        if (isEmpOffline) {
            RenderSystem.enableBlend();
            int staticLineColor = (int)(a * 255.0f) << 24 | 0xFF0000;
            for (int line = 0; line < 2; ++line) {
                int lineY = slotY + 2 + rand.nextInt(16);
                g.fill(startX + 5, lineY, startX + 21, lineY + 1, staticLineColor);
            }
            RenderSystem.disableBlend();
        }
        if (isNoPower) {
            boolean blink;
            boolean bl = blink = System.currentTimeMillis() % 1600L < 800L;
            if (blink) {
                g.pose().pushPose();
                g.pose().translate(0.0f, 0.0f, 200.0f);
                int blinkColor = (int)(a * 255.0f) << 24 | 0xFF0000;
                g.drawString(mc.font, "!", startX + 15, slotY + 10, blinkColor, true);
                g.pose().popPose();
            }
        }
    }

    public static void renderImmunityHud(GuiGraphics g, Minecraft mc, CyberwareUserData data, int x, int y, float hudAlpha) {
        int ticks = data.getImmunityTime();
        if (ticks > 0) {
            int seconds = ticks / 20;
            int minutes = seconds / 60;
            String timeStr = String.format("%02d:%02d", minutes, seconds % 60);
            int hudColor = ClientCyberwareSettings.hudColor;
            int textColor = (int)(hudAlpha * 255.0f) << 24 | hudColor & 0xFFFFFF;
            g.drawString(mc.font, "SUPPRESSANT: " + timeStr, x, y, textColor, true);
        }
    }

    private static String scrambleText(String original, float intensity) {
        char[] chars = original.toCharArray();
        Random rand = new Random();
        for (int i = 0; i < chars.length; ++i) {
            if (chars[i] == ' ' || chars[i] == '/' || !(rand.nextFloat() < intensity)) continue;
            chars[i] = (char)(33 + rand.nextInt(93));
        }
        return new String(chars);
    }

    private static /* synthetic */ boolean lambda$onClientTick$2(Integer key) {
        return !itemProgress.containsKey(key);
    }

    private static /* synthetic */ boolean lambda$onClientTick$1(Integer key) {
        return !itemProgress.containsKey(key);
    }

    private static /* synthetic */ boolean lambda$onClientTick$0(Set currentInactive, Map.Entry entry) {
        return ((Float)entry.getValue()).floatValue() == 0.0f && !currentInactive.contains(entry.getKey());
    }
}

