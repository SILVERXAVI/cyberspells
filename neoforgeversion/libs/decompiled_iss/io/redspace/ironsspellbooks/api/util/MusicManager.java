/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.level.Level
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.neoforge.client.event.ClientTickEvent$Pre
 */
package io.redspace.ironsspellbooks.api.util;

import io.redspace.ironsspellbooks.api.util.IMusicHandler;
import io.redspace.ironsspellbooks.config.ClientConfigs;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;

@EventBusSubscriber
public class MusicManager {
    private static final Map<ResourceKey<Level>, MusicManager> MUSIC_MANAGERS = new HashMap<ResourceKey<Level>, MusicManager>();
    private final LinkedHashMap<UUID, IMusicHandler> musicHandlers = new LinkedHashMap();
    private boolean resumeNext;

    public static void createEvent(Entity entity, IMusicHandler event) {
        MusicManager.createEvent((ResourceKey<Level>)entity.level.dimension(), entity.getUUID(), event);
    }

    public static void createEvent(ResourceKey<Level> dimension, UUID id, IMusicHandler event) {
        if (!((Boolean)ClientConfigs.ENABLE_BOSS_MUSIC.get()).booleanValue()) {
            return;
        }
        MusicManager manager = MusicManager.getManagerFor(dimension);
        if (!manager.musicHandlers.isEmpty()) {
            ((IMusicHandler)manager.musicHandlers.lastEntry().getValue()).stop();
        }
        event.init();
        manager.musicHandlers.put(id, event);
    }

    public static void stopEvent(UUID uuid) {
        for (MusicManager manager : MUSIC_MANAGERS.values()) {
            if (!manager.musicHandlers.containsKey(uuid)) continue;
            ((IMusicHandler)manager.musicHandlers.remove(uuid)).stop();
            if (manager.musicHandlers.isEmpty()) continue;
            manager.resumeNext = true;
        }
    }

    private static MusicManager getManagerFor(ResourceKey<Level> dimension) {
        return MUSIC_MANAGERS.computeIfAbsent(dimension, dim -> new MusicManager());
    }

    public static void clear() {
        for (MusicManager m : MUSIC_MANAGERS.values()) {
            for (IMusicHandler h : m.musicHandlers.values()) {
                h.hardStop();
            }
        }
        MUSIC_MANAGERS.clear();
    }

    @SubscribeEvent
    public static void tick(ClientTickEvent.Pre event) {
        if (Minecraft.getInstance().player != null && !Minecraft.getInstance().isPaused()) {
            MusicManager manager = MusicManager.getManagerFor((ResourceKey<Level>)Minecraft.getInstance().player.level.dimension());
            if (manager.musicHandlers.isEmpty()) {
                return;
            }
            Map.Entry entry = manager.musicHandlers.lastEntry();
            UUID uuid = (UUID)entry.getKey();
            IMusicHandler musicHandler = (IMusicHandler)entry.getValue();
            if (manager.resumeNext) {
                musicHandler.triggerResume();
                manager.resumeNext = false;
            }
            if (musicHandler.isDone()) {
                manager.musicHandlers.remove(uuid);
            } else {
                musicHandler.tick();
            }
        }
    }
}

