/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.blaze3d.vertex.VertexConsumer
 *  com.mojang.math.Axis
 *  net.minecraft.client.renderer.MultiBufferSource
 *  net.minecraft.client.renderer.RenderType
 *  net.minecraft.resources.ResourceLocation
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.api.distmarker.OnlyIn
 *  software.bernie.geckolib.animatable.GeoAnimatable
 *  software.bernie.geckolib.cache.object.BakedGeoModel
 *  software.bernie.geckolib.model.GeoModel
 *  software.bernie.geckolib.renderer.GeoRenderer
 *  software.bernie.geckolib.renderer.layer.GeoRenderLayer
 */
package io.redspace.ironsspellbooks.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMobRenderer;
import io.redspace.ironsspellbooks.entity.spells.SpinAttackModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

@OnlyIn(value=Dist.CLIENT)
public class GeoSpinAttackLayer
extends GeoRenderLayer<AbstractSpellCastingMob> {
    private GeoModel<AbstractSpellCastingMob> modelProvider;

    public GeoSpinAttackLayer(GeoRenderer<AbstractSpellCastingMob> entityRendererIn) {
        super(entityRendererIn);
    }

    public GeoSpinAttackLayer(AbstractSpellCastingMobRenderer entityRendererIn) {
        super((GeoRenderer)entityRendererIn);
        this.modelProvider = new SpinAttackModel();
    }

    public void render(PoseStack poseStack, AbstractSpellCastingMob animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        if (animatable.isAutoSpinAttack()) {
            poseStack.pushPose();
            for (int i = 0; i < 3; ++i) {
                poseStack.pushPose();
                float f = (float)animatable.tickCount + partialTick + (float)(-(45 + i * 5));
                poseStack.mulPose(Axis.YP.rotationDegrees(f));
                float f1 = 0.75f * (float)i;
                poseStack.scale(f1, f1, f1);
                poseStack.translate(0.0, -1.0 + (double)(-0.2f + 0.6f * (float)i), 0.0);
                RenderType rendertype = RenderType.entityCutoutNoCull((ResourceLocation)this.modelProvider.getTextureResource((GeoAnimatable)animatable));
                this.getRenderer().actuallyRender(poseStack, (GeoAnimatable)animatable, this.modelProvider.getBakedModel(this.modelProvider.getModelResource((GeoAnimatable)animatable)), rendertype, bufferSource, bufferSource.getBuffer(rendertype), true, partialTick, 0xF000F0, packedOverlay, -1);
                poseStack.popPose();
            }
            poseStack.popPose();
        }
    }
}

