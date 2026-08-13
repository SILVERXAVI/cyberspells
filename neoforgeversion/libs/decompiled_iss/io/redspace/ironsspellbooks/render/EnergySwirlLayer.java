/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.blaze3d.vertex.VertexConsumer
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.model.HumanoidModel
 *  net.minecraft.client.model.geom.ModelLayerLocation
 *  net.minecraft.client.renderer.MultiBufferSource
 *  net.minecraft.client.renderer.RenderType
 *  net.minecraft.client.renderer.entity.RenderLayerParent
 *  net.minecraft.client.renderer.entity.layers.RenderLayer
 *  net.minecraft.client.renderer.texture.OverlayTexture
 *  net.minecraft.core.Holder
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.effect.MobEffect
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.api.distmarker.OnlyIn
 *  software.bernie.geckolib.animatable.GeoAnimatable
 *  software.bernie.geckolib.cache.object.BakedGeoModel
 *  software.bernie.geckolib.renderer.GeoEntityRenderer
 *  software.bernie.geckolib.renderer.layer.GeoRenderLayer
 */
package io.redspace.ironsspellbooks.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import io.redspace.ironsspellbooks.render.RenderHelper;
import java.util.function.Predicate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

@OnlyIn(value=Dist.CLIENT)
public class EnergySwirlLayer {
    public static final ResourceLocation EVASION_TEXTURE = ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"textures/entity/evasion.png");
    public static final ResourceLocation CHARGE_TEXTURE = ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"textures/entity/charged.png");
    private static final int COLOR = RenderHelper.colorf(0.8f, 0.8f, 0.8f);

    private static RenderType getRenderType(ResourceLocation texture, float f) {
        return RenderType.energySwirl((ResourceLocation)texture, (float)(f * 0.02f % 1.0f), (float)(f * 0.01f % 1.0f));
    }

    private static boolean shouldRender(LivingEntity entity, Predicate<LivingEntity> shouldRenderFlag) {
        return shouldRenderFlag.test(entity);
    }

    public static class Geo
    extends GeoRenderLayer<AbstractSpellCastingMob> {
        private final ResourceLocation TEXTURE;
        private final Predicate<LivingEntity> shouldRenderFlag;

        public Geo(GeoEntityRenderer<AbstractSpellCastingMob> entityRendererIn, ResourceLocation texture, Holder<MobEffect> shouldRenderFlag) {
            this(entityRendererIn, texture, (LivingEntity living) -> living.hasEffect(shouldRenderFlag));
        }

        public Geo(GeoEntityRenderer<AbstractSpellCastingMob> entityRendererIn, ResourceLocation texture, Predicate<LivingEntity> shouldRenderFlag) {
            super(entityRendererIn);
            this.TEXTURE = texture;
            this.shouldRenderFlag = shouldRenderFlag;
        }

        public void render(PoseStack poseStack, AbstractSpellCastingMob animatable, BakedGeoModel bakedModel, RenderType renderType2, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
            if (EnergySwirlLayer.shouldRender((LivingEntity)animatable, this.shouldRenderFlag)) {
                float f = (float)animatable.tickCount + partialTick;
                RenderType renderType = EnergySwirlLayer.getRenderType(this.TEXTURE, f);
                VertexConsumer vertexconsumer = bufferSource.getBuffer(renderType);
                poseStack.pushPose();
                bakedModel.getBone("body").ifPresent(rootBone -> rootBone.getChildBones().forEach(bone -> bone.updateScale(1.1f, 1.1f, 1.1f)));
                this.getRenderer().actuallyRender(poseStack, (GeoAnimatable)animatable, bakedModel, renderType, bufferSource, vertexconsumer, true, partialTick, packedLight, OverlayTexture.NO_OVERLAY, COLOR);
                bakedModel.getBone("body").ifPresent(rootBone -> rootBone.getChildBones().forEach(bone -> bone.updateScale(1.0f, 1.0f, 1.0f)));
                poseStack.popPose();
            }
        }
    }

    public static class Vanilla
    extends RenderLayer<Player, HumanoidModel<Player>> {
        public static ModelLayerLocation ENERGY_LAYER = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"energy_layer"), "main");
        private final HumanoidModel<Player> model = new HumanoidModel(Minecraft.getInstance().getEntityModels().bakeLayer(ENERGY_LAYER));
        private final ResourceLocation TEXTURE;
        private final Predicate<LivingEntity> shouldRender;

        public Vanilla(RenderLayerParent pRenderer, ResourceLocation texture, Predicate<LivingEntity> shouldRender) {
            super(pRenderer);
            this.TEXTURE = texture;
            this.shouldRender = shouldRender;
        }

        public Vanilla(RenderLayerParent pRenderer, ResourceLocation texture, Holder<MobEffect> shouldRenderFlag) {
            this(pRenderer, texture, (LivingEntity living) -> living.hasEffect(shouldRenderFlag));
        }

        public void render(PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight, Player pLivingEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTicks, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
            if (EnergySwirlLayer.shouldRender((LivingEntity)pLivingEntity, this.shouldRender)) {
                float f = (float)pLivingEntity.tickCount + pPartialTicks;
                HumanoidModel<Player> entitymodel = this.model();
                VertexConsumer vertexconsumer = pBuffer.getBuffer(EnergySwirlLayer.getRenderType(this.TEXTURE, f));
                ((HumanoidModel)this.getParentModel()).copyPropertiesTo(entitymodel);
                entitymodel.renderToBuffer(pMatrixStack, vertexconsumer, pPackedLight, OverlayTexture.NO_OVERLAY, COLOR);
            }
        }

        protected HumanoidModel<Player> model() {
            return this.model;
        }

        protected boolean shouldRender(Player entity) {
            return true;
        }
    }
}

