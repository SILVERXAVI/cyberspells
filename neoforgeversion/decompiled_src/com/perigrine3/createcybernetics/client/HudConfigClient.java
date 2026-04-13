/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  com.google.gson.GsonBuilder
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  net.neoforged.fml.loading.FMLPaths
 *  org.jetbrains.annotations.Nullable
 */
package com.perigrine3.createcybernetics.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import net.neoforged.fml.loading.FMLPaths;
import org.jetbrains.annotations.Nullable;

public final class HudConfigClient {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Map<UUID, HudConfig> CACHE = new ConcurrentHashMap<UUID, HudConfig>();

    private HudConfigClient() {
    }

    private static HudConfig defaultConfig() {
        return new HudConfig(true, true, true, TargetMode.ABOVE_HOTBAR, BatteryMode.ICON_PLUS_CAPACITY_PLUS_STATS);
    }

    private static Path fileFor(UUID playerId) {
        Path dir = FMLPaths.CONFIGDIR.get().resolve("createcybernetics");
        return dir.resolve("hud_layout_" + String.valueOf(playerId) + ".json");
    }

    public static HudConfig get(UUID playerId) {
        return CACHE.computeIfAbsent(playerId, HudConfigClient::loadOrDefault);
    }

    public static void reload(UUID playerId) {
        CACHE.put(playerId, HudConfigClient.loadOrDefault(playerId));
    }

    public static void invalidate(UUID playerId) {
        CACHE.remove(playerId);
    }

    public static void reset(UUID playerId) {
        try {
            Files.deleteIfExists(HudConfigClient.fileFor(playerId));
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        CACHE.put(playerId, HudConfigClient.defaultConfig());
    }

    public static void save(UUID playerId, HudConfig cfg) {
        if (cfg == null) {
            return;
        }
        if (cfg.targetMode == null) {
            cfg.targetMode = TargetMode.ABOVE_HOTBAR;
        }
        if (cfg.batteryMode == null) {
            cfg.batteryMode = BatteryMode.ICON_PLUS_CAPACITY_PLUS_STATS;
        }
        CACHE.put(playerId, cfg.copy());
        Path file = HudConfigClient.fileFor(playerId);
        try {
            Files.createDirectories(file.getParent(), new FileAttribute[0]);
            JsonObject root = new JsonObject();
            root.addProperty("coordsEnabled", Boolean.valueOf(cfg.coordsEnabled));
            root.addProperty("toggleListEnabled", Boolean.valueOf(cfg.toggleListEnabled));
            root.addProperty("shardsEnabled", Boolean.valueOf(cfg.shardsEnabled));
            root.addProperty("targetMode", cfg.targetMode.name());
            root.addProperty("batteryMode", cfg.batteryMode.name());
            try (BufferedWriter w = Files.newBufferedWriter(file, new OpenOption[0]);){
                GSON.toJson((JsonElement)root, (Appendable)w);
            }
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private static HudConfig loadOrDefault(UUID playerId) {
        Path file = HudConfigClient.fileFor(playerId);
        if (!Files.exists(file, new LinkOption[0])) {
            return HudConfigClient.defaultConfig();
        }
        try (BufferedReader r = Files.newBufferedReader(file);){
            JsonObject obj = (JsonObject)GSON.fromJson((Reader)r, JsonObject.class);
            if (obj == null) {
                HudConfig hudConfig2 = HudConfigClient.defaultConfig();
                return hudConfig2;
            }
            HudConfig cfg = HudConfigClient.defaultConfig();
            if (obj.has("coordsEnabled")) {
                cfg.coordsEnabled = obj.get("coordsEnabled").getAsBoolean();
            }
            if (obj.has("toggleListEnabled")) {
                cfg.toggleListEnabled = obj.get("toggleListEnabled").getAsBoolean();
            }
            if (obj.has("shardsEnabled")) {
                cfg.shardsEnabled = obj.get("shardsEnabled").getAsBoolean();
            }
            if (obj.has("targetMode")) {
                cfg.targetMode = HudConfigClient.parseTargetMode(obj.get("targetMode").getAsString());
            }
            if (obj.has("batteryMode")) {
                cfg.batteryMode = HudConfigClient.parseBatteryMode(obj.get("batteryMode").getAsString());
            }
            if (cfg.targetMode == null) {
                cfg.targetMode = TargetMode.ABOVE_HOTBAR;
            }
            if (cfg.batteryMode == null) {
                cfg.batteryMode = BatteryMode.ICON_PLUS_CAPACITY_PLUS_STATS;
            }
            HudConfig hudConfig = cfg;
            return hudConfig;
        }
        catch (Throwable ignored) {
            return HudConfigClient.defaultConfig();
        }
    }

    private static TargetMode parseTargetMode(@Nullable String s) {
        if (s == null) {
            return TargetMode.ABOVE_HOTBAR;
        }
        try {
            return TargetMode.valueOf(s);
        }
        catch (Throwable ignored) {
            return TargetMode.ABOVE_HOTBAR;
        }
    }

    private static BatteryMode parseBatteryMode(@Nullable String s) {
        if (s == null) {
            return BatteryMode.ICON_PLUS_CAPACITY_PLUS_STATS;
        }
        try {
            return BatteryMode.valueOf(s);
        }
        catch (Throwable ignored) {
            return BatteryMode.ICON_PLUS_CAPACITY_PLUS_STATS;
        }
    }

    public static final class HudConfig {
        public boolean coordsEnabled;
        public boolean toggleListEnabled;
        public boolean shardsEnabled;
        public TargetMode targetMode;
        public BatteryMode batteryMode;

        public HudConfig(boolean coordsEnabled, boolean toggleListEnabled, boolean shardsEnabled, TargetMode targetMode, BatteryMode batteryMode) {
            this.coordsEnabled = coordsEnabled;
            this.toggleListEnabled = toggleListEnabled;
            this.shardsEnabled = shardsEnabled;
            this.targetMode = targetMode;
            this.batteryMode = batteryMode;
        }

        public HudConfig copy() {
            return new HudConfig(this.coordsEnabled, this.toggleListEnabled, this.shardsEnabled, this.targetMode, this.batteryMode);
        }
    }

    public static enum TargetMode {
        ABOVE_HOTBAR,
        UNDER_CROSSHAIR,
        OFF;

    }

    public static enum BatteryMode {
        TEXT_ONLY,
        ICON_ONLY,
        ICON_PLUS_CAPACITY,
        ICON_PLUS_CAPACITY_PLUS_STATS;

    }
}

