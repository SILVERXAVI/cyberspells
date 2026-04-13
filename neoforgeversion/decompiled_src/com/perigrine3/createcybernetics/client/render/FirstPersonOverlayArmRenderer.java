/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.blaze3d.vertex.VertexConsumer
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.model.PlayerModel
 *  net.minecraft.client.player.AbstractClientPlayer
 *  net.minecraft.client.renderer.MultiBufferSource
 *  net.minecraft.client.renderer.RenderType
 *  net.minecraft.client.renderer.entity.EntityRenderDispatcher
 *  net.minecraft.client.renderer.entity.EntityRenderer
 *  net.minecraft.client.renderer.entity.player.PlayerRenderer
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.HumanoidArm
 *  net.minecraft.world.entity.LivingEntity
 */
package com.perigrine3.createcybernetics.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;

public final class FirstPersonOverlayArmRenderer {
    private FirstPersonOverlayArmRenderer() {
    }

    public static void renderOverlayArmAndSleeve(AbstractClientPlayer player, HumanoidArm arm, PoseStack poseStack, MultiBufferSource buffers, int packedLight, ResourceLocation overlayTexture) {
        EntityRenderDispatcher dispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        EntityRenderer entityRenderer = dispatcher.getRenderer((Entity)player);
        if (!(entityRenderer instanceof PlayerRenderer)) {
            return;
        }
        PlayerRenderer playerRenderer = (PlayerRenderer)entityRenderer;
        PlayerModel model = (PlayerModel)playerRenderer.getModel();
        boolean oldHead = model.head.visible;
        boolean oldHat = model.hat.visible;
        boolean oldBody = model.body.visible;
        boolean oldJacket = model.jacket.visible;
        boolean oldLeftArm = model.leftArm.visible;
        boolean oldLeftSleeve = model.leftSleeve.visible;
        boolean oldRightArm = model.rightArm.visible;
        boolean oldRightSleeve = model.rightSleeve.visible;
        boolean oldLeftLeg = model.leftLeg.visible;
        boolean oldLeftPants = model.leftPants.visible;
        boolean oldRightLeg = model.rightLeg.visible;
        boolean oldRightPants = model.rightPants.visible;
        model.setAllVisible(false);
        model.young = false;
        model.crouching = player.isCrouching();
        model.riding = false;
        model.attackTime = 0.0f;
        model.swimAmount = 0.0f;
        model.setupAnim((LivingEntity)player, 0.0f, 0.0f, 0.0f, player.getYRot(), player.getXRot());
        if (arm == HumanoidArm.RIGHT) {
            model.rightArm.visible = true;
            model.rightSleeve.visible = true;
        } else {
            model.leftArm.visible = true;
            model.leftSleeve.visible = true;
        }
        VertexConsumer vc = buffers.getBuffer(RenderType.entityTranslucent((ResourceLocation)overlayTexture));
        model.renderToBuffer(poseStack, vc, packedLight, 0);
        model.head.visible = oldHead;
        model.hat.visible = oldHat;
        model.body.visible = oldBody;
        model.jacket.visible = oldJacket;
        model.leftArm.visible = oldLeftArm;
        model.leftSleeve.visible = oldLeftSleeve;
        model.rightArm.visible = oldRightArm;
        model.rightSleeve.visible = oldRightSleeve;
        model.leftLeg.visible = oldLeftLeg;
        model.leftPants.visible = oldLeftPants;
        model.rightLeg.visible = oldRightLeg;
        model.rightPants.visible = oldRightPants;
    }
}

