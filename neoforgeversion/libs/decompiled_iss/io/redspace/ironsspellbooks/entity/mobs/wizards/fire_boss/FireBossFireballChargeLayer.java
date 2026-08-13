/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.blaze3d.vertex.VertexConsumer
 *  com.mojang.math.Axis
 *  net.minecraft.client.model.geom.ModelPart
 *  net.minecraft.client.renderer.MultiBufferSource
 *  net.minecraft.client.renderer.RenderType
 *  net.minecraft.client.renderer.entity.EntityRendererProvider$Context
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.util.Mth
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.api.distmarker.OnlyIn
 *  org.jetbrains.annotations.Nullable
 *  software.bernie.geckolib.cache.object.BakedGeoModel
 *  software.bernie.geckolib.renderer.GeoEntityRenderer
 *  software.bernie.geckolib.renderer.GeoRenderer
 *  software.bernie.geckolib.renderer.layer.GeoRenderLayer
 */
package io.redspace.ironsspellbooks.entity.mobs.wizards.fire_boss;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import io.redspace.ironsspellbooks.entity.mobs.wizards.fire_boss.FireBossEntity;
import io.redspace.ironsspellbooks.entity.spells.fireball.FireballRenderer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

@OnlyIn(value=Dist.CLIENT)
public class FireBossFireballChargeLayer
extends GeoRenderLayer<AbstractSpellCastingMob> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"textures/entity/fire_boss/tyros_flame.png");
    protected final ModelPart fireball;

    public FireBossFireballChargeLayer(GeoEntityRenderer entityRendererIn, EntityRendererProvider.Context context) {
        super((GeoRenderer)entityRendererIn);
        ModelPart modelpart = context.bakeLayer(FireballRenderer.MODEL_LAYER_LOCATION);
        this.fireball = modelpart.getChild("body");
    }

    public void render(PoseStack poseStack, AbstractSpellCastingMob animatable, BakedGeoModel bakedModel, @Nullable RenderType renderType, MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        int tick;
        FireBossEntity fireBoss;
        if (animatable instanceof FireBossEntity && (fireBoss = (FireBossEntity)animatable).isHalfHealthAttacking() && (tick = 235 - fireBoss.halfHealthTimer) > 11 && tick < 230) {
            poseStack.pushPose();
            poseStack.mulPose(Axis.YP.rotationDegrees(fireBoss.yHeadRot));
            poseStack.translate(0.0, fireBoss.getBoundingBox().getYsize() * 1.25, 0.0);
            float scale = Mth.lerp((float)((float)tick / 235.0f), (float)1.0f, (float)1.5f);
            poseStack.scale(scale, scale, scale);
            VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull((ResourceLocation)FireballRenderer.BASE_TEXTURE));
            float f = (float)animatable.tickCount + partialTick;
            float swirlX = Mth.cos((float)(0.08f * f)) * 180.0f;
            float swirlY = Mth.sin((float)(0.08f * f)) * 180.0f;
            float swirlZ = Mth.cos((float)(0.08f * f + 5464.0f)) * 180.0f;
            poseStack.mulPose(Axis.XP.rotationDegrees(swirlX));
            poseStack.mulPose(Axis.YP.rotationDegrees(swirlY));
            poseStack.mulPose(Axis.ZP.rotationDegrees(swirlZ));
            this.fireball.render(poseStack, consumer, packedLight, packedOverlay, -1);
            poseStack.popPose();
        }
    }
}

