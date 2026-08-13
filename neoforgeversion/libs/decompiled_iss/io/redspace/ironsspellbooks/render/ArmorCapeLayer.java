/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.blaze3d.vertex.VertexConsumer
 *  com.mojang.math.Axis
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.model.HumanoidModel
 *  net.minecraft.client.model.geom.ModelLayerLocation
 *  net.minecraft.client.model.geom.ModelPart
 *  net.minecraft.client.model.geom.PartPose
 *  net.minecraft.client.model.geom.builders.CubeDeformation
 *  net.minecraft.client.model.geom.builders.CubeListBuilder
 *  net.minecraft.client.model.geom.builders.LayerDefinition
 *  net.minecraft.client.model.geom.builders.MeshDefinition
 *  net.minecraft.client.model.geom.builders.PartDefinition
 *  net.minecraft.client.player.AbstractClientPlayer
 *  net.minecraft.client.renderer.MultiBufferSource
 *  net.minecraft.client.renderer.RenderType
 *  net.minecraft.client.renderer.entity.RenderLayerParent
 *  net.minecraft.client.renderer.entity.layers.RenderLayer
 *  net.minecraft.client.renderer.texture.OverlayTexture
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.util.Mth
 *  net.minecraft.world.entity.EquipmentSlot
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.Items
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.api.distmarker.OnlyIn
 */
package io.redspace.ironsspellbooks.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import io.redspace.ironsspellbooks.item.armor.IArmorCapeProvider;
import io.redspace.ironsspellbooks.registries.DataAttachmentRegistry;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import java.util.function.Consumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(value=Dist.CLIENT)
public class ArmorCapeLayer
extends RenderLayer<LivingEntity, HumanoidModel<LivingEntity>> {
    private ModelPart cape = Minecraft.getInstance().getEntityModels().bakeLayer(ARMOR_CAPE_LAYER).getChild("cape");
    private Consumer<PoseStack> bodyTransformer = poseStack -> ((HumanoidModel)this.getParentModel()).body.translateAndRotate(poseStack);
    public static ModelLayerLocation ARMOR_CAPE_LAYER = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"armor_cape"), "main");

    public ArmorCapeLayer(RenderLayerParent<LivingEntity, HumanoidModel<LivingEntity>> pRenderer) {
        super(pRenderer);
    }

    public ArmorCapeLayer(RenderLayerParent<LivingEntity, HumanoidModel<LivingEntity>> pRenderer, Consumer<PoseStack> bodyTransformer) {
        this(pRenderer);
        this.bodyTransformer = bodyTransformer;
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        partdefinition.addOrReplaceChild("cape", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0f, 0.0f, -1.0f, 10.0f, 16.0f, 1.0f, CubeDeformation.NONE, 1.0f, 0.5f), PartPose.offset((float)0.0f, (float)0.0f, (float)0.0f));
        return LayerDefinition.create((MeshDefinition)meshdefinition, (int)64, (int)64);
    }

    public void render(PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, LivingEntity livingEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTicks, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        if (this.shouldRender(livingEntity)) {
            ResourceLocation texture = ((IArmorCapeProvider)livingEntity.getItemBySlot(EquipmentSlot.CHEST).getItem()).getCapeResourceLocation();
            IArmorCapeProvider.CapeData capeData = (IArmorCapeProvider.CapeData)livingEntity.getData(DataAttachmentRegistry.CAPE_DATA);
            int lastTick = capeData.lastTick;
            if (lastTick != livingEntity.tickCount) {
                capeData.moveCloak(livingEntity);
                capeData.lastTick = livingEntity.tickCount;
            }
            pPoseStack.pushPose();
            pPoseStack.translate(0.0f, 0.0f, 0.125f);
            double d0 = Mth.lerp((double)pPartialTicks, (double)capeData.xCloakO, (double)capeData.xCloak) - Mth.lerp((double)pPartialTicks, (double)livingEntity.xo, (double)livingEntity.getX());
            double d1 = Mth.lerp((double)pPartialTicks, (double)capeData.yCloakO, (double)capeData.yCloak) - Mth.lerp((double)pPartialTicks, (double)livingEntity.yo, (double)livingEntity.getY());
            double d2 = Mth.lerp((double)pPartialTicks, (double)capeData.zCloakO, (double)capeData.zCloak) - Mth.lerp((double)pPartialTicks, (double)livingEntity.zo, (double)livingEntity.getZ());
            float f = Mth.rotLerp((float)pPartialTicks, (float)livingEntity.yBodyRotO, (float)livingEntity.yBodyRot);
            double d3 = Mth.sin((float)(f * ((float)Math.PI / 180)));
            double d4 = -Mth.cos((float)(f * ((float)Math.PI / 180)));
            float f1 = (float)d1 * 10.0f;
            f1 = Mth.clamp((float)f1, (float)-6.0f, (float)32.0f);
            float f2 = (float)(d0 * d3 + d2 * d4) * 100.0f;
            f2 = Mth.clamp((float)f2, (float)0.0f, (float)150.0f);
            float f3 = (float)(d0 * d4 - d2 * d3) * 100.0f;
            f3 = Mth.clamp((float)f3, (float)-20.0f, (float)20.0f);
            if (f2 < 0.0f) {
                f2 = 0.0f;
            }
            float f4 = Mth.lerp((float)pPartialTicks, (float)capeData.oBob, (float)capeData.bob);
            f1 += Mth.sin((float)(Mth.lerp((float)pPartialTicks, (float)livingEntity.walkDistO, (float)livingEntity.walkDist) * 6.0f)) * 32.0f * f4;
            if (livingEntity.isCrouching()) {
                f1 += 25.0f;
                this.cape.z = 1.4f;
                this.cape.y = 1.85f;
            } else {
                this.cape.z = 0.0f;
                this.cape.y = 0.0f;
            }
            this.bodyTransformer.accept(pPoseStack);
            pPoseStack.mulPose(Axis.XP.rotationDegrees(6.0f + f2 / 2.0f + f1));
            pPoseStack.mulPose(Axis.ZP.rotationDegrees(f3 / 2.0f));
            pPoseStack.mulPose(Axis.YP.rotationDegrees(180.0f - f3 / 2.0f));
            VertexConsumer vertexconsumer = pBuffer.getBuffer(RenderType.entityCutoutNoCull((ResourceLocation)texture));
            this.cape.render(pPoseStack, vertexconsumer, pPackedLight, OverlayTexture.NO_OVERLAY);
            pPoseStack.popPose();
        }
    }

    private boolean shouldRender(LivingEntity livingEntity) {
        ItemStack itemstack = livingEntity.getItemBySlot(EquipmentSlot.CHEST);
        return !itemstack.is(Items.ELYTRA) && itemstack.getItem() instanceof IArmorCapeProvider && !this.hasPlayerCape(livingEntity) && !livingEntity.hasEffect(MobEffectRegistry.ANGEL_WINGS);
    }

    private boolean hasPlayerCape(LivingEntity livingEntity) {
        AbstractClientPlayer player;
        return livingEntity instanceof AbstractClientPlayer && (player = (AbstractClientPlayer)livingEntity).getSkin().capeTexture() != null;
    }
}

