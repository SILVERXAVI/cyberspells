/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.blaze3d.vertex.VertexConsumer
 *  net.minecraft.client.model.PlayerModel
 *  net.minecraft.client.model.geom.EntityModelSet
 *  net.minecraft.client.player.AbstractClientPlayer
 *  net.minecraft.client.renderer.MultiBufferSource
 *  net.minecraft.client.renderer.RenderType
 *  net.minecraft.client.renderer.entity.RenderLayerParent
 *  net.minecraft.client.renderer.entity.layers.RenderLayer
 *  net.minecraft.client.renderer.texture.OverlayTexture
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.entity.player.PlayerModelPart
 *  net.minecraft.world.item.Item
 *  net.neoforged.neoforge.attachment.AttachmentType
 */
package com.maxwell.cyber_ware_port.client.upgrades;

import com.maxwell.cyber_ware_port.client.upgrades.CyberLimbModel;
import com.maxwell.cyber_ware_port.common.capability.CyberwareCapabilityProvider;
import com.maxwell.cyber_ware_port.common.capability.CyberwareUserData;
import com.maxwell.cyber_ware_port.init.ModItems;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.PlayerModelPart;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.attachment.AttachmentType;

public class CyberwarePlayerLayer
extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {
    private static final ResourceLocation CYBER_SKIN_TEXTURE = ResourceLocation.fromNamespaceAndPath((String)"cyber_ware_port", (String)"textures/entity/cyber_limbs.png");
    private final CyberLimbModel<AbstractClientPlayer> cyberLimbModel;

    public CyberwarePlayerLayer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> renderer, EntityModelSet modelSet) {
        super(renderer);
        this.cyberLimbModel = new CyberLimbModel(modelSet.bakeLayer(CyberLimbModel.LAYER_LOCATION));
    }

    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, AbstractClientPlayer player, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        CyberwareUserData data = (CyberwareUserData)player.getData((AttachmentType)CyberwareCapabilityProvider.CYBERWARE_DATA.get());
        if (data.isCyberwareInstalled((Item)ModItems.SYNTHETIC_SKIN.get())) {
            return;
        }
        PlayerModel parentModel = (PlayerModel)this.getParentModel();
        VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityCutoutNoCull((ResourceLocation)CYBER_SKIN_TEXTURE));
        if (data.hasCyberRightArm() && player.isModelPartShown(PlayerModelPart.RIGHT_SLEEVE)) {
            this.cyberLimbModel.rightArm.copyFrom(parentModel.rightArm);
            this.cyberLimbModel.rightArm.visible = true;
            this.cyberLimbModel.rightArm.render(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, -1);
        }
        if (data.hasCyberLeftArm() && player.isModelPartShown(PlayerModelPart.LEFT_SLEEVE)) {
            this.cyberLimbModel.leftArm.copyFrom(parentModel.leftArm);
            this.cyberLimbModel.leftArm.visible = true;
            this.cyberLimbModel.leftArm.render(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, -1);
        }
        if (data.hasCyberRightLeg() && player.isModelPartShown(PlayerModelPart.RIGHT_PANTS_LEG)) {
            this.cyberLimbModel.rightLeg.copyFrom(parentModel.rightLeg);
            this.cyberLimbModel.rightLeg.visible = true;
            this.cyberLimbModel.rightLeg.render(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, -1);
        }
        if (data.hasCyberLeftLeg() && player.isModelPartShown(PlayerModelPart.LEFT_PANTS_LEG)) {
            this.cyberLimbModel.leftLeg.copyFrom(parentModel.leftLeg);
            this.cyberLimbModel.leftLeg.visible = true;
            this.cyberLimbModel.leftLeg.render(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, -1);
        }
    }
}

