/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.resources.PlayerSkin$Model
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.util.FastColor$ARGB32
 *  net.minecraft.world.entity.HumanoidArm
 */
package com.perigrine3.createcybernetics.client.skin;

import java.util.EnumSet;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.HumanoidArm;

public class SkinModifier {
    private final ResourceLocation wideTexture;
    private final ResourceLocation slimTexture;
    private final int color;
    private final boolean hideVanillaLayers;
    private final EnumSet<HideVanilla> hideMask;
    private final EnumSet<HumanoidArm> replaceVanillaArms;
    private final boolean needsPlayerSkinUnderlay;
    private final boolean glint;

    public SkinModifier(ResourceLocation wideTexture, ResourceLocation slimTexture) {
        this(wideTexture, slimTexture, FastColor.ARGB32.color((int)255, (int)255, (int)255, (int)255), false, false, EnumSet.noneOf(HideVanilla.class), EnumSet.noneOf(HumanoidArm.class), false);
    }

    public SkinModifier(ResourceLocation wideTexture, ResourceLocation slimTexture, int color, boolean hideVanillaLayers) {
        this(wideTexture, slimTexture, color, false, hideVanillaLayers, EnumSet.noneOf(HideVanilla.class), EnumSet.noneOf(HumanoidArm.class), false);
    }

    public SkinModifier(ResourceLocation wideTexture, ResourceLocation slimTexture, int color, boolean hideVanillaLayers, EnumSet<HideVanilla> hideMask) {
        this(wideTexture, slimTexture, color, false, hideVanillaLayers, hideMask, EnumSet.noneOf(HumanoidArm.class), false);
    }

    public SkinModifier(ResourceLocation wideTexture, ResourceLocation slimTexture, int color, boolean hideVanillaLayers, EnumSet<HideVanilla> hideMask, EnumSet<HumanoidArm> replaceVanillaArms) {
        this(wideTexture, slimTexture, color, false, hideVanillaLayers, hideMask, replaceVanillaArms, false);
    }

    public SkinModifier(ResourceLocation wideTexture, ResourceLocation slimTexture, int color, boolean glint, boolean hideVanillaLayers, EnumSet<HideVanilla> hideMask, EnumSet<HumanoidArm> replaceVanillaArms, boolean needsPlayerSkinUnderlay) {
        this.wideTexture = wideTexture;
        this.slimTexture = slimTexture;
        this.color = color;
        this.glint = glint;
        this.hideVanillaLayers = hideVanillaLayers;
        this.hideMask = hideMask == null ? EnumSet.noneOf(HideVanilla.class) : hideMask.clone();
        this.replaceVanillaArms = replaceVanillaArms == null ? EnumSet.noneOf(HumanoidArm.class) : replaceVanillaArms.clone();
        this.needsPlayerSkinUnderlay = needsPlayerSkinUnderlay;
    }

    public ResourceLocation getTexture(PlayerSkin.Model modelType) {
        return modelType == PlayerSkin.Model.SLIM ? this.slimTexture : this.wideTexture;
    }

    public int getColor() {
        return this.color;
    }

    public boolean shouldHideVanillaLayers() {
        return this.hideVanillaLayers;
    }

    public EnumSet<HideVanilla> getHideMask() {
        return this.hideMask.clone();
    }

    public boolean replacesVanillaArm(HumanoidArm arm) {
        return this.replaceVanillaArms.contains(arm);
    }

    public boolean needsPlayerSkinUnderlay() {
        return this.needsPlayerSkinUnderlay;
    }

    public boolean hasGlint() {
        return this.glint;
    }

    public static enum HideVanilla {
        HAT,
        JACKET,
        LEFT_SLEEVE,
        RIGHT_SLEEVE,
        LEFT_PANTS,
        RIGHT_PANTS;

    }
}

