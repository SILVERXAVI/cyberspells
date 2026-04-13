/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.CameraType
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.client.renderer.GameRenderer
 *  net.minecraft.client.renderer.PostChain
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.entity.player.Player
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.fml.common.EventBusSubscriber$Bus
 *  net.neoforged.neoforge.client.event.ClientTickEvent$Post
 *  net.neoforged.neoforge.client.event.ViewportEvent$ComputeFov
 */
package com.perigrine3.createcybernetics.client.render;

import com.perigrine3.createcybernetics.effect.ModEffects;
import java.lang.reflect.Field;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.ViewportEvent;

@EventBusSubscriber(modid="createcybernetics", value={Dist.CLIENT}, bus=EventBusSubscriber.Bus.GAME)
public final class SpiderEyesVisionClient {
    private static final ResourceLocation SPIDER_SHADER = ResourceLocation.withDefaultNamespace((String)"shaders/post/spider.json");
    private static final double MAX_FOV = 110.0;
    private static boolean appliedByUs = false;
    private static Field postChainField = null;
    private static boolean postChainFieldSearched = false;

    private SpiderEyesVisionClient() {
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        boolean shouldApplyNow;
        boolean inFirstPerson;
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (player == null || mc.level == null) {
            if (appliedByUs) {
                SpiderEyesVisionClient.safeDisable(mc.gameRenderer);
            }
            return;
        }
        boolean hasImplant = SpiderEyesVisionClient.hasSpiderEyesInstalled((Player)player);
        boolean bl = inFirstPerson = mc.options.getCameraType() == CameraType.FIRST_PERSON;
        if (appliedByUs && !SpiderEyesVisionClient.isAnyPostChainActive(mc.gameRenderer)) {
            appliedByUs = false;
        }
        boolean bl2 = shouldApplyNow = hasImplant && inFirstPerson;
        if (shouldApplyNow) {
            if (!appliedByUs) {
                SpiderEyesVisionClient.safeEnable(mc.gameRenderer);
            }
        } else if (appliedByUs) {
            SpiderEyesVisionClient.safeDisable(mc.gameRenderer);
        }
    }

    @SubscribeEvent
    public static void onComputeFov(ViewportEvent.ComputeFov event) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (player == null) {
            return;
        }
        if (mc.options.getCameraType() == CameraType.FIRST_PERSON && SpiderEyesVisionClient.hasSpiderEyesInstalled((Player)player)) {
            event.setFOV(110.0);
        }
    }

    private static boolean hasSpiderEyesInstalled(Player player) {
        return player.hasEffect(ModEffects.SPIDER_EYES_EFFECT);
    }

    private static void safeEnable(GameRenderer renderer) {
        try {
            renderer.loadEffect(SPIDER_SHADER);
            appliedByUs = true;
        }
        catch (Throwable t) {
            appliedByUs = false;
        }
    }

    private static void safeDisable(GameRenderer renderer) {
        try {
            renderer.shutdownEffect();
        }
        finally {
            appliedByUs = false;
        }
    }

    private static boolean isAnyPostChainActive(GameRenderer renderer) {
        PostChain chain = SpiderEyesVisionClient.getPostChain(renderer);
        return chain != null;
    }

    private static PostChain getPostChain(GameRenderer renderer) {
        try {
            PostChain pc;
            Field f = SpiderEyesVisionClient.findPostChainField();
            if (f == null) {
                return null;
            }
            Object v = f.get(renderer);
            return v instanceof PostChain ? (pc = (PostChain)v) : null;
        }
        catch (Throwable t) {
            return null;
        }
    }

    private static Field findPostChainField() {
        if (postChainFieldSearched) {
            return postChainField;
        }
        postChainFieldSearched = true;
        for (Field f : GameRenderer.class.getDeclaredFields()) {
            if (f.getType() != PostChain.class) continue;
            f.setAccessible(true);
            postChainField = f;
            break;
        }
        return postChainField;
    }
}

