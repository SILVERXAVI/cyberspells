/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.Holder
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.item.armortrim.TrimMaterial
 */
package com.perigrine3.createcybernetics.client;

import java.util.Map;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.armortrim.TrimMaterial;

public final class TrimColorPresets {
    private static final Map<ResourceLocation, Integer> BY_ID = Map.ofEntries(Map.entry(ResourceLocation.withDefaultNamespace((String)"amethyst"), -6660922), Map.entry(ResourceLocation.withDefaultNamespace((String)"copper"), -4689101), Map.entry(ResourceLocation.withDefaultNamespace((String)"diamond"), -12723754), Map.entry(ResourceLocation.withDefaultNamespace((String)"emerald"), -15213214), Map.entry(ResourceLocation.withDefaultNamespace((String)"gold"), -863409), Map.entry(ResourceLocation.withDefaultNamespace((String)"iron"), -2565928), Map.entry(ResourceLocation.withDefaultNamespace((String)"lapis"), -14004781), Map.entry(ResourceLocation.withDefaultNamespace((String)"netherite"), -11911358), Map.entry(ResourceLocation.withDefaultNamespace((String)"quartz"), -1383464), Map.entry(ResourceLocation.withDefaultNamespace((String)"redstone"), -5242880));

    private TrimColorPresets() {
    }

    public static int colorFor(Holder<TrimMaterial> material) {
        if (material == null) {
            return -1;
        }
        ResourceLocation id = material.unwrapKey().map(k -> k.location()).orElse(null);
        if (id == null) {
            return -1;
        }
        return BY_ID.getOrDefault(id, -1);
    }
}

