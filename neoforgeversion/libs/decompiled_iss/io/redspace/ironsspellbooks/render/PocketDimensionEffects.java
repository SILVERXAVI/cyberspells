/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.platform.GlStateManager$DestFactor
 *  com.mojang.blaze3d.platform.GlStateManager$SourceFactor
 *  com.mojang.blaze3d.systems.RenderSystem
 *  com.mojang.blaze3d.vertex.BufferBuilder
 *  com.mojang.blaze3d.vertex.BufferUploader
 *  com.mojang.blaze3d.vertex.DefaultVertexFormat
 *  com.mojang.blaze3d.vertex.MeshData
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.blaze3d.vertex.Tesselator
 *  com.mojang.blaze3d.vertex.VertexFormat$Mode
 *  com.mojang.math.Axis
 *  net.minecraft.client.Camera
 *  net.minecraft.client.multiplayer.ClientLevel
 *  net.minecraft.client.renderer.DimensionSpecialEffects
 *  net.minecraft.client.renderer.DimensionSpecialEffects$SkyType
 *  net.minecraft.client.renderer.GameRenderer
 *  net.minecraft.client.renderer.ShaderInstance
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.util.Mth
 *  net.minecraft.world.phys.Vec3
 *  org.joml.Matrix4f
 *  org.joml.Quaternionf
 *  org.joml.Quaternionfc
 *  org.joml.Vector3f
 */
package io.redspace.ironsspellbooks.render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.MeshData;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Axis;
import io.redspace.ironsspellbooks.IronsSpellbooks;
import java.util.Random;
import java.util.function.Supplier;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Quaternionfc;
import org.joml.Vector3f;

