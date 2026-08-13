/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.platform.GlStateManager$DestFactor
 *  com.mojang.blaze3d.platform.GlStateManager$SourceFactor
 *  com.mojang.blaze3d.systems.RenderSystem
 *  com.mojang.blaze3d.vertex.DefaultVertexFormat
 *  com.mojang.blaze3d.vertex.VertexConsumer
 *  com.mojang.blaze3d.vertex.VertexFormat
 *  com.mojang.blaze3d.vertex.VertexFormat$Mode
 *  it.unimi.dsi.fastutil.ints.IntArrayList
 *  net.minecraft.Util
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.renderer.RenderStateShard$EmptyTextureStateShard
 *  net.minecraft.client.renderer.RenderStateShard$OffsetTexturingStateShard
 *  net.minecraft.client.renderer.RenderStateShard$TextureStateShard
 *  net.minecraft.client.renderer.RenderStateShard$TexturingStateShard
 *  net.minecraft.client.renderer.RenderStateShard$TransparencyStateShard
 *  net.minecraft.client.renderer.RenderType
 *  net.minecraft.client.renderer.RenderType$CompositeState
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.util.Mth
 *  net.minecraft.world.phys.Vec2
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 *  org.joml.Matrix4f
 *  org.joml.Vector3f
 *  org.joml.Vector4f
 */
