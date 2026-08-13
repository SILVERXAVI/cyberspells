/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.blaze3d.vertex.PoseStack$Pose
 *  com.mojang.blaze3d.vertex.VertexConsumer
 *  com.mojang.math.Axis
 *  net.minecraft.client.model.EntityModel
 *  net.minecraft.client.renderer.MultiBufferSource
 *  net.minecraft.client.renderer.RenderType
 *  net.minecraft.client.renderer.entity.RenderLayerParent
 *  net.minecraft.client.renderer.entity.layers.RenderLayer
 *  net.minecraft.client.renderer.texture.OverlayTexture
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.phys.AABB
 *  org.joml.Matrix3f
 *  org.joml.Matrix4f
 *  org.joml.Vector3f
 *  software.bernie.geckolib.cache.object.BakedGeoModel
 *  software.bernie.geckolib.renderer.GeoEntityRenderer
 *  software.bernie.geckolib.renderer.layer.GeoRenderLayer
 */
package io.redspace.ironsspellbooks.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.ICastDataSerializable;
import io.redspace.ironsspellbooks.capabilities.magic.MultiTargetEntityCastData;
import io.redspace.ironsspellbooks.capabilities.magic.RecastInstance;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import io.redspace.ironsspellbooks.player.ClientMagicData;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class SpellTargetingLayer {
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"textures/entity/target/heal.png");

    private static Vector3f getColor(String spellId) {
        return SpellRegistry.getSpell(spellId).getTargetingColor();
    }

    public static void renderTargetLayer(PoseStack poseStack, MultiBufferSource bufferSource, LivingEntity entity) {
        VertexConsumer consumer = bufferSource.getBuffer(RenderType.energySwirl((ResourceLocation)TEXTURE, (float)0.0f, (float)0.0f));
        AABB aabb = entity.getBoundingBox().move(-entity.getX(), -entity.getY(), -entity.getZ());
        float width = (float)aabb.getXsize();
        float height = (float)aabb.getYsize();
        float halfWidth = width * 0.55f;
        float magicYOffset = (float)(1.5 - (double)height);
        Vector3f color = null;
        if (ClientMagicData.getRecasts().hasRecastsActive()) {
            for (RecastInstance recastInstance : ClientMagicData.getRecasts().getActiveRecasts()) {
                MultiTargetEntityCastData targetEntityCastData;
                ICastDataSerializable iCastDataSerializable = recastInstance.getCastData();
                if (!(iCastDataSerializable instanceof MultiTargetEntityCastData) || !(targetEntityCastData = (MultiTargetEntityCastData)iCastDataSerializable).isTargeted((Entity)entity)) continue;
                color = SpellTargetingLayer.getColor(recastInstance.getSpellId());
                break;
            }
        }
        if (color == null) {
            color = SpellTargetingLayer.getColor(ClientMagicData.getTargetingData().spellId);
        }
        color.mul(0.4f);
        poseStack.pushPose();
        poseStack.translate(0.0f, magicYOffset, 0.0f);
        PoseStack.Pose pose = poseStack.last();
        Matrix4f poseMatrix = pose.pose();
        Matrix3f normalMatrix = pose.normal();
        for (int i = 0; i < 4; ++i) {
            consumer.addVertex(poseMatrix, halfWidth, height, halfWidth).setColor(color.x(), color.y(), color.z(), 1.0f).setUv(0.0f, 1.0f).setOverlay(OverlayTexture.NO_OVERLAY).setLight(0xF000F0).setNormal(0.0f, 1.0f, 0.0f);
            consumer.addVertex(poseMatrix, halfWidth, 0.0f, halfWidth).setColor(color.x(), color.y(), color.z(), 1.0f).setUv(0.0f, 0.0f).setOverlay(OverlayTexture.NO_OVERLAY).setLight(0xF000F0).setNormal(0.0f, 1.0f, 0.0f);
            consumer.addVertex(poseMatrix, -halfWidth, 0.0f, halfWidth).setColor(color.x(), color.y(), color.z(), 1.0f).setUv(1.0f, 0.0f).setOverlay(OverlayTexture.NO_OVERLAY).setLight(0xF000F0).setNormal(0.0f, 1.0f, 0.0f);
            consumer.addVertex(poseMatrix, -halfWidth, height, halfWidth).setColor(color.x(), color.y(), color.z(), 1.0f).setUv(1.0f, 1.0f).setOverlay(OverlayTexture.NO_OVERLAY).setLight(0xF000F0).setNormal(0.0f, 1.0f, 0.0f);
            poseStack.mulPose(Axis.YP.rotationDegrees(90.0f));
        }
        poseStack.popPose();
    }

    public static boolean shouldRender(LivingEntity entity) {
        if (ClientMagicData.getRecasts().hasRecastsActive()) {
            for (RecastInstance recastInstance : ClientMagicData.getRecasts().getActiveRecasts()) {
                MultiTargetEntityCastData targetEntityCastData;
                ICastDataSerializable iCastDataSerializable = recastInstance.getCastData();
                if (!(iCastDataSerializable instanceof MultiTargetEntityCastData) || !(targetEntityCastData = (MultiTargetEntityCastData)iCastDataSerializable).isTargeted((Entity)entity)) continue;
                return true;
            }
        }
        return ClientMagicData.getTargetingData().isTargeted(entity);
    }

    public static class Geo
    extends GeoRenderLayer<AbstractSpellCastingMob> {
        public Geo(GeoEntityRenderer<AbstractSpellCastingMob> entityRendererIn) {
            super(entityRendererIn);
        }

        public void render(PoseStack poseStack, AbstractSpellCastingMob animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
            if (SpellTargetingLayer.shouldRender((LivingEntity)animatable)) {
                poseStack.pushPose();
                poseStack.mulPose(Axis.XP.rotationDegrees(180.0f));
                poseStack.translate(0.0, -animatable.getBoundingBox().getYsize() / 2.0, 0.0);
                SpellTargetingLayer.renderTargetLayer(poseStack, bufferSource, (LivingEntity)animatable);
                poseStack.popPose();
            }
        }
    }

    public static class Vanilla<T extends LivingEntity, M extends EntityModel<T>>
    extends RenderLayer<T, M> {
        public Vanilla(RenderLayerParent<T, M> pRenderer) {
            super(pRenderer);
        }

        public void render(PoseStack poseStack, MultiBufferSource bufferSource, int pPackedLight, T entity, float pLimbSwing, float pLimbSwingAmount, float pPartialTick, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
            if (SpellTargetingLayer.shouldRender(entity)) {
                SpellTargetingLayer.renderTargetLayer(poseStack, bufferSource, entity);
            }
        }
    }
}