public class PocketDimensionEffects
extends DimensionSpecialEffects {
    public static final ResourceLocation SKY_LOCATION = IronsSpellbooks.id("textures/environment/pocket_dimension_sky.png");
    public static final ResourceLocation CLOUDS_LOCATION = IronsSpellbooks.id("textures/environment/pocket_clouds.png");
    public static final ResourceLocation WISP_LOCATION = IronsSpellbooks.id("textures/environment/single_cloud.png");
    public static final ResourceLocation NOISE = IronsSpellbooks.id("textures/environment/noise_tile.png");

    public PocketDimensionEffects() {
        super(Float.NaN, false, DimensionSpecialEffects.SkyType.NONE, false, false);
    }

    public Vec3 getBrightnessDependentFogColor(Vec3 fogColor, float brightness) {
        return fogColor;
    }

    public boolean isFoggyAt(int x, int y) {
        return false;
    }

    public boolean renderSky(ClientLevel level, int ticks, float partialTick, Matrix4f modelViewMatrix, Camera camera, Matrix4f projectionMatrix, boolean isFoggy, Runnable setupFog) {
        PoseStack poseStack = new PoseStack();
        poseStack.mulPose(modelViewMatrix);
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        RenderSystem.depthMask((boolean)true);
        Tesselator tesselator = Tesselator.getInstance();
        float skyDistance = 100.0f;
        PocketDimensionEffects.renderBox(poseStack, tesselator, skyDistance, 0.0f, 1.0f, GameRenderer::getPositionTexColorShader, SKY_LOCATION, -12237499);
        float f = (float)ticks + partialTick;
        float scale = 0.8f;
        int layers = 6;
        Random random = new Random(431L);
        RenderSystem.blendFunc((GlStateManager.SourceFactor)GlStateManager.SourceFactor.ONE, (GlStateManager.DestFactor)GlStateManager.DestFactor.ONE);
        for (int i = 0; i < layers; ++i) {
            poseStack.pushPose();
            int j = layers - i - 1;
            float speed = (0.01f + (float)(i * i) * 0.09f) * 0.015f;
            float x = ((float)(i * 68731) + f * speed * (random.nextFloat() - 0.5f)) % 360.0f;
            float y = ((float)(i * 74869) + f * speed * (random.nextFloat() - 0.5f)) % 360.0f;
            float z = ((float)(i * 98744) + f * speed * (random.nextFloat() - 0.5f)) % 360.0f;
            poseStack.mulPose(Axis.XP.rotationDegrees(x));
            poseStack.mulPose(Axis.YP.rotationDegrees(y));
            poseStack.mulPose(Axis.ZP.rotationDegrees(z));
            Vector3f rgb = new Vector3f(random.nextFloat() * 0.5f + 0.5f, random.nextFloat() * 0.5f + 0.5f, random.nextFloat() * 0.5f + 0.5f);
            float intensity = Mth.lerp((float)((float)j / (float)layers), (float)0.25f, (float)0.8f);
            rgb.mul(intensity);
            rgb = new Vector3f(Math.min(rgb.x, 1.0f), Math.min(rgb.y, 1.0f), Math.min(rgb.z, 1.0f));
            RenderSystem.setShaderColor((float)rgb.x, (float)rgb.y, (float)rgb.z, (float)1.0f);
            PocketDimensionEffects.renderBox(poseStack, tesselator, skyDistance * scale, 0.0f, 4.0f + 2.0f * scale, GameRenderer::getPositionTexColorShader, CLOUDS_LOCATION, -8355712);
            poseStack.popPose();
            scale -= 0.04f;
        }
        Vector3f color = new Vector3f(0.1f, 0.4f, 0.6f);
        color.mul(0.075f);
        float zoff = PocketDimensionEffects.renderNebula(poseStack, color, random, f, skyDistance, tesselator, scale, 0.0f);
        color = new Vector3f(0.6f, 0.1f, 0.5f);
        color.mul(0.125f);
        zoff = PocketDimensionEffects.renderNebula(poseStack, color, random, f, skyDistance, tesselator, scale, zoff);
        color = new Vector3f(0.3f, 0.3f, 0.3f);
        color.mul(0.125f);
        zoff = PocketDimensionEffects.renderNebula(poseStack, color, random, f, skyDistance, tesselator, scale, zoff);
        this.renderBorderAura(level, ticks, partialTick, modelViewMatrix, camera, projectionMatrix);
        RenderSystem.enableDepthTest();
        RenderSystem.depthMask((boolean)true);
        RenderSystem.disableBlend();
        RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        return true;
    }

    public void renderBorderAura(ClientLevel level, int ticks, float partialTick, Matrix4f modelViewMatrix, Camera camera, Matrix4f projectionMatrix) {
        PoseStack poseStack = new PoseStack();
        Quaternionf quaternionf = camera.rotation().conjugate(new Quaternionf());
        Vec3 cameraPos = camera.getPosition();
        Matrix4f matrix4f1 = new Matrix4f().rotation((Quaternionfc)quaternionf).translate((float)(-cameraPos.x), (float)(-cameraPos.y), (float)(-cameraPos.z));
        poseStack.mulPose(matrix4f1);
        int traversal = (int)(cameraPos.z / 256.0) * 256;
        float HARDCODE_WIDTH = 7.0f;
        float halfWidth = HARDCODE_WIDTH / 2.0f;
        float HARDCODE_X = 4.0f + halfWidth;
        float HARDCODE_Y = 1.0f;
        float HARDCODE_Z = 4.0f + halfWidth + (float)traversal;
        RenderSystem.blendFunc((GlStateManager.SourceFactor)GlStateManager.SourceFactor.ONE, (GlStateManager.DestFactor)GlStateManager.DestFactor.ONE);
        RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        RenderSystem.enableBlend();
        RenderSystem.disableDepthTest();
        poseStack.translate(HARDCODE_X, HARDCODE_Y, HARDCODE_Z);
        Tesselator tesselator = Tesselator.getInstance();
        RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
        RenderSystem.setShaderTexture((int)0, (ResourceLocation)NOISE);
        float uvScrollMin = ((float)ticks + partialTick) / 20.0f / 12.0f % 1.0f;
        float uvScrollMax = uvScrollMin + 0.020833334f;
        float uvTile = Mth.floor((float)(HARDCODE_WIDTH / 3.0f));
        for (int i = 0; i < 4; ++i) {
            poseStack.pushPose();
            poseStack.mulPose(Axis.YP.rotationDegrees((float)(i * 90)));
            Matrix4f matrix4f = poseStack.last().pose();
            BufferBuilder bufferbuilder = tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
            int baseColor = -6745686;
            bufferbuilder.addVertex(matrix4f, -halfWidth, HARDCODE_Y - 1.0f, halfWidth).setUv(0.0f, uvScrollMax).setColor(baseColor);
            bufferbuilder.addVertex(matrix4f, -halfWidth, HARDCODE_Y + 2.0f, halfWidth).setUv(0.0f, uvScrollMin).setColor(-16777216);
            bufferbuilder.addVertex(matrix4f, halfWidth, HARDCODE_Y + 2.0f, halfWidth).setUv(uvTile, uvScrollMin).setColor(-16777216);
            bufferbuilder.addVertex(matrix4f, halfWidth, HARDCODE_Y - 1.0f, halfWidth).setUv(uvTile, uvScrollMax).setColor(baseColor);
            BufferUploader.drawWithShader((MeshData)bufferbuilder.buildOrThrow());
            poseStack.popPose();
        }
    }

    private static float renderNebula(PoseStack poseStack, Vector3f color, Random random, float f, float skyDistance, Tesselator tesselator, float scale, float zoff) {
        RenderSystem.setShaderColor((float)color.x, (float)color.y, (float)color.z, (float)1.0f);
        int clouds = 15;
        for (int i = 0; i < clouds; ++i) {
            float clusterScale = 0.15f + (float)i * 0.003f;
            poseStack.pushPose();
            int count = i + 1;
            float speed = 0.005f;
            float x = ((float)random.nextInt(360) + f * speed) % 360.0f;
            float y = ((float)random.nextInt(360) + f * speed) % 360.0f;
            float z = ((float)random.nextInt(360) + f * speed) % 360.0f;
            poseStack.mulPose(Axis.XP.rotationDegrees(x));
            poseStack.mulPose(Axis.YP.rotationDegrees(y));
            poseStack.mulPose(Axis.ZP.rotationDegrees(z));
            for (int j = 0; j < count; ++j) {
                Vector3f offset = new Vector3f(random.nextFloat() - 0.5f, 0.0f, random.nextFloat() - 0.5f);
                offset.mul(skyDistance * 0.25f * (1.0f + (float)j * 0.025f));
                poseStack.pushPose();
                poseStack.translate(offset.x, zoff, offset.z);
                PocketDimensionEffects.renderPlane(poseStack, tesselator, skyDistance * scale, 0.0f, 1.0f, GameRenderer::getPositionTexColorShader, WISP_LOCATION, clusterScale, -10461088);
                poseStack.popPose();
                zoff += 0.03f;
            }
            poseStack.popPose();
        }
        return zoff;
    }

    private static void renderBox(PoseStack poseStack, Tesselator tesselator, float skyDistance, float uvMin, float uvMax, Supplier<ShaderInstance> shaderSupplier, ResourceLocation texture, int color) {
        RenderSystem.setShader(shaderSupplier);
        RenderSystem.setShaderTexture((int)0, (ResourceLocation)texture);
        for (int i = 0; i < 6; ++i) {
            poseStack.pushPose();
            if (i == 1) {
                poseStack.mulPose(Axis.XP.rotationDegrees(90.0f));
            }
            if (i == 2) {
                poseStack.mulPose(Axis.XP.rotationDegrees(-90.0f));
            }
            if (i == 3) {
                poseStack.mulPose(Axis.XP.rotationDegrees(180.0f));
            }
            if (i == 4) {
                poseStack.mulPose(Axis.ZP.rotationDegrees(90.0f));
            }
            if (i == 5) {
                poseStack.mulPose(Axis.ZP.rotationDegrees(-90.0f));
            }
            Matrix4f matrix4f = poseStack.last().pose();
            BufferBuilder bufferbuilder = tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
            bufferbuilder.addVertex(matrix4f, -skyDistance, -skyDistance, -skyDistance).setUv(uvMin, uvMin).setColor(color);
            bufferbuilder.addVertex(matrix4f, -skyDistance, -skyDistance, skyDistance).setUv(uvMin, uvMax).setColor(color);
            bufferbuilder.addVertex(matrix4f, skyDistance, -skyDistance, skyDistance).setUv(uvMax, uvMax).setColor(color);
            bufferbuilder.addVertex(matrix4f, skyDistance, -skyDistance, -skyDistance).setUv(uvMax, uvMin).setColor(color);
            BufferUploader.drawWithShader((MeshData)bufferbuilder.buildOrThrow());
            poseStack.popPose();
        }
    }

    private static void renderPlane(PoseStack poseStack, Tesselator tesselator, float skyDistance, float uvMin, float uvMax, Supplier<ShaderInstance> shaderSupplier, ResourceLocation texture, float scale, int color) {
        RenderSystem.setShader(shaderSupplier);
        RenderSystem.setShaderTexture((int)0, (ResourceLocation)texture);
        poseStack.pushPose();
        Matrix4f matrix4f = poseStack.last().pose();
        BufferBuilder bufferbuilder = tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
        bufferbuilder.addVertex(matrix4f, -skyDistance * scale, -skyDistance, -skyDistance * scale).setUv(uvMin, uvMin).setColor(color);
        bufferbuilder.addVertex(matrix4f, -skyDistance * scale, -skyDistance, skyDistance * scale).setUv(uvMin, uvMax).setColor(color);
        bufferbuilder.addVertex(matrix4f, skyDistance * scale, -skyDistance, skyDistance * scale).setUv(uvMax, uvMax).setColor(color);
        bufferbuilder.addVertex(matrix4f, skyDistance * scale, -skyDistance, -skyDistance * scale).setUv(uvMax, uvMin).setColor(color);
        BufferUploader.drawWithShader((MeshData)bufferbuilder.buildOrThrow());
        poseStack.popPose();
    }
}

