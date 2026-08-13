/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.RenderType
 *  net.minecraft.client.renderer.block.model.BakedQuad
 *  net.minecraft.client.renderer.block.model.ItemOverrides
 *  net.minecraft.client.renderer.block.model.ItemTransforms
 *  net.minecraft.client.renderer.texture.TextureAtlasSprite
 *  net.minecraft.client.resources.model.BakedModel
 *  net.minecraft.core.Direction
 *  net.minecraft.util.RandomSource
 *  net.minecraft.world.inventory.InventoryMenu
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.block.state.BlockState
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package io.redspace.ironsspellbooks.render;

import io.redspace.ironsspellbooks.render.RenderHelper;
import java.util.List;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SpectralItemModel
implements BakedModel {
    private final BakedModel original;

    public SpectralItemModel(BakedModel original) {
        this.original = original;
    }

    @NotNull
    public ItemOverrides getOverrides() {
        return this.original.getOverrides();
    }

    @NotNull
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull RandomSource rand) {
        return this.original.getQuads(state, side, rand);
    }

    public boolean useAmbientOcclusion() {
        return this.original.useAmbientOcclusion();
    }

    public boolean isGui3d() {
        return this.original.isGui3d();
    }

    public boolean usesBlockLight() {
        return this.original.usesBlockLight();
    }

    public boolean isCustomRenderer() {
        return this.original.isCustomRenderer();
    }

    @NotNull
    public TextureAtlasSprite getParticleIcon() {
        return this.original.getParticleIcon();
    }

    public ItemTransforms getTransforms() {
        return this.original.getTransforms();
    }

    @NotNull
    public List<RenderType> getRenderTypes(@NotNull ItemStack itemStack, boolean fabulous) {
        return List.of(RenderHelper.CustomerRenderType.magic(InventoryMenu.BLOCK_ATLAS));
    }
}

