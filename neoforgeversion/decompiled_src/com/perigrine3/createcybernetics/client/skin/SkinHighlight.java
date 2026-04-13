/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.resources.PlayerSkin$Model
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.util.FastColor$ARGB32
 */
package com.perigrine3.createcybernetics.client.skin;

import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;

public final class SkinHighlight {
    private final ResourceLocation wideTexture;
    private final ResourceLocation slimTexture;
    private final int color;
    private final boolean emissive;
    private final boolean tintOnEmissive;

    public SkinHighlight(ResourceLocation wideTexture, ResourceLocation slimTexture) {
        this(wideTexture, slimTexture, FastColor.ARGB32.color((int)255, (int)255, (int)255, (int)255), false, false);
    }

    public SkinHighlight(ResourceLocation wideTexture, ResourceLocation slimTexture, int color, boolean emissive) {
        this(wideTexture, slimTexture, color, emissive, false);
    }

    public SkinHighlight(ResourceLocation wideTexture, ResourceLocation slimTexture, int color, boolean emissive, boolean tintOnEmissive) {
        this.wideTexture = wideTexture;
        this.slimTexture = slimTexture;
        this.color = color;
        this.emissive = emissive;
        this.tintOnEmissive = tintOnEmissive;
    }

    public ResourceLocation getTexture(PlayerSkin.Model modelType) {
        return modelType == PlayerSkin.Model.SLIM ? this.slimTexture : this.wideTexture;
    }

    public int getColor() {
        return this.color;
    }

    public boolean isEmissive() {
        return this.emissive;
    }

    public boolean tintOnEmissive() {
        return this.tintOnEmissive;
    }
}