package io.redspace.ironsspellbooks.render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import net.minecraft.Util;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class RenderHelper {
    public static int colorLerp(float f, int colorA, int colorB) {
        int redA = colorA >> 16 & 0xFF;
        int greenA = colorA >> 8 & 0xFF;
        int blueA = colorA & 0xFF;
        int redB = colorB >> 16 & 0xFF;
        int greenB = colorB >> 8 & 0xFF;
        int blueB = colorB & 0xFF;
        return RenderHelper.color255((int)Mth.lerp((float)f, (float)redA, (float)redB), (int)Mth.lerp((float)f, (float)greenA, (float)greenB), (int)Mth.lerp((float)f, (float)blueA, (float)blueB));
    }

    public static int color255(int pRed, int pGreen, int pBlue, int pAlpha) {
        return pAlpha << 24 | pRed << 16 | pGreen << 8 | pBlue;
    }

    public static int color255(int pRed, int pGreen, int pBlue) {
        return RenderHelper.color255(pRed, pGreen, pBlue, 255);
    }

    public static int colorf(float pRed, float pGreen, float pBlue, float pAlpha) {
        return RenderHelper.color255((int)(255.0f * pRed), (int)(255.0f * pGreen), (int)(255.0f * pBlue), (int)(255.0f * pAlpha));
    }

    public static int colorf(float pRed, float pGreen, float pBlue) {
        return RenderHelper.colorf(pRed, pGreen, pBlue, 1.0f);
    }

    public static QuadBuilder quadBuilder() {
        return new QuadBuilder();
    }

    public static class QuadBuilder {
        List<Vector3f> verticies = new ArrayList<Vector3f>();
        List<Vector3f> normals = new ArrayList<Vector3f>();
        List<Vec2> uvs = new ArrayList<Vec2>();
        List<Integer> colors = new IntArrayList();
        Integer light = null;
        Integer overlay = null;
        @Nullable
        Matrix4f matrix;

        private QuadBuilder() {
        }

        public QuadBuilder vertex(float x, float y) {
            this.verticies.add(new Vector3f(x, y, 0.0f));
            return this;
        }

        public QuadBuilder vertex(float x, float y, float z) {
            this.verticies.add(new Vector3f(x, y, z));
            return this;
        }

        public QuadBuilder uv(float u, float v) {
            this.uvs.add(new Vec2(u, v));
            return this;
        }

        public QuadBuilder normal(float x, float y, float z) {
            this.normals.add(new Vector3f(x, y, z));
            return this;
        }

        public QuadBuilder matrix(Matrix4f matrix) {
            this.matrix = matrix;
            return this;
        }

        public QuadBuilder color(Vector4f color) {
            this.colors.add(RenderHelper.colorf(color.x, color.y, color.z, color.w));
            return this;
        }

        public QuadBuilder color(int color) {
            this.colors.add(color);
            return this;
        }

        public QuadBuilder color(Vector3f color) {
            return this.color(new Vector4f(color.x, color.y, color.z, 1.0f));
        }

        public QuadBuilder color(float r, float g, float b) {
            return this.color(r, g, b, 1.0f);
        }

        public QuadBuilder color(float r, float g, float b, float a) {
            return this.color(new Vector4f(r, g, b, a));
        }

        public QuadBuilder light(int light) {
            this.light = light;
            return this;
        }

        public QuadBuilder overlay(int overlay) {
            this.overlay = overlay;
            return this;
        }

        public void build(VertexConsumer consumer) {
            for (int i = 0; i < this.verticies.size(); ++i) {
                Vector3f vertex = this.verticies.get(i);
                int color = this.colors.isEmpty() ? 0xFFFFFF : (this.colors.size() == 1 || this.colors.size() != this.verticies.size() ? this.colors.get(0).intValue() : this.colors.get(i).intValue());
                if (this.matrix != null) {
                    vertex = this.matrix.transformPosition(vertex.x, vertex.y, vertex.z, new Vector3f());
                }
                consumer.addVertex(vertex.x, vertex.y, vertex.z).setColor(color);
                if (!this.uvs.isEmpty()) {
                    consumer.setUv(this.uvs.get((int)i).x, this.uvs.get((int)i).y);
                }
                if (!this.normals.isEmpty()) {
                    consumer.setNormal(this.normals.get((int)i).x, this.normals.get((int)i).y, this.normals.get((int)i).z);
                }
                if (this.light != null) {
                    consumer.setLight(this.light.intValue());
                }
                if (this.overlay == null) continue;
                consumer.setOverlay(this.overlay.intValue());
            }
        }

        public void build(GuiGraphics graphics, RenderType renderType) {
            this.build(graphics.bufferSource().getBuffer(renderType));
            graphics.flush();
        }

        public void build(GuiGraphics graphics) {
            this.build(graphics, RenderType.gui());
        }
    }

    public static class CustomerRenderType
    extends RenderType {
        protected static final RenderStateShard.TransparencyStateShard ONE_MINUS = new RenderStateShard.TransparencyStateShard("one_minus", () -> {
            RenderSystem.enableBlend();
            RenderSystem.blendFuncSeparate((GlStateManager.SourceFactor)GlStateManager.SourceFactor.ONE_MINUS_SRC_ALPHA, (GlStateManager.DestFactor)GlStateManager.DestFactor.SRC_COLOR, (GlStateManager.SourceFactor)GlStateManager.SourceFactor.ONE, (GlStateManager.DestFactor)GlStateManager.DestFactor.ZERO);
        }, () -> {
            RenderSystem.disableBlend();
            RenderSystem.defaultBlendFunc();
        });
        private static final Function<ResourceLocation, RenderType> DARK_PORTAL_GLOW = Util.memoize(pLocation -> CustomerRenderType.create((String)"dark_portal", (VertexFormat)DefaultVertexFormat.NEW_ENTITY, (VertexFormat.Mode)VertexFormat.Mode.QUADS, (int)256, (boolean)false, (boolean)true, (RenderType.CompositeState)RenderType.CompositeState.builder().setShaderState(RENDERTYPE_ENERGY_SWIRL_SHADER).setTextureState((RenderStateShard.EmptyTextureStateShard)new RenderStateShard.TextureStateShard(pLocation, false, false)).setTransparencyState(ONE_MINUS).setCullState(NO_CULL).setLightmapState(LIGHTMAP).setOverlayState(OVERLAY).createCompositeState(false)));
        private static final Function<ResourceLocation, RenderType> MAGIC = Util.memoize(pLocation -> CustomerRenderType.create((String)"magic_glow", (VertexFormat)DefaultVertexFormat.NEW_ENTITY, (VertexFormat.Mode)VertexFormat.Mode.QUADS, (int)256, (boolean)false, (boolean)true, (RenderType.CompositeState)RenderType.CompositeState.builder().setShaderState(RENDERTYPE_ENERGY_SWIRL_SHADER).setTextureState((RenderStateShard.EmptyTextureStateShard)new RenderStateShard.TextureStateShard(pLocation, false, false)).setTransparencyState(ADDITIVE_TRANSPARENCY).setCullState(CULL).setLightmapState(LIGHTMAP).setOverlayState(OVERLAY).createCompositeState(false)));
        private static final Function<ResourceLocation, RenderType> MAGIC_NO_CULL = Util.memoize(pLocation -> CustomerRenderType.create((String)"magic_glow_no_cull", (VertexFormat)DefaultVertexFormat.NEW_ENTITY, (VertexFormat.Mode)VertexFormat.Mode.QUADS, (int)256, (boolean)false, (boolean)true, (RenderType.CompositeState)RenderType.CompositeState.builder().setShaderState(RENDERTYPE_ENERGY_SWIRL_SHADER).setTextureState((RenderStateShard.EmptyTextureStateShard)new RenderStateShard.TextureStateShard(pLocation, false, false)).setTransparencyState(ADDITIVE_TRANSPARENCY).setCullState(NO_CULL).setLightmapState(LIGHTMAP).setOverlayState(OVERLAY).createCompositeState(false)));
        public static final Function<ResourceLocation, RenderType> PYRIUM_STAFF_ORB = Util.memoize(pLocation -> CustomerRenderType.create((String)"dark_portal_cull", (VertexFormat)DefaultVertexFormat.NEW_ENTITY, (VertexFormat.Mode)VertexFormat.Mode.QUADS, (int)256, (boolean)false, (boolean)true, (RenderType.CompositeState)RenderType.CompositeState.builder().setShaderState(RENDERTYPE_ENERGY_SWIRL_SHADER).setTextureState((RenderStateShard.EmptyTextureStateShard)new RenderStateShard.TextureStateShard(pLocation, false, false)).setTransparencyState(NO_TRANSPARENCY).setCullState(CULL).setLightmapState(LIGHTMAP).setOverlayState(OVERLAY).createCompositeState(false)));

        public CustomerRenderType(String pName, VertexFormat pFormat, VertexFormat.Mode pMode, int pBufferSize, boolean pAffectsCrumbling, boolean pSortOnUpload, Runnable pSetupState, Runnable pClearState) {
            super(pName, pFormat, pMode, pBufferSize, pAffectsCrumbling, pSortOnUpload, pSetupState, pClearState);
        }

        @NotNull
        public static RenderType darkGlow(@NotNull ResourceLocation pLocation) {
            return DARK_PORTAL_GLOW.apply(pLocation);
        }

        public static RenderType magic(ResourceLocation pLocation) {
            return MAGIC.apply(pLocation);
        }

        public static RenderType magicNoCull(ResourceLocation pLocation) {
            return MAGIC_NO_CULL.apply(pLocation);
        }

        public static RenderType magicSwirl(ResourceLocation pLocation, float pU, float pV) {
            return CustomerRenderType.create((String)"magic_glow_swirl", (VertexFormat)DefaultVertexFormat.NEW_ENTITY, (VertexFormat.Mode)VertexFormat.Mode.QUADS, (int)256, (boolean)false, (boolean)true, (RenderType.CompositeState)RenderType.CompositeState.builder().setShaderState(RENDERTYPE_ENERGY_SWIRL_SHADER).setTextureState((RenderStateShard.EmptyTextureStateShard)new RenderStateShard.TextureStateShard(pLocation, false, false)).setTexturingState((RenderStateShard.TexturingStateShard)new RenderStateShard.OffsetTexturingStateShard(pU, pV)).setTransparencyState(ADDITIVE_TRANSPARENCY).setCullState(CULL).setLightmapState(LIGHTMAP).setOverlayState(OVERLAY).createCompositeState(false));
        }
    }
}

