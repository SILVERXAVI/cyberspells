/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableMap
 *  com.google.common.collect.ImmutableMap$Builder
 *  com.google.gson.Gson
 *  com.google.gson.GsonBuilder
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  com.mojang.datafixers.util.Pair
 *  com.mojang.serialization.Codec
 *  com.mojang.serialization.DataResult
 *  com.mojang.serialization.DynamicOps
 *  com.mojang.serialization.JsonOps
 *  javax.annotation.Nullable
 *  net.minecraft.ChatFormatting
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.MinecraftServer
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.server.packs.resources.ResourceManager
 *  net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener
 *  net.minecraft.util.profiling.ProfilerFiller
 *  net.minecraft.world.entity.player.Player
 *  net.neoforged.bus.api.Event
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.fml.loading.FMLPaths
 *  net.neoforged.neoforge.common.NeoForge
 *  net.neoforged.neoforge.common.conditions.ConditionalOps
 *  net.neoforged.neoforge.event.OnDatapackSyncEvent
 *  net.neoforged.neoforge.network.PacketDistributor
 */
package io.redspace.ironsspellbooks.api.config;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.config.ModifyDefaultConfigValuesEvent;
import io.redspace.ironsspellbooks.api.config.RegisterConfigParametersEvent;
import io.redspace.ironsspellbooks.api.config.SpellConfigHolder;
import io.redspace.ironsspellbooks.api.config.SpellConfigParameter;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.network.SyncJsonConfigPacket;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.conditions.ConditionalOps;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber
public class SpellConfigManager
extends SimpleJsonResourceReloadListener {
    public static final String SUBCONFIG_FOLDER = "irons_spellbooks_spell_config";
    public static SpellConfigManager INSTANCE = new SpellConfigManager();
    private final Gson gson;
    @Nullable
    private Map<ResourceLocation, JsonElement> datapackOverride = null;
    private ImmutableMap<AbstractSpell, SpellConfigHolder> config = ImmutableMap.of();
    public static final Set<SpellConfigParameter<?>> ALL_TYPES = new HashSet();
    private static boolean registered = false;
    private boolean dirty = true;

    public static SpellConfigManager getInstance() {
        return INSTANCE;
    }

    public static <T> T getSpellConfigValue(AbstractSpell spell, SpellConfigParameter<T> parameterType) {
        if (!SpellConfigManager.INSTANCE.config.containsKey((Object)spell)) {
            return parameterType.defaultValue().get();
        }
        return ((SpellConfigHolder)SpellConfigManager.INSTANCE.config.get((Object)spell)).get(parameterType);
    }

    public static <T> T getSpellDefaultConfigValue(AbstractSpell spell, SpellConfigParameter<T> parameterType) {
        if (!SpellConfigManager.INSTANCE.config.containsKey((Object)spell)) {
            return parameterType.defaultValue().get();
        }
        return ((SpellConfigHolder)SpellConfigManager.INSTANCE.config.get((Object)spell)).getDefaultValue(parameterType).orElse(parameterType.defaultValue().get());
    }

    public SpellConfigManager() {
        super(new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create(), SUBCONFIG_FOLDER);
        this.gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    }

    protected void apply(Map<ResourceLocation, JsonElement> object, ResourceManager resourceManager, ProfilerFiller profiler) {
        if (!object.isEmpty()) {
            HashMap<ResourceLocation, JsonElement> data = new HashMap<ResourceLocation, JsonElement>(object.size());
            for (Map.Entry<ResourceLocation, JsonElement> entry : object.entrySet()) {
                ResourceLocation key = entry.getKey();
                if (key.getPath().contains("/")) {
                    String[] path = key.getPath().split("/");
                    key = ResourceLocation.fromNamespaceAndPath((String)key.getNamespace(), (String)path[path.length - 1]);
                }
                data.put(key, entry.getValue());
            }
            this.datapackOverride = data;
        }
        this.handleServerConfigUpdate();
    }

    public void handleServerConfigUpdate() {
        SpellConfigManager.registerConfigParameterTypes();
        SpellConfigManager.initiateDefaultFiles(this.gson);
        for (AbstractSpell spell : SpellRegistry.REGISTRY) {
            spell.resetRarityWeights();
        }
        this.dirty = true;
    }

    public void handleClientSync(SyncJsonConfigPacket packet) {
        IronsSpellbooks.LOGGER.info("Handling spell config sync {} files", (Object)packet.data.size());
        this.handleServerConfigUpdate();
        this.buildConfigManager(this.toJson(packet.data));
    }

    @SubscribeEvent
    public static void onDatapackSync(OnDatapackSyncEvent event) {
        MinecraftServer server = event.getPlayerList().getServer();
        ServerPlayer player = event.getPlayer();
        boolean noErrors = true;
        if (SpellConfigManager.INSTANCE.dirty) {
            SpellConfigManager.INSTANCE.dirty = false;
            if (SpellConfigManager.INSTANCE.datapackOverride != null) {
                noErrors = INSTANCE.buildConfigManager(SpellConfigManager.INSTANCE.datapackOverride);
                SpellConfigManager.INSTANCE.datapackOverride = null;
            } else {
                noErrors = INSTANCE.buildConfigManager(INSTANCE.toJson(SpellConfigManager.getConfigFiles(SpellConfigManager.resolveConfigDirectory(server))));
            }
        }
        if (SpellConfigManager.INSTANCE.config != null) {
            if (player != null) {
                PacketDistributor.sendToPlayer((ServerPlayer)player, (CustomPacketPayload)new SyncJsonConfigPacket(INSTANCE.createNetworkData()), (CustomPacketPayload[])new CustomPacketPayload[0]);
                if (!noErrors) {
                    player.displayClientMessage((Component)Component.translatable((String)"commands.irons_spellbooks.config_load_errors").withStyle(ChatFormatting.RED), false);
                }
            } else {
                PacketDistributor.sendToAllPlayers((CustomPacketPayload)new SyncJsonConfigPacket(INSTANCE.createNetworkData()), (CustomPacketPayload[])new CustomPacketPayload[0]);
                if (!noErrors) {
                    for (Player p : server.getPlayerList().getPlayers()) {
                        p.displayClientMessage((Component)Component.translatable((String)"commands.irons_spellbooks.config_load_errors").withStyle(ChatFormatting.RED), false);
                    }
                }
            }
        } else {
            IronsSpellbooks.LOGGER.warn("Failed to sync config to players, instance is null");
        }
    }

    private static File resolveConfigDirectory(MinecraftServer server) {
        return FMLPaths.CONFIGDIR.get().resolve(SUBCONFIG_FOLDER).toFile();
    }

    private static byte[] readBytes(File file) {
        byte[] byArray;
        FileReader reader = new FileReader(file);
        try {
            int n;
            StringBuilder sb = new StringBuilder();
            char[] buf = new char[4096];
            while ((n = reader.read(buf)) != -1) {
                sb.append(buf, 0, n);
            }
            byArray = sb.toString().replaceAll("[ \n]", "").getBytes(StandardCharsets.UTF_8);
        }
        catch (Throwable throwable) {
            try {
                try {
                    reader.close();
                }
                catch (Throwable throwable2) {
                    throwable.addSuppressed(throwable2);
                }
                throw throwable;
            }
            catch (Exception e) {
                IronsSpellbooks.LOGGER.error("Failed to read config file: {}", (Throwable)e);
                return new byte[0];
            }
        }
        reader.close();
        return byArray;
    }

    private static List<File> expandAllJsonFiles(File directory) {
        ArrayList<File> files = new ArrayList<File>();
        File[] sub = directory.listFiles();
        if (sub == null) {
            return List.of();
        }
        for (File file : sub) {
            if (file.getName().endsWith(".json")) {
                files.add(file);
                continue;
            }
            if (!file.isDirectory()) continue;
            files.addAll(SpellConfigManager.expandAllJsonFiles(file));
        }
        return files;
    }

    private static Map<ResourceLocation, byte[]> getConfigFiles(File directory) {
        HashMap<ResourceLocation, byte[]> files = new HashMap<ResourceLocation, byte[]>();
        long milis = System.currentTimeMillis();
        File[] namespacedDirectories = directory.listFiles();
        if (namespacedDirectories != null) {
            for (File namespacedDir : namespacedDirectories) {
                if (namespacedDir.isDirectory()) {
                    String namespace = namespacedDir.getName();
                    List<File> entries = SpellConfigManager.expandAllJsonFiles(namespacedDir);
                    for (File entry : entries) {
                        String spellName = entry.getName().split("\\.")[0];
                        ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath((String)namespace, (String)spellName);
                        if (SpellRegistry.REGISTRY.containsKey(spellId)) {
                            if (files.containsKey(spellId)) {
                                IronsSpellbooks.LOGGER.warn("Duplicate spell config for spell {}! Overriding config.", (Object)spellId);
                            }
                            files.put(spellId, SpellConfigManager.readBytes(entry));
                            continue;
                        }
                        IronsSpellbooks.LOGGER.warn("Unknown Spell for Configuration file \"{}:{}\", will be ignored!", (Object)namespace, (Object)spellName);
                    }
                    continue;
                }
                if (!namespacedDir.getName().endsWith(".json")) continue;
                IronsSpellbooks.LOGGER.warn("Spell Configuration file \"{}\", outside of namespaced directory, will be ignored!", (Object)namespacedDir.getName());
            }
        }
        if (!files.isEmpty()) {
            IronsSpellbooks.LOGGER.info("Read {} spell config files ({} ms)", (Object)files.size(), (Object)(System.currentTimeMillis() - milis));
        }
        return files;
    }

    private Map<ResourceLocation, byte[]> createNetworkData() {
        HashMap<ResourceLocation, byte[]> data = new HashMap<ResourceLocation, byte[]>();
        for (Map.Entry entry : this.config.entrySet()) {
            JsonObject json = ((SpellConfigHolder)entry.getValue()).toJson(this.gson);
            if (json.isEmpty()) continue;
            data.put(((AbstractSpell)entry.getKey()).getSpellResource(), json.toString().getBytes(StandardCharsets.UTF_8));
        }
        return data;
    }

    private static void registerConfigParameterTypes() {
        if (!registered) {
            registered = true;
            ALL_TYPES.add(SpellConfigParameter.SCHOOL);
            ALL_TYPES.add(SpellConfigParameter.MIN_RARITY);
            ALL_TYPES.add(SpellConfigParameter.MAX_LEVEL);
            ALL_TYPES.add(SpellConfigParameter.ENABLED);
            ALL_TYPES.add(SpellConfigParameter.COOLDOWN_IN_SECONDS);
            ALL_TYPES.add(SpellConfigParameter.ALLOW_CRAFTING);
            ALL_TYPES.add(SpellConfigParameter.MANA_MULTIPLIER);
            ALL_TYPES.add(SpellConfigParameter.POWER_MULTIPLIER);
            NeoForge.EVENT_BUS.post((Event)new RegisterConfigParametersEvent(ALL_TYPES::add));
        }
    }

    private Map<ResourceLocation, JsonElement> toJson(Map<ResourceLocation, byte[]> filestreams) {
        HashMap<ResourceLocation, JsonElement> configEntries = new HashMap<ResourceLocation, JsonElement>();
        for (Map.Entry<ResourceLocation, byte[]> entry : filestreams.entrySet()) {
            ResourceLocation id = entry.getKey();
            byte[] file = entry.getValue();
            try {
                JsonObject obj = (JsonObject)this.gson.fromJson((Reader)new InputStreamReader(new ByteArrayInputStream(file)), JsonObject.class);
                configEntries.put(id, (JsonElement)obj);
            }
            catch (Exception e) {
                IronsSpellbooks.LOGGER.error("Failed to parse config file \"{}\": {}", (Object)id, (Object)e.getMessage());
            }
        }
        return configEntries;
    }

    private boolean buildConfigManager(Map<ResourceLocation, JsonElement> configEntries) {
        boolean hasErrors = false;
        ImmutableMap.Builder builder = ImmutableMap.builder();
        ConditionalOps registryops = this.makeConditionalOps();
        for (AbstractSpell spell : SpellRegistry.REGISTRY) {
            SpellConfigHolder config = new SpellConfigHolder();
            DefaultConfig raw = spell.getDefaultConfig();
            config.setDefaultValue(SpellConfigParameter.SCHOOL, SchoolRegistry.getSchool(raw.schoolResource));
            config.setDefaultValue(SpellConfigParameter.MIN_RARITY, raw.minRarity);
            config.setDefaultValue(SpellConfigParameter.MAX_LEVEL, raw.maxLevel);
            config.setDefaultValue(SpellConfigParameter.ENABLED, raw.enabled);
            config.setDefaultValue(SpellConfigParameter.COOLDOWN_IN_SECONDS, raw.cooldownInSeconds);
            config.setDefaultValue(SpellConfigParameter.ALLOW_CRAFTING, raw.allowCrafting);
            ResourceLocation spellId = spell.getSpellResource();
            if (configEntries.containsKey(spellId)) {
                try {
                    JsonObject json = configEntries.get(spellId).getAsJsonObject();
                    for (SpellConfigParameter<?> paramType : ALL_TYPES) {
                        Optional<JsonElement> elem = SpellConfigManager.resolveJsonElement(spellId, paramType, json);
                        if (!elem.isPresent()) continue;
                        try {
                            Object decoded = ((Pair)paramType.datatype().decode((DynamicOps)registryops, (Object)elem.get()).getOrThrow()).getFirst();
                            config.set(paramType, decoded);
                        }
                        catch (Exception e) {
                            IronsSpellbooks.LOGGER.error("Parsing error loading spell config \"{}\" value for \"{}\": {}", new Object[]{spellId, paramType.key(), e.getLocalizedMessage()});
                            hasErrors = true;
                        }
                    }
                }
                catch (IllegalStateException e) {
                    IronsSpellbooks.LOGGER.error("Parsing error loading spell config {}: {}", (Object)spellId, (Object)e);
                    hasErrors = true;
                }
            }
            builder.put((Object)spell, (Object)config);
        }
        this.config = builder.build();
        for (AbstractSpell spell : SpellRegistry.REGISTRY) {
            NeoForge.EVENT_BUS.post((Event)new ModifyDefaultConfigValuesEvent(spell, (SpellConfigHolder)this.config.get((Object)spell)));
        }
        return !hasErrors;
    }

    public static File getSpellConfigDir() {
        Path configDir = FMLPaths.CONFIGDIR.get();
        Path spellConfigDir = configDir.resolve(SUBCONFIG_FOLDER);
        File folder = spellConfigDir.toFile();
        if (!folder.exists()) {
            folder.mkdir();
        }
        return folder;
    }

    private static File initiateDefaultFiles(Gson gson) {
        File spellConfigDir = SpellConfigManager.getSpellConfigDir();
        File spellbookDir = spellConfigDir.toPath().resolve("irons_spellbooks").toFile();
        if (!spellbookDir.exists()) {
            spellbookDir.mkdir();
            SpellConfigManager.createExampleConfig(gson, spellbookDir.toPath().resolve("example.txt").toFile());
        }
        return spellbookDir;
    }

    private static Optional<JsonElement> resolveJsonElement(ResourceLocation spellId, SpellConfigParameter<?> dataType, JsonObject parent) {
        if (parent.has(dataType.key().toString())) {
            return Optional.of(parent.get(dataType.key().toString()));
        }
        if (parent.has(dataType.key().getPath())) {
            if (!dataType.key().getNamespace().equals("irons_spellbooks")) {
                IronsSpellbooks.LOGGER.warn("Config for {} has ambiguous entry \"{}\", adapting to \"{}\"", new Object[]{spellId, dataType.key().getPath(), dataType.key()});
            }
            return Optional.of(parent.get(dataType.key().getPath()));
        }
        return Optional.empty();
    }

    public static <T> Pair<Boolean, File> createExampleConfig(Gson gson, File file) {
        Pair pair;
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("_comment1", "Config Files must be placed in a directory labeled with their mod id, and the file name must match the spell id!");
        jsonObject.addProperty("_comment2", "For global config: /config/irons_spellbooks_spell_config/<mod_id>/<spell_id>.json");
        jsonObject.addProperty("_comment3", "For datapacks: /data/<mod_id>/irons_spellbooks_spell_config/<spell_id>.json");
        Iterator<SpellConfigParameter<?>> iterator = ALL_TYPES.iterator();
        while (iterator.hasNext()) {
            SpellConfigParameter<?> _param;
            SpellConfigParameter<?> param = _param = iterator.next();
            Codec<?> codec = param.datatype();
            DataResult result = codec.encodeStart((DynamicOps)JsonOps.INSTANCE, param.defaultValue().get());
            jsonObject.add(param.key().toString(), gson.toJsonTree(result.getOrThrow()));
        }
        FileWriter writer = new FileWriter(file);
        try {
            gson.toJson((JsonElement)jsonObject, (Appendable)writer);
            pair = Pair.of((Object)true, (Object)file);
        }
        catch (Throwable throwable) {
            try {
                try {
                    writer.close();
                }
                catch (Throwable throwable2) {
                    throwable.addSuppressed(throwable2);
                }
                throw throwable;
            }
            catch (IOException e) {
                IronsSpellbooks.LOGGER.error("Failed to write default config file {}: {}", (Object)file.getPath(), (Object)e.getMessage());
                return Pair.of((Object)false, null);
            }
        }
        writer.close();
        return pair;
    }

    public static <T> Pair<Boolean, File> generateSpellConfigFile(Gson gson, AbstractSpell spell, boolean full, boolean override) {
        ResourceLocation resourceLocation = spell.getSpellResource();
        try {
            File spellConfig;
            File spellConfigDir = SpellConfigManager.getSpellConfigDir();
            File modDir = spellConfigDir.toPath().resolve(resourceLocation.getNamespace()).toFile();
            if (!modDir.exists()) {
                modDir.mkdir();
            }
            if ((spellConfig = modDir.toPath().resolve(resourceLocation.getPath() + ".json").toFile()).exists() && !override) {
                return Pair.of((Object)false, (Object)spellConfig);
            }
            JsonObject json = new JsonObject();
            if (full) {
                Iterator<SpellConfigParameter<?>> iterator = ALL_TYPES.iterator();
                while (iterator.hasNext()) {
                    SpellConfigParameter<?> _param;
                    SpellConfigParameter<?> param = _param = iterator.next();
                    Codec<?> codec = param.datatype();
                    DataResult result = codec.encodeStart((DynamicOps)JsonOps.INSTANCE, SpellConfigManager.getSpellDefaultConfigValue(spell, param));
                    json.add(param.key().toString(), gson.toJsonTree(result.getOrThrow()));
                }
            }
            try (FileWriter writer = new FileWriter(spellConfig);){
                gson.toJson((JsonElement)json, (Appendable)writer);
            }
            return Pair.of((Object)true, (Object)spellConfig);
        }
        catch (Exception e) {
            IronsSpellbooks.LOGGER.error("Could not generate config file: {}", (Object)e.getMessage());
            return Pair.of((Object)false, null);
        }
    }
}

