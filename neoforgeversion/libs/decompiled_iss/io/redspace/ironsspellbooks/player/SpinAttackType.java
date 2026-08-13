/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.resources.ResourceLocation
 */
package io.redspace.ironsspellbooks.player;

import net.minecraft.resources.ResourceLocation;

public record SpinAttackType(ResourceLocation textureId, boolean fullbright) {
    public static final SpinAttackType FIRE = new SpinAttackType(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"textures/entity/fire_riptide.png"), true);
    public static final SpinAttackType LIGHTNING = new SpinAttackType(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"textures/entity/lightning_riptide.png"), true);
    public static final SpinAttackType RIPTIDE = new SpinAttackType(ResourceLocation.withDefaultNamespace((String)"textures/entity/trident_riptide.png"), false);
}

