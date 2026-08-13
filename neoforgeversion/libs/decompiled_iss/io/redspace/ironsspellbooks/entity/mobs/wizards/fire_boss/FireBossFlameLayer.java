/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.blaze3d.vertex.VertexConsumer
 *  com.mojang.math.Axis
 *  net.minecraft.client.renderer.MultiBufferSource
 *  net.minecraft.client.renderer.RenderType
 *  net.minecraft.client.renderer.texture.OverlayTexture
 *  net.minecraft.resources.ResourceLocation
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.api.distmarker.OnlyIn
 *  org.joml.Matrix4f
 *  software.bernie.geckolib.cache.object.GeoBone
 *  software.bernie.geckolib.renderer.GeoEntityRenderer
 *  software.bernie.geckolib.renderer.GeoRenderer
 *  software.bernie.geckolib.renderer.layer.GeoRenderLayer
 *  software.bernie.geckolib.util.RenderUtil
 */
package io.redspace.ironsspellbooks.entity.mobs.wizards.fire_boss;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import io.redspace.ironsspellbooks.entity.mobs.wizards.fire_boss.FireBossEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.joml.Matrix4f;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;
import software.bernie.geckolib.util.RenderUtil;

@OnlyIn(value=Dist.CLIENT)
public class FireBossFlameLayer
extends GeoRenderLayer<AbstractSpellCastingMob> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"textures/entity/fire_boss/tyros_flame.png");
    static int frameCount = 8;
    static int ticksPerFrame = 1;

    public FireBossFlameLayer(GeoEntityRenderer entityRendererIn) {
        super((GeoRenderer)entityRendererIn);
    }

    public void renderForBone(PoseStack poseStack, AbstractSpellCastingMob animatable, GeoBone bone, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        FireBossEntity fireBossEntity;
        if (bone.getName().equals("head") && animatable instanceof FireBossEntity && (fireBossEntity = (FireBossEntity)animatable).isSoulMode()) {
            poseStack.pushPose();
            poseStack.mulPose(Axis.YP.rotationDegrees(45.0f));
            RenderUtil.translateToPivotPoint((PoseStack)poseStack, (GeoBone)bone);
            poseStack.scale(0.5f, 0.5f, 0.5f);
            VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityCutout((ResourceLocation)TEXTURE));
            Matrix4f poseMatrix = poseStack.last().pose();
            int anim = animatable.tickCount / ticksPerFrame % frameCount;
            float uvMin = (float)anim / (float)frameCount;
            float uvMax = (float)(anim + 1) / (float)frameCount;
            float halfsqrt2 = 0.7071f;
            for (int i = 0; i < 4; ++i) {
                poseStack.mulPose(Axis.YP.rotationDegrees(90.0f));
                consumer.addVertex(poseMatrix, 0.0f, 0.0f, -halfsqrt2).setColor(255, 255, 255, 255).setUv(0.0f, uvMax).setOverlay(OverlayTexture.NO_OVERLAY).setLight(0xF000F0).setNormal(0.0f, 1.0f, 0.0f);
                consumer.addVertex(poseMatrix, 0.0f, 1.0f, -halfsqrt2).setColor(255, 255, 255, 255).setUv(0.0f, uvMin).setOverlay(OverlayTexture.NO_OVERLAY).setLight(0xF000F0).setNormal(0.0f, 1.0f, 0.0f);
                consumer.addVertex(poseMatrix, 0.0f, 1.0f, halfsqrt2).setColor(255, 255, 255, 255).setUv(1.0f, uvMin).setOverlay(OverlayTexture.NO_OVERLAY).setLight(0xF000F0).setNormal(0.0f, 1.0f, 0.0f);
                consumer.addVertex(poseMatrix, 0.0f, 0.0f, halfsqrt2).setColor(255, 255, 255, 255).setUv(1.0f, uvMax).setOverlay(OverlayTexture.NO_OVERLAY).setLight(0xF000F0).setNormal(0.0f, 1.0f, 0.0f);
            }
            poseStack.popPose();
        }
    }
}

