/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.resources.ResourceLocation
 */
package com.perigrine3.createcybernetics.client.toggle;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.resources.ResourceLocation;

public final class CyberwareToggleClientState {
    private static final Map<ResourceLocation, Boolean> STATES = new ConcurrentHashMap<ResourceLocation, Boolean>();

    private CyberwareToggleClientState() {
    }

    public static boolean isActive(ResourceLocation id) {
        return STATES.getOrDefault(id, true);
    }

    public static void setActive(ResourceLocation id, boolean active) {
        STATES.put(id, active);
    }

    public static void clear() {
        STATES.clear();
    }
}

