/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.platform.GlStateManager$DestFactor
 *  com.mojang.blaze3d.platform.GlStateManager$SourceFactor
 *  com.mojang.blaze3d.systems.RenderSystem
 *  com.mojang.blaze3d.vertex.DefaultVertexFormat
 *  com.mojang.blaze3d.vertex.VertexFormat
 *  com.mojang.blaze3d.vertex.VertexFormat$Mode
 *  net.minecraft.client.renderer.RenderStateShard
 *  net.minecraft.client.renderer.RenderStateShard$EmptyTextureStateShard
 *  net.minecraft.client.renderer.RenderStateShard$TextureStateShard
 *  net.minecraft.client.renderer.RenderStateShard$TransparencyStateShard
 *  net.minecraft.client.renderer.RenderType
 *  net.minecraft.client.renderer.RenderType$CompositeState
 *  net.minecraft.resources.ResourceLocation
 */
package com.perigrine3.createcybernetics.client.skin;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public final class SkinRenderTypes {
    private static final Map<ResourceLocation, RenderType> EMISSIVE_TINTED_CACHE = new ConcurrentHashMap<ResourceLocation, RenderType>();
    private static final Map<ResourceLocation, RenderType> OVERLAY_GLINT_CACHE = new ConcurrentHashMap<ResourceLocation, RenderType>();
    private static final RenderStateShard.TransparencyStateShard CC_ADDITIVE_ALPHA = new RenderStateShard.TransparencyStateShard("cc_additive_alpha", () -> {
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate((GlStateManager.SourceFactor)GlStateManager.SourceFactor.SRC_ALPHA, (GlStateManager.DestFactor)GlStateManager.DestFactor.ONE, (GlStateManager.SourceFactor)GlStateManager.SourceFactor.ONE, (GlStateManager.DestFactor)GlStateManager.DestFactor.ONE);
    }, () -> {
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableBlend();
    });

    private SkinRenderTypes() {
    }

    public static RenderType emissiveTinted(ResourceLocation tex) {
        return EMISSIVE_TINTED_CACHE.computeIfAbsent(tex, t -> RenderType.create((String)"cc_emissive_tinted", (VertexFormat)DefaultVertexFormat.NEW_ENTITY, (VertexFormat.Mode)VertexFormat.Mode.QUADS, (int)256, (boolean)true, (boolean)true, (RenderType.CompositeState)RenderType.CompositeState.builder().setShaderState(RenderStateShard.RENDERTYPE_ENTITY_TRANSLUCENT_SHADER).setTextureState((RenderStateShard.EmptyTextureStateShard)new RenderStateShard.TextureStateShard(t, false, false)).setTransparencyState(CC_ADDITIVE_ALPHA).setCullState(RenderStateShard.NO_CULL).setLightmapState(RenderStateShard.LIGHTMAP).setOverlayState(RenderStateShard.OVERLAY).setDepthTestState(RenderStateShard.EQUAL_DEPTH_TEST).setWriteMaskState(RenderStateShard.COLOR_WRITE).createCompositeState(true)));
    }

    public static RenderType translucentGlintOverlay(ResourceLocation tex) {
        return OVERLAY_GLINT_CACHE.computeIfAbsent(tex, t -> RenderType.create((String)"cc_overlay_glint", (VertexFormat)DefaultVertexFormat.NEW_ENTITY, (VertexFormat.Mode)VertexFormat.Mode.QUADS, (int)256, (boolean)true, (boolean)true, (RenderType.CompositeState)RenderType.CompositeState.builder().setShaderState(RenderStateShard.RENDERTYPE_ENTITY_TRANSLUCENT_SHADER).setTextureState((RenderStateShard.EmptyTextureStateShard)new RenderStateShard.TextureStateShard(t, false, false)).setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY).setCullState(RenderStateShard.NO_CULL).setLightmapState(RenderStateShard.LIGHTMAP).setOverlayState(RenderStateShard.OVERLAY).setTexturingState(RenderStateShard.GLINT_TEXTURING).setWriteMaskState(RenderStateShard.COLOR_WRITE).createCompositeState(true)));
    }
}

