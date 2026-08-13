/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.util.Mth
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.level.Level
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.neoforge.client.event.ViewportEvent$ComputeFogColor
 *  org.joml.Vector3f
 */
package io.redspace.ironsspellbooks.api.util;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ViewportEvent;
import org.joml.Vector3f;

@EventBusSubscriber
public class FogManager {
    private static final int INTERP_MAX = 80;
    private static final Map<ResourceKey<Level>, FogManager> FOG_MANAGERS = new HashMap<ResourceKey<Level>, FogManager>();
    private double interpolation;
    private FogEvent lastEvent = null;
    private final LinkedHashMap<UUID, FogEvent> fogEvents = new LinkedHashMap();

    public static void createEvent(Entity entity, FogEvent event) {
        FogManager.createEvent((ResourceKey<Level>)entity.level.dimension(), entity.getUUID(), event);
    }

    public static void createEvent(ResourceKey<Level> dimension, UUID id, FogEvent event) {
        FogManager manager = FogManager.getManagerFor(dimension);
        if (manager.fogEvents.isEmpty()) {
            manager.interpolation = 80.0;
        }
        manager.fogEvents.put(id, event);
    }

    public static void stopEvent(UUID id) {
        for (FogManager manager : FOG_MANAGERS.values()) {
            if (!manager.fogEvents.containsKey(id)) continue;
            manager.lastEvent = (FogEvent)manager.fogEvents.remove(id);
            if (!manager.fogEvents.isEmpty()) continue;
            manager.interpolation = 80.0;
        }
    }

    private static FogManager getManagerFor(ResourceKey<Level> dimension) {
        return FOG_MANAGERS.computeIfAbsent(dimension, dim -> new FogManager());
    }

    public static void clear() {
        FOG_MANAGERS.clear();
    }

    @SubscribeEvent
    public static void fog(ViewportEvent.ComputeFogColor event) {
        if (Minecraft.getInstance().player != null) {
            FogManager manager = FogManager.getManagerFor((ResourceKey<Level>)Minecraft.getInstance().player.level.dimension());
            if (!manager.fogEvents.isEmpty() || manager.lastEvent != null) {
                float fogBlue;
                float fogGreen;
                float fogRed;
                FogEvent fogEvent;
                FogEvent fogEvent2 = fogEvent = manager.fogEvents.isEmpty() ? manager.lastEvent : (FogEvent)manager.fogEvents.lastEntry().getValue();
                if (fogEvent.color.isPresent()) {
                    fogRed = fogEvent.color.get().x;
                    fogGreen = fogEvent.color.get().y;
                    fogBlue = fogEvent.color.get().z;
                } else {
                    fogRed = event.getRed();
                    fogGreen = event.getGreen();
                    fogBlue = event.getBlue();
                }
                if (fogEvent.fullbright) {
                    float f9 = Math.max(fogRed, Math.max(fogGreen, fogBlue));
                    fogRed /= f9;
                    fogGreen /= f9;
                    fogBlue /= f9;
                }
                if (manager.interpolation > 0.0) {
                    manager.interpolation -= event.getPartialTick();
                    float f = Mth.clamp((float)((float)(manager.interpolation / 80.0)), (float)0.0f, (float)1.0f);
                    if (manager.fogEvents.isEmpty()) {
                        f = 1.0f - f;
                    }
                    event.setRed(Mth.lerp((float)f, (float)fogRed, (float)event.getRed()));
                    event.setGreen(Mth.lerp((float)f, (float)fogGreen, (float)event.getGreen()));
                    event.setBlue(Mth.lerp((float)f, (float)fogBlue, (float)event.getBlue()));
                    if (manager.interpolation <= 0.0) {
                        manager.lastEvent = null;
                    }
                } else {
                    event.setRed(fogRed);
                    event.setGreen(fogGreen);
                    event.setBlue(fogBlue);
                    manager.lastEvent = null;
                }
            }
        }
    }

    public record FogEvent(Optional<Vector3f> color, boolean fullbright) {
    }
}

