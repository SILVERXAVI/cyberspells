/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  com.google.gson.GsonBuilder
 *  com.google.gson.JsonArray
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  net.minecraft.core.registries.BuiltInRegistries
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.packs.resources.ResourceManager
 *  net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener
 *  net.minecraft.util.profiling.ProfilerFiller
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.item.Item
 */
package com.maxwell.cyber_ware_port.api.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;

public class MobDataManager
extends SimpleJsonResourceReloadListener {
    public static final Map<EntityType<?>, MobData> MOB_DATA = new HashMap();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    public MobDataManager() {
        super(GSON, "cyberware/mobs");
    }

    protected void apply(Map<ResourceLocation, JsonElement> pObject, ResourceManager pResourceManager, ProfilerFiller pProfiler) {
        MOB_DATA.clear();
        pObject.forEach((location, element) -> {
            try {
                JsonObject json = element.getAsJsonObject();
                if (!json.has("mob")) {
                    return;
                }
                ResourceLocation mobId = ResourceLocation.parse((String)json.get("mob").getAsString());
                EntityType entityType = (EntityType)BuiltInRegistries.ENTITY_TYPE.get(mobId);
                if (entityType != BuiltInRegistries.ENTITY_TYPE.get(BuiltInRegistries.ENTITY_TYPE.getDefaultKey())) {
                    Item item;
                    JsonArray drops;
                    MobData data = new MobData();
                    if (json.has("replace_with")) {
                        ResourceLocation replaceId = ResourceLocation.parse((String)json.get("replace_with").getAsString());
                        data.replaceWith = (EntityType)BuiltInRegistries.ENTITY_TYPE.get(replaceId);
                    }
                    data.chance = json.has("chance") ? json.get("chance").getAsDouble() : 0.0;
                    boolean bl = data.isHighTier = json.has("is_high_tier") && json.get("is_high_tier").getAsBoolean();
                    if (json.has("special_drops")) {
                        drops = json.getAsJsonArray("special_drops");
                        for (JsonElement e : drops) {
                            item = (Item)BuiltInRegistries.ITEM.get(ResourceLocation.parse((String)e.getAsString()));
                            if (item == BuiltInRegistries.ITEM.get(BuiltInRegistries.ITEM.getDefaultKey())) continue;
                            data.specialDrops.add(item);
                        }
                    }
                    if (json.has("forbidden_drops")) {
                        drops = json.getAsJsonArray("forbidden_drops");
                        for (JsonElement e : drops) {
                            item = (Item)BuiltInRegistries.ITEM.get(ResourceLocation.parse((String)e.getAsString()));
                            if (item == BuiltInRegistries.ITEM.get(BuiltInRegistries.ITEM.getDefaultKey())) continue;
                            data.forbiddenDrops.add(item);
                        }
                    }
                    MOB_DATA.put(entityType, data);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public static class MobData {
        public EntityType<?> replaceWith;
        public double chance;
        public List<Item> specialDrops = new ArrayList<Item>();
        public List<Item> forbiddenDrops = new ArrayList<Item>();
        public boolean isHighTier = false;
    }
}

