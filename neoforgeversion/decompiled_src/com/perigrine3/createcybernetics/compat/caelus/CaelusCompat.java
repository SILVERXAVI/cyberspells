/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.Holder
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.entity.ai.attributes.Attribute
 *  net.minecraft.world.entity.ai.attributes.AttributeInstance
 *  net.minecraft.world.entity.ai.attributes.AttributeModifier
 *  net.minecraft.world.entity.ai.attributes.AttributeModifier$Operation
 *  net.minecraft.world.entity.player.Player
 *  net.neoforged.fml.ModList
 */
package com.perigrine3.createcybernetics.compat.caelus;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.neoforged.fml.ModList;

public final class CaelusCompat {
    public static final String MODID = "caelus";
    private static Holder<Attribute> fallFlyingAttr;
    private static boolean resolved;

    private CaelusCompat() {
    }

    public static boolean isLoaded() {
        return ModList.get().isLoaded(MODID);
    }

    public static AttributeInstance getFallFlyingInstance(Player player) {
        if (!CaelusCompat.isLoaded() || player == null) {
            return null;
        }
        CaelusCompat.resolveOnce();
        if (fallFlyingAttr == null) {
            return null;
        }
        return player.getAttribute(fallFlyingAttr);
    }

    public static void addOrUpdateFallFlyingTransient(Player player, ResourceLocation modifierId, double amount) {
        AttributeInstance inst = CaelusCompat.getFallFlyingInstance(player);
        if (inst == null) {
            return;
        }
        inst.addOrUpdateTransientModifier(new AttributeModifier(modifierId, amount, AttributeModifier.Operation.ADD_VALUE));
    }

    public static void removeFallFlyingModifier(Player player, ResourceLocation modifierId) {
        AttributeInstance inst = CaelusCompat.getFallFlyingInstance(player);
        if (inst == null) {
            return;
        }
        inst.removeModifier(modifierId);
    }

    private static void resolveOnce() {
        if (resolved) {
            return;
        }
        resolved = true;
        try {
            Class<?> apiClass = Class.forName("com.illusivesoulworks.caelus.api.CaelusApi");
            Object api = apiClass.getMethod("getInstance", new Class[0]).invoke(null, new Object[0]);
            Object holderObj = apiClass.getMethod("getFallFlyingAttribute", new Class[0]).invoke(api, new Object[0]);
            fallFlyingAttr = (Holder)holderObj;
        }
        catch (Throwable t) {
            fallFlyingAttr = null;
        }
    }

    static {
        resolved = false;
    }
}

