/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.blaze3d.vertex.VertexConsumer
 *  com.mojang.math.Axis
 *  net.minecraft.client.model.HumanoidModel
 *  net.minecraft.client.renderer.MultiBufferSource
 *  net.minecraft.client.renderer.RenderType
 *  net.minecraft.client.renderer.entity.RenderLayerParent
 *  net.minecraft.client.renderer.entity.layers.RenderLayer
 *  net.minecraft.world.entity.HumanoidArm
 *  net.minecraft.world.entity.LivingEntity
 *  software.bernie.geckolib.cache.object.BakedGeoModel
 *  software.bernie.geckolib.cache.object.GeoBone
 *  software.bernie.geckolib.renderer.GeoEntityRenderer
 *  software.bernie.geckolib.renderer.layer.GeoRenderLayer
 *  software.bernie.geckolib.util.RenderUtil
 */
package io.redspace.ironsspellbooks.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.capabilities.magic.SyncedSpellData;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import io.redspace.ironsspellbooks.entity.spells.fire_arrow.FireArrowRenderer;
import io.redspace.ironsspellbooks.entity.spells.lightning_lance.LightningLanceRenderer;
import io.redspace.ironsspellbooks.entity.spells.magic_arrow.MagicArrowRenderer;
import io.redspace.ironsspellbooks.entity.spells.poison_arrow.PoisonArrowRenderer;
import io.redspace.ironsspellbooks.player.ClientMagicData;
import java.util.Optional;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;
import software.bernie.geckolib.util.RenderUtil;

public class ChargeSpellLayer {
    private static <T extends LivingEntity> void handleRender(PoseStack poseStack, MultiBufferSource bufferSource, int pPackedLight, T entity, String spellId, boolean offhand) {
        if (spellId.equals(SpellRegistry.LIGHTNING_LANCE_SPELL.get().getSpellId())) {
            poseStack.translate((double)((float)(offhand ? -1 : 1) / 32.0f) - 0.125, 0.5, 0.0);
            poseStack.mulPose(Axis.YP.rotationDegrees(180.0f));
            LightningLanceRenderer.renderModel(poseStack, bufferSource, entity.tickCount);
        } else if (spellId.equals(SpellRegistry.MAGIC_ARROW_SPELL.get().getSpellId())) {
            poseStack.translate((double)((float)(offhand ? -1 : 1) / 32.0f), 0.5, 0.0);
            poseStack.mulPose(Axis.YP.rotationDegrees(180.0f));
            poseStack.mulPose(Axis.XP.rotationDegrees(90.0f));
            MagicArrowRenderer.renderModel(poseStack, bufferSource);
        } else if (spellId.equals(SpellRegistry.POISON_ARROW_SPELL.get().getSpellId())) {
            poseStack.translate((float)(offhand ? -1 : 1) / 32.0f, 1.0f, 0.0f);
            poseStack.mulPose(Axis.YP.rotationDegrees(180.0f));
            poseStack.mulPose(Axis.XP.rotationDegrees(90.0f));
            PoisonArrowRenderer.renderModel(poseStack, bufferSource, pPackedLight);
        } else if (spellId.equals(SpellRegistry.FIRE_ARROW_SPELL.get().getSpellId())) {
            poseStack.translate((double)((float)(offhand ? -1 : 1) / 32.0f), 0.5, 0.0);
            poseStack.mulPose(Axis.YP.rotationDegrees(180.0f));
            poseStack.mulPose(Axis.XP.rotationDegrees(90.0f));
            FireArrowRenderer.renderModel(poseStack, bufferSource);
        }
    }

    public static class Geo
    extends GeoRenderLayer<AbstractSpellCastingMob> {
        public Geo(GeoEntityRenderer<AbstractSpellCastingMob> entityRenderer) {
            super(entityRenderer);
        }

        public void render(PoseStack poseStack, AbstractSpellCastingMob entity, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
            SyncedSpellData syncedSpellData = ClientMagicData.getSyncedSpellData((LivingEntity)entity);
            String spellId = syncedSpellData.getCastingSpellId();
            Optional boneOpt = bakedModel.getBone("bipedHandRight");
            if (boneOpt.isPresent()) {
                GeoBone bone = (GeoBone)boneOpt.get();
                poseStack.pushPose();
                RenderUtil.translateMatrixToBone((PoseStack)poseStack, (GeoBone)bone);
                RenderUtil.rotateMatrixAroundBone((PoseStack)poseStack, (GeoBone)bone);
                ChargeSpellLayer.handleRender(poseStack, bufferSource, packedLight, entity, spellId, false);
                poseStack.popPose();
            }
        }
    }

    public static class Vanilla<T extends LivingEntity, M extends HumanoidModel<T>>
    extends RenderLayer<T, M> {
        public Vanilla(RenderLayerParent<T, M> pRenderer) {
            super(pRenderer);
        }

        public void render(PoseStack poseStack, MultiBufferSource bufferSource, int pPackedLight, T entity, float pLimbSwing, float pLimbSwingAmount, float pPartialTick, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
            SyncedSpellData syncedSpellData = ClientMagicData.getSyncedSpellData(entity);
            if (!syncedSpellData.isCasting()) {
                return;
            }
            String spellId = syncedSpellData.getCastingSpellId();
            poseStack.pushPose();
            ((HumanoidModel)this.getParentModel()).translateToHand(HumanoidArm.RIGHT, poseStack);
            ChargeSpellLayer.handleRender(poseStack, bufferSource, pPackedLight, entity, spellId, false);
            poseStack.popPose();
        }
    }
}

