/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.Holder
 *  net.minecraft.core.registries.BuiltInRegistries
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.entity.ai.attributes.Attribute
 *  net.minecraft.world.entity.ai.attributes.AttributeInstance
 *  net.minecraft.world.entity.ai.attributes.AttributeModifier
 *  net.minecraft.world.entity.ai.attributes.AttributeModifier$Operation
 *  net.minecraft.world.entity.player.Player
 *  net.neoforged.fml.ModList
 */
package com.perigrine3.createcybernetics.compat.coldsweat;

import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.neoforged.fml.ModList;

public final class ColdSweatCompat {
    public static final String MODID = "cold_sweat";
    private static final ResourceLocation HEAT_RESIST_ID = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"cc_heat_resist");
    private static final ResourceLocation COLD_RESIST_ID = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"cc_cold_resist");
    private static final ResourceLocation HEAT_DAMPEN_ID = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"cc_heat_dampen");
    private static final ResourceLocation COLD_DAMPEN_ID = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"cc_cold_dampen");
    private static Holder<Attribute> heatResistAttr;
    private static Holder<Attribute> coldResistAttr;
    private static Holder<Attribute> heatDampenAttr;
    private static Holder<Attribute> coldDampenAttr;
    private static boolean resolved;

    private ColdSweatCompat() {
    }

    public static boolean isLoaded() {
        return ModList.get().isLoaded(MODID);
    }

    public static void applyHeatResistance(Player player, double amount0to1) {
        if (!ColdSweatCompat.isLoaded() || player == null) {
            return;
        }
        ColdSweatCompat.resolveOnce();
        if (heatResistAttr == null) {
            return;
        }
        ColdSweatCompat.applyOrUpdate(player, heatResistAttr, HEAT_RESIST_ID, ColdSweatCompat.clamp01(amount0to1));
    }

    public static void applyColdResistance(Player player, double amount0to1) {
        if (!ColdSweatCompat.isLoaded() || player == null) {
            return;
        }
        ColdSweatCompat.resolveOnce();
        if (coldResistAttr == null) {
            return;
        }
        ColdSweatCompat.applyOrUpdate(player, coldResistAttr, COLD_RESIST_ID, ColdSweatCompat.clamp01(amount0to1));
    }

    public static void applyHeatDampening(Player player, double dampening) {
        if (!ColdSweatCompat.isLoaded() || player == null) {
            return;
        }
        ColdSweatCompat.resolveOnce();
        if (heatDampenAttr == null) {
            return;
        }
        ColdSweatCompat.applyOrUpdate(player, heatDampenAttr, HEAT_DAMPEN_ID, ColdSweatCompat.clampDampening(dampening));
    }

    public static void applyColdDampening(Player player, double dampening) {
        if (!ColdSweatCompat.isLoaded() || player == null) {
            return;
        }
        ColdSweatCompat.resolveOnce();
        if (coldDampenAttr == null) {
            return;
        }
        ColdSweatCompat.applyOrUpdate(player, coldDampenAttr, COLD_DAMPEN_ID, ColdSweatCompat.clampDampening(dampening));
    }

    public static void clearHeat(Player player) {
        if (!ColdSweatCompat.isLoaded() || player == null) {
            return;
        }
        ColdSweatCompat.resolveOnce();
        if (heatResistAttr != null) {
            ColdSweatCompat.remove(player, heatResistAttr, HEAT_RESIST_ID);
        }
        if (heatDampenAttr != null) {
            ColdSweatCompat.remove(player, heatDampenAttr, HEAT_DAMPEN_ID);
        }
    }

    public static void clearCold(Player player) {
        if (!ColdSweatCompat.isLoaded() || player == null) {
            return;
        }
        ColdSweatCompat.resolveOnce();
        if (coldResistAttr != null) {
            ColdSweatCompat.remove(player, coldResistAttr, COLD_RESIST_ID);
        }
        if (coldDampenAttr != null) {
            ColdSweatCompat.remove(player, coldDampenAttr, COLD_DAMPEN_ID);
        }
    }

    private static void applyOrUpdate(Player player, Holder<Attribute> attr, ResourceLocation id, double amount) {
        AttributeInstance inst = player.getAttribute(attr);
        if (inst == null) {
            return;
        }
        AttributeModifier existing = inst.getModifier(id);
        if (existing != null) {
            if (Double.compare(existing.amount(), amount) == 0 && existing.operation() == AttributeModifier.Operation.ADD_VALUE) {
                return;
            }
            inst.removeModifier(id);
        }
        inst.addPermanentModifier(new AttributeModifier(id, amount, AttributeModifier.Operation.ADD_VALUE));
    }

    private static void remove(Player player, Holder<Attribute> attr, ResourceLocation id) {
        AttributeInstance inst = player.getAttribute(attr);
        if (inst == null) {
            return;
        }
        if (inst.getModifier(id) != null) {
            inst.removeModifier(id);
        }
    }

    private static void resolveOnce() {
        if (resolved) {
            return;
        }
        resolved = true;
        heatResistAttr = ColdSweatCompat.resolveAttribute("heat_resistance");
        coldResistAttr = ColdSweatCompat.resolveAttribute("cold_resistance");
        heatDampenAttr = ColdSweatCompat.resolveAttribute("heat_dampening");
        coldDampenAttr = ColdSweatCompat.resolveAttribute("cold_dampening");
        if (heatResistAttr == null) {
            heatResistAttr = ColdSweatCompat.resolveAttributeContains("heat_resist");
        }
        if (coldResistAttr == null) {
            coldResistAttr = ColdSweatCompat.resolveAttributeContains("cold_resist");
        }
        if (heatDampenAttr == null) {
            heatDampenAttr = ColdSweatCompat.resolveAttributeContains("heat_dampen");
        }
        if (coldDampenAttr == null) {
            coldDampenAttr = ColdSweatCompat.resolveAttributeContains("cold_dampen");
        }
    }

    private static Holder<Attribute> resolveAttribute(String path) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath((String)MODID, (String)path);
        Optional holder = BuiltInRegistries.ATTRIBUTE.getHolder(id);
        return holder.orElse(null);
    }

    private static Holder<Attribute> resolveAttributeContains(String needle) {
        for (ResourceLocation id : BuiltInRegistries.ATTRIBUTE.keySet()) {
            if (!MODID.equals(id.getNamespace()) || !id.getPath().contains(needle)) continue;
            return BuiltInRegistries.ATTRIBUTE.getHolder(id).orElse(null);
        }
        return null;
    }

    private static double clamp01(double v) {
        if (v < 0.0) {
            return 0.0;
        }
        if (v > 1.0) {
            return 1.0;
        }
        return v;
    }

    private static double clampDampening(double v) {
        return v > 1.0 ? 1.0 : v;
    }

    static {
        resolved = false;
    }
}

