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
 *  net.minecraft.world.entity.ai.attributes.Attribute
 *  net.minecraft.world.entity.ai.attributes.AttributeModifier
 *  net.minecraft.world.entity.ai.attributes.AttributeModifier$Operation
 *  net.minecraft.world.item.Item
 */
package com.maxwell.cyber_ware_port.api.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.maxwell.cyber_ware_port.api.json.CyberwareData;
import com.maxwell.cyber_ware_port.api.json.DynamicCyberwareWrapper;
import com.maxwell.cyber_ware_port.common.block.robosurgeon.BodyRegionEnum;
import com.maxwell.cyber_ware_port.common.item.base.ICyberware;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;

public class CyberwareDataManager
extends SimpleJsonResourceReloadListener {
    public static final Map<Item, ICyberware> DYNAMIC_CYBERWARE = new HashMap<Item, ICyberware>();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    public CyberwareDataManager() {
        super(GSON, "cyberware");
    }

    protected void apply(Map<ResourceLocation, JsonElement> pObject, ResourceManager pResourceManager, ProfilerFiller pProfiler) {
        DYNAMIC_CYBERWARE.clear();
        pObject.forEach((location, element) -> {
            try {
                JsonObject json = element.getAsJsonObject();
                if (!json.has("item")) {
                    return;
                }
                ResourceLocation itemId = ResourceLocation.parse((String)json.get("item").getAsString());
                Item item = (Item)BuiltInRegistries.ITEM.get(itemId);
                if (item != null && item != BuiltInRegistries.ITEM.get(BuiltInRegistries.ITEM.getDefaultKey())) {
                    if (!json.has("slot")) {
                        return;
                    }
                    CyberwareData data = new CyberwareData();
                    String slotStr = json.get("slot").getAsString().toUpperCase();
                    data.slotId = BodyRegionEnum.valueOf(slotStr).getStartSlot();
                    data.essence = json.has("essence") ? json.get("essence").getAsInt() : 20;
                    int n = data.maxInstall = json.has("max_install") ? json.get("max_install").getAsInt() : 1;
                    if (json.has("attributes")) {
                        JsonArray attrs = json.getAsJsonArray("attributes");
                        Iterator iterator = attrs.iterator();
                        while (iterator.hasNext()) {
                            JsonElement attrElement = (JsonElement)iterator.next();
                            JsonObject attrObj = attrElement.getAsJsonObject();
                            ResourceLocation attrId = ResourceLocation.parse((String)attrObj.get("attribute").getAsString());
                            Attribute attr = (Attribute)BuiltInRegistries.ATTRIBUTE.get(attrId);
                            if (attr == null) continue;
                            double amount = attrObj.get("amount").getAsDouble();
                            AttributeModifier.Operation op = AttributeModifier.Operation.valueOf((String)attrObj.get("operation").getAsString().toUpperCase());
                            ResourceLocation modId = ResourceLocation.fromNamespaceAndPath((String)"cyber_ware_port", (String)("dynamic_" + location.getPath().replace("/", "_")));
                            data.attributeModifiers.put((Object)BuiltInRegistries.ATTRIBUTE.wrapAsHolder((Object)attr), (Object)new AttributeModifier(modId, amount, op));
                        }
                    }
                    if (json.has("incompatible")) {
                        for (JsonElement e : json.getAsJsonArray("incompatible")) {
                            Item incomp = (Item)BuiltInRegistries.ITEM.get(ResourceLocation.parse((String)e.getAsString()));
                            if (incomp == BuiltInRegistries.ITEM.get(BuiltInRegistries.ITEM.getDefaultKey())) continue;
                            data.incompatibleItems.add(incomp);
                        }
                    }
                    if (json.has("stacking")) {
                        String ruleStr = json.get("stacking").getAsString().toUpperCase();
                        data.stackingRule = ICyberware.StackingRule.valueOf(ruleStr);
                    }
                    if (json.has("has_energy")) {
                        data.hasEnergyProperties = json.get("has_energy").getAsBoolean();
                    } else if (json.has("energy_consumption") || json.has("energy_generation") || json.has("energy_storage")) {
                        data.hasEnergyProperties = true;
                    }
                    if (json.has("energy_consumption")) {
                        data.energyConsumption = json.get("energy_consumption").getAsInt();
                    }
                    if (json.has("energy_generation")) {
                        data.energyGeneration = json.get("energy_generation").getAsInt();
                    }
                    if (json.has("energy_storage")) {
                        data.energyStorage = json.get("energy_storage").getAsInt();
                    }
                    DYNAMIC_CYBERWARE.put(item, new DynamicCyberwareWrapper(data));
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}

