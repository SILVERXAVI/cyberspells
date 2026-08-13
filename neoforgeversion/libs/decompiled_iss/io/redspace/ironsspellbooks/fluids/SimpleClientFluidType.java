/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.resources.ResourceLocation
 *  net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions
 *  org.jetbrains.annotations.NotNull
 */
package io.redspace.ironsspellbooks.fluids;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import org.jetbrains.annotations.NotNull;

public class SimpleClientFluidType
implements IClientFluidTypeExtensions {
    private final ResourceLocation texture;

    public SimpleClientFluidType(ResourceLocation texture) {
        this.texture = texture;
    }

    @NotNull
    public ResourceLocation getStillTexture() {
        return this.texture;
    }

    @NotNull
    public ResourceLocation getFlowingTexture() {
        return this.texture;
    }
}

